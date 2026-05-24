package com.assettrack.assettrack_api.adapters.http.controller;

import com.assettrack.assettrack_api.application.dto.request.TelemetryRequest;
import com.assettrack.assettrack_api.application.dto.response.AlertResponse;
import com.assettrack.assettrack_api.application.usecase.alert.AlertUseCase;
import com.assettrack.assettrack_api.application.usecase.alert.ProcessTelemetryUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Alerts & Telemetry", description = "Sensor telemetry and alert management")
public class AlertController {

    private final ProcessTelemetryUseCase processTelemetryUseCase;
    private final AlertUseCase alertUseCase;

    public AlertController(ProcessTelemetryUseCase processTelemetryUseCase,
                           AlertUseCase alertUseCase) {
        this.processTelemetryUseCase = processTelemetryUseCase;
        this.alertUseCase = alertUseCase;
    }

    @PostMapping("/api/telemetry")
    @Operation(summary = "Receives sensor readings — generates an alert if the threshold is exceeded.")
    public ResponseEntity<AlertResponse> receiveTelemetry(@Valid @RequestBody TelemetryRequest request) {
        return processTelemetryUseCase.execute(request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/api/alerts/unacknowledged")
    @Operation(summary = "List of alerts that have not yet been recognized.")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'TECNICO')")
    public ResponseEntity<List<AlertResponse>> findUnacknowledged() {
        return ResponseEntity.ok(alertUseCase.findUnacknowledged());
    }

    @GetMapping("/api/alerts/asset/{assetId}")
    @Operation(summary = "List alerts for a specific asset.")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'TECNICO')")
    public ResponseEntity<List<AlertResponse>> findByAsset(@PathVariable String assetId) {
        return ResponseEntity.ok(alertUseCase.findByAsset(assetId));
    }

    @PatchMapping("/api/alerts/{id}/acknowledge")
    @Operation(summary = "Operator acknowledges the alert.")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'TECNICO')")
    public ResponseEntity<AlertResponse> acknowledge(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(alertUseCase.acknowledge(id, userDetails.getUsername()));
    }
}
