package com.assettrack.assettrack_api.domain.entity;

import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.valueobject.OrderStatus;
import org.springframework.core.annotation.Order;

import java.time.LocalDateTime;
import java.util.UUID;

public class ServiceOrder {
    private final UUID id;
    private final UUID assetId;
    private String description;
    private OrderStatus status;
    private String assignedTechnicianId;
    private String closingNotes;
    private final String requestedBy;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ServiceOrder(UUID assetId, String description, String requestedBy){
        if(description == null ||description.isBlank()){
            throw new DomainException("The description is required.");
        }
        this.id = UUID.randomUUID();
        this.assetId = assetId;
        this.description = description;
        this.requestedBy = requestedBy;
        this.status = OrderStatus.OPEN;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ServiceOrder(UUID id,UUID assetId,String description, OrderStatus status,
                        String assignedTechnicianId, String closingNotes, String requestedBy,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.id = id;
        this.assetId = assetId;
        this.description = description;
        this.status = status;
        this.assignedTechnicianId = assignedTechnicianId;
        this.closingNotes = closingNotes;
        this.requestedBy = requestedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void assign(String technicianId) {
        assertStatus(OrderStatus.OPEN, "atribuir um técnico");
        this.assignedTechnicianId = technicianId;
        this.status = OrderStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void complete(String notes){
        assertStatus(OrderStatus.IN_PROGRESS,"conclusion");
        if (notes == null || notes.isBlank()){
            throw new DomainException("The notes is required.");
        }
        this.closingNotes = notes;
        this.status = OrderStatus.COMPLETED;
        this.updatedAt = LocalDateTime.now();
    }
    public void validate(){
        assertStatus(OrderStatus.COMPLETED,"validate");
        this.status = OrderStatus.VALIDATED;
        this.updatedAt = LocalDateTime.now();
    }
    public void reject(String reason){
        assertStatus(OrderStatus.COMPLETED, "reject");
        this.closingNotes = "REJECTED: " + reason;
        this.status = OrderStatus.IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }
    public void assertStatus(OrderStatus expected, String action){
        if (this.status != expected){
            throw new DomainException(
               "No possible %s a OS in status '%s'. wait Status: '%s'."
                       .formatted(action, this.status,expected)
            );
        }
    }
    public UUID getId() { return id; }
    public UUID getAssetId() { return assetId; }
    public String getDescription() { return description; }
    public OrderStatus getStatus() { return status; }
    public String getAssignedTechnicianId() { return assignedTechnicianId; }
    public String getClosingNotes() { return closingNotes; }
    public String getRequestedBy() { return requestedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
