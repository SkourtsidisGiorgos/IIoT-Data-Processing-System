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
@RequestMapping("/api/devices")
public class DevicesAPIController {

   @Autowired
   @Qualifier("activeDevicesMap")
   private ConcurrentHashMap<String, ActiveStatusEnum> activeDevicesMap;

   @GetMapping("/list")
   public ConcurrentHashMap<String, ActiveStatusEnum> getActiveDevices() {
      return activeDevicesMap;
   }

   // active and return http status code
   @PostMapping("/activate/{deviceId}")
   public ResponseEntity<String> activateDevice(@PathVariable String deviceId) {
      if (activeDevicesMap.get(deviceId) == null) {
         return ResponseEntity.badRequest().body(String.format("Device %s does not exist", deviceId));
      }

      if (activeDevicesMap.get(deviceId) == ActiveStatusEnum.ACTIVE) {
         return ResponseEntity.badRequest().body(String.format("Device %s already active", deviceId));
      }
      activeDevicesMap.put(deviceId, ActiveStatusEnum.ACTIVE);
      return ResponseEntity.ok(String.format("Device %s activated", deviceId));
   }

   @PostMapping("/deactivate/{deviceId}")
   public ResponseEntity<String> deactivateDevice(@PathVariable String deviceId) {
      if (activeDevicesMap.get(deviceId) == null) {
         return ResponseEntity.badRequest().body(String.format("Device %s does not exist", deviceId));
      }

      if (activeDevicesMap.get(deviceId) == ActiveStatusEnum.INACTIVE) {
         return ResponseEntity.badRequest().body(String.format("Device %s already inactive", deviceId));
      }
      activeDevicesMap.put(deviceId, ActiveStatusEnum.INACTIVE);
      return ResponseEntity.ok(String.format("Device %s deactivated", deviceId));
   }

    @PostMapping("/isActive/{deviceId}")
    public ResponseEntity<String> isActiveDevice(@PathVariable String deviceId) {
        if (activeDevicesMap.get(deviceId) == null) {
            return ResponseEntity.badRequest().body(String.format("Device %s does not exist", deviceId));
        }

        if (activeDevicesMap.get(deviceId).equals(ActiveStatusEnum.INACTIVE)) {
            return ResponseEntity.ok(String.format("Device %s is inactive", deviceId));
        }
        return ResponseEntity.ok(String.format("Device %s is active", deviceId));
    }
}
