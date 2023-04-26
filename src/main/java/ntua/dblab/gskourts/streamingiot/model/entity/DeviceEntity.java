package ntua.dblab.gskourts.streamingiot.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import ntua.dblab.gskourts.streamingiot.model.AreaCodeEnum;
import ntua.dblab.gskourts.streamingiot.model.MeasurementTypeEnum;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Slf4j
public class DeviceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private MeasurementTypeEnum measurementType;

    @Enumerated(EnumType.STRING)
    private AreaCodeEnum areaCode;

    @Enumerated(EnumType.STRING)
    private ActiveStatusEnum activeStatus;

    @LastModifiedBy
    private String lastModifiedBy;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @PrePersist
    public void prePersist() {
//        log.info("prePersist");
    }

    @PostPersist
    public void postPersist() {
//        log.info("postPersist");
    }

}
