package ntua.dblab.gskourts.streamingiot.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

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

@Configuration
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
@EnableMBeanExport(registration = RegistrationPolicy.REPLACE_EXISTING)
@EnableAspectJAutoProxy
@Getter
@Setter
@Generated
public class AppConf {
   private static final Logger LOG = LoggerFactory.getLogger(AppConf.class);
   @Autowired
   private ApplicationContext ctx;
   @Autowired
   private BuildProperties buildProperties;

   @Bean
   public OpenAPI openAPI() {
      return new OpenAPI()
            .info(new Info().title(String.format("%s API", buildProperties.getName()))
                  .description("Thesis by student Giorgos Skourtsidis")
                  .version(buildProperties.getVersion())
                  .license(new License().name("© 2022 by Giorgos Skourtsidis").url("https://www.ntua.gr/en/")));
   }

   @Value("${application.topics.measurementTopicsCount}")
   private int measurementTopicsCount;
   @Value("${application.topics.temperature.devicesNum}")
   private int temperatureDevicesNum;
   @Value("${application.topics.temperature.replicas}")
   private int temperatureReplicas;
   @Value("${application.topics.temperature.measurementTopicId}")
   private int temperatureMeasurementTopicId;
   @Value("${application.topics.temperature.name.input}")
   private String temperatureInputTopic;
   @Value("${application.topics.temperature.name.output}")
   private String temperatureOutputTopic;
   @Value("${application.topics.power.devicesNum}")
   private int powerDevicesNum;
   @Value("${application.topics.power.replicas}")
   private int powerReplicas;
   @Value("${application.topics.power.measurementTopicId}")
   private int powerMeasurementTopicId;
   @Value("${application.topics.power.name.input}")
   private String powerInputTopic;
   @Value("${application.topics.power.name.output}")
   private String powerOutputTopic;
   @Value("${application.topics.pressure.devicesNum}")
   private int pressureDevicesNum;
   @Value("${application.topics.pressure.replicas}")
   private int pressureReplicas;
   @Value("${application.topics.pressure.measurementTopicId}")
   private int pressureMeasurementTopicId;
   @Value("${application.topics.pressure.name.input}")
   private String pressureInputTopic;
   @Value("${application.topics.pressure.name.output}")
   private String pressureOutputTopic;

   @Value("${application.producer.produceIntervalSec}")
   private int produceIntervalSec;

   @Value("${spring.kafka.consumer.properties.deadletter.topic.name}")
   private String deadLetterTopicName;

   //@Bean
   //public CommonsRequestLoggingFilter requestLoggingFilter() {
   //   CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
   //   loggingFilter.setIncludeClientInfo(true);
   //   loggingFilter.setIncludeQueryString(true);
   //   loggingFilter.setIncludePayload(true);
   //   loggingFilter.setIncludeHeaders(true);
   //   return loggingFilter;
   //}
}
