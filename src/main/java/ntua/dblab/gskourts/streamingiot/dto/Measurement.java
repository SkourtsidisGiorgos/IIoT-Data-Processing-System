package ntua.dblab.gskourts.streamingiot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Measurement {
      private String value;
      private int measurementType;
      // TODO: add timestamp
}
