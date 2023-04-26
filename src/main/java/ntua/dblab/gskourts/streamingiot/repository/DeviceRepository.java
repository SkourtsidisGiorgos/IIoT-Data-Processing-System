package ntua.dblab.gskourts.streamingiot.repository;


import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import ntua.dblab.gskourts.streamingiot.model.AreaCodeEnum;
import ntua.dblab.gskourts.streamingiot.model.MeasurementTypeEnum;
import ntua.dblab.gskourts.streamingiot.model.entity.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "/api/devices", path = "devices")
public interface DeviceRepository extends PagingAndSortingRepository<DeviceEntity, Long>, JpaRepository<DeviceEntity, Long>
{

    @RestResource(path = "by-name", rel = "by-name")
    List<DeviceEntity> findByNameContainingIgnoreCase(@Param("name") String name);

    @RestResource(path = "by-measurement-type", rel = "by-measurement-type")
    List<DeviceEntity> findByMeasurementType(@Param("measurementType") MeasurementTypeEnum measurementType);

    @RestResource(path = "by-area-code", rel = "by-area-code")
    List<DeviceEntity> findByAreaCode(@Param("areaCode") AreaCodeEnum areaCode);

    @RestResource(path = "by-active-status", rel = "by-active-status")
    List<DeviceEntity> findByActiveStatus(@Param("activeStatus") ActiveStatusEnum activeStatus);

}
