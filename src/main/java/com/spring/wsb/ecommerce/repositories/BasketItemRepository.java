package com.spring.wsb.ecommerce.repositories;

import com.spring.wsb.ecommerce.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasketItemRepository extends JpaRepository<BasketItem, Integer> {

    Optional<BasketItem> findByProduct_Id(Integer id);
}
