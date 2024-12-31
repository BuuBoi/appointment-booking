package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.model.Doctor;
import com.dut.doctorcare.model.WeeklyAvailable;
import com.dut.doctorcare.repositories.DoctorRepository;
import com.dut.doctorcare.repositories.WeeklyAvailableRepository;
import com.dut.doctorcare.service.iface.WeeklyAvailableService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class WeeklyAvailableServiceImpl implements WeeklyAvailableService {
    private final WeeklyAvailableRepository weeklyAvailableRepository;
    private final DoctorRepository doctorRepository;
    @Override
    public void createWeeklyAvailable() {
    }

    @Override
    public void updateWeeklyAvailable() {
    }

    @Override
    public void deleteWeeklyAvailable() {
    }

    @Override
    public Map<String, List<String>> getGroupedWeeklyAvailable(UUID doctorId) {
        List<WeeklyAvailable> schedules = weeklyAvailableRepository.findByDoctorId(doctorId);

        return schedules.stream()
                .collect(Collectors.groupingBy(
                        WeeklyAvailable::getDateOfWeek,
                        Collectors.mapping(weeklyAvailable -> weeklyAvailable.getTimeSlot().toString(), Collectors.toList())
                ));
    }

    @Override
    @PreAuthorize("hasRole('DOCTOR')")
    public void updateWeeklySchedule(UUID doctorId, Map<String, List<String>> scheduleMap) {
        log.info("Updating weekly schedule for doctor with id: {}", doctorId);
        log.info("New schedule: {}", scheduleMap);
        // Tìm bác sĩ
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + doctorId));

        // Xóa lịch làm việc cũ của bác sĩ
        weeklyAvailableRepository.deleteByDoctorId(doctorId);

        // Tạo lịch mới và lưu
        List<WeeklyAvailable> newSchedules = scheduleMap.entrySet().stream()
                .flatMap(entry -> entry.getValue().stream().map(timeSlot -> {
                    WeeklyAvailable schedule = new WeeklyAvailable();
                    schedule.setDoctor(doctor);
                    schedule.setDateOfWeek(entry.getKey());
                    schedule.setTimeSlot(timeSlot);
                    return schedule;
                }))
                .collect(Collectors.toList());

        weeklyAvailableRepository.saveAll(newSchedules);
    }
}
