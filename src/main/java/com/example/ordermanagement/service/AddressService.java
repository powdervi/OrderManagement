package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.AddressCreateReq;
import com.example.ordermanagement.dto.request.AddressUpdateReq;
import com.example.ordermanagement.entity.Address;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    Address createAddress(String id, @Valid AddressCreateReq addressCreateReq);

    Address updateAddress(String addressId, @Valid AddressUpdateReq req);

    void deleteAddress(String addressId);

    List<Address> getAllAdress(String userId);
}
