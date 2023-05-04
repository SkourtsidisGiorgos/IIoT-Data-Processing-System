package ntua.dblab.gskourts.streamingiot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.boot.web.context.WebServerPortFileWriter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import lombok.RequiredArgsConstructor;
import ntua.dblab.gskourts.streamingiot.util.AppConstants;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAdminServer
@EnableKafkaStreams
//@EnableWebMvc
@OpenAPIDefinition
@SpringBootApplication
//@ComponentScan(basePackages = {"ntua.dblab.gskourts.streamingiot"})
public class StreamingIotApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(StreamingIotApplication.class);
    app.setApplicationStartup(new BufferingApplicationStartup(1024));
    app.addListeners(new WebServerPortFileWriter("streaming-iot.port"));
    app.addListeners(new ApplicationPidFileWriter("streaming-iot.pid"));
    app.run(args);

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