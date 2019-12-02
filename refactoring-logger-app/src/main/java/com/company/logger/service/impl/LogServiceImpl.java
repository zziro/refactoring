package com.company.logger.service.impl;

import com.company.logger.enums.LogDestination;
import com.company.logger.enums.LogLevel;
import com.company.logger.job.CoreJobLogger;
import com.company.logger.service.LogService;
import com.company.logger.util.Constants;

public class LogServiceImpl implements LogService {

    private CoreJobLogger logger;

    public LogServiceImpl() {
        logger = new CoreJobLogger();
    }

    @Override
    public LogService storeLogTo(LogDestination destination) {
        if (destination == null) throw new IllegalArgumentException(Constants.NULL_NOT_ALLOWED);
        logger.storeLogTo(destination, Boolean.TRUE);
        return this;
    }

    @Override
    public LogService storeLogLevel(LogLevel level) {
        if (level == null) throw new IllegalArgumentException(Constants.NULL_NOT_ALLOWED);
        logger.storeLogLevel(level, Boolean.TRUE);
        return this;
    }

    @Override
    public CoreJobLogger build() {
        return logger;
    }
}
