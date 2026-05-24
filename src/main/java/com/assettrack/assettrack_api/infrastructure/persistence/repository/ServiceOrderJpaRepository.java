package com.assettrack.assettrack_api.infrastructure.persistence.repository;

import com.assettrack.assettrack_api.domain.valueobject.OrderStatus;
import com.assettrack.assettrack_api.infrastructure.persistence.entity.ServiceOrderJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceOrderJpaRepository extends JpaRepository<ServiceOrderJpaEntity, UUID> {
    List<ServiceOrderJpaEntity> findByAssetId(UUID assetId);
    List<ServiceOrderJpaEntity> findByStatus(OrderStatus status);
    List<ServiceOrderJpaEntity> findByAssignedTechnicianId(String technicianId);
}
