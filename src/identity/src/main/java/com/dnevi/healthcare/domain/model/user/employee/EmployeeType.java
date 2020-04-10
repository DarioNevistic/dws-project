package com.dnevi.healthcare.domain.model.user.employee;

public enum EmployeeType {
    DOCTOR(Constants.DOCTOR),
    NURSE(Constants.NURSE);

    EmployeeType(String type) {
        if (!type.equals(this.name())) {
            throw new IllegalArgumentException("Unsupported employee type");
        }
    }

    public static class Constants {
        public static final String DOCTOR = "DOCTOR";
        public static final String NURSE = "NURSE";

        private Constants() {
        }
    }
}
