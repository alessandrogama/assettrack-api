package com.assettrack.assettrack_api.adapters.http.controller;

import com.assettrack.assettrack_api.application.dto.request.AssignOrderRequest;
import com.assettrack.assettrack_api.application.dto.request.CloseOrderRequest;
import com.assettrack.assettrack_api.application.dto.request.CreateOrderRequest;
import com.assettrack.assettrack_api.application.dto.response.ServiceOrderResponse;
import com.assettrack.assettrack_api.application.usecase.order.ServiceOrderUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Service Orders", description = "Work Orders — Maintenance Workflow.")
@SecurityRequirement(name = "bearerAuth")
public class ServiceOrderController {

    private final ServiceOrderUseCase serviceOrderUseCase;

    public ServiceOrderController(ServiceOrderUseCase serviceOrderUseCase) {
        this.serviceOrderUseCase = serviceOrderUseCase;
    }

    @PostMapping
    @Operation(summary = "Open a new Service Order.")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'TECNICO')")
    public ResponseEntity<ServiceOrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceOrderUseCase.create(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Search OS by ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'TECNICO')")
    public ResponseEntity<ServiceOrderResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(serviceOrderUseCase.findById(id));
    }

    @GetMapping("/asset/{assetId}")
    @Operation(summary = "Lists all the (OS) of an asset.")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'TECNICO')")
    public ResponseEntity<List<ServiceOrderResponse>> findByAsset(@PathVariable String assetId) {
        return ResponseEntity.ok(serviceOrderUseCase.findByAsset(assetId));
    }

    @PatchMapping("/{id}/assign")
    @Operation(summary = "Assign technician to OS — transition OPEN → IN_PROGRESS")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<ServiceOrderResponse> assign(
            @PathVariable String id,
            @Valid @RequestBody AssignOrderRequest request) {
        return ResponseEntity.ok(serviceOrderUseCase.assign(id, request));
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Completes OS — transition IN_PROGRESS → COMPLETED")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<ServiceOrderResponse> complete(
            @PathVariable String id,
            @Valid @RequestBody CloseOrderRequest request) {
        return ResponseEntity.ok(serviceOrderUseCase.complete(id, request));
    }

    @PatchMapping("/{id}/validate")
    @Operation(summary = "Validate OS — transition COMPLETED → VALIDATED")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<ServiceOrderResponse> validate(@PathVariable String id) {
        return ResponseEntity.ok(serviceOrderUseCase.validate(id));
    }

    @PatchMapping("/{id}/reject")
    @Operation(summary = "Rejects work order — returns to IN_PROGRESS for rework.")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<ServiceOrderResponse> reject(
            @PathVariable String id,
            @Valid @RequestBody CloseOrderRequest request) {
        return ResponseEntity.ok(serviceOrderUseCase.reject(id, request));
    }
}
