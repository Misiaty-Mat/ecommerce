package com.spring.wsb.ecommerce.config;

import com.spring.wsb.ecommerce.constants.Category;
import com.spring.wsb.ecommerce.constants.PromotionType;
import com.spring.wsb.ecommerce.entity.Basket;
import com.spring.wsb.ecommerce.entity.Product;
import com.spring.wsb.ecommerce.entity.Promotion;
import com.spring.wsb.ecommerce.repositories.BasketRepository;
import com.spring.wsb.ecommerce.repositories.ProductRepository;
import com.spring.wsb.ecommerce.repositories.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final PromotionRepository promotionRepository;
    private final BasketRepository basketRepository;

    @Override
    public void run(String... args) {
        loadProductsData();
        loadBasket();
        loadPromotions();
    }

    private void loadProductsData() {
        final List<Product> products = List.of(
                Product.builder()
                        .name("T-shirt")
                        .category(Category.CLOTHING)
                        .price(new BigDecimal("29.99"))
                        .quantityOnHand(200L)
                        .available(true)
                        .build(),

                Product.builder()
                        .name("Cebula")
                        .category(Category.FOOD)
                        .price(new BigDecimal("1.50"))
                        .quantityOnHand(40L)
                        .available(true)
                        .build(),

                Product.builder()
                        .name("Czekolada")
                        .category(Category.FOOD)
                        .price(new BigDecimal("8.99"))
                        .quantityOnHand(0L)
                        .available(false)
                        .build(),

                Product.builder()
                        .name("Baton")
                        .category(Category.FOOD)
                        .price(new BigDecimal("2.99"))
                        .quantityOnHand(5L)
                        .available(true)
                        .build(),

                Product.builder()
                        .name("Makaron")
                        .category(Category.FOOD)
                        .price(new BigDecimal("14.99"))
                        .quantityOnHand(84L)
                        .available(true)
                        .build()
        );

        productRepository.saveAll(products);
    }

    private void loadPromotions() {
        final List<Promotion> promotions = List.of(
                Promotion.builder()
                        .name("10% taniej na wszystkie produkty w Koszyku")
                        .type(PromotionType.ALL_CHEAPER)
                        .percentCheaper(new BigDecimal(10))
                        .activationCode("wsb1")
                        .build(),
                Promotion.builder()
                        .name("przy zakupie 3 produktów najtańszy jest za złotówkę")
                        .type(PromotionType.CHEAPEST_FOR_1_ZL)
                        .productNumberNeeded(3L)
                        .activationCode("wsb2")
                        .build(),
                Promotion.builder()
                        .name("przy zakupie 2 takich samych produktów, drugi z nich jest za połowę ceny")
                        .type(PromotionType.SAME_PRODUCT)
                        .productNumberNeeded(2L)
                        .percentCheaper(new BigDecimal(50))
                        .activationCode("wsb3")
                        .build()
        );

        promotionRepository.saveAll(promotions);
    }

    private void loadBasket() {
        basketRepository.save(new Basket());
    }
}
