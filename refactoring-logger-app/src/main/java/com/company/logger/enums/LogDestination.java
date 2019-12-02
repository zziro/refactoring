package com.company.logger.enums;

public enum LogDestination {

    FILE(1, "FILE"), CONSOLE(2, "CONSOLE"), DATABASE(3, "DATABASE");

    int code;
    String value;

    LogDestination(int code, String value) {
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

