package com.assettrack.assettrack_api.infrastructure.persistence.repository;

import com.assettrack.assettrack_api.infrastructure.persistence.entity.AlertJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AlertJpaRepository extends JpaRepository<AlertJpaEntity, UUID> {
    List<AlertJpaEntity> findByAssetId(UUID assetId);
    List<AlertJpaEntity> findByAcknowledgedFalse();
    long countByAcknowledgedFalse();
}
