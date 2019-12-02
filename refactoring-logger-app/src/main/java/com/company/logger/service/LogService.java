package com.company.logger.service;

import com.company.logger.enums.LogDestination;
import com.company.logger.enums.LogLevel;
import com.company.logger.job.CoreJobLogger;

public interface LogService {

    LogService storeLogTo(LogDestination destination);
    LogService storeLogLevel(LogLevel level);
    CoreJobLogger build();
}
