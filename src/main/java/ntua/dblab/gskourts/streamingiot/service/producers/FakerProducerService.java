package ntua.dblab.gskourts.streamingiot.service.producers;

import java.time.Duration;
import java.util.Map;

import net.datafaker.Faker;
import ntua.dblab.gskourts.streamingiot.util.AppConstants;

import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;



@RequiredArgsConstructor
@Service
@Qualifier("fakerDataProducer")
public class FakerProducerService implements DataProducerI {
   private final Logger log = LoggerFactory.getLogger(FakerProducerService.class);

   @Autowired
   @Qualifier("measurementTypeMap")
   private Map<Integer, String> measurementTypeMap;
   private final KafkaTemplate<Integer, Integer> kafkaTemplate;
   //private final Logger logger = LogManager.getLogger(FakerProducer.class);

   Faker faker;

   @Override
   @EventListener(ApplicationStartedEvent.class)
   public void generate() {
      faker = new Faker();
      Flux.interval(Duration.ofSeconds(1))
         .map(i -> {
            Integer measurementTypeInt = faker.random().nextInt(3);
            return new Pair<>(measurementTypeInt, getValueByType(measurementTypeInt));
         })
         .subscribe(pair -> {
            kafkaTemplate.send(AppConstants.TOPIC_MEASUREMENTS, pair.getValue0(), pair.getValue1());
            log.info("Sent: <{},{}>", pair.getValue0(), pair.getValue1());
         });
   }

   private Integer getValueByType(int type){
      if(type == 0){
         return Integer.parseInt(faker.weather().temperatureCelsius().replace("°C", ""));
      }else if(type == 1){
         return faker.random().nextInt(0,100);
      }else if(type == 2){
         return faker.random().nextInt(1000, 1100);
      }
      else{
         throw new IllegalArgumentException("Measurement type not supported");
      }
   }
}
