package com.company.logger;

import com.company.logger.enums.LogDestination;
import com.company.logger.enums.LogLevel;
import com.company.logger.exception.LoggerException;
import com.company.logger.job.CoreJobLogger;
import com.company.logger.service.impl.LogServiceImpl;
import org.junit.Test;

public class CoreJobLoggerTest {

    @Test(expected = IllegalArgumentException.class)
    public void StoreLogTo_InvalidDestination_IllegalArgumentExceptionThrown() {
        LogServiceImpl logService = new LogServiceImpl();
        CoreJobLogger coreJobLogger = logService.storeLogTo(null).build(); 
        coreJobLogger.generateLog("message", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void StoreLogLevel_InvalidLogLevel_IllegalArgumentExceptionThrown() {
        LogServiceImpl logService = new LogServiceImpl();
        CoreJobLogger coreJobLogger = logService.storeLogLevel(null).build();
        coreJobLogger.generateLog("message", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GenerateLogTo_EmptyMessage_IllegalArgumentExceptionThrown() {
        LogServiceImpl logService = new LogServiceImpl();
        CoreJobLogger coreJobLogger = logService.build();
        coreJobLogger.generateLog("", null);
    }

    @Test(expected = LoggerException.class)
    public void GenerateLogTo_InvalidConfiguration_LoggerExceptionThrown() {
        LogServiceImpl logService = new LogServiceImpl();
        CoreJobLogger coreJobLogger = logService.build();
        coreJobLogger.generateLog("message", null);
    }

    @Test(expected = LoggerException.class)
    public void GenerateLogTo_LogDestinationMissingSpecified_LoggerExceptionThrown() {
        LogServiceImpl logService = new LogServiceImpl();
        CoreJobLogger coreJobLogger = logService.storeLogLevel(LogLevel.WARNING).build();
        coreJobLogger.generateLog("message", null);
    }

    @Test(expected = LoggerException.class)
    public void GenerateLogTo_LogLevelMissingSpecified_LoggerExceptionThrown() {
        LogServiceImpl logService = new LogServiceImpl();
        CoreJobLogger coreJobLogger = logService.storeLogTo(LogDestination.DATABASE).build();
        coreJobLogger.generateLog("message", null);
    }

    @Test(expected = LoggerException.class)
    public void GenerateLogTo_MissingMessageLogLevelSpecified_LoggerExceptionThrown() {
        LogServiceImpl logService = new LogServiceImpl();
        CoreJobLogger coreJobLogger = logService.storeLogTo(LogDestination.DATABASE).storeLogLevel(LogLevel.MESSAGE).build();
        coreJobLogger.generateLog("message", null);
    }
}
