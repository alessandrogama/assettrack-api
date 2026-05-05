package com.assettrack.assettrack_api.application.usecase.order;

import com.assettrack.assettrack_api.application.dto.request.AssignOrderRequest;
import com.assettrack.assettrack_api.application.dto.request.CloseOrderRequest;
import com.assettrack.assettrack_api.application.dto.request.CreateOrderRequest;
import com.assettrack.assettrack_api.application.dto.response.ServiceOrderResponse;
import com.assettrack.assettrack_api.domain.entity.ServiceOrder;
import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.repository.AssetRepository;
import com.assettrack.assettrack_api.domain.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceOrderUseCase {

    private final ServiceOrderRepository orderRepository;
    private final AssetRepository assetRepository;

    public ServiceOrderUseCase(ServiceOrderRepository orderRepository,
                               AssetRepository assetRepository) {
        this.orderRepository = orderRepository;
        this.assetRepository = assetRepository;
    }

    @Transactional
    public ServiceOrderResponse create(CreateOrderRequest request) {
        assetRepository.findById(UUID.fromString(request.assetId()))
                .orElseThrow(() -> new DomainException("asset not found."));

        ServiceOrder order = new ServiceOrder(
                UUID.fromString(request.assetId()),
                request.description(),
                request.requestedBy()
        );
        return ServiceOrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public ServiceOrderResponse assign(String orderId, AssignOrderRequest request) {
        ServiceOrder order = findOrder(orderId);
        order.assign(request.technicianId());
        return ServiceOrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public ServiceOrderResponse complete(String orderId, CloseOrderRequest request) {
        ServiceOrder order = findOrder(orderId);
        order.complete(request.notes());
        return ServiceOrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public ServiceOrderResponse validate(String orderId) {
        ServiceOrder order = findOrder(orderId);
        order.validate();
        return ServiceOrderResponse.from(orderRepository.save(order));
    }

    @Transactional
    public ServiceOrderResponse reject(String orderId, CloseOrderRequest request) {
        ServiceOrder order = findOrder(orderId);
        order.reject(request.notes());
        return ServiceOrderResponse.from(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public List<ServiceOrderResponse> findByAsset(String assetId) {
        return orderRepository.findByAssetId(UUID.fromString(assetId))
                .stream().map(ServiceOrderResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ServiceOrderResponse findById(String id) {
        return ServiceOrderResponse.from(findOrder(id));
    }

    private ServiceOrder findOrder(String id) {
        return orderRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new DomainException("Order Service '%s' not found.".formatted(id)));
    }
}
