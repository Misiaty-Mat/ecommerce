package com.spring.wsb.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.wsb.ecommerce.constants.Category;
import com.spring.wsb.ecommerce.dto.ProductDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "quantityOnHand", nullable = false)
    private Integer quantityOnHand;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @JsonIgnore
    @OneToOne(mappedBy = "product")
    private BasketItem basketItem;

    public ProductDTO toDto() {
        return ProductDTO.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .category(this.category.name().toLowerCase())
                .quantityOnHand(this.quantityOnHand)
                .price(this.price)
                .available(this.available)
                .build();
    }
}
