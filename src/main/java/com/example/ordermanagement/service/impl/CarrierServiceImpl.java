package com.example.ordermanagement.service.impl;


import com.example.ordermanagement.common.CarrierStatus;
import com.example.ordermanagement.entity.Carrier;
import com.example.ordermanagement.exception.MHErrors;
import com.example.ordermanagement.exception.MHException;
import com.example.ordermanagement.repository.CarrierRepo;
import com.example.ordermanagement.service.CarrierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CarrierServiceImpl implements CarrierService {

    private final CarrierRepo carrierRepo;

    @Override
    public Carrier validateAndGetCarrier(String carrierId) {
        Carrier carrier = carrierRepo.findById(carrierId)
                .orElseThrow(() -> new MHException(MHErrors.CARRIER_NOT_FOUND));
        if (carrier.getStatus() != CarrierStatus.ACTIVE) throw new MHException(MHErrors.CARRIER_NOT_FOUND);
        return carrier;
    }

    @Override
    public Carrier getRandomCarrier() {
        List<Carrier> carriers = carrierRepo.findAllByStatus(CarrierStatus.ACTIVE);
        if (carriers.isEmpty()) throw new MHException(MHErrors.CARRIER_NOT_FOUND);
        return carriers.get(ThreadLocalRandom.current().nextInt(carriers.size()));
    }
}
