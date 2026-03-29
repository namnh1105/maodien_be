package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class SaleNotFoundException extends BaseException {

    private static final String ERROR_CODE = "SALE_NOT_FOUND";

    public SaleNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static SaleNotFoundException byId(String id) {
        return new SaleNotFoundException("Sale not found with id: " + id);
    }
}
