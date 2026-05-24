package com.assettrack.assettrack_api.infrastructure.persistence.impl;

import com.assettrack.assettrack_api.domain.entity.Alert;
import com.assettrack.assettrack_api.domain.repository.AlertRepository;
import com.assettrack.assettrack_api.infrastructure.persistence.mapper.AlertMapper;
import com.assettrack.assettrack_api.infrastructure.persistence.repository.AlertJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AlertRepositoryImpl implements AlertRepository {

    private final AlertJpaRepository jpaRepository;
    private final AlertMapper mapper;

    public AlertRepositoryImpl(AlertJpaRepository jpaRepository, AlertMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Alert save(Alert alert) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpa(alert)));
    }

    @Override
    public Optional<Alert> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Alert> findByAssetId(UUID assetId) {
        return jpaRepository.findByAssetId(assetId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Alert> findUnacknowledged() {
        return jpaRepository.findByAcknowledgedFalse().stream().map(mapper::toDomain).toList();
    }
}
