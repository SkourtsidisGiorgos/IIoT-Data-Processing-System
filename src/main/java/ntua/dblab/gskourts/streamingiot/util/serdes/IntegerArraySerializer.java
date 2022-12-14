package ntua.dblab.gskourts.streamingiot.util.serdes;

import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

public class IntegerArraySerializer implements Serializer<Integer[]> {

   @Override
   public void configure(Map<String, ?> configs, boolean isKey) {
      // No-op
   }

   @Override
   public byte[] serialize(String topic, Integer[] data) {
      if (data == null) {
         return null;
      }

      byte[] serializedData = new byte[data.length * 4];
      for (int i = 0; i < data.length; i++) {
         ByteBuffer.wrap(serializedData, i * 4, 4).putInt(data[i]);
      }

      return serializedData;
   }

   @Override
   public void close() {
      // No-op
   }
}
