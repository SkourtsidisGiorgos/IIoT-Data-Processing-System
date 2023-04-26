package ntua.dblab.gskourts.streamingiot.controller;

import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
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
}
