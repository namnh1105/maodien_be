package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class LivestockMaterialNotFoundException extends BaseException {

    private static final String ERROR_CODE = "LIVESTOCK_MATERIAL_NOT_FOUND";

    public LivestockMaterialNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static LivestockMaterialNotFoundException byId(String id) {
        return new LivestockMaterialNotFoundException("Livestock material not found with id: " + id);
    }
}
