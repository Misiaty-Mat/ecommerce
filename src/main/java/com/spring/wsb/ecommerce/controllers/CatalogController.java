package com.spring.wsb.ecommerce.controllers;

import com.spring.wsb.ecommerce.constants.Category;
import com.spring.wsb.ecommerce.dto.BasketItemDTO;
import com.spring.wsb.ecommerce.dto.ProductDTO;
import com.spring.wsb.ecommerce.entity.Promotion;
import com.spring.wsb.ecommerce.services.BasketItemService;
import com.spring.wsb.ecommerce.services.ProductService;
import com.spring.wsb.ecommerce.services.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CatalogController {

    private final ProductService productService;
    private final BasketItemService basketItemService;
    private final PromotionService promotionService;

    @GetMapping({"/", "/{category}"})
    public String listProducts(Model model, @PathVariable(required = false) String category) {

        List<ProductDTO> products = getProducts(category);
        List<BasketItemDTO> basketItems = basketItemService.listBasketItems();

        String promotionName = Optional.ofNullable(promotionService.getActivePromotion()).map(Promotion::getName).orElse(null);
        final BigDecimal totalPrice = promotionService.getPriceWithPromotion(basketItems).setScale(2, RoundingMode.HALF_UP);
        BigDecimal basePrice = PromotionService.getPriceWithNoPromotion(basketItems);
        model.addAttribute("products", products);
        model.addAttribute("basketItems", basketItems);
        model.addAttribute("promotionName", promotionName);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("basePrice", basePrice);
        return "products";
    }

    private List<ProductDTO> getProducts(String category) {
        List<ProductDTO> products;
        Category productCategory = Category.of(category);

        if (productCategory == null) {
            products = productService.listAllProducts();
        } else {
            products = productService.listProductByCategory(productCategory);
        }
        return products;
    }

    @PostMapping("add-to-basket/{productId}")
    public String addToBasket(@PathVariable Integer productId, @RequestParam(required = false) String redirectUrl) {
        basketItemService.addToBasket(productId);
        return "redirect:" + (redirectUrl != null ? redirectUrl : "/");
    }

    @PostMapping("/basket/delete/{id}")
    public String deleteItem(@PathVariable Integer id, @RequestParam(required = false) String redirectUrl) {
        basketItemService.removeFromBasket(id);
        return "redirect:" + (redirectUrl != null ? redirectUrl : "/");
    }

    @PostMapping("/basket/promotion")
    public String checkPromotionCode(@RequestParam String code, @RequestParam(required = false) String redirectUrl) {
        promotionService.checkPromotionCode(code);
        return "redirect:" + (redirectUrl != null ? redirectUrl : "/");
    }
}
