package com.example.orderservice.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
// take all fields from OrderLineItems class
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItemsDto {
    private Long id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;
}
