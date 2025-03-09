package com.demo.brokage;


import com.demo.brokage.model.Asset;
import com.demo.brokage.model.Product;
import com.demo.brokage.model.ProductSide;
import com.demo.brokage.model.ProductStatus;
import com.demo.brokage.repository.ProductRepository;
import com.demo.brokage.service.AssetService;
import com.demo.brokage.service.ProductService;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class BrokageApplicationTests {



	@Test
	void contextLoads() {
	}

@Mock
private ProductRepository orderRepository;
	@InjectMocks
	private ProductService orderService;

	@Mock
	AssetService assetService ;
	@Test public void testCreateOrder() {
		Product order = new Product();
		order.setCustomerId("500"); order.setAssetName("TRY");
		order.setSide(ProductSide.BUY);
		order.setSize(100);
		order.setPrice(10.0);
		when(orderRepository.save(any(Product.class))).thenReturn(order);
		Optional<Asset> assesList = Optional.empty();

		//when(assetService.getAssetByCustomerIdAndAssetName(any(), any())).thenReturn( assesList);

		//Product createdOrder = orderService.createOrder(order.getCustomerId() ,order.getAssetName(),order.getSide(),order.getSize(),order.getPrice());
		//assertEquals(ProductStatus.PENDING, createdOrder.getStatus());
	}


}
