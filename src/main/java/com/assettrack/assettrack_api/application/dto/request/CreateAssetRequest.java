package com.assettrack.assettrack_api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAssetRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotBlank(message = "Model is required")
        String model,

        @NotBlank(message = "Location is required")
        String location,

        @NotBlank(message = "Responsible is required")
        String responsible
) {}
