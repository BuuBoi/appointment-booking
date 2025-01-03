package com.dut.doctorcare.controller;

import com.dut.doctorcare.dto.request.ScheduleRequest;
import com.dut.doctorcare.dto.response.ApiResponse;
import com.dut.doctorcare.dto.response.ScheduleResponse;
import com.dut.doctorcare.dto.response.ShiftsResponse;
import com.dut.doctorcare.service.iface.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    @PostMapping
    public ApiResponse<List<ScheduleResponse>> createSchedule(@RequestBody ScheduleRequest scheduleRequest) {
        List<ScheduleResponse> scheduleResponses = scheduleService.createSchedule(scheduleRequest);
        return ApiResponse.<List<ScheduleResponse>>builder()
                .status(200)
                .data(scheduleResponses)
                .build();
    }
    @GetMapping("/shifts-available")
    public ApiResponse<List<ShiftsResponse>> getShiftsAvailable(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<ShiftsResponse> shiftsResponses = scheduleService.getShiftsAvailable(localDate);
        return ApiResponse.<List<ShiftsResponse>>builder()
                .status(200)
                .data(shiftsResponses)
                .build();
    }
    @GetMapping
    public ApiResponse<List<ScheduleResponse>> getScheduleByDate(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<ScheduleResponse> scheduleResponses = scheduleService.getScheduleByDate(localDate);
        return ApiResponse.<List<ScheduleResponse>>builder()
                .status(200)
                .data(scheduleResponses)
                .build();
    }

}
