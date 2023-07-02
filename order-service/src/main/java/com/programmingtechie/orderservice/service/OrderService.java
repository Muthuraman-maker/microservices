package com.programmingtechie.orderservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.programmingtechie.orderservice.dto.InventoryResponse;
import org.springframework.stereotype.Service;

import com.programmingtechie.orderservice.dto.OrderLineItemsDto;
import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.model.Order;
import com.programmingtechie.orderservice.model.OrderLineItems;
import com.programmingtechie.orderservice.repository.OrderRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;

	private final WebClient.Builder webClientBuilder;

	public String placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		
		order.setOrderNumber(UUID.randomUUID().toString());
		
		List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList()
					.stream()
					.map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList();
		order.setOrderLineItemsList(orderLineItemsList);
		List<String> listOfSkuCode = order.getOrderLineItemsList().stream().map(orderLineItems ->
				orderLineItems.getSkuCode()
		).toList();
		//call inventory service and place order if product is in stock
		//By default web client will make an asynchronous request, but we need to make a synchronous request
		InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", listOfSkuCode).build())
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();

		//To verify the product is in stock for the each inventory response or not
		boolean allProductsIsinStock = Arrays.stream(inventoryResponses).allMatch(inventoryResponse -> inventoryResponse.isInStock());
		if(allProductsIsinStock) {
			orderRepository.save(order);
			return "Order Placed Successfully!..";
		}
		else {
			throw new IllegalArgumentException("Product is not in Stock later...");
		}
	}

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}
	
	
}
