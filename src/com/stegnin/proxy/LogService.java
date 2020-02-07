package com.stegnin.proxy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * @author Alexandr Stegnin
 */

public class LogService {

    private final Logger logger = Logger.getLogger(LogService.class.getName());

    private final Path filePath;

    public LogService() {
        this.filePath = Paths.get(".." + File.separator + "proxyLog.log");
    }

    /**
     * Записывает сообщение в лог-файл
     * @param message - сообщение, которое надо записать
     */
    public void log(String message) {
        try(FileWriter fileWriter = new FileWriter(filePath.toFile(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(message);
            logger.info(String.format("Сообщение успешно записано в лог файл: %n%s", message));
        } catch (IOException e) {
            logger.severe(String.format("При записи сообщения в лог файл возникла ошибка: %n%s", e.getLocalizedMessage()));
        }
    }

}
