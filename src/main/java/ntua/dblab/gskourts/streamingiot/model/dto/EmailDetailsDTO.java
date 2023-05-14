package ntua.dblab.gskourts.streamingiot.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetailsDTO {
   // Class data members
   private String messageBody;
   private String subject;
}
