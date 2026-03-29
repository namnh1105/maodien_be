package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class PigletHerdNotFoundException extends BaseException {

    private static final String ERROR_CODE = "PIGLET_HERD_NOT_FOUND";

    public PigletHerdNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static PigletHerdNotFoundException byId(String id) {
        return new PigletHerdNotFoundException("Piglet herd not found with id: " + id);
    }
}
