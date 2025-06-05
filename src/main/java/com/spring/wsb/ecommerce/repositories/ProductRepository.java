package com.spring.wsb.ecommerce.repositories;

import com.spring.wsb.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
