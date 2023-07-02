package com.programmingtechie.productservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.productservice.dto.ProductRequestDTO;
import com.programmingtechie.productservice.dto.ProductResponseDTO;
import com.programmingtechie.productservice.service.ProductService;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	ProductService productService;

	// List<ProductResponseDTO> prodList = new ArrayList<ProductResponseDTO>();
	// Define the mongo db containers inside the test, No Args constructor support
	// is deprecated
	// hence we have to provide the version of the mongodb we want to use
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	// In this way, first of all at the start of the integration test,
	// the test will start the mongodbcontainer, by taking the uri "mongo:4.4.2"
	// image. And then after starting the container it will get the replicaSet url
	// and add it to the
	// spring.data.mongodb.uri property dynamically at the time of creating the test
	@DynamicPropertySource // Which will add this property to our spring context, dynamically at the time
							// of running the test
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequestDTO productRequestDTO = getProductRequest();
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequestDTO))).andExpect(status().isCreated());
		// assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequestDTO getProductRequest() { // TODO Auto-generated method
		return ProductRequestDTO.builder().name("iphone-13").description("iphone 13").price(BigDecimal.valueOf(1200))
				.build();
	}

	@Test
	void testGetAllProducts() throws Exception {
		ProductResponseDTO productResponseOne = new ProductResponseDTO("1", "iphone", "iphone",
				BigDecimal.valueOf(1200));
		;

		ProductResponseDTO productResponseTwo = new ProductResponseDTO("2", "iphone2", "iphone2",
				BigDecimal.valueOf(1300));

		List<ProductResponseDTO> prodResList = new ArrayList<>();

		prodResList.add(productResponseOne);
		prodResList.add(productResponseTwo);

		when(productService.getAllProducts()).thenReturn(prodResList);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product")).andDo(print()).andExpect(status().isOk());
		assertEquals(2, productService.getAllProducts().size());
	}

}
