package com.assettrack.assettrack_api.domain;

import com.assettrack.assettrack_api.domain.entity.ServiceOrder;
import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.valueobject.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("ServiceOrder — State Machine.")
class ServiceOrderTest {

    private ServiceOrder createOrder() {
        return new ServiceOrder(UUID.randomUUID(), "Replace bearing", "operator01");
    }

    @Test
    @DisplayName("The new OS must have OPEN status.")
    void newOrderShouldBeOpen() {
        assertThat(createOrder().getStatus()).isEqualTo(OrderStatus.OPEN);
    }

    @Test
    @DisplayName("You must transition OPEN → IN_PROGRESS when assigning a technician.")
    void shouldTransitionToInProgressWhenAssigned() {
        ServiceOrder order = createOrder();
        order.assign("technical-01");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.IN_PROGRESS);
        assertThat(order.getAssignedTechnicianId()).isEqualTo("technical-01");
    }

    @Test
    @DisplayName("It should transition IN_PROGRESS → COMPLETED upon completion.")
    void shouldTransitionToCompletedWhenClosed() {
        ServiceOrder order = createOrder();
        order.assign("technical-01");
        order.complete("Bearing successfully replaced.");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
    }

    @Test
    @DisplayName("It should transition from COMPLETED to VALIDATED when validating.\n")
    void shouldTransitionToValidated() {
        ServiceOrder order = createOrder();
        order.assign("technical-01");
        order.complete("Work completed");
        order.validate();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.VALIDATED);
    }

    @Test
    @DisplayName("You should not complete an operating system without closing notes.")
    void shouldNotCompleteWithoutNotes() {
        ServiceOrder order = createOrder();
        order.assign("technical-01");
        assertThatThrownBy(() -> order.complete(""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Closing notes");
    }

    @Test
    @DisplayName("Do not skip a step — OPEN cannot go directly to COMPLETED.")
    void shouldNotSkipSteps() {
        ServiceOrder order = createOrder();
        assertThatThrownBy(() -> order.complete("Notes"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("IN_PROGRESS");
    }

    @Test
    @DisplayName("You should reject OS COMPLETED and revert to IN_PROGRESS.")
    void shouldRejectCompletedOrder() {
        ServiceOrder order = createOrder();
        order.assign("technical-01");
        order.complete("Conclusion notes");
        order.reject("Incomplete service — check point B");
        assertThat(order.getStatus()).isEqualTo(OrderStatus.IN_PROGRESS);
    }
}
