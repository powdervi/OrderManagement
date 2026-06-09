package com.example.ordermanagement.controller;

import com.example.ordermanagement.common.BaseResponse;
import com.example.ordermanagement.dto.request.AddressCreateReq;
import com.example.ordermanagement.dto.request.AddressUpdateReq;
import com.example.ordermanagement.dto.response.AddressRes;
import com.example.ordermanagement.service.AddressService;
import com.example.ordermanagement.utils.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AddressController {
    private final AddressService addressService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/addresses/my-addresses")
    public ResponseEntity<BaseResponse<AddressRes>> createAddress(
            @RequestBody @Valid AddressCreateReq addressCreateReq) {
        String currentUserId = SecurityUtils.getCurrentUserId();
        AddressRes addressRes = addressService.createAddress(currentUserId, addressCreateReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ofSuccess(addressRes));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<BaseResponse<AddressRes>> updateAddress(
            @PathVariable String addressId,
            @RequestBody @Valid AddressUpdateReq req
    ) {
        AddressRes address = addressService.updateAddress(addressId, req);
        return ResponseEntity.ok(BaseResponse.ofSuccess(address));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<BaseResponse<Object>> deleteAddress(@PathVariable String addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/addresses/my-addresses")
    public ResponseEntity<BaseResponse<List<AddressRes>>> getMyAddresses(){
        String currentUserId = SecurityUtils.getCurrentUserId();
        List<AddressRes> addressList = addressService.getAllAdress(currentUserId);
        return ResponseEntity.ok(BaseResponse.ofSuccess(addressList));
    }
}
