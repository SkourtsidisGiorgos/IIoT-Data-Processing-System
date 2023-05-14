package ntua.dblab.gskourts.streamingiot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import ntua.dblab.gskourts.streamingiot.model.AlertLevelEnum;
import ntua.dblab.gskourts.streamingiot.model.dto.EmailDetailsDTO;
import ntua.dblab.gskourts.streamingiot.util.Utils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlertService {
    @Qualifier("activeDevicesMap")
    private final ConcurrentHashMap<String, ActiveStatusEnum> activeDevicesMap;

    private final EmailService emailService;
    public void checkForAlerts(String sensorId, double value, String category) {
        switch (category) {
            case "temperature":
                checkTemperatureAlert(sensorId, value);
                break;
            case "power":
                checkPowerAlert(sensorId, value);
                break;
            case "pressure":
                checkPressureAlert(sensorId, value);
                break;
        }
    }
    private void checkTemperatureAlert(String sensorId, double temperature) {
        if (temperature > 40) {
            temperatureAlert(sensorId, temperature, AlertLevelEnum.WARNING);
        } else if (temperature > 200) {
            temperatureAlert(sensorId, temperature, AlertLevelEnum.CRITICAL);
            activeDevicesMap.put(sensorId, ActiveStatusEnum.INACTIVE);
            String message = String.format("[%s] Temperature alert for sensor \"temp-%s\" with temperature=%s. Timestamp: %s, Hostname: %s Device is now Inactive",
                    AlertLevelEnum.CRITICAL, sensorId, temperature, System.currentTimeMillis(), Utils.getHostName());
            EmailDetailsDTO emailDetails = EmailDetailsDTO.builder()
                    .subject(String.format("[%s] Temperature alert for sensor \"temp-%s\"", AlertLevelEnum.CRITICAL, sensorId))
                    .messageBody(message)
                    .build();
            emailService.sendEmail(emailDetails);
        }
    }

    private void checkPowerAlert(String sensorId, double power) {
        if (power > 28500) {
            powerAlert(sensorId, power, AlertLevelEnum.WARNING);
        } else if (power > 40000) {
            powerAlert(sensorId, power, AlertLevelEnum.CRITICAL);
            activeDevicesMap.put(sensorId, ActiveStatusEnum.INACTIVE);
            String message = String.format("[%s] Power alert for sensor \"power-%s\" with power=%s. Timestamp: %s, Hostname: %s Device is now Inactive",
                    AlertLevelEnum.CRITICAL, sensorId, power, System.currentTimeMillis(), Utils.getHostName());
            EmailDetailsDTO emailDetails = EmailDetailsDTO.builder()
                    .subject(String.format("[%s] Power alert for sensor \"power-%s\"", AlertLevelEnum.CRITICAL, sensorId))
                    .messageBody(message)
                    .build();
            emailService.sendEmail(emailDetails);

        }
    }

    private void checkPressureAlert(String sensorId, double pressure) {
        if (pressure > 180) {
            pressureAlert(sensorId, pressure, AlertLevelEnum.WARNING);
        } else if (pressure > 200) {
            pressureAlert(sensorId, pressure, AlertLevelEnum.CRITICAL);
            activeDevicesMap.put(sensorId, ActiveStatusEnum.INACTIVE);
            String message = String.format("[%s] Pressure alert for sensor \"pressure-%s\" with pressure=%s. Timestamp: %s, Hostname: %s Device is now Inactive",
                    AlertLevelEnum.CRITICAL, sensorId, pressure, System.currentTimeMillis(), Utils.getHostName());
            EmailDetailsDTO emailDetails = EmailDetailsDTO.builder()
                    .subject(String.format("[%s] Pressure alert for sensor \"pressure-%s\"", AlertLevelEnum.CRITICAL, sensorId))
                    .messageBody(message)
                    .build();
            emailService.sendEmail(emailDetails);
        }
    }
    private void temperatureAlert(String sensorId, double temperature, AlertLevelEnum alertLevel) {
        if (alertLevel == AlertLevelEnum.INFO) {
            log.info("Temperature alert for sensor \"temp-{}\" with temperature={}", sensorId, temperature);
        } else if (alertLevel == AlertLevelEnum.WARNING) {
            log.warn("Temperature alert for sensor \"temp-{}\" with temperature={}", sensorId, temperature);
        } else if (alertLevel == AlertLevelEnum.CRITICAL) {
            log.error("Temperature alert for sensor \"temp-{}\" with temperature={}", sensorId, temperature);
        }
    }

    private void powerAlert(String sensorId, double power, AlertLevelEnum alertLevel) {
        if (alertLevel == AlertLevelEnum.INFO) {
            log.info("Power alert for \"power-{}\" with power={}", sensorId, power);
        } else if (alertLevel == AlertLevelEnum.WARNING) {
            log.warn("Power alert for \"power-{}\" with power={}", sensorId, power);
        } else if (alertLevel == AlertLevelEnum.CRITICAL) {
            log.error("Power alert for sensor \"power-{}\" with power={}", sensorId, power);
        }
    }

    private void pressureAlert(String sensorId, double pressure, AlertLevelEnum alertLevel) {
        if (alertLevel == AlertLevelEnum.INFO) {
            log.info("Pressure alert for sensor \"pressure-{}\" with pressure {}", sensorId, pressure);
        } else if (alertLevel == AlertLevelEnum.WARNING) {
            log.warn("Pressure alert for sensor \"pressure-{}\" with pressure {}", sensorId, pressure);
        } else if (alertLevel == AlertLevelEnum.CRITICAL) {
            log.error("Pressure alert for sensor \"pressure-{}\" with pressure {}", sensorId, pressure);
        }
    }
}
