package main.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.exceptions.TaskNotFoundException;
import main.models.Subtask;
import main.service.TaskManager;

import java.io.IOException;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    private void getInfoAboutSubtask(HttpExchange exchange) throws IOException {
        Endpoints localEndpoint = whatShouldIDo(exchange);
        String[] path = exchange.getRequestURI().getPath().split("/");
        Integer id = null;
        if (path.length > 2) {
            if (isNumeric(path[2])) {
                id = Integer.parseInt(path[2]);
            } else sendHasInteractions(exchange);
        }
        switch (localEndpoint) {
            case GET -> getSubtaskById(exchange, id);
            case GET_ALL -> getAllSubtasks(exchange);
            case DELETE -> deleteSubtask(exchange, id);
            case DELETE_ALL -> deleteAllSubtasks(exchange);
            case POST -> createSubtask(exchange);
            case POST_UPDATE -> updateSubtask(exchange, id);
        }
    }

    private void createSubtask(HttpExchange exchange) throws IOException {
        if (readRequest(exchange).isEmpty()) {
            sendHasInteractions(exchange);
        } else {
            Subtask jsonSubtask = gson.fromJson(readRequest(exchange), Subtask.class);
            Subtask result = taskManager.createSubtask(jsonSubtask);
            if (result == null) {
                sendHasInteractions(exchange);
            } else writeResponse(exchange, taskCreateSuccess, 201);
        }
    }

    private void updateSubtask(HttpExchange exchange, Integer id) throws IOException {
        if (readRequest(exchange).isEmpty()) {
            sendHasInteractions(exchange);
        } else {
            Subtask jsonSubtask = gson.fromJson(readRequest(exchange), Subtask.class);
            jsonSubtask.setId(id);
            Subtask result = taskManager.updateSubtask(jsonSubtask);
            if (result == null) {
                sendHasInteractions(exchange);
            } else writeResponse(exchange, taskUpdateSuccess, 201);
        }
    }

    private void deleteSubtask(HttpExchange exchange, Integer id) throws TaskNotFoundException, IOException {
        Subtask subtask = taskManager.getSubtaskById(id);
        if (subtask == null) {
            sendNotFound(exchange);
        } else {
            taskManager.deleteSubtaskById(id);
            writeResponse(exchange, taskDeleteSuccess, 200);
        }
    }

    private void deleteAllSubtasks(HttpExchange exchange) throws IOException {
        taskManager.deleteAllSubTasks();
        writeResponse(exchange, taskDeleteSuccess, 200);
    }

    private void getSubtaskById(HttpExchange exchange, Integer id) throws TaskNotFoundException, IOException {
        Subtask subtask = taskManager.getSubtaskById(id);
        if (subtask == null) {
            sendNotFound(exchange);
        } else {
            String subtaskToJson = gson.toJson(subtask);
            writeResponse(exchange, subtaskToJson, 200);
        }
    }

    private void getAllSubtasks(HttpExchange exchange) throws IOException {
        String subtasksToJson = gson.toJson(taskManager.getAllSubtasks());
        writeResponse(exchange, subtasksToJson, 200);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        getInfoAboutSubtask(exchange);
    }
}
