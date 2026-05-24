package com.assettrack.assettrack_api.infrastructure.persistence.mapper;

import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.entity.MaintenanceRecord;
import com.assettrack.assettrack_api.infrastructure.persistence.entity.AssetJpaEntity;
import com.assettrack.assettrack_api.infrastructure.persistence.entity.MaintenanceRecordJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssetMapper {

    public Asset toDomain(AssetJpaEntity jpa) {
        List<MaintenanceRecord> history = jpa.getMaintenanceHistory()
                .stream()
                .map(r -> new MaintenanceRecord(r.getTechnicianName(), r.getNotes(), r.getPerformedAt()))
                .toList();

        return new Asset(
                jpa.getId(),
                jpa.getName(),
                jpa.getModel(),
                jpa.getLocation(),
                jpa.getResponsible(),
                jpa.getStatus(),
                jpa.getCreatedAt(),
                jpa.getUpdatedAt(),
                history
        );
    }

    public AssetJpaEntity toJpa(Asset domain) {
        AssetJpaEntity jpa = AssetJpaEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .model(domain.getModel())
                .location(domain.getLocation())
                .responsible(domain.getResponsible())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();

        List<MaintenanceRecordJpaEntity> records = domain.getMaintenanceHistory()
                .stream()
                .map(r -> MaintenanceRecordJpaEntity.builder()
                        .asset(jpa)
                        .technicianName(r.technicianName())
                        .notes(r.notes())
                        .performedAt(r.performedAt())
                        .build())
                .toList();

        jpa.setMaintenanceHistory(new java.util.ArrayList<>(records));
        return jpa;
    }
}
