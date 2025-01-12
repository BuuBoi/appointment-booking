package com.dut.doctorcare.mapper;

import com.dut.doctorcare.dto.request.DoctorRequest;
import com.dut.doctorcare.dto.response.DoctorResponse;
import com.dut.doctorcare.dto.response.ServiceDoctorResponse;
import com.dut.doctorcare.model.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, CustomeMapper.class, ServiceMapper.class})
public interface DoctorMapper {
    @Mapping(target = "id", ignore = true) // Bỏ qua trường id khi mapping
    @Mapping(target = "gender", source = "gender", qualifiedByName = "stringToGender")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "stringToLocalDate")
    @Mapping(target = "address", source = "addressDto", ignore = true)
    @Mapping(target = "specialization", ignore = true)
    @Mapping(target = "phone", source = "phoneNumber")
    Doctor toDoctor(DoctorRequest doctorRequest);

//    @Mapping(target = "gender", source = "gender", qualifiedByName = "genderToString")
//    @Mapping(target = "dateOfBirth", source = "dateOfBirth", qualifiedByName = "localDateToString")
    @Mapping(target = "specialization", source = "specialization")
    @Mapping(target = "service", source = "service")
    @Mapping(target = "weeklyAvailables", ignore = true)
    @Mapping( target="role" , ignore = true)
    @Mapping(target = "email", ignore = true)
    DoctorResponse toDoctorResponse(Doctor doctor);

    @Mapping(target = "specialization", source = "specialization.name")
    ServiceDoctorResponse toServiceDoctorResponse(Doctor doctor);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "specialization", ignore = true)
    @Mapping(target = "gender", source = "gender", qualifiedByName = "stringToGender")
    @Mapping(target = "phone", source = "phoneNumber")
    void updateDoctorFromDto(DoctorRequest doctorRequest, @MappingTarget Doctor doctor);
}
