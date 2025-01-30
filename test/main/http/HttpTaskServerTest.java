package main.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import main.models.*;
import main.service.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {
    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();


    @BeforeEach
    public void testPreparations() {
        taskManager.deleteAllEpics();
        taskManager.deleteAllTasks();
        taskServer.serverStart();
    }

    @AfterEach
    public void serverShutdown() {
        taskServer.serverStop();
    }

    @Test
    void taskAdding() throws IOException, InterruptedException {
        Task task = new Task("Тест", "Описание");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = taskManager.getAllTasks();
        assertEquals(1, tasksFromManager.size());
        assertEquals("Тест", tasksFromManager.getFirst().getName());
    }

    @Test
    void epicAdding() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест", "Описание");
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> tasksFromManager = taskManager.getAllEpics();
        assertEquals(1, tasksFromManager.size());
        assertEquals("Тест", tasksFromManager.getFirst().getName());
    }

    @Test
    void subtaskAdding() throws IOException, InterruptedException {
        Epic epic = new Epic("Тест", "Описание 1");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Тест 2", "Описание 2", 1);
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> tasksFromManager = taskManager.getAllSubtasks();
        assertEquals(1, tasksFromManager.size());
        assertEquals("Тест 2", tasksFromManager.getFirst().getName());
    }


    @Test
    void getAllTasks() throws IOException, InterruptedException {
        taskManager.createTask(new Task("Тест 1", "Описание 1"));
        taskManager.createTask(new Task("Тест 2", "Описание 2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(taskManager.getAllTasks());
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks);
    }

    @Test
    void getAllEpics() throws IOException, InterruptedException {
        taskManager.createEpic(new Epic("Тест 1", "Описание 1"));
        taskManager.createEpic(new Epic("Тест 2", "Описание 2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(taskManager.getAllEpics());
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks);
    }

    @Test
    void getAllSubtasks() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Тестовый эпик", "Описание 1"));
        taskManager.createSubtask(new Subtask("Тестовый сабтаск 1", "Описание 2", epic.getId()));
        taskManager.createSubtask(new Subtask("Тестовый сабтаск 2", "Описание 3", epic.getId()));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(taskManager.getAllSubtasks());
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks);
    }

    @Test
    void getOneTaskById() throws IOException, InterruptedException {
        Task task = taskManager.createTask(new Task("Тест", "Описание"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTask = gson.toJson(taskManager.getTaskById(task.getId()));
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTask, tasks);
    }

    @Test
    void getOneEpicById() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Тест", "Описание"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(taskManager.getEpicById(epic.getId()));
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks);
    }

    @Test
    void getOneSubtaskById() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Тестовый эпик", "Описание 1"));
        Subtask subtask = taskManager.createSubtask(new Subtask("Тестовый сабтаск", "Описание 2", epic.getId()));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(taskManager.getSubtaskById(subtask.getId()));
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks);
    }

    @Test
    void getSubtasksByEpicId() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Тестовый эпик", "Описание 1"));
        taskManager.createSubtask(new Subtask("Тестовый сабтаск", "Описание 2", epic.getId()));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(taskManager.getSubtasksByEpicID(epic.getId()));
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks);
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        taskManager.createTask(new Task("Тест 1", "Описание 1"));
        taskManager.createTask(new Task("Тест 2", "Описание 2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> expectedTasks = taskManager.getAllTasks();
        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size());
    }

    @Test
    void deleteAllEpics() throws IOException, InterruptedException {
        taskManager.createEpic(new Epic("Тест 1", "Описание 1"));
        taskManager.createEpic(new Epic("Тест 2", "Описание 2"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> expectedTasks = taskManager.getAllEpics();
        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size());
    }

    @Test
    void deleteAllSubtasks() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Тестовый эпик", "Описание 1"));
        taskManager.createSubtask(new Subtask("Тестовый сабтаск 1", "Описание 2", epic.getId()));
        taskManager.createSubtask(new Subtask("Тестовый сабтаск 2", "Описание 3", epic.getId()));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> expectedTasks = taskManager.getAllSubtasks();
        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size());
    }

    @Test
    void deleteTaskById() throws IOException, InterruptedException {
        taskManager.createTask(new Task("Тест", "Описание"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> expectedTasks = taskManager.getAllTasks();
        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size());
    }

    @Test
    void deleteEpicById() throws IOException, InterruptedException {
        taskManager.createEpic(new Epic("Тест", "Описание"));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> expectedTasks = taskManager.getAllEpics();
        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size());
    }

    @Test
    void deleteSubtaskById() throws IOException, InterruptedException {
        Epic epic = taskManager.createEpic(new Epic("Тестовый эпик", "Описание 1"));
        taskManager.createSubtask(new Subtask("Тестовый сабтаск", "Описание 2", epic.getId()));
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> expectedTasks = taskManager.getAllSubtasks();
        assertEquals(200, response.statusCode());
        assertEquals(0, expectedTasks.size());
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        taskManager.createTask(new Task("Тест 1", "Описание 1"));
        taskManager.createTask(new Task("Тест 2", "Описание 2"));
        taskManager.getTaskById(0);
        taskManager.getTaskById(1);
        List<Task> historyList = taskManager.getHistory();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(historyList);
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks);
    }

    @Test
    void getPrioritized() throws IOException, InterruptedException {
        taskManager.createTask(new Task("Тест 1", "Описание 1"));
        taskManager.createTask(new Task("Тест 2", "Описание 2"));
        List<Task> historyList = taskManager.getPrioritizedTasks();
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String expectedTasks = gson.toJson(historyList);
        String tasks = response.body();
        assertEquals(200, response.statusCode());
        assertEquals(expectedTasks, tasks);
    }
}