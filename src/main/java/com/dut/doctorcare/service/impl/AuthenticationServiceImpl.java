package com.dut.doctorcare.service.impl;

import com.dut.doctorcare.dao.iface.UserDao;
import com.dut.doctorcare.dto.request.AuthenticationRequest;
import com.dut.doctorcare.dto.request.IntrospecRequest;
import com.dut.doctorcare.dto.response.AuthenticationResponse;
import com.dut.doctorcare.dto.response.IntrospecResponse;
import com.dut.doctorcare.exception.AppException;
import com.dut.doctorcare.exception.ErrorCode;
import com.dut.doctorcare.model.User;
import com.dut.doctorcare.service.iface.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Date;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDao userDao;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNING_KEY;

    @Autowired
    public AuthenticationServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }


    public IntrospecResponse introspect(IntrospecRequest introspecRequest)  throws JOSEException, ParseException{
        var token = introspecRequest.getToken();
        JWSVerifier verifier = new MACVerifier(SIGNING_KEY.getBytes());
        JWSObject jwsObject = JWSObject.parse(token);
        JWTClaimsSet claimsSet = JWTClaimsSet.parse(jwsObject.getPayload().toString());
        Date expiryTime = claimsSet.getExpirationTime();
        var verified = jwsObject.verify(verifier);
        return IntrospecResponse.builder().valid(verified && expiryTime.after(new Date())).build();
    }


    @Transactional
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

        var user = userDao.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_OR_PASSWORD_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean isAuthencated = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPasswordHash());
        if (!isAuthencated) {
            throw new AppException(ErrorCode.EMAIL_OR_PASSWORD_NOT_FOUND);
        }

        return AuthenticationResponse.builder().id(user.getId().toString()).token(generateToken(user)).build();


    }

    public String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("buu.com")
                .issueTime(new Date())
                .expirationTime(new Date(new Date().getTime() + 24 * 60 * 60 * 1000)) //Xac dinh thoi han cua token
                .claim("scope", buildScope(user)) //Xac dinh pham vi cua token
                .build();
        Payload payload = new Payload(claimSet.toJSONObject()); //Chuyen claimSet thanh JSON, tao Payload
        JWSObject jwsObject = new JWSObject(header, payload);// build JWSObject

        //Ki ma hoa token
        try {
            jwsObject.sign(new MACSigner(SIGNING_KEY));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }
    private String buildScope(User user) {
        return user.getRole();
    }

}
