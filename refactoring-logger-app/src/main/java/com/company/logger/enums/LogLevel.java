package com.company.logger.enums;

public enum LogLevel {

    MESSAGE(1, "message"), WARNING(2, "warning"), ERROR(3, "error");

    int code;
    String value;

    LogLevel(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

