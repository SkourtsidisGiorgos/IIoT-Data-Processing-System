package ntua.dblab.gskourts.streamingiot.components.actuator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class StartTimeInfoContributor implements InfoContributor {

   @Override
   public void contribute(Info.Builder builder) {
      Map<String, String> appDetails = new HashMap<>();
      // Get the current timestamp
      appDetails.put("startTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
      builder.withDetail("app", appDetails);
   }
}
