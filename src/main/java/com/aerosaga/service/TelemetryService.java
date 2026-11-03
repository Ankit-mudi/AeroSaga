
package com.aerosaga.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.aerosaga.dto.TelemetryRequest;
import com.aerosaga.entity.Drone;
import com.aerosaga.entity.Telemetry;
import com.aerosaga.repository.DroneRepository;
import com.aerosaga.repository.TelemetryRepository;

@Service
public class TelemetryService {

    private static final Logger logger =
            LoggerFactory.getLogger(TelemetryService.class);

    private final TelemetryRepository telemetryRepository;
    private final DroneRepository droneRepository;
    private final SafetyRuleService safetyRuleService;
    private final EmergencyAlertService emergencyAlertService;

    public TelemetryService(
            TelemetryRepository telemetryRepository,
            DroneRepository droneRepository,
            SafetyRuleService safetyRuleService,
            EmergencyAlertService emergencyAlertService) {

        this.telemetryRepository = telemetryRepository;
        this.droneRepository = droneRepository;
        this.safetyRuleService = safetyRuleService;
        this.emergencyAlertService = emergencyAlertService;
    }

    // Save and process telemetry
    public Telemetry saveTelemetry(TelemetryRequest request) {

        Drone drone = droneRepository.findById(request.getDroneId())
                .orElseThrow(() -> new RuntimeException(
                        "Drone not found with ID: "
                                + request.getDroneId()));

        Telemetry telemetry = new Telemetry();

        telemetry.setDrone(drone);
        telemetry.setBatteryLevel(request.getBatteryLevel());
        telemetry.setAltitude(request.getAltitude());
        telemetry.setSpeed(request.getSpeed());
        telemetry.setSignalStrength(request.getSignalStrength());

        // Apply safety rules
        List<String> alerts =
                safetyRuleService.checkSafetyRules(telemetry);

        if (alerts.isEmpty()) {

            logger.info("Drone {} is SAFE", drone.getId());

        } else {

            logger.warn("Drone {} safety alerts: {}",
                    drone.getId(), alerts);

            // Automatically save alerts in database
            for (String alertType : alerts) {

                emergencyAlertService.createAlert(
                        drone.getId(), alertType);
            }
        }

        // Save telemetry
        return telemetryRepository.save(telemetry);
    }

    // Get telemetry by drone ID
    public List<Telemetry> getTelemetryByDroneId(Long droneId) {
        return telemetryRepository.findByDroneId(droneId);
    }
}
