package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.dao.iface.AddressDao;
import com.dut.doctorcare.dao.iface.UserDao;
import com.dut.doctorcare.dao.impl.HibernateUserDao;
import com.dut.doctorcare.dto.request.AddressCreateRequestDto;
import com.dut.doctorcare.dto.request.AddressUpdateOrDeleteDto;
import com.dut.doctorcare.dto.response.AddressResponseDto;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.model.Address;
import com.dut.doctorcare.model.User;
import com.dut.doctorcare.repositories.AddressRepository;
import com.dut.doctorcare.service.iface.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressDao addressDao;
    private final UserDao userDao;
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressDao addressDao, HibernateUserDao userDao, AddressRepository addressRepository) {
        this.addressDao = addressDao;
        this.userDao = userDao;
        this.addressRepository = addressRepository;
    }

    @Transactional
    @Override
    public AddressResponseDto createAddress(String userId, AddressCreateRequestDto addressCreateRequestDto) {
        User user = userDao.findById(UUID.fromString(userId)).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Address address = new Address();
        address.setProvince(addressCreateRequestDto.getProvince());
        address.setDistrict(addressCreateRequestDto.getDistrict());
        address.setWard(addressCreateRequestDto.getWard());
        address.setDetails(addressCreateRequestDto.getDetails());
        //address.setUser(user);
        address = addressDao.save(address);
        AddressResponseDto addressResponseDto = new AddressResponseDto(address);
        return addressResponseDto;
    }

    @Override
    public AddressResponseDto getAddress(String addressId) {
        return null;
    }

    @Transactional
    @Override
    public AddressResponseDto updateAddress(String addressId, AddressUpdateOrDeleteDto addressUpdateOrDeleteDto) {
        if(!addressId.equals(addressUpdateOrDeleteDto.getId())) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
        Address address = addressDao.findById(UUID.fromString(addressId)).orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        address.setProvince(addressUpdateOrDeleteDto.getProvince());
        address.setDistrict(addressUpdateOrDeleteDto.getDistrict());
        address.setWard(addressUpdateOrDeleteDto.getWard());
        address.setDetails(addressUpdateOrDeleteDto.getDetails());
        address = addressDao.update(address);
        AddressResponseDto addressResponseDto = new AddressResponseDto(address);
        return addressResponseDto;
    }

    @Override
    public void deleteAddress(String addressId) {
    }

    @Override
    public List<AddressResponseDto> getAllAddressesByUserId() {
        return null;
    }

    @Override
    public Address createOrUpdateAddress(Address address, Map<String, Object> fields) {
        if(address == null) {
            address = new Address();
        }
        //Address: city, district, ward, street
        if(fields.containsKey("city")) {
            address.setProvince((String) fields.get("city"));
        }
        if(fields.containsKey("district")) {
            address.setDistrict((String) fields.get("district"));
        }
        if(fields.containsKey("ward")) {
            address.setWard((String) fields.get("ward"));
        }
        if(fields.containsKey("street")) {
            address.setDetails((String) fields.get("street"));
        }
        return addressRepository.save(address);
    }
}
