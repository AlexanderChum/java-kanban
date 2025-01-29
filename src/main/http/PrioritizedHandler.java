package main.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.models.Task;
import main.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    private void getPrioritized(HttpExchange exchange) throws IOException {
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        String jsonPrioritized = gson.toJson(prioritizedTasks);
        writeResponse(exchange, jsonPrioritized, 200);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoints localEndpoint = whatShouldIDo(exchange);
        switch (localEndpoint) {
            case GET_ALL -> getPrioritized(exchange);
            default -> sendEndpointNotFound(exchange);
        }
    }
}
