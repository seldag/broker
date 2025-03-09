package com.demo.brokage.controller;

import com.demo.brokage.model.Asset;
import com.demo.brokage.service.AssetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assets")
@Tag(name = "Asset Controller", description = "APIs for managing stock orders")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @GetMapping
    public ResponseEntity<List<Asset>> listAssets(@RequestParam String customerId) {
        List<Asset> assets = assetService.getAssetsByCustomerId(customerId);
        return ResponseEntity.ok(assets);
    }
}