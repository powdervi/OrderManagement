package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.dto.request.AddressCreateReq;
import com.example.ordermanagement.dto.request.AddressUpdateReq;
import com.example.ordermanagement.dto.response.AddressRes;
import com.example.ordermanagement.entity.Address;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.AddressRepo;
import com.example.ordermanagement.repository.UserRepo;
import com.example.ordermanagement.service.AddressService;
import com.example.ordermanagement.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import com.example.ordermanagement.mapper.AddressMapper;


@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;
    private final AddressRepo addressRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddressRes createAddress(String userId, AddressCreateReq addressCreateReq) {
        userRepo.findById(userId)
                .orElseThrow(() -> new MHException(MHErrors.USER_NOT_FOUND));

        Address address = addressMapper.toEntity(addressCreateReq);
        address.setUserId(userId);

        Address savedAddress = addressRepo.save(address);

        return addressMapper.toRes(savedAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AddressRes updateAddress(String addressId, AddressUpdateReq req) {
        Address address = getAddressEntity(addressId);
        addressMapper.updateAddressFromReq(req, address);
        Address savedAddress = addressRepo.save(address);
        return  addressMapper.toRes(savedAddress);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAddress(String addressId) {
        Address address = getAddressEntity(addressId);
        address.setDeletedAt(LocalDateTime.now());
        addressRepo.save(address);
    }

    @Override
    public List<AddressRes> getAllAdress(String userId) {
        List<Address> addressList = addressRepo.findAllByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new MHException(MHErrors.ADDRESS_NOT_FOUND));
        return addressMapper.toResList(addressList);
    }

    @Override
    public Address validateAddress(String addressId, String userId) {
        Address address = addressRepo.findById(addressId).orElseThrow(() -> new MHException(MHErrors.ADDRESS_NOT_FOUND));
        if (!address.getUserId().equals(userId)) {
            throw new MHException(MHErrors.ADDRESS_NOT_FOUND);
        }
        return address;
    }


    private Address getAddressEntity(String addressId) {
        Address address = addressRepo.findByIdAndDeletedAtIsNull(addressId)
                .orElseThrow(() -> new MHException(MHErrors.ADDRESS_NOT_FOUND));

        String currentUserId = SecurityUtils.getCurrentUserId();

        if (!address.getUserId().equals(currentUserId)) {
            throw new MHException(MHErrors.ACCESS_DENIED, "Bạn không có quyền thao tác trên địa chỉ của người khác!");
        }

        return address;
    }
}
