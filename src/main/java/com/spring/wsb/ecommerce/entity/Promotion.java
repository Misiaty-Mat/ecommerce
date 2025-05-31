package com.spring.wsb.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.spring.wsb.ecommerce.constants.PromotionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "promotion")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PromotionType type;

    @Column(name = "percentCheaper")
    private BigDecimal percentCheaper;

    @Column(name = "productNumberNeeded")
    private Integer productNumberNeeded;

    @Column(name = "activationCode")
    private String activationCode;

//    @JsonIgnore
//    @OneToOne(mappedBy = "promotion")
//    private Basket basket;
}
