package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.AddressCreateReq;
import com.example.ordermanagement.dto.request.AddressUpdateReq;
import com.example.ordermanagement.dto.response.AddressRes;
import com.example.ordermanagement.dto.response.UserRes;
import com.example.ordermanagement.entity.Address;
import com.example.ordermanagement.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AddressController {
    private final AddressService addressService;
    private final ModelMapper modelMapper;

    @PostMapping("/users/{userId}/addresses")
    public ResponseEntity<BaseResponse<AddressRes>> createAddress(
            @PathVariable String userId,
            @RequestBody @Valid AddressCreateReq addressCreateReq) {
        Address address = addressService.createAddress(userId, addressCreateReq);
        AddressRes addressRes = modelMapper.map(address, AddressRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(addressRes));
    }

    @PatchMapping("/addresses/{addressId}")
    public ResponseEntity<BaseResponse<AddressRes>> updateAddress(
            @PathVariable String addressId,
            @RequestBody @Valid AddressUpdateReq req
    ) {
        Address address = addressService.updateAddress(addressId, req);
        AddressRes res = modelMapper.map(address, AddressRes.class);
        return ResponseEntity.ok(BaseResponse.ofSuccess(res));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<BaseResponse<Object>> deleteUser(@PathVariable String addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok(BaseResponse.ofSuccess("delete is success"));
    }

    @GetMapping("/addresses/{userId}")
    public ResponseEntity<BaseResponse<List<AddressRes>>> getAllAdress(@PathVariable String userId){
        List<Address> addressList = addressService.getAllAdress(userId);
        List<AddressRes> addressResList = modelMapper.map(addressList, new TypeToken<List<AddressRes>>(){}.getType());
        return ResponseEntity.ok(BaseResponse.ofSuccess(addressResList));
    }
}
