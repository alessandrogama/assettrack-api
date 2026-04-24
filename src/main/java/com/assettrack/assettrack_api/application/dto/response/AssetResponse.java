package com.assettrack.assettrack_api.application.dto.response;

import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.valueobject.AssetStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AssetResponse(
        UUID id,
        String name,
        String model,
        String location,
        String responsible,
        AssetStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static AssetResponse from(Asset asset) {
        return new AssetResponse(
                asset.getId(),
                asset.getName(),
                asset.getModel(),
                asset.getLocation(),
                asset.getResponsible(),
                asset.getStatus(),
                asset.getCreatedAt(),
                asset.getUpdatedAt()
        );
    }
}
