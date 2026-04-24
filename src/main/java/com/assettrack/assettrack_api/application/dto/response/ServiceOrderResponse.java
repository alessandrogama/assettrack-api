package com.assettrack.assettrack_api.application.dto.response;

import com.assettrack.assettrack_api.domain.entity.ServiceOrder;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServiceOrderResponse(
        UUID id,
        UUID assetId,
        String description,
        String assignedTechnicianId,
        String closingNotes,
        String requestedBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ServiceOrderResponse from(ServiceOrder order){
        return new ServiceOrderResponse(
          order.getId(),
          order.getAssetId(),
          order.getDescription(),
          order.getAssignedTechnicianId(), order.getClosingNotes(),order.getRequestedBy(),
          order.getCreatedAt(),
          order.getUpdatedAt()
        );
    }
}
