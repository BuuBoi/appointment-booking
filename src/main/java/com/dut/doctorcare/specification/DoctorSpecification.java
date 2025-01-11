package com.dut.doctorcare.specification;

import com.dut.doctorcare.model.Address;
import com.dut.doctorcare.model.Doctor;
import com.dut.doctorcare.model.Service;
import com.dut.doctorcare.model.Specialization;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class DoctorSpecification {

    public static Specification<Doctor> filterDoctors(String name, String address, String specialization, String service) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by name
            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("fullName"), "%" + name + "%"));
            }

            // Join with Address and filter by address
            if (address != null && !address.isEmpty()) {
                Join<Doctor, Address> addressJoin = root.join("address");
                predicates.add(criteriaBuilder.like(addressJoin.get("province"), "%" + address + "%"));
            }

            // Join with Specialization and filter by specialization
            if (specialization != null && !specialization.isEmpty()) {
                Join<Doctor, Specialization> specializationJoin = root.join("specialization");
                predicates.add(criteriaBuilder.like(specializationJoin.get("slug"),specialization));
            }

            // Join with Service and filter by service
            if (service != null && !service.isEmpty()) {
                Join<Doctor, Service> serviceJoin = root.join("service");
                predicates.add(criteriaBuilder.like(serviceJoin.get("slug"), service));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}