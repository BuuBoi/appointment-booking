package com.dut.doctorcare.service.iface;

import com.dut.doctorcare.dto.request.AddressCreateRequestDto;
import com.dut.doctorcare.dto.request.AddressUpdateOrDeleteDto;
import com.dut.doctorcare.dto.response.AddressResponseDto;
import com.dut.doctorcare.model.Address;

import java.util.List;
import java.util.Map;

public interface AddressService {
    AddressResponseDto createAddress(String userId, AddressCreateRequestDto addressCreateRequestDto);

    AddressResponseDto getAddress(String addressId);

    AddressResponseDto updateAddress(String addressId, AddressUpdateOrDeleteDto addressUpdateOrDeleteDto);

    void deleteAddress(String addressId);

    List<AddressResponseDto> getAllAddressesByUserId();

    Address createOrUpdateAddress(Address address, Map<String, Object> fields);
}
