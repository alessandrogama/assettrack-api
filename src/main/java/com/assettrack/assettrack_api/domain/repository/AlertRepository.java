package com.assettrack.assettrack_api.domain.repository;

import java.util.List;
import java.util.Optional;
import com.assettrack.assettrack_api.domain.entity.Alert;
import java.util.UUID;

public interface AlertRepository {
Alert save(Alert alert);
Optional<Alert> findById(UUID uuid);
List<Alert> findByAssetId(UUID assetId);
List<Alert> findUnacknowledged();
}
