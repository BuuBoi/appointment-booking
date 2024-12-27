package com.dut.doctorcare.mapper;

import com.dut.doctorcare.dto.request.ServiceDto;
import com.dut.doctorcare.dto.request.SpecializationDto;
import com.dut.doctorcare.model.Service;
import com.dut.doctorcare.model.Specialization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {CustomeMapper.class})
public interface ServiceMapper {

    @Mapping(target = "id", source = "id", qualifiedByName = "uUIDToString")
    ServiceDto toServiceDto(Service service);

    @Mapping(target = "id", source = "id", ignore = true)
    @Mapping(target = "slug", source = "slug")
    @Mapping(target = "imageUrl", source = "imageUrl")
    Service toService(ServiceDto serviceDto);
}
