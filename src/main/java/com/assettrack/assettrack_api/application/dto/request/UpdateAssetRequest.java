package com.assettrack.assettrack_api.application.dto.request;

import jakarta.validation.constraints.Size;
public record UpdateAssetRequest(
        @Size(min = 3, max = 150) String name,
        String model,
        String location,
        String responsible
) { }
