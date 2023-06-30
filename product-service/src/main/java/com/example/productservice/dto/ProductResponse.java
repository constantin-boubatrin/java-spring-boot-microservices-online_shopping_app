package com.example.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/** ProductResponse acts as a DTO
 * insetead of using the product class we created productrequest and product response classees
 * because it is a good practice to separate the model entities and the DTO's
 * we dont have to expose our product entities to outside world because if in the future we will add more fields
 * to the Product Class that will be neccesary for the bussines model but not for the outside world
 * because of this we have to keep it separate
 **/
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
