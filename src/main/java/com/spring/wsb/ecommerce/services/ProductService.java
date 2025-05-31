package com.spring.wsb.ecommerce.services;

import com.spring.wsb.ecommerce.constants.Category;
import com.spring.wsb.ecommerce.dto.ProductDTO;
import com.spring.wsb.ecommerce.entity.Product;
import com.spring.wsb.ecommerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductDTO> listAllProducts() {
        return productRepository.findAll().stream()
                .map(Product::toDto)
                .sorted(Comparator.comparing(ProductDTO::getName))
                .toList();
    }

    public List<ProductDTO> listProductByCategory(final Category category) {
        return productRepository.findAll().stream()
                .filter(product -> product.getCategory() == category)
                .filter(Product::getAvailable)
                .map(Product::toDto)
                .sorted(Comparator.comparing(ProductDTO::getPrice))
                .toList();
    }
}
