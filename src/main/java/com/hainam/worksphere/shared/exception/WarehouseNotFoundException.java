package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class WarehouseNotFoundException extends BaseException {

    private static final String ERROR_CODE = "WAREHOUSE_NOT_FOUND";

    public WarehouseNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static WarehouseNotFoundException byId(String id) {
        return new WarehouseNotFoundException("Warehouse not found with id: " + id);
    }
}
