package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    // to consume the Exception from .sleep() method,
    // dont use in the production instead catch the Exception
    @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        // Simulating the slow behavior to test Resilience4 Timeout Property
        log.info("Wait Started");
        Thread.sleep(10000);
        log.info("Wait Ended");

        // find all the inventory objects for the given skuCode
        return inventoryRepository.findBySkuCodeIn(skuCode)
                .stream()
                // mapping the inventory objects to the inventory response object
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
    }
}
