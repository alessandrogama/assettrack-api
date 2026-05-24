package com.assettrack.assettrack_api.infrastructure.persistence.mapper;

import com.assettrack.assettrack_api.domain.entity.Alert;
import com.assettrack.assettrack_api.infrastructure.persistence.entity.AlertJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper {

    public Alert toDomain(AlertJpaEntity jpa) {
        return new Alert(
                jpa.getId(), jpa.getAssetId(), jpa.getSensorType(),
                jpa.getMeasuredValue(), jpa.getThreshold(), jpa.getSeverity(),
                jpa.isAcknowledged(), jpa.getAcknowledgedBy(),
                jpa.getTriggeredAt(), jpa.getAcknowledgedAt()
        );
    }

    public AlertJpaEntity toJpa(Alert domain) {
        return AlertJpaEntity.builder()
                .id(domain.getId())
                .assetId(domain.getAssetId())
                .sensorType(domain.getSensorType())
                .measuredValue(domain.getMeasuredValue())
                .threshold(domain.getThreshold())
                .severity(domain.getSeverity())
                .acknowledged(domain.isAcknowledged())
                .acknowledgedBy(domain.getAcknowledgedBy())
                .triggeredAt(domain.getTriggeredAt())
                .acknowledgedAt(domain.getAcknowledgedAt())
                .build();
    }
}