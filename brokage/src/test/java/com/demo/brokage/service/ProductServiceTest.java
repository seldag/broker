package com.demo.brokage.service;


import com.demo.brokage.model.Asset;
import com.demo.brokage.model.Product;
import com.demo.brokage.model.ProductSide;
import com.demo.brokage.model.ProductStatus;
import com.demo.brokage.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTests {

    @Mock
    private ProductRepository orderRepository;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private ProductService orderService;

    @Test
    void testCreateOrder() {
        Product order = new Product();
        order.setCustomerId("500");
        order.setAssetName("TRY");
        order.setSide(ProductSide.BUY);
        order.setSize(100);
        order.setPrice(10.0);

        Asset tryAsset = new Asset();
        tryAsset.setUsableSize(2000);

        when(assetService.getAssetByCustomerIdAndAssetName("500", "TRY")).thenReturn(Optional.of(tryAsset));
        when(orderRepository.save(any(Product.class))).thenReturn(order);

        Product createdOrder = orderService.createOrder(order.getCustomerId(), order.getAssetName(), order.getSide(), order.getSize(), order.getPrice());


        verify(assetService, times(1)).updateAsset(any(Asset.class));
    }

    @Test
    void testListOrders() {
        List<Product> orders = new ArrayList<>();
        Product order = new Product();
        order.setCustomerId("500");
        order.setCreateDate(LocalDateTime.now());
        orders.add(order);

        when(orderRepository.findByCustomerIdAndCreateDateBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(orders);

        List<Product> result = orderService.listOrders("500", LocalDateTime.now().minusDays(1), LocalDateTime.now());

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteOrder() {
        Product order = new Product();
        order.setId(1L);
        order.setCustomerId("500");
        order.setAssetName("TRY");
        order.setSide(ProductSide.BUY);
        order.setSize(100);
        order.setPrice(10.0);
        order.setStatus(ProductStatus.PENDING);

        Asset tryAsset = new Asset();
        tryAsset.setUsableSize(2000);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(assetService.getAssetByCustomerIdAndAssetName("500", "TRY")).thenReturn(Optional.of(tryAsset));

        orderService.deleteOrder(1L);

        verify(orderRepository, times(1)).delete(order);
        verify(assetService, times(1)).updateAsset(any(Asset.class));
    }

    @Test
    void testDeleteOrderThrowsExceptionWhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.deleteOrder(1L));
    }

    @Test
    void testDeleteOrderThrowsExceptionWhenOrderNotPending() {
        Product order = new Product();
        order.setId(1L);
        order.setStatus(ProductStatus.COMPLETED);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> orderService.deleteOrder(1L));
    }
}
