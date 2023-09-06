package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // this placeOrder method will be executed in a different Thread and whenever the timeout is reached
    // will throw a TimeoutException
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // the Resilience4j will apply the CB logic/pattern to this particular method at each call
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory") // the name should be the same as we provided inside the app.prop file
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        return CompletableFuture.supplyAsync(() -> orderService.placeOrder(orderRequest));
    }

//    public String planeOrder(@RequestBody OrderRequest orderRequest) {
//        return orderService.placeOrder(orderRequest);
//    }

    // We have one more parameter RuntimeException because when our call will fail will come with an exception,
    // and we will consume this exception into the fallbackMethod
    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest,
                                                    RuntimeException runtimeException) {
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please order after some time!");
    }
}
