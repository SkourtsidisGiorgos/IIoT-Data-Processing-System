package ntua.dblab.gskourts.streamingiot.service.producers;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.plc4x.java.PlcDriverManager;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.messages.PlcReadRequest;
import org.apache.plc4x.java.api.messages.PlcReadResponse;
import org.apache.plc4x.java.api.types.PlcResponseCode;
import org.javatuples.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ntua.dblab.gskourts.streamingiot.util.AppConstants;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Service
@Qualifier("plc4xProducer")
public class Plc4xProducer implements DataProducerI {
   private final Logger LOG = LoggerFactory.getLogger(Plc4xProducer.class);
   private final KafkaTemplate<Integer, Integer> kafkaTemplate;
   @Autowired
   @Qualifier("measurementTypeMap")
   private Map<Integer, String> measurementTypeMap;
   @Value("${application.producer.produceIntervalSec}")
   private int produceIntervalSec;
   @Value("${application.producer.plc4xProducer.enabled}")
   private boolean enabled;

   @Value("${application.modbusPal.host}")
   private String modbusPalHost;
   @Value("${application.modbusPal.port}")
   private int modbusPalPort;

   @Override
   //@EventListener(ApplicationStartedEvent.class)
   public void generate() {
      if (!enabled) {
         LOG.info("Plc4xProducer is disabled");
         return;
      }
      String connectionString = String.format("modbus-tcp://%s:%d", modbusPalHost, modbusPalPort);
      LOG.trace("Connecting to {}", connectionString);

      try (PlcConnection plcConnection = new PlcDriverManager().getConnection(connectionString)) {
         if (!plcConnection.isConnected()) {
            LOG.error("Could not connect to {}", connectionString);
            return;
         }
         if (!canReadVirtualModbus(plcConnection)) {
            return;
         }

         Flux.interval(Duration.ofSeconds(produceIntervalSec))
               .map(i -> {
                  PlcReadResponse response = readModbus(plcConnection);
                  processPlcResponse(response);
                  return new Pair<Integer, Integer>(1, response.getInteger("value-3"));
               })
               .subscribe(pair -> {
                  kafkaTemplate.send(AppConstants.TOPIC_MEASUREMENTS, pair.getValue0(), pair.getValue1());
                  LOG.info("Sent: <{},{}>", pair.getValue0(), pair.getValue1());
               });

      } catch (Exception e) {
         LOG.error("Error connecting to ModbusPal", e);
      }

   }

   private PlcReadResponse readModbus(PlcConnection plcConnection) {
      // Create a new read request:
      // - Give the single item requested the alias name "value"
      PlcReadRequest.Builder builder = plcConnection.readRequestBuilder();
      //builder.addItem("value-1", "coil:1");
      //builder.addItem("value-2", "coil:3[4]");
      builder.addItem("value-3", "holding-register:1");
      //builder.addItem("value-4", "holding-register:3[4]");
      PlcReadRequest readRequest = builder.build();
      PlcReadResponse response;
      try {
         LOG.debug("Reading registers from ModbusPal");
         response = readRequest.execute().get();
      } catch (InterruptedException | ExecutionException e) {
         LOG.error("", e);
         return null;
      }
      return response;

   }

   private void processPlcResponse(PlcReadResponse response) {
      for (String fieldName : response.getFieldNames()) {
         if (response.getResponseCode(fieldName) == PlcResponseCode.OK) {
            int numValues = response.getNumberOfValues(fieldName);
            // If it's just one element, output just one single line.
            if (numValues == 1) {
               LOG.info("Value[" + fieldName + "]: " + response.getObject(fieldName));
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
