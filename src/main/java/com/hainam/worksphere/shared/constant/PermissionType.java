package com.hainam.worksphere.shared.constant;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Permission type constants.
 * These are used as permission codes in the database and in @RequirePermission annotations.
 */
public final class PermissionType {

    private PermissionType() {
        // Prevent instantiation
    }

    // User profile permissions
    public static final String VIEW_PROFILE = "VIEW_PROFILE";
    public static final String UPDATE_PROFILE = "UPDATE_PROFILE";
    public static final String DELETE_PROFILE = "DELETE_PROFILE";

    // User management permissions
    public static final String MANAGE_USER = "MANAGE_USER";
    public static final String VIEW_USER = "VIEW_USER";
    public static final String CREATE_USER = "CREATE_USER";
    public static final String UPDATE_USER = "UPDATE_USER";
    public static final String DELETE_USER = "DELETE_USER";

    // Soft delete specific permissions
    public static final String RESTORE_USER = "RESTORE_USER";
    public static final String PERMANENT_DELETE_USER = "PERMANENT_DELETE_USER";

    // Employee management permissions
    public static final String VIEW_EMPLOYEE = "VIEW_EMPLOYEE";
    public static final String CREATE_EMPLOYEE = "CREATE_EMPLOYEE";
    public static final String UPDATE_EMPLOYEE = "UPDATE_EMPLOYEE";
    public static final String DELETE_EMPLOYEE = "DELETE_EMPLOYEE";

    // Employee salary management permissions
    public static final String VIEW_EMPLOYEE_SALARY = "VIEW_EMPLOYEE_SALARY";
    public static final String CREATE_EMPLOYEE_SALARY = "CREATE_EMPLOYEE_SALARY";
    public static final String UPDATE_EMPLOYEE_SALARY = "UPDATE_EMPLOYEE_SALARY";
    public static final String DELETE_EMPLOYEE_SALARY = "DELETE_EMPLOYEE_SALARY";

    // Attendance management permissions
    public static final String VIEW_ATTENDANCE = "VIEW_ATTENDANCE";
    public static final String CREATE_ATTENDANCE = "CREATE_ATTENDANCE";
    public static final String UPDATE_ATTENDANCE = "UPDATE_ATTENDANCE";
    public static final String DELETE_ATTENDANCE = "DELETE_ATTENDANCE";

    // Work shift management permissions
    public static final String VIEW_WORK_SHIFT = "VIEW_WORK_SHIFT";
    public static final String CREATE_WORK_SHIFT = "CREATE_WORK_SHIFT";
    public static final String UPDATE_WORK_SHIFT = "UPDATE_WORK_SHIFT";
    public static final String DELETE_WORK_SHIFT = "DELETE_WORK_SHIFT";

    // Role and permission management
    public static final String MANAGE_ROLES = "MANAGE_ROLES";
    public static final String MANAGE_PERMISSIONS = "MANAGE_PERMISSIONS";
    public static final String ASSIGN_ROLES = "ASSIGN_ROLES";
    public static final String REVOKE_ROLES = "REVOKE_ROLES";

    // User role management
    public static final String MANAGE_USER_ROLE = "MANAGE_USER_ROLE";
    public static final String READ_USER_ROLE = "READ_USER_ROLE";

    // System administration
    public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";
    public static final String SUPER_ADMIN = "SUPER_ADMIN";

    // Leave request permissions
    public static final String VIEW_LEAVE_REQUEST = "VIEW_LEAVE_REQUEST";
    public static final String CREATE_LEAVE_REQUEST = "CREATE_LEAVE_REQUEST";
    public static final String APPROVE_LEAVE_REQUEST = "APPROVE_LEAVE_REQUEST";
    public static final String DELETE_LEAVE_REQUEST = "DELETE_LEAVE_REQUEST";

