package com.demo.brokage.service;

import com.demo.brokage.model.Asset;
import com.demo.brokage.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public List<Asset> getAssetsByCustomerId(String customerId) {
        return assetRepository.findByCustomerId(customerId);
    }

    public Asset updateAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public Optional<Asset> getAssetByCustomerIdAndAssetName(String customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(customerId, assetName);
    }
}
