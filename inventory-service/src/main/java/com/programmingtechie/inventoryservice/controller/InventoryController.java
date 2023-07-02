package com.programmingtechie.inventoryservice.controller;

import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
	
	private final InventoryService inventoryService;
	
	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	//http://localhost:8082/api/inventory?skuCode=iphone_13&skuCode=iphone_13_red
	public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
		
		return inventoryService.isInStock(skuCode);
	}
}
