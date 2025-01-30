package main.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.exceptions.TaskNotFoundException;
import main.models.Task;
import main.service.TaskManager;

import java.io.IOException;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    private void getInfoAboutTask(HttpExchange exchange) throws IOException {
        Endpoints localEndpoint = whatShouldIDo(exchange);
        String[] path = exchange.getRequestURI().getPath().split("/");
        Integer id = null;
        if (path.length > 2) {
            if (isNumeric(path[2])) {
                id = Integer.parseInt(path[2]);
            } else sendHasInteractions(exchange);
        }
        switch (localEndpoint) {
            case GET -> getTaskById(exchange, id);
            case GET_ALL -> getAllTasks(exchange);
            case DELETE -> deleteTask(exchange, id);
            case DELETE_ALL -> deleteAllTasks(exchange);
            case POST -> createTask(exchange);
            case POST_UPDATE -> updateTask(exchange, id);
        }
    }


    private void createTask(HttpExchange exchange) throws IOException {
        try {
            String requestBody = readRequest(exchange);
            if (requestBody.isEmpty()) {
                sendHasInteractions(exchange);
            } else {
                Task jsonTask = gson.fromJson(requestBody, Task.class);
                Task result = taskManager.createTask(jsonTask);
                if (result == null) {
                    sendHasInteractions(exchange);
                } else writeResponse(exchange, TASKCREATESUCCESS, 201);
            }
        } catch (Exception exception) {
            handleExceptions(exchange, exception);
        }
    }

    private void updateTask(HttpExchange exchange, Integer id) throws IOException {
        try {
            String requestBody = readRequest(exchange);
            if (requestBody.isEmpty()) {
                sendHasInteractions(exchange);
            } else {
                Task jsonTask = gson.fromJson(requestBody, Task.class);
                jsonTask.setId(id);
                Task result = taskManager.updateTask(jsonTask);
                if (result == null) {
                    sendHasInteractions(exchange);
                } else writeResponse(exchange, TASKUPDATESUCCESS, 200);
            }
        } catch (Exception exception) {
            handleExceptions(exchange, exception);
        }
    }

    private void deleteTask(HttpExchange exchange, Integer id) throws TaskNotFoundException, IOException {
        try {
            Task task = taskManager.getTaskById(id);
            if (task == null) {
                sendNotFound(exchange);
            } else {
                taskManager.deleteTaskById(id);
                writeResponse(exchange, TASKDELETESUCCESS, 200);
            }
        } catch (Exception exception) {
            handleExceptions(exchange, exception);
        }
    }

    private void deleteAllTasks(HttpExchange exchange) throws IOException {
        try {
            taskManager.deleteAllTasks();
            writeResponse(exchange, TASKDELETESUCCESS, 200);
        } catch (Exception exception) {
            handleExceptions(exchange, exception);
        }
    }

    private void getTaskById(HttpExchange exchange, Integer id) throws TaskNotFoundException, IOException {
        try {
            Task task = taskManager.getTaskById(id);
            if (task == null) {
                sendNotFound(exchange);
            } else {
                String taskToJson = gson.toJson(task);
                writeResponse(exchange, taskToJson, 200);
            }
        } catch (Exception exception) {
            handleExceptions(exchange, exception);
        }
    }

    private void getAllTasks(HttpExchange exchange) throws IOException {
        try {
            String tasksToJson = gson.toJson(taskManager.getAllTasks());
            writeResponse(exchange, tasksToJson, 200);
        } catch (Exception exception) {
            handleExceptions(exchange, exception);
        }
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        getInfoAboutTask(exchange);
    }
}