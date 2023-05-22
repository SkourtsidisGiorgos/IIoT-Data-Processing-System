package ntua.dblab.gskourts.streamingiot.controller;

import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import ntua.dblab.gskourts.streamingiot.model.AreaCodeEnum;
import ntua.dblab.gskourts.streamingiot.model.MeasurementTypeEnum;
import ntua.dblab.gskourts.streamingiot.model.dto.DeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/devices")
public class DevicesController {

   @Autowired
   @Qualifier("activeDevicesMap")
   private ConcurrentMap<String, ActiveStatusEnum> activeDevicesMap;

   @GetMapping
   public String getActiveDevices(Model model) {
      model.addAttribute("activeDevices", activeDevicesMap);
      return "devices";
   }

   @PostMapping("/activate/{deviceId}")
   public String activateDevice(@PathVariable String deviceId) {
      log.info("Activating device {}", deviceId);
      if (deviceId == null){
         return  "redirect:/devices";
      }
      if (activeDevicesMap.get(deviceId) == null) {
         log.error("Device {} does not exist", deviceId);
         return "redirect:/devices";
      }
      activeDevicesMap.put(deviceId, ActiveStatusEnum.ACTIVE);
      return "redirect:/devices";
   }

   @PostMapping("/deactivate/{deviceId}")
   public String deactivateDevice(@PathVariable String deviceId) {
      log.info("Deactivating device {}", deviceId);
      if (deviceId == null){
         return  "redirect:/devices";
      }
      if (activeDevicesMap.get(deviceId) == null) {
         log.error("Device {} does not exist", deviceId);
         return "redirect:/devices";
      }
      activeDevicesMap.put(deviceId, ActiveStatusEnum.INACTIVE);
      return "redirect:/devices";
   }


    @GetMapping("/details/{deviceId}")
    public String deviceDetails(@PathVariable("deviceId") String deviceId, Model model) {
        log.info("Getting details for device {}", deviceId);
        DeviceDTO device = new DeviceDTO();
        device.setDeviceId(deviceId);
        device.setDeviceStatus(activeDevicesMap.get(deviceId));
        if (deviceId.contains("temp")){
           device.setDeviceType(MeasurementTypeEnum.TEMPERATURE);
       }
        else if (deviceId.contains("pressure")){
           device.setDeviceType(MeasurementTypeEnum.PRESSURE);
        }
        else if (deviceId.contains("power")){
           device.setDeviceType(MeasurementTypeEnum.POWER);
        }
        device.setDeviceAreaCode(AreaCodeEnum.values()[(int) (Math.random() * AreaCodeEnum.values().length)]);
        log.info("Device details: {}", device);
        model.addAttribute("device", device);
        return "device-details";
    }

    @GetMapping("/data/raw")
    public String getRawData(Model model) {
        model.addAttribute("activeDevices", activeDevicesMap);
        return "raw-data";
    }
}
