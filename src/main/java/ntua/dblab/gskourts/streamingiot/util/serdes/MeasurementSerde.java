//package ntua.dblab.gskourts.streamingiot.util.serdes;
//
//import ntua.dblab.gskourts.streamingiot.dto.Measurement;
//import org.apache.kafka.common.serialization.Deserializer;
//import org.apache.kafka.common.serialization.Serde;
//import org.apache.kafka.common.serialization.Serializer;
//
//import java.util.Map;
//
//public class MeasurementSerde implements Serde<Measurement> {
//
//   private final Serializer<Measurement> serializer;
//   private final Deserializer<Measurement> deserializer;
//
//   public MeasurementSerde() {
//      this.serializer = new JsonPOJOSerializer<>();
//      this.deserializer = new JsonPOJODeserializer<>();
//   }
//
//   @Override
//   public Serializer<Measurement> serializer() {
//      return serializer;
//   }
//
//   @Override
//   public Deserializer<Measurement> deserializer() {
//      return deserializer;
//   }
//
//   @Override
//   public void configure(Map<String, ?> configs, boolean isKey) {
//      serializer.configure(configs, isKey);
//      deserializer.configure(configs, isKey);
//   }
//
//   @Override
//   public void close() {
//      serializer.close();
//      deserializer.close();
//   }
//}
