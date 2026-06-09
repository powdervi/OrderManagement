package com.example.ordermanagement.service;

import com.example.ordermanagement.entity.Carrier;

public interface CarrierService {
    Carrier validateAndGetCarrier(String carrierId);
    Carrier getRandomCarrier();
}
