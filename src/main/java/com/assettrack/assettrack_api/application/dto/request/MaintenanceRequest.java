package com.assettrack.assettrack_api.application.dto.request;

public record MaintenanceRequest(
        String reason,
        String technicianName,
        String notes
) {
}
