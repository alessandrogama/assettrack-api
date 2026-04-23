package com.assettrack.assettrack_api.domain.repository;

import com.assettrack.assettrack_api.domain.entity.ServiceOrder;
import com.assettrack.assettrack_api.domain.valueobject.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderRepository {
    ServiceOrder save(ServiceOrder order);
    Optional<ServiceOrder> findById(UUID id);
    List<ServiceOrder> findByAssetId(UUID assetId);
    List<ServiceOrder> findByStatus(OrderStatus status);
}
