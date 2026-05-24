package com.assettrack.assettrack_api.adapters.http.controller;

import com.assettrack.assettrack_api.application.dto.request.CreateAssetRequest;
import com.assettrack.assettrack_api.application.dto.request.MaintenanceRequest;
import com.assettrack.assettrack_api.application.dto.request.UpdateAssetRequest;
import com.assettrack.assettrack_api.application.dto.response.AssetResponse;
import com.assettrack.assettrack_api.application.usecase.asset.CreateAssetUseCase;
import com.assettrack.assettrack_api.application.usecase.asset.GetAssetUseCase;
import com.assettrack.assettrack_api.application.usecase.asset.MaintenanceUseCase;
import com.assettrack.assettrack_api.application.usecase.asset.UpdateAssetUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@Tag(name = "Assets", description = "Industrial asset management")
@SecurityRequirement(name = "bearerAuth")
public class AssetController {

    private final CreateAssetUseCase createAssetUseCase;
    private final GetAssetUseCase getAssetUseCase;
    private final UpdateAssetUseCase updateAssetUseCase;
    private final MaintenanceUseCase maintenanceUseCase;

    public AssetController(CreateAssetUseCase createAssetUseCase,
                           GetAssetUseCase getAssetUseCase,
                           UpdateAssetUseCase updateAssetUseCase,
                           MaintenanceUseCase maintenanceUseCase) {
        this.createAssetUseCase = createAssetUseCase;
        this.getAssetUseCase = getAssetUseCase;
        this.updateAssetUseCase = updateAssetUseCase;
        this.maintenanceUseCase = maintenanceUseCase;
    }

    @GetMapping
    @Operation(summary = "List all assets")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'TECNICO')")
    public ResponseEntity<List<AssetResponse>> findAll() {
        return ResponseEntity.ok(getAssetUseCase.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Asset search by ID.")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'TECNICO')")
    public ResponseEntity<AssetResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(getAssetUseCase.findById(id));
    }

    @PostMapping
    @Operation(summary = "Register new asset\n")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<AssetResponse> create(@Valid @RequestBody CreateAssetRequest request) {
        AssetResponse response = createAssetUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update asset data\n")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR')")
    public ResponseEntity<AssetResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateAssetRequest request
    ) {
        return ResponseEntity.ok(updateAssetUseCase.execute(id, request));
    }

    @PostMapping("/{id}/maintenance/start")
    @Operation(summary = "Initiate asset maintenance.")
    @PreAuthorize("hasAnyRole('ADMIN', 'GESTOR', 'TECNICO')")
    public ResponseEntity<AssetResponse> startMaintenance(
            @PathVariable String id,
            @RequestBody MaintenanceRequest request
    ) {
        return ResponseEntity.ok(maintenanceUseCase.start(id, request));
    }

    @PostMapping("/{id}/maintenance/complete")
    @Operation(summary = "Asset maintenance completed.")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<AssetResponse> completeMaintenance(
            @PathVariable String id,
            @RequestBody MaintenanceRequest request
    ) {
        return ResponseEntity.ok(maintenanceUseCase.complete(id, request));
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivates the asset permanently.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AssetResponse> deactivate(@PathVariable String id) {
        return ResponseEntity.ok(maintenanceUseCase.deactivate(id));
    }
}
