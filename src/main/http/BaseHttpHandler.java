package main.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import main.service.TaskManager;

import java.io.*;
import java.nio.charset.*;

public class BaseHttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected final Gson gson;
    protected final TaskManager taskManager;
    protected final String endpointNotFound = "Такого эндпоинта не существует";
    protected final String taskNotFound = "Задачи с таким id не существует";
    protected final String taskNotAccepted = "Задача не смогла обработаться";
    protected final String taskCreateSuccess = "Задача успешно создана";
    protected final String taskUpdateSuccess = "Задача успешно обновлена";
    protected final String taskDeleteSuccess = "Задача(и) успешно удалена";


    public BaseHttpHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    protected Endpoints whatShouldIDo(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] path = exchange.getRequestURI().getPath().split("/");
        Endpoints endpoint = null;

        switch (method) {
            case "GET":
                if (path.length < 3) {
                    endpoint = Endpoints.GET_ALL;
                } else {
                    endpoint = Endpoints.GET;
                }
                break;
            case "DELETE":
                if (path.length < 3) {
                    endpoint = Endpoints.DELETE_ALL;
                } else {
                    endpoint = Endpoints.DELETE;
                }
                break;
            case "POST":
                if (path.length < 3) {
                    endpoint = Endpoints.POST;
                } else {
                    endpoint = Endpoints.POST_UPDATE;
                }
                break;
            default: {
                sendEndpointNotFound(exchange);
                break;
            }
        }
        return endpoint;
    }

    protected String readRequest(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        if (inputStream == null) return "";
        return new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
    }

    protected void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        byte[] response = responseString.getBytes(DEFAULT_CHARSET);
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(responseCode, response.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        //exchange.close();
    }

    protected void sendEndpointNotFound(HttpExchange exchange) throws IOException {
        writeResponse(exchange, endpointNotFound, 400);
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        writeResponse(exchange, taskNotFound, 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        writeResponse(exchange, taskNotAccepted, 406);
    }

    public boolean isNumeric(String str) {
        if (str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