    // Contract management permissions
    public static final String VIEW_CONTRACT = "VIEW_CONTRACT";
    public static final String CREATE_CONTRACT = "CREATE_CONTRACT";
    public static final String UPDATE_CONTRACT = "UPDATE_CONTRACT";
    public static final String DELETE_CONTRACT = "DELETE_CONTRACT";

    // Payroll management permissions
    public static final String VIEW_PAYROLL = "VIEW_PAYROLL";
    public static final String CREATE_PAYROLL = "CREATE_PAYROLL";
    public static final String UPDATE_PAYROLL = "UPDATE_PAYROLL";
    public static final String DELETE_PAYROLL = "DELETE_PAYROLL";

    // Insurance management permissions
    public static final String VIEW_INSURANCE = "VIEW_INSURANCE";
    public static final String CREATE_INSURANCE = "CREATE_INSURANCE";
    public static final String UPDATE_INSURANCE = "UPDATE_INSURANCE";
    public static final String DELETE_INSURANCE = "DELETE_INSURANCE";

    // Degree management permissions
    public static final String VIEW_DEGREE = "VIEW_DEGREE";
    public static final String CREATE_DEGREE = "CREATE_DEGREE";
    public static final String UPDATE_DEGREE = "UPDATE_DEGREE";
    public static final String DELETE_DEGREE = "DELETE_DEGREE";

    // Relative management permissions
    public static final String VIEW_RELATIVE = "VIEW_RELATIVE";
    public static final String CREATE_RELATIVE = "CREATE_RELATIVE";
    public static final String UPDATE_RELATIVE = "UPDATE_RELATIVE";
    public static final String DELETE_RELATIVE = "DELETE_RELATIVE";

    // Audit logging permissions
    public static final String VIEW_AUDIT_LOGS = "VIEW_AUDIT_LOGS";
    public static final String MANAGE_AUDIT_LOGS = "MANAGE_AUDIT_LOGS";

    // ===== FARM MODULE PERMISSIONS =====

    // Pig management
    public static final String VIEW_PIG = "VIEW_PIG";
    public static final String CREATE_PIG = "CREATE_PIG";
    public static final String UPDATE_PIG = "UPDATE_PIG";
    public static final String DELETE_PIG = "DELETE_PIG";

    // Piglet herd management
    public static final String VIEW_PIGLET_HERD = "VIEW_PIGLET_HERD";
    public static final String CREATE_PIGLET_HERD = "CREATE_PIGLET_HERD";
    public static final String UPDATE_PIGLET_HERD = "UPDATE_PIGLET_HERD";
    public static final String DELETE_PIGLET_HERD = "DELETE_PIGLET_HERD";

    // Pen management
    public static final String VIEW_PEN = "VIEW_PEN";
    public static final String CREATE_PEN = "CREATE_PEN";
    public static final String UPDATE_PEN = "UPDATE_PEN";
    public static final String DELETE_PEN = "DELETE_PEN";

    // Vaccine management
    public static final String VIEW_VACCINE = "VIEW_VACCINE";
    public static final String CREATE_VACCINE = "CREATE_VACCINE";
    public static final String UPDATE_VACCINE = "UPDATE_VACCINE";
    public static final String DELETE_VACCINE = "DELETE_VACCINE";

    // Warehouse management
    public static final String VIEW_WAREHOUSE = "VIEW_WAREHOUSE";
    public static final String CREATE_WAREHOUSE = "CREATE_WAREHOUSE";
    public static final String UPDATE_WAREHOUSE = "UPDATE_WAREHOUSE";
    public static final String DELETE_WAREHOUSE = "DELETE_WAREHOUSE";

    // Supplier management
    public static final String VIEW_SUPPLIER = "VIEW_SUPPLIER";
    public static final String CREATE_SUPPLIER = "CREATE_SUPPLIER";
    public static final String UPDATE_SUPPLIER = "UPDATE_SUPPLIER";
    public static final String DELETE_SUPPLIER = "DELETE_SUPPLIER";

