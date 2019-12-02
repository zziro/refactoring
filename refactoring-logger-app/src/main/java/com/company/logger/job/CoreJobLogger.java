package com.company.logger.job;

import com.company.logger.enums.LogDestination;
import com.company.logger.enums.LogLevel;
import com.company.logger.exception.LoggerException;
import com.company.logger.model.MessageLog;
import com.company.logger.util.Constants;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public final class CoreJobLogger {

            private HashMap<LogDestination, Boolean> logToMap;
            private HashMap<LogLevel, Boolean> logLevelMap;
            private static Map dbParams;
            private Logger logger;

            public CoreJobLogger() {
                logToMap = new HashMap<>();
        logLevelMap = new HashMap<>();
        logger = Logger.getLogger("MyLog");

        Stream.of(LogDestination.values()).forEach(dest -> logToMap.put(dest, Boolean.FALSE));
        Stream.of(LogLevel.values()).forEach(dest -> logLevelMap.put(dest, Boolean.FALSE));
    }

    public void storeLogTo(LogDestination dest, Boolean enable) {logToMap.put(dest, enable); }

    public void storeLogLevel(LogLevel level, Boolean enable) {
        logLevelMap.put(level, enable);
    }

    public void generateLog(String message, LogLevel level) throws LoggerException {
        message = Objects.toString(message, "").trim();
        if (message.isEmpty()) {
            throw  new IllegalArgumentException(Constants.EMPTY_NOT_ALLOWED);
        }

        if (!logToMap.containsValue(Boolean.TRUE)) {
            throw new LoggerException(Constants.INVALID_CONFIGURATION);
        }

        if ((!logLevelMap.containsValue(Boolean.TRUE)) || level == null) {
            throw new LoggerException(Constants.TYPE_NOT_SPECIFIED);
        }

        MessageLog msgLog = new MessageLog(message, level);

        if (logToMap.get(LogDestination.FILE)) {
            writeLogInFile(msgLog, dbParams.get("logFileFolder").toString());
        }

        if (logToMap.get(LogDestination.CONSOLE)) {
            printInConsole(msgLog);
        }

        if (logToMap.get(LogDestination.DATABASE)) {
            saveLogInDB(msgLog, dbParams);
        }

    }

    private void printInConsole(MessageLog msgLog) {
        ConsoleHandler ch = new ConsoleHandler();
        logger.addHandler(ch);
        logger.log(Level.INFO, msgLog.toString());
    }

    private void writeLogInFile(MessageLog msgLog, String fileName) {
        try (
                FileOutputStream outputStream = new FileOutputStream(fileName)) {
            outputStream.getChannel().position(0);
            outputStream.write(msgLog.toString().getBytes());
        } catch (IOException e) {
            throw new LoggerException(e.getMessage());
        }
    }

    private void saveLogInDB(MessageLog msgLog, Map dbParams) {
        try (
                Connection conn = createConnection(dbParams);
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("insert into Log_Values('" + msgLog.toString() + "', " + msgLog.getLevel().getValue() + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection(Map dbParams) throws SQLException {
        Properties connectionProps = new Properties();
        connectionProps.put("user", dbParams.get("userName"));
        connectionProps.put("password", dbParams.get("password"));

        String url = new StringBuilder()
                .append("jdbc:")
                .append(dbParams.get("dbms"))
                .append("://")
                .append(dbParams.get("serverName"))
                .append(":")
                .append(dbParams.get("portNumber"))
                .append("/").toString();

        return DriverManager.getConnection(url, connectionProps);
    }
}
