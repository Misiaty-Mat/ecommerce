package com.spring.wsb.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketItemDTO {

    private Long id;
    private Long quantity;
    private ProductDTO product;
}
