package com.example.productservice.service;

import com.example.productservice.dto.ProductRequest;
import com.example.productservice.dto.ProductResponse;
import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
// injecting the ProductRepository
@RequiredArgsConstructor// will create at the compile time the constructor will all the required constructor arguments
// make use of logging with the help of slf4j from lombok
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * This will CREATE a product. C of CRUD.
     * @param productRequest
     * @return
     */
    public void createProduct(ProductRequest productRequest) {
        // first we have to map the product request to the product model
        // builder method to build the product object
        // build() method will create the object with all the requested details
        // instance of the product object
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        // save to database
        productRepository.save(product);

        // add the logg for info
        // with slf4j we have make use of placeholder {} - this wil dynamicaly will get the id of the product
        // and will replace it on the placeholder
        log.info("Product {} is saved.", product.getId());
    }

    /**
     * This will CREATE a product. C of CRUD.
     * @param productRequest
     * @return
     */
    public List<ProductResponse> getAllProducts() {
        // read all products insede the db
        List<Product> products = productRepository.findAll();
        // now we have to map the products class from List into a product response class
        // by using the java 8 Streams with map function/method
        // Using Lambda
//        products.stream().map(product -> mapToProductResponse(product));
        // Because the method is inside the class we can use method reference instead of Lambda
        return  products.stream().map(this::mapToProductResponse).toList();
    }

    // Maps the Product Class to the ProductResponse Class
    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId()) // map the product id to the productresponse id
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
