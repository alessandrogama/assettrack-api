package com.assettrack.assettrack_api.application.usecase.alert;

import com.assettrack.assettrack_api.application.dto.request.TelemetryRequest;
import com.assettrack.assettrack_api.application.dto.response.AlertResponse;
import com.assettrack.assettrack_api.domain.entity.Alert;
import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.repository.AlertRepository;
import com.assettrack.assettrack_api.domain.repository.AssetRepository;
import com.assettrack.assettrack_api.domain.valueobject.AlertSeverity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ProcessTelemetryUseCase  {

    private final AssetRepository assetRepository;
    private final AlertRepository alertRepository;
    private final AlertNotificationPort notificationPort;

    public ProcessTelemetryUseCase(AssetRepository assetRepository,
                                   AlertRepository alertRepository,
                                   AlertNotificationPort notificationPort) {
        this.assetRepository = assetRepository;
        this.alertRepository = alertRepository;
        this.notificationPort = notificationPort;
    }

    @Transactional
    public Optional<AlertResponse> execute(TelemetryRequest request) {
        assetRepository.findById(UUID.fromString(request.assetId()))
                .orElseThrow(() -> new DomainException(
                        "Asset with id '%s' not found.".formatted(request.assetId())
                ));

        if (request.value() <= request.threshold()) {
            return Optional.empty();
        }


        AlertSeverity severity = calculateSeverity(request.value(), request.threshold());


        Alert alert = new Alert(
                UUID.fromString(request.assetId()),
                request.sensorType(),
                request.value(),
                request.threshold(),
                severity
        );
        Alert saved = alertRepository.save(alert);


        notificationPort.notify(saved);

        return Optional.of(AlertResponse.from(saved));
    }

    private AlertSeverity calculateSeverity(double value, double threshold) {
        double excess = (value - threshold) / threshold;
        if (excess < 0.10) return AlertSeverity.LOW;
        if (excess < 0.25) return AlertSeverity.MEDIUM;
        if (excess < 0.50) return AlertSeverity.HIGH;
        return AlertSeverity.CRITICAL;
    }
}
