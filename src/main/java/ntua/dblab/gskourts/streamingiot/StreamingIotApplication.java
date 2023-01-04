package ntua.dblab.gskourts.streamingiot;

import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.RequiredArgsConstructor;
import ntua.dblab.gskourts.streamingiot.util.AppConstants;

@EnableAdminServer
@EnableKafkaStreams
@SpringBootApplication
public class StreamingIotApplication {

  public static void main(String[] args) {
    SpringApplication.run(StreamingIotApplication.class, args);
  }
}

//@RestController
//@RequiredArgsConstructor
//class RestService {

//  private final StreamsBuilderFactoryBean factoryBean;

//  @GetMapping("/aggregatedMeasurements/count/{partition}")
//  public Long getAggregatedMeasurementsCount(@PathVariable String partition) {
//    final KafkaStreams kafkaStreams = factoryBean.getKafkaStreams();
//    if (kafkaStreams == null) {
//      return -1L;
//    }
//    final ReadOnlyKeyValueStore<String, Long> aggregatedMeasurements;
//    aggregatedMeasurements = kafkaStreams.store(StoreQueryParameters
//        .fromNameAndType(AppConstants.TOPIC_AGGREGATED_MEASUREMENTS, QueryableStoreTypes.keyValueStore()));
//    return aggregatedMeasurements.get(partition);
//  }
//}