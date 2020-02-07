package com.stegnin.proxy;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import static com.stegnin.proxy.util.Constants.*;

/**
 * @author Alexandr Stegnin
 */

public class LogService {

    private final Logger logger = Logger.getLogger(LogService.class.getName());

    private final Path filePath;

    public LogService() {
        this.filePath = Paths.get(PATH_TO_LOG_FILE);
    }

    /**
     * Записывает сообщение в лог-файл
     * @param message - сообщение, которое надо записать
     */
    public void log(String message) {
        try(FileWriter fileWriter = new FileWriter(filePath.toFile(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(message);
            logger.info(WRITE_LOG_FILE_SUCCESS);
        } catch (IOException e) {
            logger.severe(WRITE_LOG_FILE_FAILED.concat(e.getLocalizedMessage()));
        }
    }

}
