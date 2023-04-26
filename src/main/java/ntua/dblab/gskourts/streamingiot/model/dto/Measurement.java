package ntua.dblab.gskourts.streamingiot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("LatestMeasurement")
public class Measurement implements Serializable {
   private String value;
   private int measurementType;
   private long timestamp;
   private String sensorId;
}
