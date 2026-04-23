package com.assettrack.assettrack_api.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;
import com.assettrack.assettrack_api.domain.valueobject.AlertSeverity;

public class Alert {
    private final UUID id;
    private final UUID assetId;
    private final String sensorType;
    private final Double measuredValue;
    private final Double threshold;
    private final AlertSeverity severity;
    private boolean acknowledged;
    private String acknowledgedBy;
    private final LocalDateTime triggeredAt;
    private LocalDateTime acknowledgedAt;

    public Alert(UUID assetId, String sensorType, Double measuredValue, Double threshold,
                 AlertSeverity severity) {
        this.id = UUID.randomUUID();
        this.assetId = assetId;
        this.sensorType = sensorType;
        this.measuredValue = measuredValue;
        this.threshold = threshold;
        this.severity = severity;
        this.acknowledged = false;
        this.triggeredAt = LocalDateTime.now();
    }
    public void acknowledge(String operatorName){
        if(this.acknowledged){
            throw new com.assettrack.assettrack_api.domain.exception.DomainException("Alert has already been acknowledged");
        }
        this.acknowledged = true;
        this.acknowledgedBy = operatorName;
        this.acknowledgedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getAssetId() { return assetId; }
    public String getSensorType() { return sensorType; }
    public Double getMeasuredValue() { return measuredValue; }
    public Double getThreshold() { return threshold; }
    public AlertSeverity getSeverity() { return severity; }
    public boolean isAcknowledged() { return acknowledged; }
    public String getAcknowledgedBy() { return acknowledgedBy; }
    public LocalDateTime getTriggeredAt() { return triggeredAt; }
    public LocalDateTime getAcknowledgedAt() { return acknowledgedAt; }

}
