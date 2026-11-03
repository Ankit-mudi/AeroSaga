
package com.aerosaga.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aerosaga.dto.TelemetryRequest;
import com.aerosaga.entity.Telemetry;
import com.aerosaga.service.TelemetryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryController {

    private final TelemetryService telemetryService;

    public TelemetryController(TelemetryService telemetryService) {
        this.telemetryService = telemetryService;
    }

    @PostMapping
    public ResponseEntity<Telemetry> saveTelemetry(
            @Valid @RequestBody TelemetryRequest request) {

        Telemetry savedTelemetry =
                telemetryService.saveTelemetry(request);

        return new ResponseEntity<>(
                savedTelemetry, HttpStatus.CREATED);
    }
    
    @GetMapping("/drone/{droneId}")
    public ResponseEntity<?> getTelemetryByDroneId(@PathVariable Long droneId) {
        return ResponseEntity.ok(
            telemetryService.getTelemetryByDroneId(droneId)
        );
    }
}
