package com.assettrack.assettrack_api.application.usecase.asset;

import com.assettrack.assettrack_api.application.dto.request.CreateAssetRequest;
import com.assettrack.assettrack_api.application.dto.response.AssetResponse;
import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.repository.AssetRepository;
import jakarta.transaction.Transactional;

public class CreateAssetUseCase {
    private final AssetRepository assetRepository;

    public CreateAssetUseCase(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Transactional
    public AssetResponse execute(CreateAssetRequest request) {
        if (assetRepository.existsByName(request.name())){
            throw new DomainException("Asset already exists with name '%s'.".formatted(request.name())
            );
        }
        Asset asset = new Asset(
                request.name(),
                request.model(),
                request.location(),
                request.responsible()
        );
        Asset saved = assetRepository.save(asset);
        return AssetResponse.from(saved);
    }
}
