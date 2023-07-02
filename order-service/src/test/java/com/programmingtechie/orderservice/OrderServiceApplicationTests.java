package com.programmingtechie.orderservice;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.orderservice.dto.OrderLineItemsDto;
import com.programmingtechie.orderservice.dto.OrderRequest;
import com.programmingtechie.orderservice.service.OrderService;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceApplicationTests {
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	OrderService orderService; 
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testPlaceOrder() throws JsonProcessingException, Exception {
		
		OrderRequest orderRequest = createOrderRequest();
		mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(orderRequest)))
				.andExpect(status().isCreated());
	}

	private OrderRequest createOrderRequest() {
		return OrderRequest.builder().orderLineItemsDtoList(orderLineDtoList()).build();
	}

	private List<OrderLineItemsDto> orderLineDtoList() {
		OrderLineItemsDto orderLine1 = new OrderLineItemsDto();
		orderLine1.setId(Long.valueOf("1"));
		orderLine1.setPrice(BigDecimal.valueOf(1200));
		orderLine1.setQuantity(4);
		orderLine1.setSkuCode("iphone21");
		OrderLineItemsDto orderLine2 = new OrderLineItemsDto();
		orderLine2.setId(Long.valueOf("2"));
		orderLine2.setPrice(BigDecimal.valueOf(1000));
		orderLine2.setQuantity(3);
		orderLine2.setSkuCode("iphone13");
		
		List<OrderLineItemsDto> list = new ArrayList<>();
		list.add(orderLine1);
		list.add(orderLine2);
		return list;
		
	}

}
