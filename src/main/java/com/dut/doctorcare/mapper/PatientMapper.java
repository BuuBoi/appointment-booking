package com.dut.doctorcare.mapper;

import com.dut.doctorcare.dto.request.PatientRequest;
import com.dut.doctorcare.dto.response.PatientResponse;
import com.dut.doctorcare.model.Patient;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, CustomeMapper.class})
public interface PatientMapper {
    @Mapping(target = "gender", source = "gender", qualifiedByName = "stringToGender")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "address", source = "addressDto", ignore = true)
    Patient toPatient(PatientRequest patientRequest);

    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderToString")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "localDateToString")
    @Mapping(target = "id", source = "id", qualifiedByName = "uUIDToString")
    PatientResponse toPatientResponse(Patient patient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "gender", source = "gender", qualifiedByName = "stringToGender")
    void updatePatientFromDto(PatientRequest patientRequest, @MappingTarget Patient patient);
}
