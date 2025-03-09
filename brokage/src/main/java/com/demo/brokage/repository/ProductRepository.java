package com.demo.brokage.repository;

import com.demo.brokage.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCustomerIdAndCreateDateBetween(String customerId, LocalDateTime startDate, LocalDateTime endDate);
    List<Product> findByCustomerId(String customerId);
}