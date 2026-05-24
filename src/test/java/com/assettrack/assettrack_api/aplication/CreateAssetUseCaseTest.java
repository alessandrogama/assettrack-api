package com.assettrack.assettrack_api.aplication;

import com.assettrack.assettrack_api.application.dto.request.CreateAssetRequest;
import com.assettrack.assettrack_api.application.dto.response.AssetResponse;
import com.assettrack.assettrack_api.application.usecase.asset.CreateAssetUseCase;
import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("CreateAssetUseCase")
class CreateAssetUseCaseTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private CreateAssetUseCase createAssetUseCase;

    private CreateAssetRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new CreateAssetRequest(
                "PUMP B-01", "Grundfos CM5-A", "Bl A — Class 12", "João Mock"
        );
    }

    @Test
    @DisplayName("It should successfully create the asset when the name does not exist.")
    void shouldCreateAssetSuccessfully() {
        // Given
        when(assetRepository.existsByName("Bomba B-01")).thenReturn(false);
        when(assetRepository.save(any(Asset.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        AssetResponse response = createAssetUseCase.execute(validRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Pump B-01");
        assertThat(response.model()).isEqualTo("Grundfos CM5-A");
        assertThat(response.id()).isNotNull();

        verify(assetRepository, times(1)).existsByName("Pump B-01");
        verify(assetRepository, times(1)).save(any(Asset.class));
    }

    @Test
    @DisplayName("It should throw a DomainException when the name already exists.")
    void shouldThrowExceptionWhenNameAlreadyExists() {
        // Given
        when(assetRepository.existsByName("Pump B-01")).thenReturn(true);

        // When / Then
        assertThatThrownBy(() -> createAssetUseCase.execute(validRequest))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Pump B-01");

        verify(assetRepository, never()).save(any());
    }
}
