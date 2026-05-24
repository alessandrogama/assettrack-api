package com.assettrack.assettrack_api.domain;

import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.exception.DomainException;
import com.assettrack.assettrack_api.domain.valueobject.AssetStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Asset — Regras de Negócio")
class AssetTest {

    // ─── Create ──────────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Asset Create")
    class Creation {

        @Test
        @DisplayName("You should create an asset with an OPERATIONAL status by default.\n")
        void shouldCreateAssetWithOperationalStatus() {
            // Given / When
            Asset asset = new Asset("Pump B-01", "Grundfos CM5-A", "Bloco A", "João Silva");

            // Then
            assertThat(asset.getStatus()).isEqualTo(AssetStatus.OPERATIONAL);
            assertThat(asset.getId()).isNotNull();
            assertThat(asset.getCreatedAt()).isNotNull();
            assertThat(asset.getMaintenanceHistory()).isEmpty();
        }

        @Test
        @DisplayName("An exception should be thrown if the name is empty.")
        void shouldThrowExceptionWhenNameIsBlank() {
            assertThatThrownBy(() -> new Asset("", "Model", "Local", "Resp"))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("Asset name is required.");
        }

        @Test
        @DisplayName("An exception should be thrown if the name has fewer than 3 characters.")
        void shouldThrowExceptionWhenNameIsTooShort() {
            assertThatThrownBy(() -> new Asset("AB", "Model", "Local", "Resp"))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("3 e 100 characters");
        }
    }

    // ─── Maintenance ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Maintenance cycle")
    class Maintenance {

        @Test
        @DisplayName("Operational asset maintenance must begin.")
        void shouldStartMaintenanceFromOperationalStatus() {
            // Given
            Asset asset = new Asset("Compressor C-01", "Atlas Copco", "BL B", "Maria");

            // When
            asset.startMaintenance("Bearing failure");

            // Then
            assertThat(asset.getStatus()).isEqualTo(AssetStatus.UNDER_MAINTENANCE);
        }

        @Test
        @DisplayName("Do not initiate maintenance if the asset is already undergoing maintenance.")
        void shouldNotStartMaintenanceIfAlreadyInMaintenance() {
            // Given
            Asset asset = new Asset("Valve V-01", "Emerson", "BL C", "Carlos");
            asset.startMaintenance("Leak detected");

            // When / Then
            assertThatThrownBy(() -> asset.startMaintenance("Other problem"))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("UNDER_MAINTENANCE");
        }

        @Test
        @DisplayName("You must complete maintenance and record it in the history.")
        void shouldCompleteMaintenance() {
            // Given
            Asset asset = new Asset("Engine M-01", "WEG", "BL D", "Ana");
            asset.startMaintenance("Overheating");

            // When
            asset.completeMaintenance("Carlos Technical", "Rear wheel bearing replaced.");

            // Then
            assertThat(asset.getStatus()).isEqualTo(AssetStatus.OPERATIONAL);
            assertThat(asset.getMaintenanceHistory()).hasSize(1);
            assertThat(asset.getMaintenanceHistory().get(0).technicianName()).isEqualTo("Carlos Technical");
        }

        @Test
        @DisplayName("Não deve concluir manutenção sem notas")
        void shouldNotCompleteMaintenanceWithoutNotes() {
            // Given
            Asset asset = new Asset("Pump B-02", "Grundfos", "BL A", "Pedro");
            asset.startMaintenance("Problem");

            // When / Then
            assertThatThrownBy(() -> asset.completeMaintenance("Technical", ""))
                    .isInstanceOf(DomainException.class)
                    .hasMessageContaining("Closing notes");
        }

        @Test
        @DisplayName("History must be externally immutable.")
        void maintenanceHistoryShouldBeImmutable() {
            // Given
            Asset asset = new Asset("Sensor S-01", "Siemens", "BL E", "Lucas");

            // When / Then — tries to modify the returned list
            assertThatThrownBy(() -> asset.getMaintenanceHistory().clear())
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }

    // ─── Deactivation ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("Deactivation")
    class Deactivation {

        @Test
        @DisplayName("You must disable the active status and change it to INACTIVE.\n")
        void shouldDeactivateAsset() {
            Asset asset = new Asset("Equipment E-01", "Model", "Local", "Resp");
            asset.deactivate();
            assertThat(asset.getStatus()).isEqualTo(AssetStatus.INACTIVE);
        }
    }
}
