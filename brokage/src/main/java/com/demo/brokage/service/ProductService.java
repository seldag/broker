package com.demo.brokage.service;

import com.demo.brokage.model.Asset;
import com.demo.brokage.model.Product;
import com.demo.brokage.model.ProductSide;
import com.demo.brokage.model.ProductStatus;
import com.demo.brokage.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository orderRepository;

    @Autowired
    private AssetService assetService;

    public Product createOrder(String customerId, String assetName, ProductSide side, int size, double price) {
        // Check if the customer has enough usable size in their TRY asset or the asset they want to sell
        Asset asset = assetService.getAssetByCustomerIdAndAssetName(customerId, assetName)
                .orElseThrow(() -> new RuntimeException("Asset not found"));

        if (side == ProductSide.BUY) {
            Asset tryAsset = assetService.getAssetByCustomerIdAndAssetName(customerId, "TRY")
                    .orElseThrow(() -> new RuntimeException("TRY asset not found"));
            if (tryAsset.getUsableSize() < size * price) {
                throw new RuntimeException("Not enough TRY to buy the asset");
            }
            tryAsset.setUsableSize(tryAsset.getUsableSize() - (int) (size * price));
            assetService.updateAsset(tryAsset);
        } else if (side == ProductSide.SELL) {
            if (asset.getUsableSize() < size) {
                throw new RuntimeException("Not enough asset to sell");
            }
            asset.setUsableSize(asset.getUsableSize() - size);
            assetService.updateAsset(asset);
        }

        Product order = new Product();
        order.setCustomerId(customerId);
        order.setAssetName(assetName);
        order.setSide(side);
        order.setSize(size);
        order.setPrice(price);
        order.setStatus(ProductStatus.PENDING);
        order.setCreateDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public List<Product> listOrders(String customerId, LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate);
    }

    public void deleteOrder(Long orderId) {
        Product order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getStatus() != com.demo.brokage.model.ProductStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be deleted");
        }

        // Update the usable size of the TRY asset or the asset that was being sold
        if (order.getSide() == ProductSide.BUY) {
            Asset tryAsset = assetService.getAssetByCustomerIdAndAssetName(order.getCustomerId(), "TRY")
                    .orElseThrow(() -> new RuntimeException("TRY asset not found"));
            tryAsset.setUsableSize(tryAsset.getUsableSize() + (int) (order.getSize() * order.getPrice()));
            assetService.updateAsset(tryAsset);
        } else if (order.getSide() == ProductSide.SELL) {
            Asset asset = assetService.getAssetByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                    .orElseThrow(() -> new RuntimeException("Asset not found"));
            asset.setUsableSize(asset.getUsableSize() + order.getSize());
            assetService.updateAsset(asset);
        }

        orderRepository.delete(order);
    }
}