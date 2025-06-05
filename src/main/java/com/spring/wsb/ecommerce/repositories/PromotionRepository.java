package com.spring.wsb.ecommerce.repositories;

import com.spring.wsb.ecommerce.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByActivationCodeIgnoreCase(String activationCode);
}