    // Livestock material management
    public static final String VIEW_LIVESTOCK_MATERIAL = "VIEW_LIVESTOCK_MATERIAL";
    public static final String CREATE_LIVESTOCK_MATERIAL = "CREATE_LIVESTOCK_MATERIAL";
    public static final String UPDATE_LIVESTOCK_MATERIAL = "UPDATE_LIVESTOCK_MATERIAL";
    public static final String DELETE_LIVESTOCK_MATERIAL = "DELETE_LIVESTOCK_MATERIAL";

    // Feed management
    public static final String VIEW_FEED = "VIEW_FEED";
    public static final String CREATE_FEED = "CREATE_FEED";
    public static final String UPDATE_FEED = "UPDATE_FEED";
    public static final String DELETE_FEED = "DELETE_FEED";

    // Customer management
    public static final String VIEW_CUSTOMER = "VIEW_CUSTOMER";
    public static final String CREATE_CUSTOMER = "CREATE_CUSTOMER";
    public static final String UPDATE_CUSTOMER = "UPDATE_CUSTOMER";
    public static final String DELETE_CUSTOMER = "DELETE_CUSTOMER";

    // Vaccination management
    public static final String VIEW_VACCINATION = "VIEW_VACCINATION";
    public static final String CREATE_VACCINATION = "CREATE_VACCINATION";
    public static final String UPDATE_VACCINATION = "UPDATE_VACCINATION";
    public static final String DELETE_VACCINATION = "DELETE_VACCINATION";

    // Warehouse import management
    public static final String VIEW_WAREHOUSE_IMPORT = "VIEW_WAREHOUSE_IMPORT";
    public static final String CREATE_WAREHOUSE_IMPORT = "CREATE_WAREHOUSE_IMPORT";
    public static final String UPDATE_WAREHOUSE_IMPORT = "UPDATE_WAREHOUSE_IMPORT";
    public static final String DELETE_WAREHOUSE_IMPORT = "DELETE_WAREHOUSE_IMPORT";

    // Sale management
    public static final String VIEW_SALE = "VIEW_SALE";
    public static final String CREATE_SALE = "CREATE_SALE";
    public static final String UPDATE_SALE = "UPDATE_SALE";
    public static final String DELETE_SALE = "DELETE_SALE";

    /**
     * Permission definition containing code, description, resource, and action
     */
    public static class PermissionDef {
        private final String code;
        private final String description;
        private final String resource;
        private final String action;

        public PermissionDef(String code, String description, String resource, String action) {
            this.code = code;
            this.description = description;
            this.resource = resource;
            this.action = action;
        }

        public String getCode() { return code; }
        public String getDescription() { return description; }
        public String getResource() { return resource; }
        public String getAction() { return action; }
        public String getDisplayName() { return description; }
    }

