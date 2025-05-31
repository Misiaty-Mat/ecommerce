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

    public void checkPromotionCode(String code) {
        promotionRepository.findByActivationCodeIgnoreCase(code).ifPresent(promotion -> {
            Basket basket = basketRepository.findTopBy();
            basket.setPromotion(promotion);
            basketRepository.save(basket);
        });
    }

    public Promotion getActivePromotion() {
        return basketRepository.findTopBy().getPromotion();
    }

    public BigDecimal getPriceWithPromotion(final List<BasketItemDTO> basketItems) {
        Promotion promotion = getActivePromotion();

        if (promotion == null) {
            return getPriceWithNoPromotion(basketItems);
        }

        switch (promotion.getType()) {
            case ALL_CHEAPER -> {
                final BigDecimal basePrice = basketItems.stream()
                        .map(PromotionService::getItemPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal discountPercent = BigDecimal.ONE.subtract(promotion.getPercentCheaper().movePointLeft(2));
                return basePrice.multiply(discountPercent);
            }
            case CHEAPEST_FOR_1_ZL -> {
                int numOfProductsFor1Zl = basketItems.size() / promotion.getProductNumberNeeded();
                if (numOfProductsFor1Zl != 0) {

                    List<Integer> productsFor1Zl = basketItems.stream()
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

    public static BigDecimal getPriceWithNoPromotion(List<BasketItemDTO> basketItems) {
        return basketItems.stream()
                .map(PromotionService::getItemPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateSameItemDiscountPrice(BasketItemDTO item) {
        Promotion promotion = getActivePromotion();
        int itemsNeeded = promotion.getProductNumberNeeded();
        int discountedQuantity = item.getQuantity() / itemsNeeded ;
        int notDiscountedQuantity = item.getQuantity() - discountedQuantity;

        BigDecimal discounted = item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(discountedQuantity))
                .multiply(promotion.getPercentCheaper().movePointLeft(2));
        BigDecimal notDiscounted = item.getProduct().getPrice()
                .multiply(BigDecimal.valueOf(notDiscountedQuantity));

        return discounted.add(notDiscounted);
    }

    private static BigDecimal getItemPrice(BasketItemDTO basketItem) {
        return basketItem.getProduct().getPrice().multiply(BigDecimal.valueOf(basketItem.getQuantity()));
    }
}
