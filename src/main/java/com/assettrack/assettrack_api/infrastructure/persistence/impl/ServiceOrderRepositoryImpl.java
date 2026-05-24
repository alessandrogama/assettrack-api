package com.assettrack.assettrack_api.infrastructure.persistence.impl;

import com.assettrack.assettrack_api.domain.entity.ServiceOrder;
import com.assettrack.assettrack_api.domain.repository.ServiceOrderRepository;
import com.assettrack.assettrack_api.domain.valueobject.OrderStatus;
import com.assettrack.assettrack_api.infrastructure.persistence.mapper.ServiceOrderMapper;
import com.assettrack.assettrack_api.infrastructure.persistence.repository.ServiceOrderJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ServiceOrderRepositoryImpl implements ServiceOrderRepository {

    private final ServiceOrderJpaRepository jpaRepository;
    private final ServiceOrderMapper mapper;

    public ServiceOrderRepositoryImpl(ServiceOrderJpaRepository jpaRepository, ServiceOrderMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public ServiceOrder save(ServiceOrder order) {
        return mapper.toDomain(jpaRepository.save(mapper.toJpa(order)));
    }

    @Override
    public Optional<ServiceOrder> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<ServiceOrder> findByAssetId(UUID assetId) {
        return jpaRepository.findByAssetId(assetId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<ServiceOrder> findByStatus(OrderStatus status) {
        return jpaRepository.findByStatus(status).stream().map(mapper::toDomain).toList();
    }
}
