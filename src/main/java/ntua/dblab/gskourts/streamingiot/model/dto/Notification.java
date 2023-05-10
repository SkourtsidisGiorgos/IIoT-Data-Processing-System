package ntua.dblab.gskourts.streamingiot.model.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import ntua.dblab.gskourts.streamingiot.model.AlertLevelEnum;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {
    private AlertLevelEnum status;
    private String message;
}
