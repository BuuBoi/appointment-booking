package com.dut.doctorcare.mapper;

import com.dut.doctorcare.dto.request.AppointmentDto;
import com.dut.doctorcare.model.Appointment;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

//@Mapper(componentModel = "spring", uses = {CustomeMapper.class})
//public interface AppointmentMapper {
//
//
//    @Mapping(target="id" , source = "id" , ignore = true)
//    @Mapping(target = "doctor", ignore = true)
//    @Mapping(target = "patient", ignore = true)
//    @Mapping(target = "user", ignore = true)
//    public Appointment toEntity(AppointmentDto appointmentDto);
//
//    @Mapping(target="id" , source = "id" , qualifiedByName = "uUIDToString")
//    @Mapping(source="doctor.id", target="doctorId", qualifiedByName = "uUIDToString")
//    @Mapping(source="patient.id", target="patientId", qualifiedByName = "uUIDToString")
//    @Mapping(source="user.id", target="userId", qualifiedByName = "uUIDToString")
//    public AppointmentDto toDto(Appointment appointment);
//}
//Dung abstract class thay vi interface de co the custom thu cong, cai ma interface khong lam duoc
@Mapper(componentModel = "spring", uses = {CustomeMapper.class})
public abstract class AppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    @Mapping(target = "user", ignore = true)
    public abstract Appointment toEntity(AppointmentDto appointmentDto);

    @Mapping(target = "id", source = "id", qualifiedByName = "uUIDToString")
    @Mapping(target = "doctorId", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    public abstract AppointmentDto toDto(Appointment appointment);

    @AfterMapping
    protected void afterToDto(@MappingTarget AppointmentDto dto, Appointment appointment) {
        if (appointment.getDoctor() != null && appointment.getDoctor().getId() != null) {
            dto.setDoctorId(appointment.getDoctor().getId().toString());
        }

        if (appointment.getPatient() != null && appointment.getPatient().getId() != null) {
            dto.setPatientId(appointment.getPatient().getId().toString());
        }

        if (appointment.getUser() != null && appointment.getUser().getId() != null) {
            dto.setUserId(appointment.getUser().getId().toString());
        }
    }
}
