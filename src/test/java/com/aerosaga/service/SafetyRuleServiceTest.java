
package com.aerosaga.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aerosaga.entity.Telemetry;

class SafetyRuleServiceTest {

    private SafetyRuleService safetyRuleService;

    @BeforeEach
    void setUp() {
        safetyRuleService = new SafetyRuleService();
    }

    // Test 1: Safe telemetry
    @Test
    void testSafeTelemetry() {

        Telemetry telemetry = new Telemetry();

        telemetry.setBatteryLevel(85.0);
        telemetry.setSignalStrength(90);

        List<String> alerts =
                safetyRuleService.checkSafetyRules(telemetry);

        assertTrue(alerts.isEmpty());
    }

    // Test 2: Low battery
    @Test
    void testLowBattery() {

        Telemetry telemetry = new Telemetry();

        telemetry.setBatteryLevel(15.0);
        telemetry.setSignalStrength(90);

        List<String> alerts =
                safetyRuleService.checkSafetyRules(telemetry);

        assertTrue(alerts.contains("LOW_BATTERY"));
    }

    // Test 3: Weak signal
    @Test
    void testWeakSignal() {

        Telemetry telemetry = new Telemetry();

        telemetry.setBatteryLevel(85.0);
        telemetry.setSignalStrength(20);

        List<String> alerts =
                safetyRuleService.checkSafetyRules(telemetry);

        assertTrue(alerts.contains("SIGNAL_LOSS"));
    }
}
