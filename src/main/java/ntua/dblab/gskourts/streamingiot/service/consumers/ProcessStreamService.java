package ntua.dblab.gskourts.streamingiot.service.consumers;

import java.time.Duration;

import io.lettuce.core.api.sync.RedisCommands;
import ntua.dblab.gskourts.streamingiot.components.InfluxDBWriter;
import ntua.dblab.gskourts.streamingiot.service.AlertService;
import ntua.dblab.gskourts.streamingiot.util.InfluxDbConfiguration;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import ntua.dblab.gskourts.streamingiot.util.AppConf;
import ntua.dblab.gskourts.streamingiot.util.avro.CountAndSum;

@Component
@ConditionalOnProperty(name="web-server-only", havingValue="false")
public class ProcessStreamService {
      private final Logger log = LoggerFactory.getLogger(ProcessStreamService.class);
      private final String tempInputTopic;
      private final AlertService alertService;
      private final String tempOutputTopic;
      private final String pressureInputTopic;
      private final String pressureOutputTopic;
      private final String powerInputTopic;
      private final String powerOutputTopic;
//      private final RedisTemplate redisTemplate;
      private final RedisCommands<String,String> redisCommands;
      private final InfluxDbConfiguration influxDbConfiguration;
      private final SpecificAvroSerde<CountAndSum> countAndSumSerde;
      @Value("${application.consumer.aggregator.aggregateWindowSizeSec}")
      private int aggregateWindowSizeSec;
      private InfluxDBWriter influxDBWriter;
      @Value("${application.consumer.aggregator.gracePeriodSec}")
      private int gracePeriodSec;

      ProcessStreamService(AppConf appConf, InfluxDbConfiguration influxDbConfiguration,
                           SpecificAvroSerde<CountAndSum> countAndSumSerde,
                            AlertService alertService,
                           RedisCommands redisCommands) {
            this.tempInputTopic = appConf.getTemperatureInputTopic();
            this.tempOutputTopic = appConf.getTemperatureOutputTopic();
            this.pressureInputTopic = appConf.getPressureInputTopic();
            this.pressureOutputTopic = appConf.getPressureOutputTopic();
            this.powerInputTopic = appConf.getPowerInputTopic();
            this.powerOutputTopic = appConf.getPowerOutputTopic();
            this.influxDbConfiguration = influxDbConfiguration;
            this.countAndSumSerde = countAndSumSerde;
            this.alertService = alertService;
            this.redisCommands = redisCommands;
            this.influxDBWriter = new InfluxDBWriter(influxDbConfiguration.getHost(), influxDbConfiguration.getPort(),
                    influxDbConfiguration.getToken(), influxDbConfiguration.getOrg(),
                    influxDbConfiguration.getBucket());
      }

      @Autowired
      public void process(StreamsBuilder builder) {
            log.info("START: Process");
            processTemperature(builder);
            processPower(builder);
            processPressure(builder);
      }

      @Async
      void processTemperature(StreamsBuilder builder) {
            log.info("START: Process Temperature");
            try {
                  KStream<Integer, Integer> stream = builder
                          .stream(tempInputTopic, Consumed.with(Serdes.Integer(), Serdes.Integer()))
                          .filter((key, value) -> key != null && value != null)
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
                          .peek((key, value) -> {
                                log.trace("Topic: {}. AGGREGATED: key={}, average={}",
                                        tempOutputTopic, key, value);
                                alertService.checkForAlerts(key.toString(), value, "temperature");

                                try {
                                        influxDBWriter.writeAggregatedData(key, "temperature", value.intValue(),
                                                System.currentTimeMillis(), String.format("temp-%s", key));
                                } catch (Exception e) {
                                        log.error("Error in writing to influxDB", e);
                                }
                                try {
                                        redisCommands.set(String.format("temp-%s", key), value.toString());
                                } catch (Exception e) {
                                        log.error("Error in writing to redis", e);
                                }
                          })
                          .to(tempOutputTopic, Produced.with(Serdes.Integer(), Serdes.Float()));
            } catch (Exception e) {
                  log.error("Error in processTemperature", e);
            }

            log.info("END: Process Temperature");
      }

      @Async
      void processPower(StreamsBuilder builder) {
            log.info("START: Process power");
            try {

                  KStream<Integer, Integer> stream = builder
                          .stream(powerInputTopic, Consumed.with(Serdes.Integer(), Serdes.Integer()))
                          .filter((key, value) -> key != null && value != null)
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
                          .peek((key, value) -> {
                                log.trace("Topic: {}. AGGREGATED: key={}, value={}",
                                        powerOutputTopic, key, value);
                                alertService.checkForAlerts(key.toString(), value, "power");
                                try {
                                        influxDBWriter.writeAggregatedData(key, "power", value, System.currentTimeMillis(),
                                                String.format("power-%s", key));
                                } catch (Exception e) {
                                        log.error("Error in writing to influxDB", e);
                                }
                                try {
                                        redisCommands.set(String.format("power-%s", key), value.toString());
                                } catch (Exception e) {
                                        log.error("Error in writing to redis", e);
                                }
                          })
                          .to(powerOutputTopic, Produced.with(Serdes.Integer(), Serdes.Integer()));
            } catch (Exception e) {
                  log.error("Error in processPower", e);
            }
            log.info("END: Process power");
      }

      @Async
      void processPressure(StreamsBuilder builder) {
            log.info("START: Process pressure");
            try{
                  KStream<Integer, Integer> stream = builder
                          .stream(pressureInputTopic, Consumed.with(Serdes.Integer(), Serdes.Integer()))
                          .filter((key, value) -> key != null && value != null)
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
                          .peek((key, value) ->{
                                log.trace("Topic: {}. AGGREGATED: key={}, value={}", pressureOutputTopic, key, value);
                                alertService.checkForAlerts(key.toString(), value, "pressure");
                                try{
                                      influxDBWriter.writeAggregatedData(key, "pressure", value.intValue(),
                                              System.currentTimeMillis(), String.format("pressure-%s", key));
                                } catch (Exception e){
                                      log.error("Error in writing to influxDB", e);
                                }
                                try{
                                      redisCommands.set(String.format("pressure-%s", key), value.toString());
                                } catch (Exception e){
                                      log.error("Error in writing to redis", e);
                                }
                          })
                          .to(pressureOutputTopic, Produced.with(Serdes.Integer(), Serdes.Float()));
            }
            catch (Exception e){
                  log.error("Error in processPressure", e);
                  // Handle the exception, for example, by sending an alert or falling back to a default behavior
            }
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
//} find / -name "superset*" 2>/dev/null