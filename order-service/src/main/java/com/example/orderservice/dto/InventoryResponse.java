package com.example.orderservice.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

// this class will store the skuCode and the variable that will say if the product is in stock or not
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {
    private String skuCode;
    private boolean isInStock;
}
