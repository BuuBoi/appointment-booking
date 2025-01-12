package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.dao.iface.AddressDao;
import com.dut.doctorcare.dao.iface.DoctorDao;
import com.dut.doctorcare.dao.iface.SpecializationDao;
import com.dut.doctorcare.dao.iface.UserDao;
import com.dut.doctorcare.dto.request.DoctorCreateDTO;
import com.dut.doctorcare.dto.request.DoctorRequest;
import com.dut.doctorcare.dto.request.ServiceDto;
import com.dut.doctorcare.dto.request.SpecializationDto;
import com.dut.doctorcare.dto.response.DoctorResponse;
import com.dut.doctorcare.dto.response.PatientResponse;
import com.dut.doctorcare.dto.response.UserResponseDto;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.mapper.*;
import com.dut.doctorcare.model.*;

import com.dut.doctorcare.repositories.*;
import com.dut.doctorcare.service.iface.AddressService;
import com.dut.doctorcare.service.iface.DoctorService;
import com.dut.doctorcare.service.iface.UserService;
import com.dut.doctorcare.specification.DoctorSpecification;
import com.nimbusds.jose.JWSObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.sql.Date;
import java.util.stream.Collectors;

import static com.dut.doctorcare.exception.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {
    private final DoctorDao doctorDao;
    private final DoctorMapper doctorMapper;
    private final AddressMapper addressMapper;
    private final SpecializationMapper specializationMapper;
    private final AddressDao addressDao;
    private final AddressService addressService;
    private final UserDao userDao;
    private final SpecializationDao specializationDao;
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final ServiceMapper serviceMapper;
    private final ServiceRepository serviceRepository;
    private final SpecializationRepository specializationRepository;
    private final PatientMapper patientMapper;
    private final AppointmentRepository appointmentRepository;
    private final UserService userService;


    @Override
    public DoctorResponse saveOrUpdate(DoctorRequest request) {
        var context = SecurityContextHolder.getContext();
        var email = context.getAuthentication().getName();
        log.info("email: {}", email);
        User user = userDao.findByEmail(email).orElseThrow(() -> new AppException(USER_NOT_FOUND));
        Doctor doctor = doctorDao.findById(user.getId()).orElse(null);
        if(doctor == null) {
            Address address = addressDao.save(addressMapper.toAddress(request.getAddressDto()));
           // Specialization spec = specializationDao.findById(UUID.fromString(request.getSpecializationDto().getId())).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            doctor = doctorMapper.toDoctor(request);
            doctor.setAddress(address);
            doctor.setUser(user);
           // doctor.setSpecialization(spec);
            return doctorMapper.toDoctorResponse(doctorDao.save(doctor));
        } else {
            doctorMapper.updateDoctorFromDto(request, doctor);
            Address address = doctor.getAddress();
            if(address.getProvince().equals(request.getAddressDto().getProvince()) ||
                    address.getDistrict().equals(request.getAddressDto().getDistrict()) ||
                    address.getWard().equals(request.getAddressDto().getWard()) ||
                    address.getDetails().equals(request.getAddressDto().getDetails())) {
                addressMapper.updateAddressFromDto(request.getAddressDto(), address);
                address = addressDao.update(address);
                doctor.setAddress(address);
            }
//           // Specialization spec = specializationDao.findById(UUID.fromString(request.getSpecializationDto().getId())).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
//            if (!doctor.getSpecialization().getId().equals(spec.getId())) {
//                doctor.setSpecialization(spec);
//            }
            return doctorMapper.toDoctorResponse(doctorDao.update(doctor));
        }
    }

    @Override
    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findByIsActiveTrue();
        doctors.forEach(doctor -> doctor.getWeeklyAvailables().size()); //dam bao duoc load truoc khi mapper
        return doctors.stream().map(doctor -> {
            DoctorResponse response = doctorMapper.toDoctorResponse(doctor);
            Map<String,List<String>> groupedweeklyAvailable = doctor.getWeeklyAvailables().stream()
                    .collect(Collectors.groupingBy(
                            WeeklyAvailable::getDateOfWeek,
                            Collectors.mapping(weeklyAvailable -> weeklyAvailable.getTimeSlot().toString(), Collectors.toList())
                    ));
            response.setWeeklyAvailables(groupedweeklyAvailable);
            return response;
//            doctorMapper.toDoctorResponse(doctor)).collect(Collectors.toList()
        }).collect(Collectors.toList());
    }

    @Override
    public DoctorResponse getDoctorById(String doctorId) {
        Doctor doctor = doctorRepository.findById(UUID.fromString(doctorId)).orElseThrow(() -> new AppException(USER_NOT_FOUND));
        DoctorResponse doctorResponse = doctorMapper.toDoctorResponse(doctor);
        Map<String,List<String>> groupedweeklyAvailable = doctor.getWeeklyAvailables().stream()
                .collect(Collectors.groupingBy(
                        WeeklyAvailable::getDateOfWeek,
                        Collectors.mapping(weeklyAvailable -> weeklyAvailable.getTimeSlot().toString(), Collectors.toList())
                ));
        doctorResponse.setWeeklyAvailables(groupedweeklyAvailable);
        return doctorResponse;
    }

    @Override
    public DoctorResponse getMyInfo() {
        log.info("Get my profile");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Kiểm tra và log authentication
        log.info("Authentication: {}", authentication);
        log.info("Authentication principal: {}", authentication.getPrincipal());

        if (authentication == null || authentication.getName().equals("anonymousUser")) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String email = authentication.getName();
        log.info("Email from security context: {}", email);


        User user = userDao.findByEmail(email).orElseThrow(() -> new AppException(USER_NOT_FOUND));
        Doctor doctor = doctorDao.findById(user.getId()).orElseThrow(() -> new AppException(USER_NOT_FOUND));
        return doctorMapper.toDoctorResponse(doctor);
    }


    @Override
    public Doctor createDoctor(DoctorCreateDTO doctorCreateDTO) {
        // Lấy thông tin User từ database
        Optional<User> userOptional = userRepository.findById(UUID.fromString(doctorCreateDTO.getId()));
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User không tồn tại với ID: " + doctorCreateDTO.getId());
        }

        User user = userOptional.get();

        // Tạo đối tượng Doctor
        Doctor doctor = Doctor.builder()
                .user(user)
                .fullName(doctorCreateDTO.getFullName())
                .bio(doctorCreateDTO.getBio())
                .dateOfBirth(doctorCreateDTO.getDob())
                .gender(doctorCreateDTO.getGender())
                .medicalLicense(doctorCreateDTO.getMedicalLicense())
                .medicalLicenseExpiry(doctorCreateDTO.getMedicalLicenseExpiry())
                .page(doctorCreateDTO.getPage())
                .profilePicture(doctorCreateDTO.getProfilePicture())
                .yearsOfExperience(doctorCreateDTO.getYearsOfExperience())
                .truckingNumber(doctorCreateDTO.getTruckingNumber())
                .build();

        // Lưu Doctor vào database
        return doctorRepository.save(doctor);
    }
    @Override
    public Doctor updateDoctor(String id, Map<String,Object> fields) {
        Doctor doctor;

        // Check if doctor exists
        Optional<Doctor> doctorOptional = doctorRepository.findById(UUID.fromString(id));

        if (doctorOptional.isPresent()) {
            doctor = doctorOptional.get();
            // Initialize lazy relationships if doctor exists
            Hibernate.initialize(doctor.getAddress());
        } else {
            // Check if user exists for create case
            User user = userRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new IllegalArgumentException("User không tồn tại với ID: " + id));

            // Create new doctor if not exists
            doctor = Doctor.builder()
                    .user(user)
                    .build();
        }

        // Cập nhật thông tin Doctor
        fields.forEach((key, value) -> {
            if(value != null) {
            switch (key) {
                case "fullName":
                    doctor.setFullName((String) value);
                    break;
                case "bio":
                    doctor.setBio((String) value);
                    break;
                case "dob":
                    doctor.setDateOfBirth(LocalDate.parse((String) value));
                    break;
                    case "gender":
                    doctor.setGender((String) value);
                    break;
                case "medicalLicense":
                    doctor.setMedicalLicense((String) value);
                    break;
                case "medicalLicenseExpiry":
                    doctor.setMedicalLicenseExpiry(LocalDate.parse((String) value));
                    break;
                case "profilePicture":
                    doctor.setProfilePicture((String) value);
                    break;
                case "truckingNumber":
                    doctor.setTruckingNumber((String) value);
                    break;
                case "yearsOfExperience":
                    doctor.setYearsOfExperience((int) value);
                    break;
                case "price":
                    BigDecimal price = new BigDecimal(value.toString());
                    doctor.setPrice(price);
                    break;
                case "hospitalName":
                    doctor.setHospitalName((String) value);
                    break;
                case "hospitalAddress":
                    doctor.setHospitalAddress((String) value);
                    break;
                case "hospitalContactNumber":
                    doctor.setHospitalContactNumber((String) value);
                    break;
                case "hospitalEmailAddress":
                    doctor.setHospitalEmailAddress((String) value);
                    break;
                case "hospitalWebsite":
                    doctor.setHospitalWebsite((String) value);
                    break;
                case "yearGraduation":
                    doctor.setYearGraduation(LocalDate.parse((String) value));
                    break;
                case "medicalSchool":
                    doctor.setMedicalSchool((String) value);
                    break;
                case "specialPrimary":
                    doctor.setSpecialPrimary((String) value);
                    break;
                case "id":
                    break;
                case "imageUrl":
//                    doctor.setProfilePicture((String) value);
//                    break;
                    break;
                case "page":
                    doctor.setPage((String) value);
                    break;
//                case "dateOfBirth":
//                    doctor.setDateOfBirth((String) value);
//                    break;
                case "emailAddress":
                    doctor.setEmailAddress((String) value);
                    break;
                case "phone":
                    doctor.setPhone((String) value);
                    break;
                case "city":
                case "district":
                case "ward":
                case "street":
                    // Kiểm tra và xử lý Address
                    Address updatedAddress = addressService.createOrUpdateAddress(doctor.getAddress(), fields);
                    doctor.setAddress(updatedAddress);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown field: " + key);
    }}});

          // Lưu Doctor vào database
        return doctorRepository.save(doctor);
    }

    @Override
    public ServiceDto getServiceByDoctorId(String doctorId) {
        var service =  doctorRepository.findById(UUID.fromString(doctorId)).map(
            Doctor::getService).orElse(null);
        ServiceDto serviceDto = new ServiceDto();
        serviceDto = serviceMapper.toServiceDto(service);
        return serviceDto;
    }

    @Override
    public Doctor setServiceForDoctor(String doctorId, String serviceId) {
        Doctor doctor = doctorRepository.findById(UUID.fromString(doctorId)).orElseThrow(() -> new AppException(USER_NOT_FOUND));
        com.dut.doctorcare.model.Service service = serviceRepository.findById(UUID.fromString(serviceId)).orElseThrow(() -> new AppException(USER_NOT_FOUND));
        doctor.setService(service);
        return doctorRepository.save(doctor);
    }

    @Override
    public SpecializationDto getSpecializationByDoctorId(String doctorId) {
        var service =  doctorRepository.findById(UUID.fromString(doctorId)).map(
                Doctor::getSpecialization).orElse(null);
        SpecializationDto serviceDto = new SpecializationDto();
        serviceDto = specializationMapper.toSpecializationDto(service);
        return serviceDto;
    }

    @Override
    public Doctor setSpecializationForDoctor(String doctorId, String serviceId) {
        Doctor doctor = doctorRepository.findById(UUID.fromString(doctorId)).orElseThrow(() -> new AppException(USER_NOT_FOUND));
        com.dut.doctorcare.model.Specialization service = specializationRepository.findById(UUID.fromString(serviceId)).orElseThrow(() -> new AppException(USER_NOT_FOUND));
        doctor.setSpecialization(service);
        return doctorRepository.save(doctor);
    }
    private String extractDoctorIdFromToken(Authentication authentication) {
        // Handle Nimbus JWT token
        if (authentication.getCredentials() instanceof String) {
            String token = (String) authentication.getCredentials();
            try {
                JWSObject jwsObject = JWSObject.parse(token);
                Map<String, Object> claims = jwsObject.getPayload().toJSONObject();
                return (String) claims.get("id");
            } catch (ParseException e) {
                throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
            }
        }
        throw new AppException(ErrorCode.UNAUTHENTICATED);
    }
    @Transactional
    @Override
    public List<PatientResponse> getMyPatient(String doctorId) {
        return appointmentRepository
                    .findDistinctPatientsByDoctorAndStatus(
                            UUID.fromString(doctorId),
                            Appointment.Status.ACCEPTED.toString()
                    )
                    .stream()
                    .map(row ->{

                       return new PatientResponse(
                                (String) row[0],
                                (String) row[1],
                                (String) row[2],
                                (String) row[3],
                                (String) row[4],
                                (String) row[5],
                                (String) row[6],
                                ((java.sql.Date) row[7]).toLocalDate(),
                                (String) row[8]
                        );}
                    )
                    .collect(Collectors.toList());
    }
    //get appointment by doctor and ptient


    @Override
    public List<DoctorResponse> getDoctorBySpecialization(String specialSlug) {
        return List.of();
    }

    @Override
    public List<DoctorResponse> getAllByOrderByCreatedAtDesc() {
        return doctorRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(Doctor::isActive)
                .map(doctorMapper::toDoctorResponse).collect(Collectors.toList());
    }

    @Override
    public Page<DoctorResponse> searchDoctors(String name, String address, String specialization, String service, int page, int size) {
        Specification<Doctor> spec = DoctorSpecification.filterDoctors(name, address, specialization, service);
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Doctor> doctorPage= doctorRepository.findAll(spec, pageable);
        // Chuyển đổi mỗi đối tượng Doctor thành DoctorResponseDTO
        List<DoctorResponse> doctorResponseDTOList = doctorPage.getContent().stream()
                .map(doctor -> doctorMapper.toDoctorResponse(doctor))
                .collect(Collectors.toList());


        return new PageImpl<>(doctorResponseDTOList, pageable, doctorPage.getTotalElements());
    }

    @Override
    public DoctorResponse updateDoctorActive(String doctorId, boolean active) {
        Doctor doctor = doctorRepository.findById(UUID.fromString(doctorId)).orElseThrow(() -> new AppException(USER_NOT_FOUND));
        doctor.setActive(active);
        return doctorMapper.toDoctorResponse(doctorRepository.save(doctor));
    }

    @Override
    public List<DoctorResponse> getAllDoctorsIncludeAllActive() {
        List<Doctor> doctors = doctorRepository.findAll();
        doctors.forEach(doctor -> doctor.getWeeklyAvailables().size()); //dam bao duoc load truoc khi mapper
        return doctors.stream().map(doctor -> {
            DoctorResponse response = doctorMapper.toDoctorResponse(doctor);
            Map<String,List<String>> groupedweeklyAvailable = doctor.getWeeklyAvailables().stream()
                    .collect(Collectors.groupingBy(
                            WeeklyAvailable::getDateOfWeek,
                            Collectors.mapping(weeklyAvailable -> weeklyAvailable.getTimeSlot().toString(), Collectors.toList())
                    ));
            response.setWeeklyAvailables(groupedweeklyAvailable);
            return response;
//            doctorMapper.toDoctorResponse(doctor)).collect(Collectors.toList()
        }).collect(Collectors.toList());
    }
}

