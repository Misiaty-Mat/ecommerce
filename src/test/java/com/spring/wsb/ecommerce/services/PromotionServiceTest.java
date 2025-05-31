package com.spring.wsb.ecommerce.services;

import com.spring.wsb.ecommerce.constants.PromotionType;
import com.spring.wsb.ecommerce.dto.BasketItemDTO;
import com.spring.wsb.ecommerce.dto.ProductDTO;
import com.spring.wsb.ecommerce.entity.Basket;
import com.spring.wsb.ecommerce.entity.Promotion;
import com.spring.wsb.ecommerce.repositories.BasketRepository;
import com.spring.wsb.ecommerce.repositories.PromotionRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PromotionServiceTest {

    private final PromotionRepository promotionRepository = mock(PromotionRepository.class);
    private final BasketRepository basketRepository = mock(BasketRepository.class);

    private final PromotionService promotionService = new PromotionService(promotionRepository, basketRepository);

    @Test
    void getPriceWithTenPercentCheaperPromotionTest() {
        //given
        BigDecimal percentCheaper = BigDecimal.TEN;
        when(basketRepository.findTopBy()).thenReturn(Basket.builder().promotion(
                Promotion.builder().type(PromotionType.ALL_CHEAPER).percentCheaper(percentCheaper).build()
        ).build());

        //when
        BigDecimal priceWithPromotion = promotionService.getPriceWithPromotion(getBasketItems());

        //then
        BigDecimal expectedPrice = PromotionService.getPriceWithNoPromotion(getBasketItems())
                .multiply(BigDecimal.ONE.subtract(percentCheaper.movePointLeft(2)));
        assertEquals(expectedPrice, priceWithPromotion);
    }

    @Test
    void getPriceWithProductsFor1ZlPromotionTest() {
        //given
        when(basketRepository.findTopBy()).thenReturn(Basket.builder().promotion(
                Promotion.builder().type(PromotionType.CHEAPEST_FOR_1_ZL).productNumberNeeded(3).build()
        ).build());

        //when
        BigDecimal priceWithPromotion = promotionService.getPriceWithPromotion(getBasketItems());

        //then
        BigDecimal expectedPrice = BigDecimal.valueOf(797);
        assertEquals(expectedPrice, priceWithPromotion);
    }

    @Test
    void getPriceSameProductCheaperPromotionTest() {
        //given
        when(basketRepository.findTopBy()).thenReturn(Basket.builder().promotion(
                Promotion.builder()
                        .type(PromotionType.SAME_PRODUCT)
                        .productNumberNeeded(2)
                        .percentCheaper(BigDecimal.valueOf(50))
                        .build()
        ).build());

        //when
        BigDecimal priceWithPromotion = promotionService.getPriceWithPromotion(getBasketItems());

        //then
        BigDecimal expectedPrice = BigDecimal.valueOf(680.00);
        assertEquals(0, priceWithPromotion.compareTo(expectedPrice));
    }

    private List<BasketItemDTO> getBasketItems() {
        return List.of(
                BasketItemDTO.builder()
                        .id(1)
                        .quantity(5)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.TEN)
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(2)
                        .quantity(3)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.valueOf(20))
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(3)
                        .quantity(1)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.valueOf(100))
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(4)
                        .quantity(10)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.valueOf(30))
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(5)
                        .quantity(2)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.TEN)
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(6)
                        .quantity(15)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.TEN)
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(7)
                        .quantity(30)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.valueOf(6))
                                .build())
                        .build()
        );
    }
}