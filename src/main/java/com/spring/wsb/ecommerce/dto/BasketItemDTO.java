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

    private Integer id;
    private Integer quantity;
    private ProductDTO product;
}
