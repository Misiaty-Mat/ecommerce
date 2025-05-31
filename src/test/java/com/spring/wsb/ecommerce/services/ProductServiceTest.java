package com.spring.wsb.ecommerce.services;

import com.spring.wsb.ecommerce.constants.Category;
import com.spring.wsb.ecommerce.dto.ProductDTO;
import com.spring.wsb.ecommerce.entity.Product;
import com.spring.wsb.ecommerce.repositories.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);

    private final ProductService productService = new ProductService(productRepository);

    @Test
    void listProductByCategoryTest() {
        //given
        when(productRepository.findAll()).thenReturn(getProducts());

        //when
        List<ProductDTO> products = productService.listProductByCategory(Category.FOOD);

        //then
        assertEquals(1, products.size());
    }

    private List<Product> getProducts() {
        return List.of(
                Product.builder()
                        .category(Category.FOOD)
                        .available(true)
                        .build(),
                Product.builder()
                        .category(Category.FOOD)
                        .available(false)
                        .build(),
                Product.builder()
                        .category(Category.CLOTHING)
                        .available(true)
                        .build()
        );
    }
}