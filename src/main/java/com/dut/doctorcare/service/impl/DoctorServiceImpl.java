package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.dao.iface.AddressDao;
import com.dut.doctorcare.dao.iface.DoctorDao;
import com.dut.doctorcare.dao.iface.SpecializationDao;
import com.dut.doctorcare.dao.iface.UserDao;
import com.dut.doctorcare.dto.request.DoctorCreateDTO;
import com.dut.doctorcare.dto.request.DoctorRequest;
import com.dut.doctorcare.dto.response.DoctorResponse;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.mapper.AddressMapper;
import com.dut.doctorcare.mapper.DoctorMapper;
import com.dut.doctorcare.mapper.SpecializationMapper;
import com.dut.doctorcare.model.Address;
import com.dut.doctorcare.model.Doctor;
import com.dut.doctorcare.model.Specialization;
import com.dut.doctorcare.model.User;
import com.dut.doctorcare.repositories.DoctorRepository;
import com.dut.doctorcare.repositories.UserRepository;
import com.dut.doctorcare.service.iface.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {
    private final DoctorDao doctorDao;
    private final DoctorMapper doctorMapper;
    private final AddressMapper addressMapper;
    private final SpecializationMapper specializationMapper;
    private final AddressDao addressDao;
    private final UserDao userDao;
    private final SpecializationDao specializationDao;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    @Override
    public DoctorResponse saveOrUpdate(DoctorRequest request) {
        var context = SecurityContextHolder.getContext();
        var email = context.getAuthentication().getName();
        User user = userDao.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Doctor doctor = doctorDao.findById(user.getId()).orElse(null);
        if(doctor == null) {
            Address address = addressDao.save(addressMapper.toAddress(request.getAddressDto()));
           // Specialization spec = specializationDao.findById(UUID.fromString(request.getSpecializationDto().getId())).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            doctor = doctorMapper.toDoctor(request);
            doctor.setAddress(address);
            doctor.setUser(user);
           // doctor.setSpecialization(spec);
            return doctorMapper.toDoctorResponse(doctorDao.save(doctor));
        } else {
            doctorMapper.updateDoctorFromDto(request, doctor);
            Address address = doctor.getAddress();
            if(address.getProvince().equals(request.getAddressDto().getProvince()) ||
                    address.getDistrict().equals(request.getAddressDto().getDistrict()) ||
                    address.getWard().equals(request.getAddressDto().getWard()) ||
                    address.getDetails().equals(request.getAddressDto().getDetails())) {
                addressMapper.updateAddressFromDto(request.getAddressDto(), address);
                address = addressDao.update(address);
                doctor.setAddress(address);
            }
//           // Specialization spec = specializationDao.findById(UUID.fromString(request.getSpecializationDto().getId())).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//            if (!doctor.getSpecialization().getId().equals(spec.getId())) {
//                doctor.setSpecialization(spec);
//            }
            return doctorMapper.toDoctorResponse(doctorDao.update(doctor));
        }
    }
    @Override
    public DoctorResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        var email = context.getAuthentication().getName();
        User user = userDao.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Doctor doctor = doctorDao.findById(user.getId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return doctorMapper.toDoctorResponse(doctor);
    }


    @Override
    public Doctor createDoctor(DoctorCreateDTO doctorCreateDTO) {
        // Lấy thông tin User từ database
        Optional<User> userOptional = userRepository.findById(UUID.fromString(doctorCreateDTO.getId()));
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User không tồn tại với ID: " + doctorCreateDTO.getId());
        }

        User user = userOptional.get();

        // Tạo đối tượng Doctor
        Doctor doctor = Doctor.builder()
                .user(user)
                .fullName(doctorCreateDTO.getFullName())
                .bio(doctorCreateDTO.getBio())
                .dateOfBirth(doctorCreateDTO.getDob())
                .gender(doctorCreateDTO.getGender())
                .medicalLicense(doctorCreateDTO.getMedicalLicense())
                .medicalLicenseExpiry(doctorCreateDTO.getMedicalLicenseExpiry())
                .page(doctorCreateDTO.getPage())
                .profilePicture(doctorCreateDTO.getProfilePicture())
                .yearsOfExperience(doctorCreateDTO.getYearsOfExperience())
                .truckingNumber(doctorCreateDTO.getTruckingNumber())
                .build();

        // Lưu Doctor vào database
        return doctorRepository.save(doctor);
    }
}
