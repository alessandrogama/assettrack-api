package com.assettrack.assettrack_api.aplication;


import com.assettrack.assettrack_api.application.dto.request.TelemetryRequest;
import com.assettrack.assettrack_api.application.dto.response.AlertResponse;
import com.assettrack.assettrack_api.application.usecase.alert.AlertNotificationPort;
import com.assettrack.assettrack_api.application.usecase.alert.ProcessTelemetryUseCase;
import com.assettrack.assettrack_api.domain.entity.Alert;
import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.repository.AlertRepository;
import com.assettrack.assettrack_api.domain.repository.AssetRepository;
import com.assettrack.assettrack_api.domain.valueobject.AlertSeverity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProcessTelemetryUseCase")
class ProcessTelemetryUseCaseTest {

    @Mock private AssetRepository assetRepository;
    @Mock private AlertRepository alertRepository;
    @Mock private AlertNotificationPort notificationPort;

    @InjectMocks
    private ProcessTelemetryUseCase processTelemetryUseCase;

    private final UUID assetId = UUID.randomUUID();

    @Test
    @DisplayName("You should not create an alert when the reading is below the threshold.")
    void shouldNotCreateAlertWhenBelowThreshold() {
        TelemetryRequest request = new TelemetryRequest(assetId.toString(), "TEMPERATURE", 85.0, 90.0);
        when(assetRepository.findById(assetId)).thenReturn(Optional.of(mockAsset()));

        // When
        Optional<AlertResponse> result = processTelemetryUseCase.execute(request);

        // Then
        assertThat(result).isEmpty();
        verify(alertRepository, never()).save(any());
        verify(notificationPort, never()).notify(any());
    }

    @Test
    @DisplayName("A LOW alert should be created when the value is 5% above the threshold.")
    void shouldCreateLowAlertWhen5PercentAboveThreshold() {
        // Given: temperature 94.5°C (5% above de 90°C)
        TelemetryRequest request = new TelemetryRequest(assetId.toString(), "TEMPERATURE", 94.5, 90.0);
        when(assetRepository.findById(assetId)).thenReturn(Optional.of(mockAsset()));
        when(alertRepository.save(any(Alert.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Optional<AlertResponse> result = processTelemetryUseCase.execute(request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().severity()).isEqualTo(AlertSeverity.LOW);
        verify(notificationPort, times(1)).notify(any());
    }

    @Test
    @DisplayName("A CRITICAL alert should be created when the value is 60% above the threshold.\n")
    void shouldCreateCriticalAlertWhen60PercentAboveThreshold() {
        // Given: pressure 144 bar (60% above 90 bar)
        TelemetryRequest request = new TelemetryRequest(assetId.toString(), "PRESSURE", 144.0, 90.0);
        when(assetRepository.findById(assetId)).thenReturn(Optional.of(mockAsset()));
        when(alertRepository.save(any(Alert.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Optional<AlertResponse> result = processTelemetryUseCase.execute(request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().severity()).isEqualTo(AlertSeverity.CRITICAL);
    }

    private Asset mockAsset() {
        return new Asset("Test Pump", "Model", "Local", "Responsible");
    }
}
