package com.assettrack.assettrack_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(
        @NotBlank String assetId,
        @NotBlank String description,
        @NotBlank String requestedBy
) {
}
