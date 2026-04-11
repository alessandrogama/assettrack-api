package com.assettrack.assettrack_api.domain.entity;

import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.valueobject.AssetStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Asset {
    private final UUID id;
    private String name;
    private String model;
    private String location;
    private String responsible;
    private AssetStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private final List<MaintenanceRecord> maintenanceHistory;

    public Asset(String name, String model, String location, String responsible) {
        validateName(name);
        this.id = UUID.randomUUID();
        this.name = name;
        this.model = model;
        this.location = location;
        this.responsible = responsible;
        this.status = AssetStatus.OPERATIONAL;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.maintenanceHistory = new ArrayList<>();

    }
    public Asset(UUID id, String name, String model, String location,
                 String responsible, AssetStatus status,
                 LocalDateTime createdAt, LocalDateTime updatedAt,
                 List<MaintenanceRecord> maintenanceHistory) {
        this.id = id;
        this.name = name;
        this.model = model;
        this.location = location;
        this.responsible = responsible;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.maintenanceHistory = new ArrayList<>(maintenanceHistory);
    }
    public void startMaintenance(String reason) {
        if (this.status != AssetStatus.OPERATIONAL) {
            throw new DomainException(
                    "Asset ‘%s’ cannot be placed on maintenance because its status is: %s"
                            .formatted(this.name, this.status)
            );
        }
        this.status = AssetStatus.UNDER_MAINTENANCE;
        this.updatedAt = LocalDateTime.now();
    }
    public void completeMaintenance(String technicianName, String notes) {
        if (this.status != AssetStatus.UNDER_MAINTENANCE) {
            throw new DomainException("The asset is not currently undergoing maintenance.");
        }
        this.maintenanceHistory.add(new MaintenanceRecord(technicianName, notes, LocalDateTime.now()));
        this.status = AssetStatus.OPERATIONAL;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = AssetStatus.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public List<MaintenanceRecord> getMaintenanceHistory() {
        return Collections.unmodifiableList(maintenanceHistory);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new DomainException("The asset name is required.");
        }
        if (name.length() < 3 || name.length() > 100) {
            throw new DomainException("The name must be between 3 and 100 characters long.");
        }
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getModel() { return model; }
    public String getLocation() { return location; }
    public String getResponsible() { return responsible; }
    public AssetStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}