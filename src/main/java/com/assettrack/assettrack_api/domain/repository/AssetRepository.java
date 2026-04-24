package com.assettrack.assettrack_api.domain.repository;

import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.valueobject.AssetStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AssetRepository {
    Asset save(Asset asset);
    Optional<Asset> findById(UUID id);
    List<Asset> findAll();
    List<Asset> findAllByStatus(AssetStatus status);
    boolean existsByName(String name);
    void deleteById(UUID id);
}
