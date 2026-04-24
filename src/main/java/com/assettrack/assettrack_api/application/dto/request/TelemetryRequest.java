package com.assettrack.assettrack_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TelemetryRequest(@NotBlank String assetId,
                               @NotBlank String sensorType,
                               @NotNull @Positive Double value,
                               @NotNull @Positive Double threshold) {
}
