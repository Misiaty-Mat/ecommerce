package com.spring.wsb.ecommerce.services;

import com.spring.wsb.ecommerce.entity.BasketItem;
import com.spring.wsb.ecommerce.entity.Product;
import com.spring.wsb.ecommerce.repositories.BasketItemRepository;
import com.spring.wsb.ecommerce.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BasketItemServiceTest {

    private final BasketItemRepository basketItemRepository = mock(BasketItemRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);

    private final BasketItemService basketItemService = new BasketItemService(basketItemRepository, productRepository);

    final ArgumentCaptor<BasketItem> basketItemArgumentCaptor = ArgumentCaptor.forClass(BasketItem.class);

    @Test
    void addNewProductToBasketTest() {
        //given
        final long productId = 1;
        final Product product = Product.builder()
                .id(productId)
                .name("product name")
                .build();
        when(basketItemRepository.findByProduct_Id(productId)).thenReturn(Optional.empty());
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        //when
        basketItemService.addToBasket(productId);

        //then
        verify(basketItemRepository).save(basketItemArgumentCaptor.capture());
        assertEquals(1, basketItemArgumentCaptor.getValue().getQuantity());
        assertEquals(productId, basketItemArgumentCaptor.getValue().getProduct().getId());
    }

    @Test
    void addMoreThanOneProductToBasketTest() {
        //given
        final long productId = 1;
        when(basketItemRepository.findByProduct_Id(productId)).thenReturn(Optional.of(
                BasketItem.builder()
                        .quantity(1L)
                        .product(Product.builder().id(productId).quantityOnHand(4L).build())
                        .build()
        ));

        //when
        basketItemService.addToBasket(productId);

        //then
        verify(basketItemRepository).save(basketItemArgumentCaptor.capture());
        assertEquals(2, basketItemArgumentCaptor.getValue().getQuantity());
        assertEquals(productId, basketItemArgumentCaptor.getValue().getProduct().getId());
    }
}