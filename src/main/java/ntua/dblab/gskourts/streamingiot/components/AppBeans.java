package ntua.dblab.gskourts.streamingiot.components;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import ntua.dblab.gskourts.streamingiot.util.AppConf;
import ntua.dblab.gskourts.streamingiot.util.AppConstants;
import org.springframework.util.StringUtils;

@Component
public class AppBeans {

   private final AppConf appConf;

   //@Autowired
   public AppBeans(AppConf appConf) {
      this.appConf = appConf;
   }

   @Bean
   @ConditionalOnProperty(name="web-server-only", havingValue="false")
   public KafkaAdmin kafkaAdmin() {
      Map<String, Object> configs = new HashMap<>();
      configs.put(org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
            appConf.getBootstrapServers());
      return new KafkaAdmin(configs);
   }

   //   @Bean
   //   public KafkaAdminClient kafkaAdminClient(KafkaAdmin kafkaAdmin) {
   //      return (KafkaAdminClient) KafkaAdminClient.create(kafkaAdmin.getConfigurationProperties());
   //   }

   @Bean
   @ConditionalOnProperty(name="web-server-only", havingValue="false")
   NewTopic temperatureInputTopic() {
      return TopicBuilder.name(appConf.getTemperatureInputTopic()).partitions(appConf.getTemperatureDevicesNum())
            .replicas(appConf.getTemperatureReplicas())
            .build();
   }

   @Bean
   @ConditionalOnProperty(name="web-server-only", havingValue="false")
   NewTopic temperatureOutputTopic() {
      return TopicBuilder.name(appConf.getTemperatureOutputTopic()).partitions(appConf.getTemperatureDevicesNum())
            .replicas(appConf.getTemperatureReplicas())
            .build();
   }

   @Bean
   @ConditionalOnProperty(name="web-server-only", havingValue="false")
   NewTopic pressureInputTopic() {
      return TopicBuilder.name(appConf.getPressureInputTopic()).partitions(appConf.getPressureDevicesNum())
            .replicas(appConf.getPressureReplicas())
            .build();
   }

   @Bean
   @ConditionalOnProperty(name="web-server-only", havingValue="false")
   NewTopic pressureOutputTopic() {
      return TopicBuilder.name(appConf.getPressureOutputTopic()).partitions(appConf.getPressureDevicesNum())
            .replicas(appConf.getPressureReplicas())
            .build();
   }

   @Bean
   @ConditionalOnProperty(name="web-server-only", havingValue="false")
   NewTopic powerInputTopic() {
      return TopicBuilder.name(appConf.getPowerInputTopic()).partitions(appConf.getPowerDevicesNum())
            .replicas(appConf.getPowerReplicas())
            .build();
   }

   @Bean
   @ConditionalOnProperty(name="web-server-only", havingValue="false")
   NewTopic powerOutputTopic() {
      return TopicBuilder.name(appConf.getPowerOutputTopic()).partitions(appConf.getPowerDevicesNum())
            .replicas(appConf.getPowerReplicas())
            .build();
   }

   @Bean
   @ConditionalOnProperty(name="web-server-only", havingValue="false")
   NewTopic deadLetterTopic() {
      return TopicBuilder.name(appConf.getDeadLetterTopicName()).partitions(1)
            .replicas(1)
            .build();
   }

   @Bean
   @ConditionalOnProperty(name="web-server-only", havingValue="false")
   @Qualifier("topicTypeMap")
   Map<Integer, String> topicTypeMap() {
      Map<Integer, String> topicTypeMap = new HashMap<>();
      topicTypeMap.put(appConf.getTemperatureMeasurementTopicId(), appConf.getTemperatureInputTopic());
      topicTypeMap.put(appConf.getPowerMeasurementTopicId(), appConf.getPowerInputTopic());
      topicTypeMap.put(appConf.getPressureMeasurementTopicId(), appConf.getPressureInputTopic());
      return topicTypeMap;
   }

   // define a thread safe Map to store the active devices
   @Bean
   @Qualifier("activeDevicesMap")
   ConcurrentHashMap<String, ActiveStatusEnum> activeDevicesMap() {
      ConcurrentHashMap<String, ActiveStatusEnum> activeDevicesMap = new ConcurrentHashMap<>();
      for (int i = 0; i < appConf.getTemperatureDevicesNum(); i++) {
         activeDevicesMap.put(AppConstants.TEMPERATURE_DEVICE_PREFIX + i, ActiveStatusEnum.ACTIVE);
      }
      for (int i = 0; i < appConf.getPressureDevicesNum(); i++) {
         activeDevicesMap.put(AppConstants.PRESSURE_DEVICE_PREFIX + i, ActiveStatusEnum.ACTIVE);
      }
      for (int i = 0; i < appConf.getPowerDevicesNum(); i++) {
         activeDevicesMap.put(AppConstants.POWER_DEVICE_PREFIX + i, ActiveStatusEnum.ACTIVE);
      }
      return activeDevicesMap;
   }

   //   @Bean
   //   JedisConnectionFactory jedisConnectionFactory() {
   //      return new JedisConnectionFactory();
   //   }
   //
   //   @Bean
   //   public RedisTemplate<String, Object> redisTemplate() {
   //      RedisTemplate<String, Object> template = new RedisTemplate<>();
   //      template.setConnectionFactory(jedisConnectionFactory());
   //      return template;
   //   }


//   @Bean
//   public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier,
//                                                                        ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier,
//                                                                        EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties,
//                                                                        WebEndpointProperties webEndpointProperties, Environment environment) {
//      List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
//      Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
//      allEndpoints.addAll(webEndpoints);
//      allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
//      allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
//      String basePath = webEndpointProperties.getBasePath();
//      EndpointMapping endpointMapping = new EndpointMapping(basePath);
//      boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment,
//              basePath);
//      return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
//              corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
//              shouldRegisterLinksMapping, null);
//   }
//
//   private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment,
//                                              String basePath) {
//      return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
//              || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
//   }
}
