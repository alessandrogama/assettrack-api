package com.assettrack.assettrack_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String username,
                           @NotBlank String password) {
}
