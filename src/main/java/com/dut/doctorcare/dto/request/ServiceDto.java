package com.dut.doctorcare.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDto {
    private String id;
    @JsonProperty("title")
    private String name;

    @JsonProperty("slug")
    private String slug;

    private String imageUrl;
}
