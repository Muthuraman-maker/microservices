package com.programmingtechie.inventoryservice.service;

import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.programmingtechie.inventoryservice.repository.InventoryRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {
	
	private final InventoryRepository inventoryRepo;
	@Transactional(readOnly=true)
	@SneakyThrows
	public List<InventoryResponse> isInStock(List<String> skuCode) {
		log.info("Thread started");
		Thread.sleep(10000);
		log.info("Thread Ended");
		return inventoryRepo.findBySkuCodeIn(skuCode)
				.stream()
				.map(inventory ->
					InventoryResponse.builder().skuCode(inventory.getSkuCode())
							.isInStock(inventory.getQuantity() > 0)
							.build()
				).toList();
	}
}
