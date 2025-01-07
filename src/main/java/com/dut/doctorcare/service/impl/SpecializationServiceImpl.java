package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.dao.iface.SpecializationDao;
import com.dut.doctorcare.dto.request.SpecializationDto;
import com.dut.doctorcare.dto.response.ServiceDoctorResponse;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.exception.ResourceNotFoundException;
import com.dut.doctorcare.mapper.DoctorMapper;
import com.dut.doctorcare.mapper.SpecializationMapper;
import com.dut.doctorcare.model.Specialization;
import com.dut.doctorcare.repositories.SpecializationRepository;
import com.dut.doctorcare.service.iface.SpecializationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository specializationRepository;
    private final SpecializationMapper specializationMapper;
    private final DoctorMapper doctorMapper;


//    public SpecializationServiceImpl(SpecializationRepository specializationRepository) {
//        this.specializationRepository = specializationRepository;
//
//    }

    @Override
    public SpecializationDto createSpecialization(SpecializationDto request) {
        try{
            log.info("Creating specialization: {}", request);
            Specialization specialization = specializationMapper.toSpecialization(request);
            System.out.println(specialization.getSlug());
            log.info("Mapped to entity: {}", specialization);
            Specialization specialization1 = specializationRepository.save(specialization);
            log.info("Saved entity: {}", specialization1);
            return specializationMapper.toSpecializationDto(specialization1);
        }catch (Exception e){
            log.error("Error creating specialization", e);
            throw e;
        }
    }

    @Override
    public List<SpecializationDto> getAllSpecializations() {
        List<Specialization> specializations= specializationRepository.findAll();
        return specializations.stream().map(specialization -> specializationMapper.toSpecializationDto(specialization)).collect(Collectors.toList());
    }

    @Override
    public Specialization getSpecializationById(UUID id) {
        return specializationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialization not found with id: " + id));
    }

    @Override
    public SpecializationDto updateSpecialization(UUID id, SpecializationDto specializationDetails) {
        Specialization specialization = getSpecializationById(id);
        specialization.setName(specializationDetails.getName());
        specialization.setSlug(specializationDetails.getSlug());
        Specialization spe =  specializationRepository.save(specialization);

        return specializationMapper.toSpecializationDto(spe);
    }

    @Override
    public void deleteSpecialization(UUID id) {
        Specialization specialization = getSpecializationById(id);
        specializationRepository.delete(specialization);
    }

    @Override
    public List<ServiceDoctorResponse> getDoctorsBySpecialSlug(String slug) {
        Specialization special = specializationRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.SlUG_NOT_FOUND));

        return special.getDoctors().stream()
                .map(doctorMapper::toServiceDoctorResponse)
                .collect(Collectors.toList());
    }
}

