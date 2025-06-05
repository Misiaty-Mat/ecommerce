package com.spring.wsb.ecommerce.entity;

import com.spring.wsb.ecommerce.dto.BasketItemDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Entity
@Builder
@Table(name = "basketItem")
@NoArgsConstructor
@AllArgsConstructor
public class BasketItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    public BasketItemDTO toDto() {
        return BasketItemDTO.builder()
                .id(this.id)
                .quantity(this.quantity)
                .product(Optional.ofNullable(this.product).map(Product::toDto).orElse(null))
                .build();
    }
}
