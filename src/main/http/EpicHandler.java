package main.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.exceptions.TaskNotFoundException;
import main.models.Epic;
import main.service.TaskManager;

import java.io.IOException;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    public EpicHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    private void getInfoAboutEpic(HttpExchange exchange) throws IOException {
        Endpoints localEndpoint = whatShouldIDo(exchange);
        String[] path = exchange.getRequestURI().getPath().split("/");
        Integer id = null;
        String subtasksTrue = null;

        if (path.length > 2) {
            if (isNumeric(path[2])) {
                id = Integer.parseInt(path[2]);
            }
        }
        if (path.length == 4) {
            subtasksTrue = path[3];
        }

        if ("subtasks".equals(subtasksTrue)) {
            getEpicSubtasks(exchange, id);
        } else {
            switch (localEndpoint) {
                case GET -> getEpicById(exchange, id);
                case GET_ALL -> getAllEpics(exchange);
                case DELETE -> deleteEpic(exchange, id);
                case DELETE_ALL -> deleteAllEpics(exchange);
                case POST -> createEpic(exchange);
                case POST_UPDATE -> updateEpic(exchange, id);
            }
        }
    }

    private void createEpic(HttpExchange exchange) throws IOException {
        String requestBody = readRequest(exchange);
        if (requestBody.isEmpty()) {
            sendHasInteractions(exchange);
        } else {
            Epic jsonEpic = gson.fromJson(requestBody, Epic.class);
            Epic result = taskManager.createEpic(jsonEpic);
            if (result == null) {
                sendHasInteractions(exchange);
            } else writeResponse(exchange, TASK_CREATE_SUCCESS, 201);
        }
    }

    private void updateEpic(HttpExchange exchange, Integer id) throws IOException {
        String requestBody = readRequest(exchange);
        if (requestBody.isEmpty()) {
            sendHasInteractions(exchange);
        } else {
            Epic jsonEpic = gson.fromJson(requestBody, Epic.class);
            jsonEpic.setId(id);
            Epic result = taskManager.updateEpic(jsonEpic);
            if (result == null) {
                sendHasInteractions(exchange);
            } else writeResponse(exchange, TASK_UPDATE_SUCCESS, 200);
        }
    }

    private void deleteEpic(HttpExchange exchange, Integer id) throws TaskNotFoundException, IOException {
        Epic epic = taskManager.getEpicById(id);
        if (epic == null) {
            sendNotFound(exchange);
        } else {
            taskManager.deleteEpicById(id);
            writeResponse(exchange, TASK_DELETE_SUCCESS, 200);
        }
    }

    private void deleteAllEpics(HttpExchange exchange) throws IOException {
        taskManager.deleteAllEpics();
        writeResponse(exchange, TASK_DELETE_SUCCESS, 200);
    }

    private void getEpicById(HttpExchange exchange, Integer id) throws TaskNotFoundException, IOException {
        Epic epic = taskManager.getEpicById(id);
        if (epic == null) {
            sendNotFound(exchange);
        } else {
            String epicToJson = gson.toJson(epic);
            writeResponse(exchange, epicToJson, 200);
        }
    }

    private void getAllEpics(HttpExchange exchange) throws IOException {
        String epicsToJson = gson.toJson(taskManager.getAllEpics());
        writeResponse(exchange, epicsToJson, 200);
    }

    private void getEpicSubtasks(HttpExchange exchange, Integer id) throws IOException {
        String epicSubtasksToJson = gson.toJson(taskManager.getSubtasksByEpicID(id));
        writeResponse(exchange, epicSubtasksToJson, 200);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        getInfoAboutEpic(exchange);
    }
}
