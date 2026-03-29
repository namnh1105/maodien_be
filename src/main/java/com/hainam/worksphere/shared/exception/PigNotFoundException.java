package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class PigNotFoundException extends BaseException {

    private static final String ERROR_CODE = "PIG_NOT_FOUND";

    public PigNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static PigNotFoundException byId(String id) {
        return new PigNotFoundException("Pig not found with id: " + id);
    }
}
