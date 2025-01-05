package com.dut.doctorcare.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found.", HttpStatus.NOT_FOUND),
    SlUG_NOT_FOUND(1001, "Slug not found.", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1002, "User already exists.",HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1003, "Role not found.",HttpStatus.NOT_FOUND),
    ROLE_ALREADY_EXISTS(1004, "Role already exists.",HttpStatus.BAD_REQUEST),
    Email_ALREADY_EXISTS(1005, "Email already exists.",HttpStatus.BAD_REQUEST),
    INVALID_CURRENT_PASSWORD(1006, "Invalid current password.",HttpStatus.BAD_REQUEST),
    PASSWORD_MISMATCH(1007, "Password mismatch.",HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1008, "Email is not in correct format.",HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1009, "Password must be at least 6 characters long.",HttpStatus.BAD_REQUEST),
    INVALID_DISPLAY_NAME(1010, "Display name must be at least 6 characters long.",HttpStatus.BAD_REQUEST),
    EMAIL_OR_PASSWORD_NOT_FOUND(1011, "Email or password not found.",HttpStatus.NOT_FOUND),
    INVALID_ADDRESS(1012, "Invalid address.",HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND(1013, "Address not found.",HttpStatus.NOT_FOUND),
    UNCATEGORIZED_EXCEPTION(9999, "Uncatalogued error.", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1000, "Invalid key.",HttpStatus.BAD_REQUEST), //kiem tra Key hop le
    UNAUTHENTICATED(8888, "Unauthenticated.",HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(7777, "you do not have permission.",HttpStatus.FORBIDDEN),
    DATABASE_OPERATION_FAILED(5555, "Database operation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private int code;
    private String message;
    private HttpStatusCode statusCode;

    ErrorCode(int i, String s, HttpStatusCode statusCode) {
        this.code = i;
        this.message = s;
        this.statusCode = statusCode;
    }


}
