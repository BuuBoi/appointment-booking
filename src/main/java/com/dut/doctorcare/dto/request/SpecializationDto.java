package com.dut.doctorcare.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecializationDto {
    private String id;
    @JsonProperty("title")
    private String name;

    @JsonProperty("slug")
    private String slug;
}
