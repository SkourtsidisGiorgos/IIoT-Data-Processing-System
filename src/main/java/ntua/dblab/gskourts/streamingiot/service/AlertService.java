package ntua.dblab.gskourts.streamingiot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ntua.dblab.gskourts.streamingiot.model.ActiveStatusEnum;
import ntua.dblab.gskourts.streamingiot.model.AlertLevelEnum;
import ntua.dblab.gskourts.streamingiot.model.dto.EmailDetailsDTO;
import ntua.dblab.gskourts.streamingiot.util.Utils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class AlertService {

    @Value("${application.alerts.email.enabled}")
    private boolean emailEnabled;
    @Value("${application.alerts.threshold.warning.temperature}")
    private double warningTemperatureThreshold;
    @Value("${application.alerts.threshold.warning.pressure}")
    private double warningPressureThreshold;
    @Value("${application.alerts.threshold.warning.power}")
    private double warningPowerThreshold;
    @Value("${application.alerts.threshold.critical.temperature}")
    private double criticalTemperatureThreshold;
    @Value("${application.alerts.threshold.critical.pressure}")
    private double criticalPressureThreshold;
    @Value("${application.alerts.threshold.critical.power}")
    private double criticalPowerThreshold;

    private final EmailService emailService;
    private final ConcurrentHashMap<String, ActiveStatusEnum> activeDevicesMap;

    public AlertService(EmailService emailService, @Qualifier("activeDevicesMap") ConcurrentHashMap<String, ActiveStatusEnum> activeDevicesMap) {
        this.emailService = emailService;
        this.activeDevicesMap = activeDevicesMap;
    }
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
        if (temperature > warningTemperatureThreshold) {
            temperatureAlert(sensorId, temperature, AlertLevelEnum.WARNING);
        } else if (temperature > criticalTemperatureThreshold) {
            temperatureAlert(sensorId, temperature, AlertLevelEnum.CRITICAL);
            activeDevicesMap.put(sensorId, ActiveStatusEnum.INACTIVE);
        }
    }

    private void checkPowerAlert(String sensorId, double power) {
        if (power > warningPowerThreshold) {
            powerAlert(sensorId, power, AlertLevelEnum.WARNING);
        } else if (power > criticalPowerThreshold) {
            powerAlert(sensorId, power, AlertLevelEnum.CRITICAL);
            activeDevicesMap.put(sensorId, ActiveStatusEnum.INACTIVE);
        }
    }

    private void checkPressureAlert(String sensorId, double pressure) {
        if (pressure > warningPressureThreshold) {
            pressureAlert(sensorId, pressure, AlertLevelEnum.WARNING);
        } else if (pressure > criticalPressureThreshold) {
            pressureAlert(sensorId, pressure, AlertLevelEnum.CRITICAL);
            activeDevicesMap.put(sensorId, ActiveStatusEnum.INACTIVE);

        }
    }
    private void temperatureAlert(String sensorId, double temperature, AlertLevelEnum alertLevel) {
        String message = String.format("[%s] Temperature alert for sensor \"temp-%s\" with temperature=%s. Timestamp: %s, Hostname: %s",
                alertLevel, sensorId, temperature, Utils.getCurrentDateTime(), Utils.getHostName());
        if (alertLevel == AlertLevelEnum.INFO) {
            log.info("Temperature alert for sensor \"temp-{}\" with temperature={}", sensorId, temperature);
        } else if (alertLevel == AlertLevelEnum.WARNING) {
            log.warn("Temperature alert for sensor \"temp-{}\" with temperature={}", sensorId, temperature);
        } else if (alertLevel == AlertLevelEnum.CRITICAL) {
            log.error("Temperature alert for sensor \"temp-{}\" with temperature={}", sensorId, temperature);
            message += " Device is now Inactive";
        }
        if (emailEnabled) {
            EmailDetailsDTO emailDetails = EmailDetailsDTO.builder()
                    .subject(String.format("[%s] Temperature alert for sensor \"temp-%s\"", alertLevel, sensorId))
                    .messageBody(message)
                    .build();
            emailService.sendEmail(emailDetails);
        }
    }

    private void powerAlert(String sensorId, double power, AlertLevelEnum alertLevel) {
        String message = String.format("[%s] Power alert for sensor \"power-%s\" with power=%s. Timestamp: %s, Hostname: %s",
                alertLevel, sensorId, power, Utils.getCurrentDateTime(), Utils.getHostName());
        if (alertLevel == AlertLevelEnum.INFO) {
            log.info("Power alert for \"power-{}\" with power={}", sensorId, power);
        } else if (alertLevel == AlertLevelEnum.WARNING) {
            log.warn("Power alert for \"power-{}\" with power={}", sensorId, power);
        } else if (alertLevel == AlertLevelEnum.CRITICAL) {
            log.error("Power alert for sensor \"power-{}\" with power={}", sensorId, power);
            message += " Device is now Inactive";
        }
        if (emailEnabled) {
            EmailDetailsDTO emailDetails = EmailDetailsDTO.builder()
                    .subject(String.format("[%s] Power alert for sensor \"power-%s\"", alertLevel, sensorId))
                    .messageBody(message)
                    .build();
            emailService.sendEmail(emailDetails);
        }
    }

    private void pressureAlert(String sensorId, double pressure, AlertLevelEnum alertLevel) {
        String message = String.format("[%s] Pressure alert for sensor \"pressure-%s\" with pressure=%s. Timestamp: %s, Hostname: %s",
                alertLevel, sensorId, pressure, Utils.getCurrentDateTime(), Utils.getHostName());
        if (alertLevel == AlertLevelEnum.INFO) {
            log.info("Pressure alert for sensor \"pressure-{}\" with pressure {}", sensorId, pressure);
        } else if (alertLevel == AlertLevelEnum.WARNING) {
            log.warn("Pressure alert for sensor \"pressure-{}\" with pressure {}", sensorId, pressure);
        } else if (alertLevel == AlertLevelEnum.CRITICAL) {
            log.error("Pressure alert for sensor \"pressure-{}\" with pressure {}", sensorId, pressure);
            message += " Device is now Inactive";
        }

        if (!emailEnabled) {
            return;
        }
        EmailDetailsDTO emailDetails = EmailDetailsDTO.builder()
                .subject(String.format("[%s] Pressure alert for sensor \"pressure-%s\"", alertLevel, sensorId))
                .messageBody(message)
                .build();
        emailService.sendEmail(emailDetails);
    }
}
