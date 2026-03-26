package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.dto.request.AddressCreateReq;
import com.example.ordermanagement.dto.request.AddressUpdateReq;
import com.example.ordermanagement.entity.Address;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.AddressRepo;
import com.example.ordermanagement.repository.UserRepo;
import com.example.ordermanagement.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final ModelMapper modelMapper;
    private final AddressRepo addressRepo;
    private final UserRepo userRepo;

    @Override
    @Transactional
    public Address createAddress(String id, AddressCreateReq addressCreateReq) {

        userRepo.findById(id)
                .orElseThrow(() -> new MHException(MHErrors.USER_NOT_FOUND));

        Address address = modelMapper.map(addressCreateReq, Address.class);
        address.setId(null);
        address.setUserId(id);
        return addressRepo.save(address);
    }

    @Transactional
    @Override
    public Address updateAddress(String addressId, AddressUpdateReq req) {
        Address address = getAddressEntity(addressId);

        if (req.getRecipientName() != null && !req.getRecipientName().isBlank()) {
            address.setRecipientName(req.getRecipientName());
        }

        if (req.getRecipientPhone() != null && !req.getRecipientPhone().isBlank()) {
            address.setRecipientPhone(req.getRecipientPhone());
        }

        if (req.getProvince() != null && !req.getProvince().isBlank()) {
            address.setProvince(req.getProvince());
        }

        if (req.getDistrict() != null && !req.getDistrict().isBlank()) {
            address.setDistrict(req.getDistrict());
        }

        if (req.getWard() != null && !req.getWard().isBlank()) {
            address.setWard(req.getWard());
        }

        if (req.getStreet() != null) {
            address.setStreet(req.getStreet());
        }

        if (req.getDetail() != null && !req.getDetail().isBlank()) {
            address.setDetail(req.getDetail());
        }

        if (req.getPostalCode() != null) {
            address.setPostalCode(req.getPostalCode());
        }

        if (req.getIsDefault() != null) {
            address.setIsDefault(req.getIsDefault());
        }

        return addressRepo.save(address);
    }

    @Transactional
    @Override
    public void deleteAddress(String addressId) {
        Address address = getAddressEntity(addressId);

        address.setDeletedAt(LocalDateTime.now());
        addressRepo.save(address);
    }

    @Override
    public List<Address> getAllAdress(String userId) {
        return addressRepo.findAllByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new MHException(MHErrors.ADDRESS_NOT_FOUND));
    }

    private Address getAddressEntity(String addressId) {
        return addressRepo.findByIdAndDeletedAtIsNull(addressId)
                .orElseThrow(() -> new MHException(MHErrors.ADDRESS_NOT_FOUND));
    }
}
