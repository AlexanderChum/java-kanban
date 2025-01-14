package main.service;

import main.models.Epic;
import main.models.Subtask;
import main.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTimeTests {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    InMemoryTaskManager tMng = new InMemoryTaskManager();

    Task task1 = new Task("Первый таск", "Описание1", 15, "2025-01-13 21:00");
    Task task2 = new Task("Второй таск", "Описание2", 15, "2025-01-13 21:10");
    Task task3 = new Task("Третий таск", "Описание3", 15, "2025-01-13 20:30");
    Task task4 = new Task("Четвертый таск", "Описание4", "2025-01-13 21:00");
    Epic epic1 = new Epic("Первый эпик", "Описание1");
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;

    @BeforeEach
    void setTest() {
        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.createEpic(epic1);
    }

    @Test
    void secondTaskIsALie() {
        List<Task> result = tMng.getAllTasks();
        assertEquals(1, result.size());
        assertEquals(task1, result.getFirst());
    }

    @Test
    void prioritizationOfTasks() {
        tMng.createTask(task3);
        List<Task> result = tMng.getPrioritizedTasks();
        assertEquals(2, result.size());
        assertEquals(task3, result.getFirst());
        assertEquals(task1, result.getLast());
    }

    @Test
    void taskWithoutDurationNotPrioritized() {
        tMng.createTask(task3);
        tMng.createTask(task4);
        List<Task> result = tMng.getPrioritizedTasks();
        assertEquals(2, result.size());
    }

    @Test
    void EpicTimeUpdate() {
        subtask1 = new Subtask("Первый подтаск", "Описание1", 5,
                "2025-01-13 20:00", epic1.getId());
        subtask2 = new Subtask("Второй подтаск", "Описание2", 5,
                "2025-01-13 21:30", epic1.getId());
        subtask3 = new Subtask("Третий подтаск", "Описание3", 5,
                "2025-01-13 21:55", epic1.getId());
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        tMng.createSubtask(subtask3);
        assertEquals("2025-01-13 20:00", epic1.getEpicStartTime().format(formatter));
        assertEquals("2025-01-13 22:00", epic1.getEpicEndTime().format(formatter));
    }
}