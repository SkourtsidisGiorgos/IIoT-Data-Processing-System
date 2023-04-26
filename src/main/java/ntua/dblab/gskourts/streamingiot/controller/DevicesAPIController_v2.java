package ntua.dblab.gskourts.streamingiot.controller;
import lombok.RequiredArgsConstructor;
import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import ntua.dblab.gskourts.streamingiot.model.AreaCodeEnum;
import ntua.dblab.gskourts.streamingiot.model.MeasurementTypeEnum;
import ntua.dblab.gskourts.streamingiot.model.entity.DeviceEntity;
import ntua.dblab.gskourts.streamingiot.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/devices")
public class DevicesAPIController_v2 {

    private final DeviceService deviceService;

    @GetMapping("/list")
    public List<DeviceEntity> getDevices() {
        return (List<DeviceEntity>) deviceService.findAll();
    }

    @GetMapping("/list/{name}")
    public List<DeviceEntity> getDevicesByName(@PathVariable String name) {
        return (List<DeviceEntity>) deviceService.findByName(name);
    }

    @GetMapping("/list/{areaCode}")
    public List<DeviceEntity> getDevicesByAreaCode(@PathVariable AreaCodeEnum areaCode) {
        return (List<DeviceEntity>) deviceService.findByAreaCode(areaCode);
    }

    @GetMapping("/list/{measurementType}")
    public List<DeviceEntity> getDevicesByMeasurementType(@PathVariable MeasurementTypeEnum measurementType) {
        return (List<DeviceEntity>) deviceService.findByMeasurementType(measurementType);
    }

//    @GetMapping("changeActiveStatus/{deviceId}")
//    public ResponseEntity<String> changeActiveStatus(@PathVariable String deviceId) {
//        if (activeDevicesMap.get(deviceId) == null) {
//            return ResponseEntity.badRequest().body(String.format("Device %s does not exist", deviceId));
//        }
//        if (activeDevicesMap.get(deviceId) == ActiveStatusEnum.ACTIVE) {
//            activeDevicesMap.put(deviceId, ActiveStatusEnum.INACTIVE);
//            return ResponseEntity.ok(String.format("Device %s deactivated", deviceId));
//        }
//        activeDevicesMap.put(deviceId, ActiveStatusEnum.ACTIVE);
//        return ResponseEntity.ok(String.format("Device %s activated", deviceId));
//    }
}
