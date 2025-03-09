package com.demo.brokage.controller;

import com.demo.brokage.model.Product;

import com.demo.brokage.model.ProductSide;
import com.demo.brokage.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Product Controller", description = "APIs for managing stock orders")
public class ProductController {

    @Autowired
    private ProductService orderService;

    @PostMapping
    public ResponseEntity<Product> createOrder(@RequestParam String customerId, @RequestParam String assetName,
                                               @RequestParam ProductSide side, @RequestParam int size, @RequestParam double price) {
        Product order = orderService.createOrder(customerId, assetName, side, size, price);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Product>> listOrders(@RequestParam String customerId,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Product> orders = orderService.listOrders(customerId, startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}
