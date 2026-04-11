package com.assettrack.assettrack_api.domain.entity;
import java.time.LocalDateTime;

public record MaintenanceRecord(
    String technicianName,
    String notes,
    LocalDateTime performedAt
){}
