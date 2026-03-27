package com.example.ordermanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart extends AbstractEntity implements Serializable {

    @Column(name = "user_id", nullable = false)
    private String userId;
}
