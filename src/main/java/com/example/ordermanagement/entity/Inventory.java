package com.example.ordermanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class Inventory extends AbstractEntity implements Serializable {

    @Column(name = "product_id")
    private String productId;

    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;
}
