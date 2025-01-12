package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.dto.request.ServiceDto;
import com.dut.doctorcare.dto.response.ServiceDoctorResponse;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.exception.ResourceNotFoundException;
import com.dut.doctorcare.mapper.DoctorMapper;
import com.dut.doctorcare.mapper.ServiceMapper;
import com.dut.doctorcare.model.Doctor;
import com.dut.doctorcare.model.Service;
import com.dut.doctorcare.repositories.ServiceRepository;
import com.dut.doctorcare.service.iface.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Transactional
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository ServiceRepository;
    private final ServiceMapper ServiceMapper;
    private final DoctorMapper doctorMapper;

    @Override
    public ServiceDto createService(ServiceDto request) {
        try{
            Service Service = ServiceMapper.toService(request);
            Service Service1 = ServiceRepository.save(Service);
            return ServiceMapper.toServiceDto(Service1);
        }catch (Exception e){
            log.error("Error creating Service", e);
            throw e;
        }
    }

    @Override
    public List<ServiceDto> getAllServices() {
        List<Service> Services= ServiceRepository.findAll();
        return Services.stream().map(Service -> ServiceMapper.toServiceDto(Service)).collect(Collectors.toList());
    }

    @Override
    public Service getServiceById(UUID id) {
        return ServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id: " + id));
    }

    @Override
    public ServiceDto updateService(UUID id, ServiceDto ServiceDetails) {
        Service Service = getServiceById(id);
        Service.setName(ServiceDetails.getName());
        Service.setSlug(ServiceDetails.getSlug());
        Service.setImageUrl(ServiceDetails.getImageUrl());
        Service spe =  ServiceRepository.save(Service);

        return ServiceMapper.toServiceDto(spe);
    }

    @Override
    public void deleteService(UUID id) {
        Service Service = getServiceById(id);
        ServiceRepository.delete(Service);
    }

    @Override
    public List<ServiceDoctorResponse> getDoctorsByServiceSlug(String slug) {
        Service service = ServiceRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.SlUG_NOT_FOUND));

        return service.getDoctors().stream()
                .filter(Doctor::isActive)
                .map(doctorMapper::toServiceDoctorResponse)
                .collect(Collectors.toList());
    }


}

