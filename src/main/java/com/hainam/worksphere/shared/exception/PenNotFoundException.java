package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class PenNotFoundException extends BaseException {

    private static final String ERROR_CODE = "PEN_NOT_FOUND";

    public PenNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static PenNotFoundException byId(String id) {
        return new PenNotFoundException("Pen not found with id: " + id);
    }
}
