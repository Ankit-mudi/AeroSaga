
package com.aerosaga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aerosaga.entity.EmergencyAlert;

public interface EmergencyAlertRepository
        extends JpaRepository<EmergencyAlert, Long> {

    List<EmergencyAlert> findByDroneId(Long droneId);

    List<EmergencyAlert> findByStatus(String status);

    boolean existsByDroneIdAndAlertTypeAndStatus(
            Long droneId,
            String alertType,
            String status
    );
}
