package ntua.dblab.gskourts.streamingiot.model.dto;

import lombok.*;
import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import ntua.dblab.gskourts.streamingiot.model.AreaCodeEnum;
import ntua.dblab.gskourts.streamingiot.model.MeasurementTypeEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeviceDTO {
    private String deviceId;
    private MeasurementTypeEnum deviceType;
    private ActiveStatusEnum deviceStatus;
    private AreaCodeEnum deviceAreaCode;
    private String deviceDescription;
}
