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

import ntua.dblab.gskourts.streamingiot.util.AppConf;
import ntua.dblab.gskourts.streamingiot.util.AppConstants;

@Component
public class Processor {
      private final Logger LOG = LoggerFactory.getLogger(Processor.class);
      private final String tempInputTopic;
      private final String tempOutputTopic;
      private final String pressureInputTopic;
      private final String pressureOutputTopic;
      private final String powerInputTopic;
      private final String powerOutputTopic;
      @Value("${application.consumer.aggregator.aggregateWindowSizeSec}")
      private int aggregateWindowSizeSec;

      @Value("${application.consumer.aggregator.gracePeriodSec}")
      private int gracePeriodSec;

      Processor(AppConf appConf) {
            this.tempInputTopic = appConf.getTemperatureInputTopic();
            this.tempOutputTopic = appConf.getTemperatureOutputTopic();
            this.pressureInputTopic = appConf.getPressureInputTopic();
            this.pressureOutputTopic = appConf.getPressureOutputTopic();
            this.powerInputTopic = appConf.getPowerInputTopic();
            this.powerOutputTopic = appConf.getPowerOutputTopic();

      }

      @Autowired
      public void processTemperature(StreamsBuilder builder) {

            // Serializers/deserializers (serde)
            final Serde<Integer> integerSerde = Serdes.Integer();
            final Serde<String> stringSerde = Serdes.String();
            final Serde<Long> longSerde = Serdes.Long();

            // Construct a `KStream` from the input topic
            KStream<Integer, Integer> stream = builder
                        .stream(tempInputTopic, Consumed.with(integerSerde, integerSerde))
                        .peek((key, value) -> LOG.info("Received measurement={}, key={}", value, key));

            // Create a window
            Duration windowSize = Duration.ofSeconds(aggregateWindowSizeSec);
            Duration graceSize = Duration.ofSeconds(gracePeriodSec);
            TimeWindows tumblingWindow = TimeWindows.ofSizeAndGrace(windowSize, graceSize);
            LOG.info("Aggregate the values of the same key every {} seconds", windowSize.getSeconds());
            // Perform Aggregation
            final KTable<Windowed<Integer>, Integer> aggregated = stream.groupByKey()
                        .windowedBy(tumblingWindow)
                        .aggregate(() -> 0, (key, value, total) -> total + value,
                                    Materialized.with((Serdes.Integer()), Serdes.Integer()))
                        .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));
            //         .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()));

            aggregated.toStream()
                        .peek((key, value) -> LOG.trace("Aggregated measurement={}, key={}", value, key))
                        .map((wk, value) -> KeyValue.pair(wk.key(), value))
                        .peek((key, value) -> LOG.trace("AGGREGATED: key={}, value={}", key, value))
                        .to(tempOutputTopic, Produced.with(Serdes.Integer(), Serdes.Integer()));
      }
}

//@Component
//public class TestConsumer {
//      private final Logger log = LoggerFactory.getLogger(Processor.class);

//      @Autowired
//      public TestConsumer(AppConf appConf) {
//      }

//      @KafkaListener(topics = AppConstants.TOPIC_TEMPERATURE_INPUT, groupId = AppConstants.CONSUMER_TEMPERATURE_AGGREGATOR)
//      public void consume(@NotNull ConsumerRecord<String, Long> record) {
//            log.info("received={} with key={}", record.value(), record.key());
//      }
//}