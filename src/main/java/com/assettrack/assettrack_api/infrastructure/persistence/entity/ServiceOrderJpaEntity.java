package com.assettrack.assettrack_api.infrastructure.persistence.entity;

import com.assettrack.assettrack_api.domain.valueobject.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_orders")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceOrderJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "asset_id", nullable = false, columnDefinition = "uuid")
    private UUID assetId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "assigned_technician_id")
    private String assignedTechnicianId;

    @Column(name = "closing_notes", columnDefinition = "TEXT")
    private String closingNotes;

    @Column(name = "requested_by", nullable = false)
    private String requestedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
