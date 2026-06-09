package com.example.ordermanagement.mapper;

import com.example.ordermanagement.dto.request.AddressCreateReq;
import com.example.ordermanagement.dto.request.AddressUpdateReq;
import com.example.ordermanagement.dto.response.AddressRes;
import com.example.ordermanagement.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    Address toEntity(AddressCreateReq req);

    AddressRes toRes(Address address);

    List<AddressRes> toResList(List<Address> addresses);

    void updateAddressFromReq(AddressUpdateReq req, @MappingTarget Address address);
}
