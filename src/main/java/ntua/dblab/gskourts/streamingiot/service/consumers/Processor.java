package ntua.dblab.gskourts.streamingiot.service.consumers;

import java.time.Duration;
import static org.apache.kafka.common.serialization.Serdes.Long;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.Suppressed;
import org.apache.kafka.streams.kstream.TimeWindowedKStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.vavr.Tuple2;
import ntua.dblab.gskourts.streamingiot.util.AppConf;
import ntua.dblab.gskourts.streamingiot.util.serdes.IntegerArraySerializer;
import ntua.dblab.gskourts.streamingiot.util.avro.CountAndSum;

@Component
public class Processor {
      private final Logger log = LoggerFactory.getLogger(Processor.class);
      private final String tempInputTopic;
      private final String tempOutputTopic;
      private final String pressureInputTopic;
      private final String pressureOutputTopic;
      private final String powerInputTopic;
      private final String powerOutputTopic;
      private final SpecificAvroSerde<CountAndSum> countAndSumSerde;
      @Value("${application.consumer.aggregator.aggregateWindowSizeSec}")
      private int aggregateWindowSizeSec;

      @Value("${application.consumer.aggregator.gracePeriodSec}")
      private int gracePeriodSec;

      Processor(AppConf appConf, SpecificAvroSerde<CountAndSum> countAndSumSerde) {
            this.tempInputTopic = appConf.getTemperatureInputTopic();
            this.tempOutputTopic = appConf.getTemperatureOutputTopic();
            this.pressureInputTopic = appConf.getPressureInputTopic();
            this.pressureOutputTopic = appConf.getPressureOutputTopic();
            this.powerInputTopic = appConf.getPowerInputTopic();
            this.powerOutputTopic = appConf.getPowerOutputTopic();
            this.countAndSumSerde = countAndSumSerde;
      }

      @Autowired
      public void process(StreamsBuilder builder) {
            processTemperature(builder);
            processPressure(builder);
            processPower(builder);
      }

      @Async
      private void processTemperature(StreamsBuilder builder) {
            log.info("START: Process Temperature");
            KStream<Integer, Integer> stream = builder
                        .stream(tempInputTopic, Consumed.with(Serdes.Integer(), Serdes.Integer()))
                        .peek((key, value) -> log.info("Topic: {}. Temperature={}, key={}",
                                    tempInputTopic, value, key));
            Duration windowSize = Duration.ofSeconds(aggregateWindowSizeSec);
            Duration graceSize = Duration.ofSeconds(gracePeriodSec);
            TimeWindows tumblingWindow = TimeWindows.ofSizeAndGrace(windowSize, graceSize);
            log.info("Temp: Aggregate the values of the same key every {} seconds", windowSize.getSeconds());

            TimeWindowedKStream<Integer, Integer> windowed = stream.groupByKey()
                        .windowedBy(tumblingWindow);

            KTable<Windowed<Integer>, CountAndSum> aggregated = windowed.aggregate(() -> new CountAndSum(0L, 0.0),
                        (key, value, aggregate) -> {
                              aggregate.setCount(aggregate.getCount() + 1);
                              aggregate.setSum(aggregate.getSum() + value);
                              return aggregate;
                        },
                        Materialized.with(Serdes.Integer(), countAndSumSerde));

            aggregated.toStream()
                        .map((wk, value) -> KeyValue.pair(wk.key(), (float) value.getSum() / value.getCount()))
                        .peek((key, value) -> log.trace("Topic: {}. AGGREGATED: key={}, average={}",
                                    tempOutputTopic, key, value))
                        .to(tempOutputTopic, Produced.with(Serdes.Integer(), Serdes.Float()));

            log.info("END: Process Temperature");
      }

      @Async
      private void processPower(StreamsBuilder builder) {
            log.info("START: Process power");
            KStream<Integer, Integer> stream = builder
                        .stream(powerInputTopic, Consumed.with(Serdes.Integer(), Serdes.Integer()))
                        .peek((key, value) -> log.info("Topic: {}. Power={}, key={}",
                                    powerInputTopic, value, key));
            Duration windowSize = Duration.ofSeconds(aggregateWindowSizeSec);
            Duration graceSize = Duration.ofSeconds(gracePeriodSec);
            TimeWindows tumblingWindow = TimeWindows.ofSizeAndGrace(windowSize, graceSize);
            log.info("Power: Aggregate the values of the same key every {} seconds", windowSize.getSeconds());

            final KTable<Windowed<Integer>, Integer> aggregated = stream.groupByKey()
                        .windowedBy(tumblingWindow)
                        .aggregate(() -> 0, (key, value, total) -> total + value,
                                    Materialized.with((Serdes.Integer()), Serdes.Integer()))
                        .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded().shutDownWhenFull()));

            aggregated.toStream()
                        .map((wk, value) -> KeyValue.pair(wk.key(), value))
                        .peek((key, value) -> log.trace("Topic: {}. AGGREGATED: key={}, value={}",
                                    powerOutputTopic, key, value))
                        .to(powerOutputTopic, Produced.with(Serdes.Integer(), Serdes.Integer()));

            log.info("END: Process power");
      }

      @Async
      private void processPressure(StreamsBuilder builder) {
            log.info("START: Process pressure");
            KStream<Integer, Integer> stream = builder
                        .stream(tempInputTopic, Consumed.with(Serdes.Integer(), Serdes.Integer()))
                        .peek((key, value) -> log.info("Topic: {}. Pressure={}, key={}",
                                    pressureInputTopic, value, key));
            Duration windowSize = Duration.ofSeconds(aggregateWindowSizeSec);
            Duration graceSize = Duration.ofSeconds(gracePeriodSec);
            TimeWindows tumblingWindow = TimeWindows.ofSizeAndGrace(windowSize, graceSize);

            TimeWindowedKStream<Integer, Integer> windowed = stream.groupByKey()
                        .windowedBy(tumblingWindow);

            KTable<Windowed<Integer>, CountAndSum> aggregated = windowed.aggregate(() -> new CountAndSum(0L, 0.0),
                        (key, value, aggregate) -> {
                              aggregate.setCount(aggregate.getCount() + 1);
                              aggregate.setSum(aggregate.getSum() + value);
                              return aggregate;
                        },
                        Materialized.with(Serdes.Integer(), countAndSumSerde));

            aggregated.toStream()
                        .map((wk, value) -> KeyValue.pair(wk.key(), (float) value.getSum() / value.getCount()))
                        .peek((key, value) -> log.trace("Topic: {}. AGGREGATED: key={}, value={}",
                                    pressureOutputTopic, key, value))
                        .to(pressureOutputTopic, Produced.with(Serdes.Integer(), Serdes.Float()));

            log.info("END: Process pressure");
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