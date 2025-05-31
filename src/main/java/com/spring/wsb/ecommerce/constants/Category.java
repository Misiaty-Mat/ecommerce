package com.spring.wsb.ecommerce.constants;

import io.micrometer.common.util.StringUtils;

import java.util.Arrays;

public enum Category {
    FURNITURE,
    FOOD,
    CLOTHING;


    public static Category of(final String stringCategory) {
        if (StringUtils.isBlank(stringCategory)) {
            return null;
        }

        return Arrays.stream(values())
                .filter(category -> category.name().equals(stringCategory.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