    /**
     * Returns all permission definitions for database seeding.
     */
    public static List<PermissionDef> all() {
        return Arrays.asList(
            new PermissionDef(VIEW_PROFILE, "View user profile", "PROFILE", "VIEW"),
            new PermissionDef(UPDATE_PROFILE, "Update user profile", "PROFILE", "UPDATE"),
            new PermissionDef(DELETE_PROFILE, "Delete user profile", "PROFILE", "DELETE"),

            new PermissionDef(MANAGE_USER, "Manage users", "USER", "MANAGE"),
            new PermissionDef(VIEW_USER, "View users", "USER", "VIEW"),
            new PermissionDef(CREATE_USER, "Create users", "USER", "CREATE"),
            new PermissionDef(UPDATE_USER, "Update users", "USER", "UPDATE"),
            new PermissionDef(DELETE_USER, "Delete users", "USER", "DELETE"),
            new PermissionDef(RESTORE_USER, "Restore deleted users", "USER", "RESTORE"),
            new PermissionDef(PERMANENT_DELETE_USER, "Permanently delete users", "USER", "PERMANENT_DELETE"),

            new PermissionDef(VIEW_EMPLOYEE, "View employees", "EMPLOYEE", "VIEW"),
            new PermissionDef(CREATE_EMPLOYEE, "Create employees", "EMPLOYEE", "CREATE"),
            new PermissionDef(UPDATE_EMPLOYEE, "Update employees", "EMPLOYEE", "UPDATE"),
            new PermissionDef(DELETE_EMPLOYEE, "Delete employees", "EMPLOYEE", "DELETE"),

            new PermissionDef(VIEW_EMPLOYEE_SALARY, "View employee salaries", "EMPLOYEE_SALARY", "VIEW"),
            new PermissionDef(CREATE_EMPLOYEE_SALARY, "Create employee salaries", "EMPLOYEE_SALARY", "CREATE"),
            new PermissionDef(UPDATE_EMPLOYEE_SALARY, "Update employee salaries", "EMPLOYEE_SALARY", "UPDATE"),
            new PermissionDef(DELETE_EMPLOYEE_SALARY, "Delete employee salaries", "EMPLOYEE_SALARY", "DELETE"),

            new PermissionDef(VIEW_ATTENDANCE, "View attendance records", "ATTENDANCE", "VIEW"),
            new PermissionDef(CREATE_ATTENDANCE, "Create attendance records", "ATTENDANCE", "CREATE"),
            new PermissionDef(UPDATE_ATTENDANCE, "Update attendance records", "ATTENDANCE", "UPDATE"),
            new PermissionDef(DELETE_ATTENDANCE, "Delete attendance records", "ATTENDANCE", "DELETE"),

            new PermissionDef(VIEW_WORK_SHIFT, "View work shifts", "WORK_SHIFT", "VIEW"),
            new PermissionDef(CREATE_WORK_SHIFT, "Create work shifts", "WORK_SHIFT", "CREATE"),
            new PermissionDef(UPDATE_WORK_SHIFT, "Update work shifts", "WORK_SHIFT", "UPDATE"),
            new PermissionDef(DELETE_WORK_SHIFT, "Delete work shifts", "WORK_SHIFT", "DELETE"),

            new PermissionDef(MANAGE_ROLES, "Manage roles", "ROLE", "MANAGE"),
            new PermissionDef(MANAGE_PERMISSIONS, "Manage permissions", "PERMISSION", "MANAGE"),
            new PermissionDef(ASSIGN_ROLES, "Assign roles to users", "ROLE", "ASSIGN"),
            new PermissionDef(REVOKE_ROLES, "Revoke roles from users", "ROLE", "REVOKE"),

            new PermissionDef(MANAGE_USER_ROLE, "Manage user role assignments", "USER_ROLE", "MANAGE"),
            new PermissionDef(READ_USER_ROLE, "Read user role assignments", "USER_ROLE", "READ"),

            new PermissionDef(SYSTEM_ADMIN, "System administration privileges", "SYSTEM", "ADMIN"),
            new PermissionDef(SUPER_ADMIN, "Super administrator privileges", "SYSTEM", "SUPER_ADMIN"),

            new PermissionDef(VIEW_LEAVE_REQUEST, "View leave requests", "LEAVE_REQUEST", "VIEW"),
            new PermissionDef(CREATE_LEAVE_REQUEST, "Create leave requests", "LEAVE_REQUEST", "CREATE"),
            new PermissionDef(APPROVE_LEAVE_REQUEST, "Approve leave requests", "LEAVE_REQUEST", "APPROVE"),
            new PermissionDef(DELETE_LEAVE_REQUEST, "Delete leave requests", "LEAVE_REQUEST", "DELETE"),

            new PermissionDef(VIEW_CONTRACT, "View contracts", "CONTRACT", "VIEW"),
            new PermissionDef(CREATE_CONTRACT, "Create contracts", "CONTRACT", "CREATE"),
            new PermissionDef(UPDATE_CONTRACT, "Update contracts", "CONTRACT", "UPDATE"),
            new PermissionDef(DELETE_CONTRACT, "Delete contracts", "CONTRACT", "DELETE"),

            new PermissionDef(VIEW_PAYROLL, "View payrolls", "PAYROLL", "VIEW"),
            new PermissionDef(CREATE_PAYROLL, "Create payrolls", "PAYROLL", "CREATE"),
            new PermissionDef(UPDATE_PAYROLL, "Update payrolls", "PAYROLL", "UPDATE"),
            new PermissionDef(DELETE_PAYROLL, "Delete payrolls", "PAYROLL", "DELETE"),

            new PermissionDef(VIEW_INSURANCE, "View insurances", "INSURANCE", "VIEW"),
            new PermissionDef(CREATE_INSURANCE, "Create insurances", "INSURANCE", "CREATE"),
            new PermissionDef(UPDATE_INSURANCE, "Update insurances", "INSURANCE", "UPDATE"),
            new PermissionDef(DELETE_INSURANCE, "Delete insurances", "INSURANCE", "DELETE"),

            new PermissionDef(VIEW_DEGREE, "View degrees", "DEGREE", "VIEW"),
            new PermissionDef(CREATE_DEGREE, "Create degrees", "DEGREE", "CREATE"),
            new PermissionDef(UPDATE_DEGREE, "Update degrees", "DEGREE", "UPDATE"),
            new PermissionDef(DELETE_DEGREE, "Delete degrees", "DEGREE", "DELETE"),

            new PermissionDef(VIEW_RELATIVE, "View relatives", "RELATIVE", "VIEW"),
            new PermissionDef(CREATE_RELATIVE, "Create relatives", "RELATIVE", "CREATE"),
            new PermissionDef(UPDATE_RELATIVE, "Update relatives", "RELATIVE", "UPDATE"),
            new PermissionDef(DELETE_RELATIVE, "Delete relatives", "RELATIVE", "DELETE"),

            new PermissionDef(VIEW_AUDIT_LOGS, "View audit logs", "AUDIT_LOG", "VIEW"),
            new PermissionDef(MANAGE_AUDIT_LOGS, "Manage audit logs", "AUDIT_LOG", "MANAGE"),

            // Farm module permissions
            new PermissionDef(VIEW_PIG, "View pigs", "PIG", "VIEW"),
            new PermissionDef(CREATE_PIG, "Create pigs", "PIG", "CREATE"),
            new PermissionDef(UPDATE_PIG, "Update pigs", "PIG", "UPDATE"),
            new PermissionDef(DELETE_PIG, "Delete pigs", "PIG", "DELETE"),

            new PermissionDef(VIEW_PIGLET_HERD, "View piglet herds", "PIGLET_HERD", "VIEW"),
            new PermissionDef(CREATE_PIGLET_HERD, "Create piglet herds", "PIGLET_HERD", "CREATE"),
            new PermissionDef(UPDATE_PIGLET_HERD, "Update piglet herds", "PIGLET_HERD", "UPDATE"),
            new PermissionDef(DELETE_PIGLET_HERD, "Delete piglet herds", "PIGLET_HERD", "DELETE"),

            new PermissionDef(VIEW_PEN, "View pens", "PEN", "VIEW"),
            new PermissionDef(CREATE_PEN, "Create pens", "PEN", "CREATE"),
            new PermissionDef(UPDATE_PEN, "Update pens", "PEN", "UPDATE"),
            new PermissionDef(DELETE_PEN, "Delete pens", "PEN", "DELETE"),

            new PermissionDef(VIEW_VACCINE, "View vaccines", "VACCINE", "VIEW"),
            new PermissionDef(CREATE_VACCINE, "Create vaccines", "VACCINE", "CREATE"),
            new PermissionDef(UPDATE_VACCINE, "Update vaccines", "VACCINE", "UPDATE"),
            new PermissionDef(DELETE_VACCINE, "Delete vaccines", "VACCINE", "DELETE"),

            new PermissionDef(VIEW_WAREHOUSE, "View warehouses", "WAREHOUSE", "VIEW"),
            new PermissionDef(CREATE_WAREHOUSE, "Create warehouses", "WAREHOUSE", "CREATE"),
            new PermissionDef(UPDATE_WAREHOUSE, "Update warehouses", "WAREHOUSE", "UPDATE"),
            new PermissionDef(DELETE_WAREHOUSE, "Delete warehouses", "WAREHOUSE", "DELETE"),

            new PermissionDef(VIEW_SUPPLIER, "View suppliers", "SUPPLIER", "VIEW"),
            new PermissionDef(CREATE_SUPPLIER, "Create suppliers", "SUPPLIER", "CREATE"),
            new PermissionDef(UPDATE_SUPPLIER, "Update suppliers", "SUPPLIER", "UPDATE"),
            new PermissionDef(DELETE_SUPPLIER, "Delete suppliers", "SUPPLIER", "DELETE"),

            new PermissionDef(VIEW_LIVESTOCK_MATERIAL, "View livestock materials", "LIVESTOCK_MATERIAL", "VIEW"),
            new PermissionDef(CREATE_LIVESTOCK_MATERIAL, "Create livestock materials", "LIVESTOCK_MATERIAL", "CREATE"),
            new PermissionDef(UPDATE_LIVESTOCK_MATERIAL, "Update livestock materials", "LIVESTOCK_MATERIAL", "UPDATE"),
            new PermissionDef(DELETE_LIVESTOCK_MATERIAL, "Delete livestock materials", "LIVESTOCK_MATERIAL", "DELETE"),

            new PermissionDef(VIEW_FEED, "View feeds", "FEED", "VIEW"),
            new PermissionDef(CREATE_FEED, "Create feeds", "FEED", "CREATE"),
            new PermissionDef(UPDATE_FEED, "Update feeds", "FEED", "UPDATE"),
            new PermissionDef(DELETE_FEED, "Delete feeds", "FEED", "DELETE"),

            new PermissionDef(VIEW_CUSTOMER, "View customers", "CUSTOMER", "VIEW"),
            new PermissionDef(CREATE_CUSTOMER, "Create customers", "CUSTOMER", "CREATE"),
            new PermissionDef(UPDATE_CUSTOMER, "Update customers", "CUSTOMER", "UPDATE"),
            new PermissionDef(DELETE_CUSTOMER, "Delete customers", "CUSTOMER", "DELETE"),

            new PermissionDef(VIEW_VACCINATION, "View vaccinations", "VACCINATION", "VIEW"),
            new PermissionDef(CREATE_VACCINATION, "Create vaccinations", "VACCINATION", "CREATE"),
            new PermissionDef(UPDATE_VACCINATION, "Update vaccinations", "VACCINATION", "UPDATE"),
            new PermissionDef(DELETE_VACCINATION, "Delete vaccinations", "VACCINATION", "DELETE"),

            new PermissionDef(VIEW_WAREHOUSE_IMPORT, "View warehouse imports", "WAREHOUSE_IMPORT", "VIEW"),
            new PermissionDef(CREATE_WAREHOUSE_IMPORT, "Create warehouse imports", "WAREHOUSE_IMPORT", "CREATE"),
            new PermissionDef(UPDATE_WAREHOUSE_IMPORT, "Update warehouse imports", "WAREHOUSE_IMPORT", "UPDATE"),
            new PermissionDef(DELETE_WAREHOUSE_IMPORT, "Delete warehouse imports", "WAREHOUSE_IMPORT", "DELETE"),

            new PermissionDef(VIEW_SALE, "View sales", "SALE", "VIEW"),
            new PermissionDef(CREATE_SALE, "Create sales", "SALE", "CREATE"),
            new PermissionDef(UPDATE_SALE, "Update sales", "SALE", "UPDATE"),
            new PermissionDef(DELETE_SALE, "Delete sales", "SALE", "DELETE")
        );
    }

    /**
     * Returns descriptions map for all permissions
     */
    public static Map<String, String> descriptions() {
        return all().stream().collect(Collectors.toMap(PermissionDef::getCode, PermissionDef::getDescription));
    }
}
