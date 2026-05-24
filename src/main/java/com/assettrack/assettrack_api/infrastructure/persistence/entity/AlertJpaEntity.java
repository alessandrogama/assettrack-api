package com.assettrack.assettrack_api.infrastructure.persistence.entity;

import com.assettrack.assettrack_api.domain.valueobject.AlertSeverity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alerts",
        indexes = {
                @Index(name = "idx_alerts_asset_id", columnList = "asset_id"),
                @Index(name = "idx_alerts_acknowledged", columnList = "acknowledged")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AlertJpaEntity {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "asset_id", nullable = false, columnDefinition = "uuid")
    private UUID assetId;

    @Column(name = "sensor_type", nullable = false, length = 30)
    private String sensorType;

    @Column(name = "measured_value", nullable = false)
    private Double measuredValue;

    @Column(nullable = false)
    private Double threshold;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AlertSeverity severity;

    @Column(nullable = false)
    private boolean acknowledged;

    @Column(name = "acknowledged_by")
    private String acknowledgedBy;

    @Column(name = "triggered_at", nullable = false)
    private LocalDateTime triggeredAt;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;
}
