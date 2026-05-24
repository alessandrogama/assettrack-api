package com.assettrack.assettrack_api.infrastructure.persistence.impl;

import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.repository.AssetRepository;
import com.assettrack.assettrack_api.domain.valueobject.AssetStatus;
import com.assettrack.assettrack_api.infrastructure.persistence.mapper.AssetMapper;
import com.assettrack.assettrack_api.infrastructure.persistence.repository.AssetJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AssetRepositoryImpl implements AssetRepository {

    private final AssetJpaRepository jpaRepository;
    private final AssetMapper mapper;

    public AssetRepositoryImpl(AssetJpaRepository jpaRepository, AssetMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Asset save(Asset asset) {
        var jpaEntity = mapper.toJpa(asset);
        var saved = jpaRepository.save(jpaEntity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Asset> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Asset> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Asset> findAllByStatus(AssetStatus status) {
        return List.of();
    }

    @Override
    public List<Asset> findByStatus(AssetStatus status) {
        return jpaRepository.findByStatus(status).stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
