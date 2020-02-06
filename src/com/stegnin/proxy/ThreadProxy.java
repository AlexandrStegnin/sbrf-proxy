package com.stegnin.proxy;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * @author Alexandr Stegnin
 *
 * Класс
 */

public class ThreadProxy extends Thread {

    private final Logger log = Logger.getLogger(ThreadProxy.class.getName());
    private final LogService logService;
    private final Socket clientSocket;
    private final String serverUrl;
    private final int serverPort;

    ThreadProxy(Socket clientSocket, String serverUrl, int serverPort) {
        this.serverUrl = serverUrl;
        this.serverPort = serverPort;
        this.clientSocket = clientSocket;
        this.logService = new LogService();
        setDaemon(true);
        this.start();
    }

    @Override
    public void run() {
        try {
            final byte[] request = new byte[4096];
            byte[] reply = new byte[4096];
            final InputStream inputFromClient = clientSocket.getInputStream();
            final OutputStream outToClient = clientSocket.getOutputStream();
            Socket server;
            try {
                server = new Socket(serverUrl, serverPort);
            } catch (IOException e) {
                PrintWriter out = new PrintWriter(new OutputStreamWriter(outToClient));
                out.println(e.getLocalizedMessage());
                out.flush();
                log.severe(e.getLocalizedMessage());
                throw new RuntimeException(e);
            }

            final InputStream inFromServer = server.getInputStream();
            final OutputStream outToServer = server.getOutputStream();

            // новый thread для отправки запросов на сервер от клиента
            new Thread(() -> {
                int bytes_read;
                try {
                    while ((bytes_read = inputFromClient.read(request)) != -1) {
                        outToServer.write(request, 0, bytes_read);
                        outToServer.flush();
                        logService.log(convert(new String(request), MessageType.REQUEST));
                    }
                } catch (IOException e) {
                    log.severe(e.getLocalizedMessage());
                }
                try {
                    outToServer.close();
                } catch (IOException e) {
                    log.severe(e.getLocalizedMessage());
                }
            }).start();

            int bytes_read;
            try {
                while ((bytes_read = inFromServer.read(reply)) != -1) {
                    outToClient.write(reply, 0, bytes_read);
                    outToClient.flush();
                    logService.log(convert(new String(reply), MessageType.RESPONSE));
                }
            } catch (IOException e) {
                log.severe(e.getLocalizedMessage());
            } finally {
                try {
                    server.close();
                } catch (IOException e) {
                    log.severe(e.getLocalizedMessage());
                }
            }
            outToClient.close();
            clientSocket.close();
        } catch (IOException e) {
            log.severe(e.getLocalizedMessage());
        }
    }

    private String convert(String message, MessageType type) {
        String[] headers = message.split("\r\n");
        StringBuilder result = new StringBuilder();
        switch (type) {
            case RESPONSE:
                result.append("Ответ сервера: [").append(headers[0].trim()).append("].").append(" Содержимое: [").append(headers[headers.length - 1]).append("]");
                break;
            case REQUEST:
                String host = "Host:";
                int startPosition = message.indexOf(host);
                int endPosition = startPosition;
                endPosition += message.substring(startPosition).indexOf("\r\n");
                String address = message.substring(startPosition + host.length(), endPosition).trim();
                result.append("Запрос клиента: [").append(headers[0]).append("].").append(" Направлен с адреса: [").append(address).append("]");
                break;
        }
        return result.toString();
    }
}


