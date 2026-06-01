package com.example.ordermanagement.repository;

import com.example.ordermanagement.common.CarrierStatus;
import com.example.ordermanagement.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarrierRepo extends JpaRepository<Carrier, String> {
    List<Carrier> findAllByStatus(CarrierStatus carrierStatus);
}
