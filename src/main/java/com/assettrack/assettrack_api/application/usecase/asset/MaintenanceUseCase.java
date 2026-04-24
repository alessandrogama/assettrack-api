package com.assettrack.assettrack_api.application.usecase.asset;

import com.assettrack.assettrack_api.application.dto.request.MaintenanceRequest;
import com.assettrack.assettrack_api.application.dto.response.AssetResponse;
import com.assettrack.assettrack_api.domain.repository.AssetRepository;
import jakarta.transaction.Transactional;
import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.exception.DomainException;

import java.util.UUID;


public class MaintenanceUseCase {
    private final AssetRepository assetRepository;

    public MaintenanceUseCase(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Transactional
    public AssetResponse start(String assetId, MaintenanceRequest request){
        Asset asset = findAsset(assetId);
        asset.startMaintenance(request.reason());
        return AssetResponse.from(assetRepository.save(asset));
    }
    @Transactional
    public AssetResponse complete(String assetId, MaintenanceRequest request){
        Asset asset = findAsset(assetId);
        asset.completeMaintenance(request.technicianName(), request.notes());
        return AssetResponse.from(assetRepository.save(asset));
    }
    @Transactional
    public AssetResponse deactivate(String assetId, MaintenanceRequest request){
        Asset asset = findAsset(assetId);
        asset.deactivate();
        return AssetResponse.from(assetRepository.save(asset));
    }

    private Asset findAsset(String id) {
        return assetRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DomainException("Asset '%s' not found.".formatted(id)));
    }
}
