
package com.aerosaga.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.aerosaga.entity.EmergencyAlert;
import com.aerosaga.repository.EmergencyAlertRepository;

@ExtendWith(MockitoExtension.class)
class EmergencyAlertServiceTest {

    @Mock
    private EmergencyAlertRepository alertRepository;

    @InjectMocks
    private EmergencyAlertService alertService;

    @Test
    void testLowBatteryAlertCreation() {

        EmergencyAlert alert = new EmergencyAlert();
        alert.setDroneId(1L);
        alert.setAlertType("LOW_BATTERY");
        alert.setMessage("Drone battery level is critically low");

        when(alertRepository.save(any(EmergencyAlert.class)))
                .thenReturn(alert);

        EmergencyAlert result =
                alertService.createAlert(1L, "LOW_BATTERY");

        assertEquals(1L, result.getDroneId());
        assertEquals("LOW_BATTERY", result.getAlertType());
        assertEquals(
                "Drone battery level is critically low",
                result.getMessage());

        verify(alertRepository, times(1))
                .save(any(EmergencyAlert.class));
    }

    @Test
    void testSignalLossAlertCreation() {

        EmergencyAlert alert = new EmergencyAlert();
        alert.setDroneId(1L);
        alert.setAlertType("SIGNAL_LOSS");
        alert.setMessage("Drone signal strength is critically low");

        when(alertRepository.save(any(EmergencyAlert.class)))
                .thenReturn(alert);

        EmergencyAlert result =
                alertService.createAlert(1L, "SIGNAL_LOSS");

        assertEquals("SIGNAL_LOSS", result.getAlertType());
        assertEquals(
                "Drone signal strength is critically low",
                result.getMessage());
    }
    
    @Test
    void testResolveAlertSuccessfully() {

        EmergencyAlert alert = new EmergencyAlert();
        alert.setDroneId(1L);
        alert.setAlertType("LOW_BATTERY");
        alert.setStatus("ACTIVE");

        when(alertRepository.findById(1L))
                .thenReturn(java.util.Optional.of(alert));

        when(alertRepository.save(any(EmergencyAlert.class)))
                .thenReturn(alert);

        EmergencyAlert result = alertService.resolveAlert(1L);

        assertEquals("RESOLVED", result.getStatus());

        verify(alertRepository, times(1)).findById(1L);
        verify(alertRepository, times(1))
                .save(any(EmergencyAlert.class));
    }
    
    @Test
    void testResolveAlertNotFound() {

        when(alertRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        assertThrows(
                java.util.NoSuchElementException.class,
                () -> alertService.resolveAlert(99L)
        );

        verify(alertRepository, never())
                .save(any(EmergencyAlert.class));
    }
    
    @Test
    void testDuplicateAlertNotCreated() {

        when(alertRepository
                .existsByDroneIdAndAlertTypeAndStatus(
                        1L, "LOW_BATTERY", "ACTIVE"))
                .thenReturn(true);

        EmergencyAlert result =
                alertService.createAlert(1L, "LOW_BATTERY");

        assertNull(result);

        verify(alertRepository, never())
                .save(any(EmergencyAlert.class));
    }

    @Test
    void testNewAlertCreatedWhenNoActiveDuplicate() {

        when(alertRepository
                .existsByDroneIdAndAlertTypeAndStatus(
                        1L, "LOW_BATTERY", "ACTIVE"))
                .thenReturn(false);

        EmergencyAlert alert = new EmergencyAlert();

        when(alertRepository.save(any(EmergencyAlert.class)))
                .thenReturn(alert);

        EmergencyAlert result =
                alertService.createAlert(1L, "LOW_BATTERY");

        assertNotNull(result);

        verify(alertRepository, times(1))
                .save(any(EmergencyAlert.class));
    }
}
