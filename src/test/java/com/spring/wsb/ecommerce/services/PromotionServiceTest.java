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
        final BigDecimal percentCheaper = BigDecimal.TEN;
        when(basketRepository.findTopBy()).thenReturn(Basket.builder().promotion(
                Promotion.builder().type(PromotionType.ALL_CHEAPER).percentCheaper(percentCheaper).build()
        ).build());

        //when
        final BigDecimal priceWithPromotion = promotionService.getPriceWithPromotion(getBasketItems());

        //then
        final BigDecimal expectedPrice = PromotionService.getPriceWithNoPromotion(getBasketItems())
                .multiply(BigDecimal.ONE.subtract(percentCheaper.movePointLeft(2)));
        assertEquals(expectedPrice, priceWithPromotion);
    }

    @Test
    void getPriceWithProductsFor1ZlPromotionTest() {
        //given
        when(basketRepository.findTopBy()).thenReturn(Basket.builder().promotion(
                Promotion.builder().type(PromotionType.CHEAPEST_FOR_1_ZL).productNumberNeeded(3L).build()
        ).build());

        //when
        final BigDecimal priceWithPromotion = promotionService.getPriceWithPromotion(getBasketItems());

        //then
        final BigDecimal expectedPrice = BigDecimal.valueOf(797);
        assertEquals(expectedPrice, priceWithPromotion);
    }

    @Test
    void getPriceSameProductCheaperPromotionTest() {
        //given
        when(basketRepository.findTopBy()).thenReturn(Basket.builder().promotion(
                Promotion.builder()
                        .type(PromotionType.SAME_PRODUCT)
                        .productNumberNeeded(2L)
                        .percentCheaper(BigDecimal.valueOf(50))
                        .build()
        ).build());

        //when
        final BigDecimal priceWithPromotion = promotionService.getPriceWithPromotion(getBasketItems());

        //then
        final BigDecimal expectedPrice = BigDecimal.valueOf(680.00);
        assertEquals(0, priceWithPromotion.compareTo(expectedPrice));
    }

    private List<BasketItemDTO> getBasketItems() {
        return List.of(
                BasketItemDTO.builder()
                        .id(1L)
                        .quantity(5L)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.TEN)
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(2L)
                        .quantity(3L)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.valueOf(20))
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(3L)
                        .quantity(1L)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.valueOf(100))
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(4L)
                        .quantity(10L)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.valueOf(30))
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(5L)
                        .quantity(2L)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.TEN)
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(6L)
                        .quantity(15L)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.TEN)
                                .build())
                        .build(),
                BasketItemDTO.builder()
                        .id(7L)
                        .quantity(30L)
                        .product(ProductDTO.builder()
                                .price(BigDecimal.valueOf(6))
                                .build())
                        .build()
        );
    }
}