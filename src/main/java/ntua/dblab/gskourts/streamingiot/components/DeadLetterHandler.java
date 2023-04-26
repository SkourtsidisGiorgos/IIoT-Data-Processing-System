package ntua.dblab.gskourts.streamingiot.components;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.DltHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;

@Slf4j
@ConditionalOnProperty(name="web-server-only", havingValue="false")
public class DeadLetterHandler {

   @DltHandler
   public void handle(String message,
                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                      @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key) {
        log.error("Dead letter handler: message = {}, topic = {}, key = {}", message, topic, key);
   }
}
