package com.stegnin.proxy;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import static com.stegnin.proxy.util.Constants.*;

/**
 * @author Alexandr Stegnin
 */

public class Proxy {

    static Logger log = Logger.getLogger(Proxy.class.getName());

    public static void main(String[] args) {
        String host = DEFAULT_HOST_NAME;
        int apiPort = DEFAULT_API_PORT;
        int proxyPort = DEFAULT_PROXY_PORT;
        int maxConnections = DEFAULT_MAX_CONNECTIONS;
        if (args.length == 4) {
            try {
                host = args[0];
                apiPort = Integer.parseInt(args[1]);
                proxyPort = Integer.parseInt(args[2]);
                maxConnections = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                log.severe(WRONG_ARGUMENTS_EXCEPTION);
            }
        }
        try {
            log.info("Стартует прокси для ".concat(host).concat(":").concat(String.valueOf(apiPort)).concat(" на порту ").concat(String.valueOf(proxyPort)));
            ServerSocket server = new ServerSocket(proxyPort, maxConnections);
            server.setSoTimeout(DEFAULT_TIMEOUT);
            Socket client;
            while ((client = server.accept()) != null) {
                new ThreadProxy(client, host, apiPort);
            }
        } catch (InterruptedIOException e) {
            log.severe("Сервер остановлен по таймауту ".concat(String.valueOf((DEFAULT_TIMEOUT / 1000))));
        } catch (IOException e) {
            log.severe(e.getLocalizedMessage());
        }
    }
}
