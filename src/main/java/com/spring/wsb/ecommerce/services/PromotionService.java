package com.spring.wsb.ecommerce.services;

import com.spring.wsb.ecommerce.dto.BasketItemDTO;
import com.spring.wsb.ecommerce.entity.Basket;
import com.spring.wsb.ecommerce.entity.Promotion;
import com.spring.wsb.ecommerce.repositories.BasketRepository;
import com.spring.wsb.ecommerce.repositories.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final BasketRepository basketRepository;

    public void checkPromotionCode(final String code) {
        promotionRepository.findByActivationCodeIgnoreCase(code).ifPresent(promotion -> {
            final Basket basket = basketRepository.findTopBy();
            basket.setPromotion(promotion);
            basketRepository.save(basket);
        });
    }

    public Promotion getActivePromotion() {
        return basketRepository.findTopBy().getPromotion();
    }

    public BigDecimal getPriceWithPromotion(final List<BasketItemDTO> basketItems) {
        final Promotion promotion = getActivePromotion();

        if (promotion == null) {
            return getPriceWithNoPromotion(basketItems);
        }

        switch (promotion.getType()) {
            case ALL_CHEAPER -> {
                final BigDecimal basePrice = basketItems.stream()
                        .map(PromotionService::getItemPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                final BigDecimal discountPercent = BigDecimal.ONE.subtract(promotion.getPercentCheaper().movePointLeft(2));
                return basePrice.multiply(discountPercent);
            }
            case CHEAPEST_FOR_1_ZL -> {
                long numOfProductsFor1Zl = basketItems.size() / promotion.getProductNumberNeeded();
                if (numOfProductsFor1Zl != 0) {

                    final List<Long> productsFor1Zl = basketItems.stream()
                            .sorted(Comparator.comparing(PromotionService::getItemPrice))
                            .map(BasketItemDTO::getId)
                            .limit(numOfProductsFor1Zl)
                            .toList();

                    return basketItems.stream()
                            .map(basketItem -> {
                                if (productsFor1Zl.contains(basketItem.getId())) {
                                    return BigDecimal.valueOf(basketItem.getQuantity());
                                } else {
                                    return getItemPrice(basketItem);
                                }
                            })
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                } else {
                    return getPriceWithNoPromotion(basketItems);
                }
            }
            case SAME_PRODUCT -> {
                return basketItems.stream()
                        .map(this::calculateSameItemDiscountPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            default -> {
                return getPriceWithNoPromotion(basketItems);
            }
        }
    }

    public static BigDecimal getPriceWithNoPromotion(final List<BasketItemDTO> basketItems) {
        return basketItems.stream()
                .map(PromotionService::getItemPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateSameItemDiscountPrice(final BasketItemDTO item) {
        final Promotion promotion = getActivePromotion();
        final long itemsNeeded = promotion.getProductNumberNeeded();
        final long discountedQuantity = item.getQuantity() / itemsNeeded ;
        final long notDiscountedQuantity = item.getQuantity() - discountedQuantity;

        final BigDecimal discounted = item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(discountedQuantity))
                .multiply(promotion.getPercentCheaper().movePointLeft(2));
        final BigDecimal notDiscounted = item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(notDiscountedQuantity));

        return discounted.add(notDiscounted);
    }

    private static BigDecimal getItemPrice(final BasketItemDTO basketItem) {
        return basketItem.getProduct().getPrice().multiply(BigDecimal.valueOf(basketItem.getQuantity()));
    }
}
