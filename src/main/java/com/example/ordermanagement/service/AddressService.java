package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.AddressCreateReq;
import com.example.ordermanagement.dto.request.AddressUpdateReq;
import com.example.ordermanagement.dto.response.AddressRes;
import com.example.ordermanagement.entity.Address;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressRes createAddress(String id, @Valid AddressCreateReq addressCreateReq);

    AddressRes updateAddress(String addressId, @Valid AddressUpdateReq req);

    void deleteAddress(String addressId);

    List<AddressRes> getAllAdress(String userId);

    Address validateAddress(String addressId, String userId);
}
