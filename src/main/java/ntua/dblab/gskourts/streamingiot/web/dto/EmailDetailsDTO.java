package ntua.dblab.gskourts.streamingiot.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetailsDTO {
   // Class data members
   private String sender;
   private String recipientTo;
   private String recipientCc;
   private String recipientBcc;
   private String messageBody;
   private String subject;

}
