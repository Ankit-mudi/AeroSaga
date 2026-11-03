package com.aerosaga.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.aerosaga.entity.Telemetry;

@Service
public class SafetyRuleService {

    public List<String> checkSafetyRules(Telemetry telemetry) {

        List<String> alerts = new ArrayList<>();

        // Rule 1: Low Battery
        if (telemetry.getBatteryLevel() != null
                && telemetry.getBatteryLevel() < 20) {

            alerts.add("LOW_BATTERY");
        }

        // Rule 2: Weak Signal
        if (telemetry.getSignalStrength() != null
                && telemetry.getSignalStrength() < 30) {

            alerts.add("SIGNAL_LOSS");
        }

        return alerts;
    }
}