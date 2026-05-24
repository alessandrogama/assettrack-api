package com.assettrack.assettrack_api.infrastructure;

import com.assettrack.assettrack_api.domain.entity.Asset;
import com.assettrack.assettrack_api.domain.repository.AssetRepository;
import com.assettrack.assettrack_api.domain.valueobject.AssetStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
@DisplayName("AssetRepository — Integration with PostgreSQL")
class AssetRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("assettrack_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.autoconfigure.exclude",
                () -> "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration");
    }

    @Autowired
    private AssetRepository assetRepository;

    @Test
    @DisplayName("It must persist and recover the asset with the correct status.")
    void shouldPersistAndRetrieveAsset() {
        // Given
        Asset asset = new Asset("Pump B-99", "Grundfos XL", "Bloco Z", "Integration Test");

        // When
        Asset saved = assetRepository.save(asset);
        Asset found = assetRepository.findById(saved.getId()).orElseThrow();

        // Then
        assertThat(found.getName()).isEqualTo("Pump B-99");
        assertThat(found.getStatus()).isEqualTo(AssetStatus.OPERATIONAL);
        assertThat(found.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("existsByName should return true for existing name.")
    void shouldReturnTrueForExistingName() {
        // Given
        assetRepository.save(new Asset("Compressor C-99", "Atlas", "Local", "Resp"));

        // When / Then
        assertThat(assetRepository.existsByName("Compressor C-99")).isTrue();
        assertThat(assetRepository.existsByName("Compressor Non-existent")).isFalse();
    }

    @Test
    @DisplayName("Maintenance records should be kept with the asset.")
    void shouldPersistMaintenanceHistory() {
        // Given
        Asset asset = new Asset("Engine M-99", "WEG", "Bloco W", "Resp");
        asset.startMaintenance("Periodic review");
        asset.completeMaintenance("Carlos Technical", "Inspection completed, all parts OK.");

        // When
        Asset saved = assetRepository.save(asset);
        Asset found = assetRepository.findById(saved.getId()).orElseThrow();

        // Then
        assertThat(found.getMaintenanceHistory()).hasSize(1);
        assertThat(found.getMaintenanceHistory().get(0).technicianName()).isEqualTo("Carlos Technical");
    }
}
