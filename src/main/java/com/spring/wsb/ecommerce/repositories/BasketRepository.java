package com.spring.wsb.ecommerce.repositories;

import com.spring.wsb.ecommerce.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findTopBy();
}
