package ntua.dblab.gskourts.streamingiot.util.serdes;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IntegerArrayDeserializer implements Deserializer<Integer[]> {

   private static final String CHARSET = "UTF-8";
   private static final Logger LOG = LoggerFactory.getLogger(IntegerArrayDeserializer.class);

   @Override
   public void configure(Map<String, ?> configs, boolean isKey) {
      // No-op
   }

   @Override
   public Integer[] deserialize(String topic, byte[] data) {
      if (data == null) {
         return null;
      }

      String[] strings;
      try {
         strings = new String(data, CHARSET).split(",");
      } catch (UnsupportedEncodingException e) {
         LOG.error("Error while deserializing", e);
         return null;
      }
      Integer[] integers = new Integer[strings.length];

      for (int i = 0; i < strings.length; i++) {
         integers[i] = Integer.parseInt(strings[i]);
      }

      return integers;
   }

   @Override
   public void close() {
      // No-op
   }

}
