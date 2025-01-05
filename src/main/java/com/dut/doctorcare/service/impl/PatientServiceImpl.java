package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.dao.iface.AddressDao;
import com.dut.doctorcare.dao.iface.PatientDao;
import com.dut.doctorcare.dao.iface.UserDao;
import com.dut.doctorcare.dto.request.PatientRequest;
import com.dut.doctorcare.dto.response.PatientResponse;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.EntityOperationException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.mapper.AddressMapper;
import com.dut.doctorcare.mapper.PatientMapper;
import com.dut.doctorcare.model.Address;
import com.dut.doctorcare.model.Patient;
import com.dut.doctorcare.model.User;
import com.dut.doctorcare.service.iface.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
        private final PatientDao patientDao;
        private final PatientMapper patientMapper;
        private final AddressMapper addressMapper;
        private  final AddressDao addressDao;
        private final UserDao userDao;
        @Override
        public PatientResponse saveOrUpdate(PatientRequest patientRequest) {
//            var context = SecurityContextHolder.getContext();
//            var email = context.getAuthentication().getName();
//            User user = userDao.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//            Patient patient = patientDao.findById(user.getId()).orElse(null);
//            if(patient == null) {
//                patient = patientMapper.toPatient(patientRequest);
//                Address address = addressMapper.toAddress(patientRequest.getAddressDto());
//                address = addressDao.save(address);
//                patient.setAddress(address);
//                patient.setUser(user); //auto id user
//               return patientMapper.toPatientResponse(patientDao.save(patient));
//            }else {
//                patientMapper.updatePatientFromDto(patientRequest, patient);
//                Address address = patient.getAddress();
//                if(address.getProvince().equals(patientRequest.getAddressDto().getProvince())  ||
//                        address.getDistrict().equals(patientRequest.getAddressDto().getDistrict()) ||
//                        address.getWard().equals(patientRequest.getAddressDto().getWard()) ||
//                        address.getDetails().equals(patientRequest.getAddressDto().getDetails())) {
//                    addressMapper.updateAddressFromDto(patientRequest.getAddressDto(), address);
//                    address = addressDao.update(address);
//                    patient.setAddress(address);
//                }
//                return patientMapper.toPatientResponse(patientDao.update(patient));
//            }

    return null;
        }
    @Override
    public PatientResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        var email = context.getAuthentication().getName();
        User user = userDao.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        Patient patient = patientDao.findById(user.getId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return patientMapper.toPatientResponse(patient);
    }

    @Override
    public List<PatientResponse> getPatients() {
            Map<String, Object> filterParams = new HashMap<>();
        return patientDao.findAll(filterParams).stream()
                .map(patientMapper::toPatientResponse)
                .toList();
    }

    @Override
    public PatientResponse getPatientById(String patientId) {
        Patient patient = patientDao.findById(UUID.fromString(patientId)).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return patientMapper.toPatientResponse(patient);
    }

    @Override
    public void deletePatient(String patientId)  {
        patientDao.findById(UUID.fromString(patientId)).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        try {
            patientDao.softDelete(UUID.fromString(patientId));
        } catch (EntityOperationException e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }
}
