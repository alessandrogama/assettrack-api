package com.assettrack.assettrack_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CloseOrderRequest(@NotBlank String notes) {
}
