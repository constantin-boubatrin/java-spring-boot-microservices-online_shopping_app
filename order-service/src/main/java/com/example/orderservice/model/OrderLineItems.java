package com.example.orderservice.model;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_order_line_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderLineItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // SKU - stands for "stock keeping unit”
    // is a number (usually eight alphanumeric digits) that retailers assign
    // to products to keep track of stock levels internally.
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}
