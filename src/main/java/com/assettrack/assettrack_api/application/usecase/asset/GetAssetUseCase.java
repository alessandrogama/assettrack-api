package com.assettrack.assettrack_api.application.usecase.asset;

import com.assettrack.assettrack_api.application.dto.response.AssetResponse;
import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.repository.AssetRepository;

import java.util.List;
import java.util.UUID;

public class GetAssetUseCase {
    private final AssetRepository assetRepository;

    public GetAssetUseCase(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public AssetResponse findById(String id) {
        return assetRepository.findById(UUID.fromString(id))
                .map(AssetResponse::from)
                .orElseThrow(() -> new DomainException("Asset with id '.%s' not found".formatted(id)));
    }
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<AssetResponse> findAll() {
        return  assetRepository.findAll()
                .stream()
                .map(AssetResponse::from)
                .toList();
    }
}
