package com.demo.brokage.service;


import com.demo.brokage.model.Asset;
import com.demo.brokage.repository.AssetRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AssetServiceTests {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetService assetService;

    @Test
    void testGetAssetByCustomerIdAndAssetName() {
        Asset asset = new Asset();
        asset.setCustomerId("500");
        asset.setAssetName("TRY");
        asset.setUsableSize(2000);

        when(assetRepository.findByCustomerIdAndAssetName("500", "TRY")).thenReturn(Optional.of(asset));

        Optional<Asset> result = assetService.getAssetByCustomerIdAndAssetName("500", "TRY");

        assertEquals(asset, result.orElse(null));
    }

    @Test
    void testGetAssetByCustomerIdAndAssetNameNotFound() {
        when(assetRepository.findByCustomerIdAndAssetName("500", "TRY")).thenReturn(Optional.empty());

        Optional<Asset> result = assetService.getAssetByCustomerIdAndAssetName("500", "TRY");

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testUpdateAsset() {
        Asset asset = new Asset();
        asset.setCustomerId("500");
        asset.setAssetName("TRY");
        asset.setUsableSize(2000);

        when(assetRepository.save(any(Asset.class))).thenReturn(asset);

        assetService.updateAsset(asset);

        verify(assetRepository, times(1)).save(asset);
    }

    @Test
    void testUpdateAssetThrowsException() {
        Asset asset = new Asset();
        asset.setCustomerId("500");
        asset.setAssetName("TRY");
        asset.setUsableSize(2000);

        doThrow(new RuntimeException("Update failed")).when(assetRepository).save(any(Asset.class));

        assertThrows(RuntimeException.class, () -> assetService.updateAsset(asset));
    }
}
