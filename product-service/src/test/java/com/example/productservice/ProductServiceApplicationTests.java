package com.example.productservice;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// first we are going to create an integration test where se will spin up the application context
// the whole app and we will test wheter the 'post' api and the 'get' api if is working or not
// 9
//
//I am working on developing a few integration tests for my spring boot application.
// I am using testcontainers in order to create a MongoDB docker image
// Jupiter integration is provided by means of the @Testcontainers annotation.

// before running the test make sure that your docker is up and running
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	// mongo:4.4.2 the version of mongo to be used for tests
	// static because we will access staticaly the mongodbcontainer and fetch the url of the mongodb database uri
	// Containers declared as static fields will be shared between test methods
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
	// to be able to autowire mockmvc we added @AutoConfigureMockMvc ann
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper; // will convect a pojo object to json and json to pojo object (vice versa)

	// autowire productRepository to ckeck if the product was saved in the db
	@Autowired
	ProductRepository productRepository;

	// at a time of starting the integration test first the test will start the mongodb container by downloading the
	// "mongo:4.4.2" image and after starting the container will get the reprica get url and will add to the
	// spring.data.mongodb.uri property dynamically at a time of creating the test
	// we are doing this because we no longer make use of the property specified manually in the app.properties file
	// and in the integration test we still have to provide the spring.data.uri dynamically at the time we creating the test
	// because we are not using th local mongodb database but we are using mongodb docker container and because of this we have to
	// provide the spring data uri property dynamically
	// to be able to provide it dynamically we added the @DynamicPropertySource ann that will add this property to our
	// spring context dynamically at the time of running the test
	// this are configurations
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry ) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	/** Integration test for the 'addProduct' endpoint from 'ProductController' */
	// To be able to make a request from our integration test we will use mock mvc
	// that provides mocked servlet environment where we can call the productControlelr endpoints
	// and recieve response as was difined in the controllers
	// perform() will tak ein a builder and an object of type request builder
	@Test
	void shouldCreateProduct() throws Exception {
		// Create a productRequest object
		ProductRequest productRequest = getProductRequest();

		// Convert a pojo object to json
		String productRequestString =
				objectMapper.writeValueAsString(productRequest);

		// now the mockmvc will make a call to the api product addProduct endpoint
		// by providing the product request string as a request object
		// and it will expect if is created of not
		mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());

		Assertions.assertEquals(1, productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("iPhone 13")
				.description("iPhone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

	// TODO: Integration test for the 'getProduct' method from 'ProductController'
	/** Integration test for the 'addProduct' endpoint from 'ProductController' */


}
