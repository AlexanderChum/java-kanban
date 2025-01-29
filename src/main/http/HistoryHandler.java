package main.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import main.models.Task;
import main.service.TaskManager;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    public HistoryHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    private void getHistory(HttpExchange exchange) throws IOException {
        List<Task> historyTasks = taskManager.getHistory();
        String jsonHistory = gson.toJson(historyTasks);
        writeResponse(exchange, jsonHistory, 200);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoints localEndpoint = whatShouldIDo(exchange);
        switch (localEndpoint) {
            case GET_ALL -> getHistory(exchange);
            default -> sendEndpointNotFound(exchange);
        }
    }
}
