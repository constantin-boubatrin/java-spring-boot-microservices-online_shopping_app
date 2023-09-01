package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional // automatically will create and commit the transactions
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        System.out.println(orderRequest);

        // mapping the 'orderLineItems' that is inside 'orderRequest'
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        // collecting all the skuCodes from the order object
        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        // call inventory service , and place order if product is in stock
        // get method because inside the inventory controller we defined a get-mapping request
        // webclient will format the uri
        // in this format: // http://localhost:8082/api/v2/inventory?skuCode=iphone-13&skuCode=iphone13-red
        InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
//                .uri("http://localhost:8082/api/v1/inventory",
                .uri("http://inventory-service/api/v1/inventory", // becahse we named the application as inv-serv in pom file
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve() // retrieving the response
                .bodyToMono(InventoryResponse[].class) // by default webclient will make an async request but we need a synch. request
                .block();// in this case we are using the .block() method and web client will make an syn req

        // converting the inventoryResponsesArray into a stream and calling the allMath method
        // from java 8
        // if all the elements inside the inventoryResponsesArray are in stock the result will be true
        // even if one is false and the rest are true the return will be false
        boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                .allMatch(InventoryResponse::isInStock);
        // if all the products are in stock then we will save the order in db
        // if result is true the product is in stock otherwise throw exception
        if (allProductsInStock) {
            // saving into order repo
            orderRepository.save(order);
            return "Order Placed Successfully";
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later.");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {

        OrderLineItems orderLineItems = new OrderLineItems();

        // mapping all the field
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());

        return orderLineItems;
    }
}
