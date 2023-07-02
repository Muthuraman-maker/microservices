package com.programmingtechie.productservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.programmingtechie.productservice.dto.ProductRequestDTO;
import com.programmingtechie.productservice.dto.ProductResponseDTO;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {
	
	private final ProductRepository productRepository;
	
	//constructor injection
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	

	public void createProduct(ProductRequestDTO productReq) {
		Product product = Product.builder()
								 .name(productReq.getName())
								 .description(productReq.getDescription())
								 .price(productReq.getPrice())
								 .build();
		productRepository.save(product);
		
		log.info("Product {} is saved..", product.getId());
		
	}


	public List<ProductResponseDTO> getAllProducts() {
		List<Product> productList = productRepository.findAll();
		
		return productList.stream()
				   .map(product -> mapToProductResponse(product))
				   .collect(Collectors.toList());
	}
	
	private ProductResponseDTO mapToProductResponse(Product product) {
		return ProductResponseDTO.builder()
				.id(product.getId())
				.name(product.getName())
				.description(product.getDescription())
				.price(product.getPrice())
				.build();
	}
}
