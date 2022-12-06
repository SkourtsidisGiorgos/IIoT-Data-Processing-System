package ntua.dblab.gskourts.streamingiot.components;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.stereotype.Component;

import ntua.dblab.gskourts.streamingiot.util.AppConstants;

@Component
public class AppBeans {

   @Bean
   NewTopic measurementsInputTopic() {
      return TopicBuilder.name(AppConstants.TOPIC_MEASUREMENTS).partitions(3).replicas(1).build();
   }

   @Bean
   NewTopic measurementsAggregatedTopic() {
      return TopicBuilder.name(AppConstants.TOPIC_AGGREGATED_MEASUREMENTS).partitions(3).replicas(1).build();
   }

   @Bean
   @Qualifier("measurementTypeMap")
   Map measurementTypeMap() {
      Map<Integer, String> measurementType = new HashMap<>();
      measurementType.put(0, "temperature");
      measurementType.put(1, "humidity");
      measurementType.put(2, "pressure");
      return measurementType;
   }
}
