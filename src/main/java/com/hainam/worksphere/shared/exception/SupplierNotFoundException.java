package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class SupplierNotFoundException extends BaseException {

    private static final String ERROR_CODE = "SUPPLIER_NOT_FOUND";

    public SupplierNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static SupplierNotFoundException byId(String id) {
        return new SupplierNotFoundException("Supplier not found with id: " + id);
    }
}
