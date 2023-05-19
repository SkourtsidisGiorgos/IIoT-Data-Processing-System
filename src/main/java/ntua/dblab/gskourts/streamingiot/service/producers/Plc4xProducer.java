package ntua.dblab.gskourts.streamingiot.service.producers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import org.apache.plc4x.java.PlcDriverManager;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.messages.PlcReadRequest;
import org.apache.plc4x.java.api.messages.PlcReadResponse;
import org.apache.plc4x.java.api.types.PlcResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import ntua.dblab.gskourts.streamingiot.util.AppConf;
import ntua.dblab.gskourts.streamingiot.util.AppConstants;

@Service
@Slf4j
@Qualifier("plc4xProducer")
@ConditionalOnProperty(name="application.producer.plc4xProducer.enabled", havingValue="true")
public class Plc4xProducer {
   private final Logger LOG = LoggerFactory.getLogger(Plc4xProducer.class);
   private final KafkaTemplate<Integer, Integer> kafkaTemplate;
   @Autowired
   @Qualifier("topicTypeMap")
   private Map<Integer, String> topicTypeMap;

   @Autowired
   @Qualifier("activeDevicesMap")
   private ConcurrentHashMap<String, ActiveStatusEnum> activeDevicesMap;

   @Value("${application.producer.produceIntervalSec}")
   private int produceIntervalSec;
   @Value("${application.producer.plc4xProducer.enabled}")
   private boolean enabled;

   @Value("${application.modbusPal.host}")
   private String modbusPalHost;
   @Value("${application.modbusPal.port}")
   private int modbusPalPort;

   private AppConf appConf;

   public Plc4xProducer(AppConf appConf, KafkaTemplate<Integer, Integer> kafkaTemplate) {
      this.appConf = appConf;
      this.kafkaTemplate = kafkaTemplate;
   }

   @EventListener(ApplicationStartedEvent.class)
   private PlcReadResponse readModbus() {
       if (!enabled) {
           LOG.info("Plc4xProducer is disabled");
           return null;
       }
       String connectionString = "modbus-tcp://" + modbusPalHost + ":" + modbusPalPort;
       try (PlcConnection plcConnection = new PlcDriverManager().getConnection(connectionString)) {
           // Check if this connection support reading of data.
           canReadVirtualModbus(plcConnection);

           LOG.info("Create a new read request");
           // Create a new read request:
           // - Give the single item requested the alias name "value"
           PlcReadRequest.Builder builder = plcConnection.readRequestBuilder();
//            builder.addItem("value-1", "coil:1");
//            builder.addItem("value-2", "coil:3[4]");
//            builder.addItem("value-3", "holding-register:1");
//            builder.addItem("value-4", "holding-register:3[4]");

           builder.addItem("pressure-1", "holding-register:2");
           builder.addItem("pressure-2", "holding-register:3");
           builder.addItem("pressure-3", "holding-register:4");
           builder.addItem("power-1", "holding-register:5");
           builder.addItem("power-2", "holding-register:6");
           builder.addItem("power-3", "holding-register:7");
           builder.addItem("temperature-1", "holding-register:8");
           builder.addItem("temperature-2", "holding-register:9");
           builder.addItem("temperature-3", "holding-register:10");
           builder.addItem("temperature-4", "holding-register:11");

           PlcReadRequest readRequest = builder.build();
//            logger.info("Request - " + readRequest);

           while (true){
               Thread.sleep(1000);
               PlcReadResponse response = readRequest.execute().get();
//                logger.info("Response - " + response);
               processPlcResponse(response);
           }
       } catch (Exception ex) {
           throw new RuntimeException(ex);
       }
   }

   private void processPlcResponse(PlcReadResponse response) {
      for (String fieldName : response.getFieldNames()) {
         if (response.getResponseCode(fieldName) == PlcResponseCode.OK) {
            int numValues = response.getNumberOfValues(fieldName);
            // If it's just one element, output just one single line.
            if (numValues == 1) {
                Integer key = Integer.parseInt(fieldName.split("-")[1]);
                Integer value = ((Short) response.getObject(fieldName)).intValue();
//                LOG.info("Value[" + fieldName + "]: " + value);
//              if name contains pressure, power or temperature
                if (fieldName.contains("pressure")) {
                    ActiveStatusEnum status = activeDevicesMap.get(AppConstants.PRESSURE_DEVICE_PREFIX + key);
                    if (status == null || status.equals(ActiveStatusEnum.INACTIVE)) {
                        log.trace("{} status: {}", AppConstants.PRESSURE_DEVICE_PREFIX + key, status);
                        continue;
                    }
                    kafkaTemplate.send(AppConstants.TOPIC_PRESSURE_INPUT, key, value);
                } else if (fieldName.contains("power")) {
                    ActiveStatusEnum status = activeDevicesMap.get(AppConstants.POWER_DEVICE_PREFIX + key);
                    if (status == null || status.equals(ActiveStatusEnum.INACTIVE)) {
                        log.trace("{} status: {}", AppConstants.POWER_DEVICE_PREFIX + key, status);
                        continue;
                    }
                    kafkaTemplate.send(AppConstants.TOPIC_POWER_INPUT, key, value);
                } else if (fieldName.contains("temperature")) {
                    ActiveStatusEnum status = activeDevicesMap.get(AppConstants.TEMPERATURE_DEVICE_PREFIX + key);
                    if (status == null || status.equals(ActiveStatusEnum.INACTIVE)) {
                        log.trace("{} status: {}", AppConstants.TEMPERATURE_DEVICE_PREFIX + key, status);
                        continue;
                    }
                    kafkaTemplate.send(AppConstants.TOPIC_TEMPERATURE_INPUT, key, value);
                }

            }
            // If it's more than one element, output each in a single row.
            else {
               LOG.info("Value[" + fieldName + "]:");
               for (int i = 0; i < numValues; i++) {
                  LOG.info(" - " + response.getObject(fieldName, i));
               }
            }
         }
         // Something went wrong, to output an error message instead.
         else {
            LOG.error("Error[" + fieldName + "]: " + response.getResponseCode(fieldName).name());
         }
      }

   }

   private boolean canReadVirtualModbus(PlcConnection plcConnection) {
      // Check if this connection support reading of data.
      if (!plcConnection.getMetadata().canRead()) {
         LOG.error("This connection doesn't support reading.");
         return false;
      }
      return true;
   }
}
