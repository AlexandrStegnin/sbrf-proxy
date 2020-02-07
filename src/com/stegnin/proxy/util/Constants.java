package com.stegnin.proxy.util;

import java.io.File;

/**
 * @author Alexandr Stegnin
 */

public class Constants {

    public static final Integer DEFAULT_API_PORT = 8080;

    public static final Integer DEFAULT_PROXY_PORT = 8989;

    public static final String DEFAULT_HOST_NAME = "localhost";

    public static final Integer DEFAULT_MAX_CONNECTIONS = 100;

    public static final Integer DEFAULT_TIMEOUT = 1_000 * 60 * 5; // 5 min

    public static final String WRONG_ARGUMENTS_EXCEPTION = "Некорректные входные параметры! Проверьте правильность ввода:\n"
            .concat("<host> - строка\n<api port> - число\n<proxy port> - число\n<max connections> - число");

    public static final String WRITE_LOG_FILE_SUCCESS = "Сообщение успешно записано в лог файл.\n";

    public static final String WRITE_LOG_FILE_FAILED = "При записи сообщения в лог файл возникла ошибка:\n";

    public static final String PATH_TO_LOG_FILE = "..".concat(File.separator).concat("proxyLog.log");
}
