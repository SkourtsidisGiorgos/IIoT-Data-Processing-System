package ntua.dblab.gskourts.streamingiot.service.consumers;

import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import ntua.dblab.gskourts.streamingiot.util.AppConstants;

@Component
public class TestConsumer {
      private final Logger log = LoggerFactory.getLogger(Processor.class);

      @KafkaListener(topics = { AppConstants.TOPIC_MEASUREMENTS }, groupId = AppConstants.CONSUMER_AGGREGATOR)
      public void consume(@NotNull ConsumerRecord<String, Long> record) {
            log.info("received={} with key={}", record.value(), record.key());
      }
}

@Component
class Processor {
      private final Logger LOG = LoggerFactory.getLogger(Processor.class);

      @Value("${application.consumer.aggregator.aggregateWindowSizeSec}")
      private int aggregateWindowSizeSec;

      @Value("${application.consumer.aggregator.gracePeriodSec}")
      private int gracePeriodSec;

      @Autowired
      public void process(StreamsBuilder builder) {

            // Serializers/deserializers (serde)
            final Serde<Integer> integerSerde = Serdes.Integer();
            final Serde<String> stringSerde = Serdes.String();
            final Serde<Long> longSerde = Serdes.Long();

            // Construct a `KStream` from the input topic
            KStream<Integer, Integer> stream = builder
                        .stream(AppConstants.TOPIC_MEASUREMENTS, Consumed.with(integerSerde, integerSerde))
                        .peek((key, value) -> LOG.info("Received measurement={}", value));

            // Create a window
            Duration windowSize = Duration.ofSeconds(aggregateWindowSizeSec);
            Duration graceSize = Duration.ofSeconds(gracePeriodSec);
            TimeWindows tumblingWindow = TimeWindows.ofSizeAndGrace(windowSize, graceSize);

            // Perform Aggregation
            final KTable<Windowed<Integer>, Integer> aggregated = stream.groupByKey()
                        .windowedBy(tumblingWindow)
                        .aggregate(() -> 0,
                                    (key, value, total) -> total + value,
                                    Materialized.with((Serdes.Integer()), Serdes.Integer()))
                        .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));
            //         .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()));

            aggregated.toStream()
                        .map((wk, value) -> KeyValue.pair(wk.key(), value))
                        .peek((key, value) -> LOG.info("AGGREGATED: key={}, value={}", key, value))
                        .to(AppConstants.TOPIC_AGGREGATED_MEASUREMENTS,
                                    Produced.with(Serdes.Integer(), Serdes.Integer()));

            LOG.info("Aggregate the values of the same key every 10 seconds");
            //      KTable<Integer, Long> wordCounts = stream
            //                              .groupBy((key, value) -> value, Grouped.with(integerSerde, integerSerde))
            //                              .windowedBy(SessionWindows.with(Duration.ofSeconds(10L)).grace(Duration.ofSeconds(10L)))
            //                              .count(Materialized.as("measurements_30_seconds"));

            //   .groupBy((key, value) -> value, Grouped.with(stringSerde, stringSerde))
            //   .count(Materialized.as("counts"));

            // Convert the `KTable<String, Long>` into a `KStream<String, Long>` and write to the output topic.
            //wordCounts.toStream().to("streams-wordcount-output", Produced.with(stringSerde, longSerde));
      }
}
