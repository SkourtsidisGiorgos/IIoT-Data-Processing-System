package ntua.dblab.gskourts.streamingiot.util;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.Data;
import lombok.Generated;

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
@Data
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
