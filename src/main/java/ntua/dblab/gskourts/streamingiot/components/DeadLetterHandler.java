package ntua.dblab.gskourts.streamingiot.components;

import org.springframework.kafka.annotation.DltHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeadLetterHandler {

   @DltHandler
   public void handle(String in) {
      log.info("Handling dead letter: " + in);
   }
}
