package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends BaseException {

    private static final String ERROR_CODE = "CUSTOMER_NOT_FOUND";

    public CustomerNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static CustomerNotFoundException byId(String id) {
        return new CustomerNotFoundException("Customer not found with id: " + id);
    }
}
