package com.company.logger.model;

import com.company.logger.enums.LogLevel;

import java.text.DateFormat;
import java.util.Date;

public class MessageLog {

    private String message;
    private LogLevel level;
    private Date date;

    public MessageLog(final String message, final LogLevel level) {
        this(message, level, new Date());
    }

    public MessageLog(String message, LogLevel level, Date date) {
        this.message = message;
        this.level = level;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public LogLevel getLevel() {
        return level;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public String toString() {
        return level.getValue() + " | " + DateFormat.getDateInstance(DateFormat.LONG).format(date) + " | " + message;
    }
}
