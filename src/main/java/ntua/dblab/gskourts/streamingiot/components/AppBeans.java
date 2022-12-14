package ntua.dblab.gskourts.streamingiot.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

import ntua.dblab.gskourts.streamingiot.util.AppConf;
import ntua.dblab.gskourts.streamingiot.util.AppConstants;

@Component
public class AppBeans {

   private final AppConf appConf;

   //@Autowired
   public AppBeans(AppConf appConf) {
      this.appConf = appConf;
   }

   @Bean
   NewTopic temperatureInputTopic() {
      return TopicBuilder.name(appConf.getTemperatureInputTopic()).partitions(appConf.getTemperatureDevicesNum())
            .replicas(appConf.getTemperatureReplicas())
            .build();
   }

   @Bean
   NewTopic temperatureOutputTopic() {
      return TopicBuilder.name(appConf.getTemperatureOutputTopic()).partitions(appConf.getTemperatureDevicesNum())
            .replicas(appConf.getTemperatureReplicas())
            .build();
   }

   @Bean
   NewTopic pressureInputTopic() {
      return TopicBuilder.name(appConf.getPressureInputTopic()).partitions(appConf.getPressureDevicesNum())
            .replicas(appConf.getPressureReplicas())
            .build();
   }

   @Bean
   NewTopic pressureOutputTopic() {
      return TopicBuilder.name(appConf.getPressureOutputTopic()).partitions(appConf.getPressureDevicesNum())
            .replicas(appConf.getPressureReplicas())
            .build();
   }

   @Bean
   NewTopic powerInputTopic() {
      return TopicBuilder.name(appConf.getPowerInputTopic()).partitions(appConf.getPowerDevicesNum())
            .replicas(appConf.getPowerReplicas())
            .build();
   }

   @Bean
   NewTopic powerOutputTopic() {
      return TopicBuilder.name(appConf.getPowerOutputTopic()).partitions(appConf.getPowerDevicesNum())
            .replicas(appConf.getPowerReplicas())
            .build();
   }

   @Bean
   NewTopic deadLetterTopic() {
      return TopicBuilder.name(appConf.getDeadLetterTopicName()).partitions(1)
            .replicas(1)
            .build();
   }

   @Bean
   @Qualifier("topicTypeMap")
   Map<Integer, String> topicTypeMap() {
      Map<Integer, String> topicTypeMap = new HashMap<>();
      topicTypeMap.put(appConf.getTemperatureMeasurementTopicId(), appConf.getTemperatureInputTopic());
      topicTypeMap.put(appConf.getPowerMeasurementTopicId(), appConf.getPowerInputTopic());
      topicTypeMap.put(appConf.getPressureMeasurementTopicId(), appConf.getPressureInputTopic());
      return topicTypeMap;
   }
}
