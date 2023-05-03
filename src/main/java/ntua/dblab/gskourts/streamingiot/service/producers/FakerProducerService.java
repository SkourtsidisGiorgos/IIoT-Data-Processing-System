package ntua.dblab.gskourts.streamingiot.service.producers;

import java.time.Duration;
import java.util.Map;

import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import ntua.dblab.gskourts.streamingiot.util.AppConf;
import reactor.core.publisher.Flux;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("fakerDataProducer")
@ConditionalOnProperty(name="application.producer.fakerProducer.enabled", havingValue="true")
public class FakerProducerService implements DataProducerI {
   private final Map<Integer, String> topicTypeMap;
   private final KafkaTemplate<Integer, Integer> kafkaTemplate;
   private final AppConf appConf;

   @Autowired
   @Qualifier("activeDevicesMap")
   private ConcurrentHashMap<String, ActiveStatusEnum> activeDevicesMap;

   @Value("${application.producer.fakeProducer.enabled}")
   private boolean enabled;
   Integer currKey;

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
   @ConditionalOnProperty(name="application.producer.fakerProducer.enabled", havingValue="true")
//   @ConditionalOnProperty(name="web-server-only", havingValue="false")
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
               String topic = topicTypeMap.get(pair.getValue0());
               String measurementCategory = topic.replace("streaming.input.", "").replace("Measurements", "");
               Integer deviceIdInt = getKey(pair.getValue0());
               String device = String.format("%s-%s", measurementCategory, getKey(pair.getValue0()));
               //               log.trace("Sending message to topic: {}, device: {}, value: {}", topic, device, pair.getValue1());
               if (activeDevicesMap.get(device) == ActiveStatusEnum.INACTIVE) {
                  log.trace("Device {} is inactive, skipping message", device);
                  return;
               }

               kafkaTemplate.send(topic, deviceIdInt, pair.getValue1());
            });
   }

   private Integer getValueByType(int type) {
      if (type == 0) { // temperature
         return Integer.parseInt(faker.weather().temperatureCelsius().replace("°C", ""));
      } else if (type == 1) { // pressure
         return faker.random().nextInt(3, 222);
      } else if (type == 2) { // power
         return faker.random().nextInt(2500, 6900);
      } else {
         throw new IllegalArgumentException(String.format("Measurement type %s not supported", type));
      }
   }

   private Integer getKey(Integer topicId) {
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
