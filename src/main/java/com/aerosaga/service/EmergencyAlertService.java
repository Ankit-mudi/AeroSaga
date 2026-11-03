
package com.aerosaga.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aerosaga.entity.EmergencyAlert;
import com.aerosaga.repository.EmergencyAlertRepository;
import java.util.NoSuchElementException;

@Service
public class EmergencyAlertService {

    private final EmergencyAlertRepository alertRepository;

    public EmergencyAlertService(
            EmergencyAlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    // Save a new emergency alert without duplicates
    public EmergencyAlert createAlert(
            Long droneId, String alertType) {

        boolean alreadyExists =
                alertRepository.existsByDroneIdAndAlertTypeAndStatus(
                        droneId, alertType, "ACTIVE");

        if (alreadyExists) {
            return null;
        }

        EmergencyAlert alert = new EmergencyAlert();

        alert.setDroneId(droneId);
        alert.setAlertType(alertType);

        if (alertType.equals("LOW_BATTERY")) {
            alert.setMessage(
                    "Drone battery level is critically low");
        } else if (alertType.equals("SIGNAL_LOSS")) {
            alert.setMessage(
                    "Drone signal strength is critically low");
        } else {
            alert.setMessage("Drone safety alert detected");
        }

        return alertRepository.save(alert);
    }

    // Get all emergency alerts
    public List<EmergencyAlert> getAllAlerts() {
        return alertRepository.findAll();
    }

    // Get alerts by drone ID
    public List<EmergencyAlert> getAlertsByDroneId(Long droneId) {
        return alertRepository.findByDroneId(droneId);
    }

    // Get active alerts
    public List<EmergencyAlert> getActiveAlerts() {
        return alertRepository.findByStatus("ACTIVE");
    }
    
 // Resolve an emergency alert
    public EmergencyAlert resolveAlert(Long alertId) {

        EmergencyAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new NoSuchElementException(
                        "Emergency alert not found with ID: " + alertId));

        alert.setStatus("RESOLVED");

        return alertRepository.save(alert);
    }
}
