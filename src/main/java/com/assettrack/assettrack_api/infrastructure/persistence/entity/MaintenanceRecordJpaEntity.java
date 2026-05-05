package com.assettrack.assettrack_api.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "maintenance_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRecordJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Relacionamento com o ativo pai
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private AssetJpaEntity asset;

    @Column(name = "technician_name", nullable = false)
    private String technicianName;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String notes;

    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;
}
