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

    public void addToBasket(final Long productId) {
        basketItemRepository.findByProduct_Id(productId)
                .ifPresentOrElse(
                        this::updateQuantity,
                        () -> saveNewBasketItem(productId)
                );
    }

    public void removeFromBasket(final Long id) {
        basketItemRepository.findById(id)
                .ifPresent(basketItem -> {
                    final Product product = basketItem.getProduct();
                    product.setAvailable(true);
                    productRepository.save(product);
                    basketItemRepository.delete(basketItem);
                });
    }

    public void confirmPurchase() {
        basketItemRepository.findAll().forEach(basketItem -> {
            final long quantity = basketItem.getQuantity();
            final Product product = basketItem.getProduct();
            long newQuantity = product.getQuantityOnHand() - quantity;

            if (newQuantity < 0) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }

            product.setQuantityOnHand(newQuantity);
            productRepository.save(product);
        });
        basketItemRepository.deleteAll();
    }

    private void updateQuantity(final BasketItem basketItem) {
        final Product product = basketItem.getProduct();
        basketItem.setQuantity(basketItem.getQuantity() + 1);

        final Long availableProducts = product.getQuantityOnHand();
        if (availableProducts - basketItem.getQuantity() == 0) {
            product.setAvailable(false);
            productRepository.save(product);
        }

        basketItemRepository.save(basketItem);
    }

    private void saveNewBasketItem(final Long productId) {
        final Product product = productRepository.findById(productId)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
        final BasketItem basketItem = BasketItem.builder()
                .quantity(1L)
                .product(product)
                .build();

        basketItemRepository.save(basketItem);
    }
}
