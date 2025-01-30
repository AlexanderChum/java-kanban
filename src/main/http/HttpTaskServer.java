package main.http;

import com.google.gson.*;
import com.sun.net.httpserver.HttpServer;
import main.service.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    static HttpServer httpServer;
    TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    private void serverHandlers() {
        httpServer.createContext("/tasks", new TaskHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedHandler(taskManager, gson));
    }

    public void serverStart() {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverHandlers();
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void serverStop() {
        httpServer.stop(0);
        System.out.println("HTTP-сервер успешно остановлен.");
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefaultFileManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.serverStart();
    }
}