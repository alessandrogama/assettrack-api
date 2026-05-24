package com.assettrack.assettrack_api.infrastructure.persistence.repository;

import com.assettrack.assettrack_api.infrastructure.persistence.entity.AssetJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AssetJpaRepository extends JpaRepository<AssetJpaEntity, UUID> {
    List<AssetJpaEntity> findByStatus(String name);

    boolean existsByName(String name);

    @Query("SELECT DISTINCT a FROM AssetJpaEntity a " +
            "JOIN ServiceOrderJpaEntity o ON o.assetId = a.id " +
            "WHERE o.status = 'OPEN'")
    List<AssetJpaEntity> findAssetsWithOpenOrders();
}
