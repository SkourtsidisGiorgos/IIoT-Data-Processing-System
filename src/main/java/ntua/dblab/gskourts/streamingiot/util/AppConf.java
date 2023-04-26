package ntua.dblab.gskourts.streamingiot.util;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.*;
import ntua.dblab.gskourts.streamingiot.util.avro.CountAndSum;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.Data;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;

import org.apache.kafka.streams.StreamsConfig;
import static java.lang.Integer.parseInt;
import static java.lang.Short.parseShort;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.apache.kafka.common.serialization.Serdes.Double;
import static org.apache.kafka.common.serialization.Serdes.Long;
import static org.apache.kafka.streams.StreamsConfig.APPLICATION_ID_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG;
import static org.apache.kafka.streams.StreamsConfig.REPLICATION_FACTOR_CONFIG;

@Configuration
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
@EnableMBeanExport(registration = RegistrationPolicy.REPLACE_EXISTING)
@EnableAspectJAutoProxy
@Getter
@Setter
@Slf4j
@Generated
@RequiredArgsConstructor
public class AppConf {
   private final ApplicationContext ctx;
   private final BuildProperties buildProperties;

   @Bean
   public OpenAPI openAPI() {
      return new OpenAPI()
            .info(new Info().title(String.format("%s API", buildProperties.getName()))
                  .description("NTUA Thesis by student Giorgos Skourtsidis")
                  .version(buildProperties.getVersion())
                  .license(new License().name("© 2023 by Giorgos Skourtsidis").url("https://www.ntua.gr/en/")));
   }

   @Value("${application.topics.measurementTopicsCount}")
   private Integer measurementTopicsCount;
   @Value("${application.topics.temperature.devicesNum}")
   private Integer temperatureDevicesNum;
   @Value("${application.topics.temperature.replicas}")
   private Integer temperatureReplicas;
   @Value("${application.topics.temperature.measurementTopicId}")
   private Integer temperatureMeasurementTopicId;
   @Value("${application.topics.temperature.name.input}")
   private String temperatureInputTopic;
   @Value("${application.topics.temperature.name.output}")
   private String temperatureOutputTopic;
   @Value("${application.topics.power.devicesNum}")
   private Integer powerDevicesNum;
   @Value("${application.topics.power.replicas}")
   private Integer powerReplicas;
   @Value("${application.topics.power.measurementTopicId}")
   private Integer powerMeasurementTopicId;
   @Value("${application.topics.power.name.input}")
   private String powerInputTopic;
   @Value("${application.topics.power.name.output}")
   private String powerOutputTopic;
   @Value("${application.topics.pressure.devicesNum}")
   private Integer pressureDevicesNum;
   @Value("${application.topics.pressure.replicas}")
   private Integer pressureReplicas;
   @Value("${application.topics.pressure.measurementTopicId}")
   private Integer pressureMeasurementTopicId;
   @Value("${application.topics.pressure.name.input}")
   private String pressureInputTopic;
   @Value("${application.topics.pressure.name.output}")
   private String pressureOutputTopic;

   @Value("${application.producer.produceIntervalSec}")
   private Integer produceIntervalSec;

   @Value("${spring.kafka.consumer.properties.deadletterqueue.topic.name}")
   private String deadLetterTopicName;

   @Value("${spring.kafka.properties.bootstrap.servers}")
    private String bootstrapServers;
   //@Bean
   //public CommonsRequestLoggingFilter requestLoggingFilter() {
   //   CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
   //   loggingFilter.setIncludeClientInfo(true);
   //   loggingFilter.setIncludeQueryString(true);
   //   loggingFilter.setIncludePayload(true);
   //   loggingFilter.setIncludeHeaders(true);
   //   return loggingFilter;
   //}

   @Value("${application.schema.registry.host}")
   private String schemaRegistryHost;
   @Value("${application.schema.registry.port}")
   private String schemaRegistryPort;

   //@Value("${spring.kafka.properties.schema.registry.url}")
   //private String schemaRegistryHost;
   //@Value("${spring.kafka.properties.schema.registry.port}")
   //private String schemaRegistryPort;

   @Bean
   public SpecificAvroSerde<CountAndSum> countAndSumSerde() {
      SpecificAvroSerde<CountAndSum> serde = new SpecificAvroSerde<>();
      Map<String, String> serdeConfig = new HashMap<>();
      serdeConfig.put("schema.registry.url", String.format("http://%s:%s", schemaRegistryHost, schemaRegistryPort));
      serde.configure(serdeConfig, false);
      return serde;
   }
}
