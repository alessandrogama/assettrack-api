package com.assettrack.assettrack_api.application.dto.response;

import com.assettrack.assettrack_api.domain.entity.Alert;
import com.assettrack.assettrack_api.domain.valueobject.AlertSeverity;

import java.time.LocalDateTime;
import java.util.UUID;

public record AlertResponse(
        UUID id,
        UUID assetId,
        String sensorType,
        Double measuredValue,
        Double threshold,
        AlertSeverity severity,
        boolean acknowledged,
        LocalDateTime triggeredAt
) {
    public static AlertResponse from(Alert alert) {
        return new AlertResponse(
                alert.getId(),
                alert.getAssetId(),
                alert.getSensorType(),
                alert.getMeasuredValue(),
                alert.getThreshold(),
                alert.getSeverity(),
                alert.isAcknowledged(),
                alert.getTriggeredAt()
        );
    }
}
