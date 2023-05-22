package ntua.dblab.gskourts.streamingiot.service.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ntua.dblab.gskourts.streamingiot.util.Utils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.*;

//this class will be a Kafka consumer
//it will consume the raw data from the Kafka topic
@Service
@Slf4j
@RequiredArgsConstructor
public class RawDataConsumer {

    private final SimpMessagingTemplate template;

    @KafkaListener(topics = "${application.topics.temperature.name.input}")
    public void consume(ConsumerRecord<Integer, Integer> record) {
        Map<String, Object> data = new HashMap<>();
        data.put("key", record.key());
        data.put("value", record.value());
        data.put("timestamp", record.timestamp()); // Include the consume timestamp
        template.convertAndSend("/topic/temperature", data);
    }
}



//------------------------------------VERSION 2--------------------------------------------------------
//-------------------------- AGGREGATE EVERY 3 SECOND -------------------------------------------------
//-----------------------------------------------------------------------------------------------------
//package ntua.dblab.gskourts.streamingiot.service.consumers;
//
//        import lombok.RequiredArgsConstructor;
//        import lombok.extern.slf4j.Slf4j;
//        import ntua.dblab.gskourts.streamingiot.util.Utils;
//        import org.apache.kafka.clients.consumer.ConsumerRecord;
//        import org.springframework.beans.factory.annotation.Value;
//        import org.springframework.kafka.annotation.KafkaListener;
//        import org.springframework.messaging.handler.annotation.Payload;
//        import org.springframework.messaging.simp.SimpMessagingTemplate;
//        import org.springframework.stereotype.Service;
//
//        import javax.annotation.PostConstruct;
//        import javax.annotation.PreDestroy;
//        import java.util.*;
//        import java.util.concurrent.*;
//
////this class will be a Kafka consumer
////it will consume the raw data from the Kafka topic
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class RawDataConsumer {
//
//    private final SimpMessagingTemplate template;
//    private ConcurrentMap<Integer, List<Integer>> buffer = new ConcurrentHashMap<>();
//    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
//
//    @KafkaListener(topics = "${application.topics.temperature.name.input}")
//    public void consume(ConsumerRecord<Integer, Integer> record) {
//        Integer key = record.key();
//        Integer value = record.value();
//        buffer.computeIfAbsent(key, k -> Collections.synchronizedList(new ArrayList<>())).add(value);
//    }
//    @PostConstruct
//    private void init() {
//        executor.scheduleAtFixedRate(() -> {
//            buffer.forEach((key, values) -> {
//                if (!values.isEmpty()) {
//                    double average = values.stream().mapToInt(Integer::intValue).average().orElse(0.0);
//                    Map<String, Object> data = new HashMap<>();
//                    data.put("key", key);
//                    data.put("value", average);
//                    data.put("timestamp", System.currentTimeMillis());
//                    template.convertAndSend("/topic/temperature", data);
//                    values.clear();
//                }
//            });
//        }, 0, 3, TimeUnit.SECONDS);
//    }
//
//    @PreDestroy
//    private void destroy() {
//        executor.shutdownNow();
//    }
//
//}
//
//
