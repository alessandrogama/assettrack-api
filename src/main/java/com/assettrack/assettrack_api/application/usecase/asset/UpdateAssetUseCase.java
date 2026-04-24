package com.assettrack.assettrack_api.application.usecase.asset;

import com.assettrack.assettrack_api.application.dto.request.UpdateAssetRequest;
import com.assettrack.assettrack_api.application.dto.response.AssetResponse;
import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.repository.AssetRepository;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

public class UpdateAssetUseCase {
    private final AssetRepository assetRepository;

    public UpdateAssetUseCase(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;

    }

    @Transactional
    public AssetResponse execute(String id, UpdateAssetRequest request) {
        Asset asset = assetRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DomainException("Asset with id '.%s' not found.".formatted(id)));

        Asset updated = new Asset(
                asset.getId(),
                request.name() != null ? request.name() : asset.getName(),
                request.model() != null ? request.model() : asset.getModel(),
                request.location() != null ? request.location() : asset.getLocation(),
                request.responsible() != null ? request.responsible() : asset.getResponsible(),
                asset.getStatus(),
                asset.getCreatedAt(),
                LocalDateTime.now(),
                asset.getMaintenanceHistory()
        );
        return  AssetResponse.from(assetRepository.save(updated));
    }
}
