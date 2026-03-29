package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class VaccineNotFoundException extends BaseException {

    private static final String ERROR_CODE = "VACCINE_NOT_FOUND";

    public VaccineNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static VaccineNotFoundException byId(String id) {
        return new VaccineNotFoundException("Vaccine not found with id: " + id);
    }
}
