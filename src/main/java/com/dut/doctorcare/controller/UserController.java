package com.dut.doctorcare.controller;

import com.dut.doctorcare.dto.request.CreateDoctorRequest;
import com.dut.doctorcare.dto.request.PasswordChangeDto;
import com.dut.doctorcare.dto.request.UserRegistrationDto;
import com.dut.doctorcare.dto.request.UserUpdateOrDeleteDto;
import com.dut.doctorcare.dto.response.ApiResponse;
import com.dut.doctorcare.dto.response.DoctorResponse;
import com.dut.doctorcare.dto.response.UserResponseDto;
import com.dut.doctorcare.exception.EntityOperationException;
import com.dut.doctorcare.service.iface.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(@Autowired UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @RequestMapping("/register")
    public ApiResponse<UserResponseDto> registerPatient(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        ApiResponse<UserResponseDto> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(userService.registerPatient(userRegistrationDto));
        return response;
    }
    @PostMapping
    @RequestMapping("/register/doctor")
    public ApiResponse<DoctorResponse> registerDoctor(@RequestBody @Valid CreateDoctorRequest request) {
        ApiResponse<DoctorResponse> response = new ApiResponse<>();
        response.setStatus(HttpStatus.OK.value());
        response.setData(userService.registerDoctor(request));
        return response;
    }


    @GetMapping
    public ApiResponse<List<UserResponseDto>> getAllUsers() {
        ApiResponse<List<UserResponseDto>> response = new ApiResponse<>();
        response.setStatus(200);
        response.setData(userService.getAllUsers());
        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponseDto> getUserById(@PathVariable String id) {
        ApiResponse<UserResponseDto> response = new ApiResponse<>();
        response.setStatus(200);
        response.setData(userService.getUser(id));
        return response;
    }
    @GetMapping("/my-profile")
    public ApiResponse<Object> getMyInfo() {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setStatus(200);
        response.setData(userService.getMyProfile());
        return response;
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponseDto> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateOrDeleteDto userUpdateOrDeleteDto) {
        ApiResponse<UserResponseDto> response = new ApiResponse<>();
        response.setStatus(200);
        response.setData(userService.updateUser(id, userUpdateOrDeleteDto));
        return response;
    }

    @PutMapping("/{id}/change-password")
    public ApiResponse<String> changePassword(@PathVariable String id, @RequestBody @Valid PasswordChangeDto passwordChangeDto) {
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(200);
        userService.changePassword(id, passwordChangeDto);
        response.setMessage("Change password successfully");
        return response;
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable String id) throws EntityOperationException {
        ApiResponse<String> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMessage("Delete user successfully");
        userService.deleteUser(id);
        return response;
    }

}
