package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class VaccinationNotFoundException extends BaseException {

    private static final String ERROR_CODE = "VACCINATION_NOT_FOUND";

    public VaccinationNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static VaccinationNotFoundException byId(String id) {
        return new VaccinationNotFoundException("Vaccination not found with id: " + id);
    }
}
