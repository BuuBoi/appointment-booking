package com.dut.doctorcare.service.impl;
import com.dut.doctorcare.dao.iface.DoctorDao;
import com.dut.doctorcare.dao.iface.PatientDao;
import com.dut.doctorcare.dao.iface.UserDao;
import com.dut.doctorcare.dto.request.UserRegistrationDto;
import com.dut.doctorcare.dto.response.UserResponseDto;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.mapper.UserMapper;
import com.dut.doctorcare.model.Doctor;
import com.dut.doctorcare.model.Patient;
import com.dut.doctorcare.model.Role;
import com.dut.doctorcare.model.User;
import com.dut.doctorcare.service.iface.RegistrationService;
import com.dut.doctorcare.service.iface.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {
    private final UserDao userDao;
    private final RoleService roleService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PatientDao patientDao;
    private final DoctorDao doctorDao;

    @Override
    @Transactional
    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        // Check if email already exists
        if (userDao.findByEmail(registrationDto.getEmail()).isPresent()) {
            throw new AppException(ErrorCode.Email_ALREADY_EXISTS);
        }


        // Create and save user
        User user = userMapper.toUser(registrationDto);
        User savedUser = userDao.save(user);
        return userMapper.toUserResponseDto(savedUser);
    }
}
