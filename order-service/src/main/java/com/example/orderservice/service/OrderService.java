package com.example.orderservice.service;

import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional // automatically will create and commit the transactions
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Transactional
    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        System.out.println(orderRequest);
        // mapping the 'orderLineItems' that is inside 'orderRequest'
        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        // call inventory service , and place order if product is in stock
        // get method because inside the inventory controller we defined a get-mapping request
        Boolean result = webClient.get()
                .uri("http://localhost:8082/api/v1/inventory")
                .retrieve() // retrivind the response
                .bodyToMono(Boolean.class) // by default webclient will make an async request but we need a synch. request
                .block();// in this case we are using the .block() method and web client will make an syn req

        // if result is true the product is in stock otherwise throw exception
        if (result) {
            // saving into order repo
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
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
