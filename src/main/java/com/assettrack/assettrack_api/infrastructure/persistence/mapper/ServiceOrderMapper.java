package com.assettrack.assettrack_api.infrastructure.persistence.mapper;

import com.assettrack.assettrack_api.domain.entity.ServiceOrder;
import com.assettrack.assettrack_api.infrastructure.persistence.entity.ServiceOrderJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ServiceOrderMapper {

    public ServiceOrder toDomain(ServiceOrderJpaEntity jpa) {
        return new ServiceOrder(
                jpa.getId(), jpa.getAssetId(), jpa.getDescription(),
                jpa.getStatus(), jpa.getAssignedTechnicianId(),
                jpa.getClosingNotes(), jpa.getRequestedBy(),
                jpa.getCreatedAt(), jpa.getUpdatedAt()
        );
    }

    public ServiceOrderJpaEntity toJpa(ServiceOrder domain) {
        return ServiceOrderJpaEntity.builder()
                .id(domain.getId())
                .assetId(domain.getAssetId())
                .description(domain.getDescription())
                .status(domain.getStatus())
                .assignedTechnicianId(domain.getAssignedTechnicianId())
                .closingNotes(domain.getClosingNotes())
                .requestedBy(domain.getRequestedBy())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
