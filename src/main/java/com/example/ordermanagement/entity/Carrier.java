package com.example.ordermanagement.entity;

import com.example.ordermanagement.common.CarrierStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "carriers")
public class Carrier extends AbstractEntity implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "shipping_fee")
    private BigDecimal shippingFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CarrierStatus status;
}
