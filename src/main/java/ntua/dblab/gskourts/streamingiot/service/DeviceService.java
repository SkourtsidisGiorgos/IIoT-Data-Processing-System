package ntua.dblab.gskourts.streamingiot.service;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import ntua.dblab.gskourts.streamingiot.model.AreaCodeEnum;
import ntua.dblab.gskourts.streamingiot.model.MeasurementTypeEnum;
import ntua.dblab.gskourts.streamingiot.model.entity.DeviceEntity;
import ntua.dblab.gskourts.streamingiot.repository.DeviceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceService {

    private final DeviceRepository deviceRepository;


    public void saveAll(Iterable iterable) {
        deviceRepository.saveAll(iterable);
    }
    public void save(DeviceEntity device) {
        deviceRepository.save(device);
    }

    public Iterable<DeviceEntity> findAll() {
        return deviceRepository.findAll();
    }

    public Iterable<DeviceEntity> findByName(String name) {
        return deviceRepository.findByNameContainingIgnoreCase(name);
    }

    public Iterable<DeviceEntity> findByMeasurementType(MeasurementTypeEnum measurementType) {
        return deviceRepository.findByMeasurementType(measurementType);
    }

    public Iterable<DeviceEntity> findByAreaCode(AreaCodeEnum areaCode) {
        return deviceRepository.findByAreaCode(areaCode);
    }

    public Iterable<DeviceEntity> findByActiveStatus(ActiveStatusEnum activeStatus) {
        return deviceRepository.findByActiveStatus(activeStatus);
    }

    public void deleteAll() {
        deviceRepository.deleteAll();
    }

    public void deleteById(Long id) {
        deviceRepository.deleteById(id);
    }
}
