package com.example.ordermanagement.repository;

import com.example.ordermanagement.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address, String> {
    Optional<Address> findByIdAndDeletedAtIsNull(String addressId);

    Optional<List<Address>> findAllByUserIdAndDeletedAtIsNull(String userId);
}
