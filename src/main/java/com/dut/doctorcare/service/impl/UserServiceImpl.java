package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.dao.iface.DoctorDao;
import com.dut.doctorcare.dao.iface.SpecializationDao;
import com.dut.doctorcare.dao.iface.UserDao;
import com.dut.doctorcare.dto.request.CreateDoctorRequest;
import com.dut.doctorcare.dto.request.PasswordChangeDto;
import com.dut.doctorcare.dto.request.UserRegistrationDto;
import com.dut.doctorcare.dto.request.UserUpdateOrDeleteDto;
import com.dut.doctorcare.dto.response.DoctorResponse;
import com.dut.doctorcare.dto.response.UserResponseDto;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.EntityOperationException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.mapper.AddressMapper;
import com.dut.doctorcare.mapper.DoctorMapper;
import com.dut.doctorcare.mapper.UserMapper;
import com.dut.doctorcare.model.Address;
import com.dut.doctorcare.model.Doctor;
import com.dut.doctorcare.model.Role;
import com.dut.doctorcare.model.User;
import com.dut.doctorcare.service.iface.RoleService;
import com.dut.doctorcare.service.iface.UserService;
import com.dut.doctorcare.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final DoctorDao doctorDao;
    private final RoleService roleService;
    private final AddressMapper addressMapper;
    private final UserMapper userMapper;
    private final DoctorMapper doctorMapper;
    private final SpecializationDao specializationDao;

    @Override
    public DoctorResponse registerDoctor(CreateDoctorRequest request) {
//        if (userDao.findByEmail(request.getUserRegistrationDto().getEmail()).isPresent()) {
//            throw new AppException(ErrorCode.Email_ALREADY_EXISTS);
//        }
//        // Create user
//         User user = new User();
//        Role role = roleService.findRole(Role.RoleName.DOCTOR);
//        if (role == null) {
//            role = roleService.createRole(Role.RoleName.DOCTOR);
//        }
//        user = userMapper.toUser(request.getUserRegistrationDto());
//        user.setRole(role);
//        user = userDao.save(user);
//
//        // Create doctor
//        Doctor doctor = new Doctor();
//        doctor = doctorMapper.toDoctor(request);
//        doctor.setUser(user);
//        doctor.setAddress(addressMapper.toAddress(request.getAddressDto()));
//       // doctor.setSpecialization(specializationDao.findById(UUID.fromString(request.getSpecializationDto().getId()))
//         //       .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
//        doctor = doctorDao.save(doctor);
//
//        return doctorMapper.toDoctorResponse(doctor);
        return null;
    }
    @Override
    public UserResponseDto registerPatient(UserRegistrationDto userRegistrationDto) {
//        if (userDao.findByEmail(userRegistrationDto.getEmail()).isPresent()) {
//            throw new AppException(ErrorCode.Email_ALREADY_EXISTS);
//        }
//        Role role = roleService.findRole(Role.RoleName.USER);
//        if (role == null) {
//            role = roleService.createRole(Role.RoleName.USER);
//        }
//        User user = new User();
//        user = userMapper.toUser(userRegistrationDto);
//        user.setRole(role); // Thiết lập vai trò mặc định
//        user = userDao.save(user);
//        return userMapper.toUserResponseDto(user);
        return null;

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDto> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication(); //lay toan bo thong tin cua user dang dang nhap
        log.info("email: {}", authentication.getName());
        log.info("role: {}", authentication.getAuthorities().stream().findFirst().map(GrantedAuthority::getAuthority).get()); //lay ra role cua user dang dang nhap
        return userDao.findAll(Collections.emptyMap()).stream().map(UserUtils::convertToDTO).collect(Collectors.toList());
    }

    //dam bao ren user chi lay duoc thong tin cua chinh minh hoac la admin
    @PostAuthorize("returnObject.email == authentication.getName() || hasRole('ADMIN')")
    @Override
    public UserResponseDto getUser(String id) {
        User user = userDao.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return UserUtils.convertToDTO(user);
    }

    //lay thong tin user dang dang nhap ma khong can truyen id
    //sau khi dang nhap thanh cong thi info dc luu tru trong security context
//    @Transactional
//    @Override
//    public UserResponseDto getMyProfile() {
//        log.info("Get my profile");
//        var context = SecurityContextHolder.getContext();
//        var email = context.getAuthentication().getName();
//        User user = userDao.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//        return UserUtils.convertToDTO(user);
//    }

    @Transactional
    @Override
    public UserResponseDto getMyProfile() {
        log.info("Get my profile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra và log authentication
        log.info("Authentication: {}", authentication);
        log.info("Authentication principal: {}", authentication.getPrincipal());

        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String email = authentication.getName();
        log.info("Email from security context: {}", email);

        User user = userDao.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", email);
                    return new AppException(ErrorCode.USER_NOT_FOUND);
                });

        return UserUtils.convertToDTO(user);
    }



    @Override
    @Transactional
    public UserResponseDto updateUser(String id, UserUpdateOrDeleteDto userUpdateOrDeleteDto) {
        return userDao.findById(UUID.fromString(id)).map(user -> {
            User savedUser = userDao.save(user);
            return UserUtils.convertToDTO(savedUser);

        }).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
    @Transactional
    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String id) throws EntityOperationException {
        User user = userDao.findById(UUID.fromString(id)).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        user.setActive(false);
        userDao.update(user);
    }



    @Transactional
    public void changePassword(String userId, PasswordChangeDto passwordChangeDto) {
        User user = userDao.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Verify current password
        if (!user.getPasswordHash().equals(passwordChangeDto.getCurrentPassword())) {
            throw new AppException(ErrorCode.INVALID_CURRENT_PASSWORD);
        }

        // Check if new password and confirm password match
        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        // Encode and set new password
        user.setPasswordHash(passwordChangeDto.getNewPassword());
        userDao.save(user);
    }
}
