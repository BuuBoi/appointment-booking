package com.dut.doctorcare.service.iface;

import com.dut.doctorcare.dto.request.CreateDoctorRequest;
import com.dut.doctorcare.dto.request.PasswordChangeDto;
import com.dut.doctorcare.dto.request.UserRegistrationDto;
import com.dut.doctorcare.dto.request.UserUpdateOrDeleteDto;
import com.dut.doctorcare.dto.response.DoctorResponse;
import com.dut.doctorcare.dto.response.UserResponseDto;
import com.dut.doctorcare.exception.EntityOperationException;

import java.util.List;

public interface UserService {
    DoctorResponse registerDoctor(CreateDoctorRequest request);
    UserResponseDto registerPatient(UserRegistrationDto userRegistrationDto);
//    void deleteUser(String username);
//    void updateUser(String username, String password);
//    void getUser(String username);
//    void getAllUsers();

    List<UserResponseDto> getAllUsers();
    UserResponseDto getUser(String id);
    void deleteUser(String id) throws EntityOperationException;
    UserResponseDto updateUser(String id, UserUpdateOrDeleteDto userUpdateOrDeleteDto);
    void changePassword(String userId, PasswordChangeDto passwordChangeDto);
    Object getMyProfile();
}
