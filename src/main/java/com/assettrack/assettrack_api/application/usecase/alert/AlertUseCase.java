package com.assettrack.assettrack_api.application.usecase.alert;

import com.assettrack.assettrack_api.application.dto.response.AlertResponse;
import com.assettrack.assettrack_api.domain.entity.Alert;
import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.repository.AlertRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public class AlertUseCase {

    private final AlertRepository alertRepository;

    public AlertUseCase(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Transactional
    public AlertResponse acknowledge(String alertId, String operatorName ) {
        Alert alert = alertRepository.findById(UUID.fromString(alertId))
                .orElseThrow(() -> new DomainException("Alert '%s' not found.".formatted(alertId)));

        alert.acknowledge(operatorName);
        return AlertResponse.from(alertRepository.save(alert));
    }
    @Transactional(readOnly = true)
    public List<AlertResponse> findByAsset(String assetId) {
        return alertRepository.findByAssetId(UUID.fromString(assetId))
                .stream().map(AlertResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<AlertResponse> findUnacknowledged() {
        return alertRepository.findUnacknowledged()
                .stream().map(AlertResponse::from).toList();
    }
}
