package com.example.ordermanagement.entity;

import com.example.ordermanagement.common.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends AbstractEntity implements Serializable {

    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "base_price")
    private BigDecimal basePrice;

    @Column(name = "weight")
    private BigDecimal weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;
}
