package com.dut.doctorcare.utils;

import com.dut.doctorcare.dto.response.UserResponseDto;
import com.dut.doctorcare.model.User;

public class UserUtils {
    public static UserResponseDto convertToDTO(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId().toString());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }




}
