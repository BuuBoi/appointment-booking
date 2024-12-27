package com.dut.doctorcare.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "specialists")
public class Specialization extends BaseClazz{

    @Column(nullable = false, unique = true, name = "name")
    private String name;

    @Column(name = "slug")
    private String slug;

    @OneToMany(mappedBy = "specialization")
    @JsonIgnore
    private Set<Doctor> doctors;

}
