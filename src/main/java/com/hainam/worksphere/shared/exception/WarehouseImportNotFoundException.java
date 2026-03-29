package com.hainam.worksphere.shared.exception;

import org.springframework.http.HttpStatus;

public class WarehouseImportNotFoundException extends BaseException {

    private static final String ERROR_CODE = "WAREHOUSE_IMPORT_NOT_FOUND";

    public WarehouseImportNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, ERROR_CODE);
    }

    public static WarehouseImportNotFoundException byId(String id) {
        return new WarehouseImportNotFoundException("Warehouse import not found with id: " + id);
    }
}
