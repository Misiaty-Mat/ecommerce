package com.spring.wsb.ecommerce.services;

import com.spring.wsb.ecommerce.dto.BasketItemDTO;
import com.spring.wsb.ecommerce.entity.BasketItem;
import com.spring.wsb.ecommerce.entity.Product;
import com.spring.wsb.ecommerce.repositories.BasketItemRepository;
import com.spring.wsb.ecommerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BasketItemService {

    private final BasketItemRepository basketItemRepository;
    private final ProductRepository productRepository;

    public List<BasketItemDTO> listBasketItems() {
        return basketItemRepository.findAll().stream()
                .map(BasketItem::toDto)
                .toList();
    }

    public void addToBasket(final Integer productId) {
        basketItemRepository.findByProduct_Id(productId)
                .ifPresentOrElse(
                        this::updateQuantity,
                        () -> saveNewBasketItem(productId)
                );
    }

    public void removeFromBasket(final Integer id) {
       basketItemRepository.deleteById(id);
    }

    private void updateQuantity(BasketItem basketItem) {
        basketItem.setQuantity(basketItem.getQuantity() + 1);
        basketItemRepository.save(basketItem);
    }

    private void saveNewBasketItem(Integer productId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        final BasketItem basketItem = BasketItem.builder()
                .quantity(1)
                .product(product)
                .build();

        basketItemRepository.save(basketItem);
    }
}
