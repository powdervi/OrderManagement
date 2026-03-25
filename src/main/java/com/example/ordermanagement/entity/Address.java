package com.example.ordermanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address extends AbstractEntity implements Serializable {

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "recipient_name", nullable = false)
    private String recipientName;

    @Column(name = "recipient_phone", nullable = false)
    private String recipientPhone;

    @Column(name = "province",  nullable = false)
    private String province;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "ward", nullable = false)
    private String ward;

    @Column(name = "street")
    private String street;

    @Column(name = "detail", nullable = false)
    private String detail;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
