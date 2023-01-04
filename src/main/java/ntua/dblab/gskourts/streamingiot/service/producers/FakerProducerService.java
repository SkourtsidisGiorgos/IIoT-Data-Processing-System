package ntua.dblab.gskourts.streamingiot.service.producers;

import java.time.Duration;
import java.util.Map;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import ntua.dblab.gskourts.streamingiot.util.AppConf;
import ntua.dblab.gskourts.streamingiot.util.AppConstants;
import reactor.core.publisher.Flux;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("fakerDataProducer")
public class FakerProducerService implements DataProducerI {
   private final Map<Integer, String> topicTypeMap;
   private final KafkaTemplate<Integer, Integer> kafkaTemplate;
   private final AppConf appConf;
   @Value("${application.producer.produceIntervalSec}")
   private int produceIntervalSec;
   @Value("${application.producer.fakeProducer.enabled}")
   private boolean enabled;

   Faker faker;

   @Autowired
   public FakerProducerService(AppConf appConf, KafkaTemplate<Integer, Integer> kafkaTemplate,
         @Qualifier("topicTypeMap") Map<Integer, String> topicTypeMap) {
      this.appConf = appConf;
      this.kafkaTemplate = kafkaTemplate;
      this.topicTypeMap = topicTypeMap;
   }

   @Override
   @EventListener(ApplicationStartedEvent.class)
   public void generate() {
      if (!enabled) {
         log.trace("FakerProducerService is disabled");
         return;
      }
      faker = new Faker();
      Flux.interval(Duration.ofSeconds(appConf.getProduceIntervalSec()))
            .map(i -> {
               Integer topicId = faker.random().nextInt(appConf.getMeasurementTopicsCount()); // topicId = measurementTypeInt
               return new Pair<>(topicId, getValueByType(topicId));
            })
            .subscribe(pair -> {
               kafkaTemplate.send(topicTypeMap.get(pair.getValue0()), getPartition(pair.getValue0()), pair.getValue1());
            });
   }

   private Integer getValueByType(int type) {
      if (type == 0) {
         return Integer.parseInt(faker.weather().temperatureCelsius().replace("°C", ""));
      } else if (type == 1) {
         return faker.random().nextInt(3, 222);
      } else if (type == 2) {
         return faker.random().nextInt(2500, 6900);
      } else {
         throw new IllegalArgumentException(String.format("Measurement type %s not supported", type));
      }
   }

   private Integer getPartition(int topicId) {
      if (topicId == 0) {
         return faker.random().nextInt(0, appConf.getTemperatureDevicesNum());
      } else if (topicId == 1) {
         return faker.random().nextInt(0, appConf.getPowerDevicesNum());
      } else if (topicId == 2) {
         return faker.random().nextInt(0, appConf.getPressureDevicesNum());
      } else {
         throw new IllegalArgumentException(String.format("Measurement type %s not supported", topicId));
      }

   }
}
