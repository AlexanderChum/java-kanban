package main.service;

import main.models.Epic;
import main.models.Subtask;
import main.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryTaskManager tMng = new InMemoryTaskManager();

    Task task1 = new Task("Первый таск", "Описание1");
    Task task2 = new Task("Второй таск", "Описание2");
    Task task3 = new Task("Третий таск", "Описание3");

    @BeforeEach
    void setTest() {
        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.getTaskById(task1.getId());
        tMng.getTaskById(task2.getId());
    }

    @Test
    void getHistory() {
        List<Task> testTaskList = tMng.getHistory();
        assertNotNull(testTaskList);
        assertEquals(2, testTaskList.size());
    }

    @Test
    void addedSameTaskTwiceAndRemoveCheck() {
        tMng.getTaskById(task1.getId());
        List<Task> testTaskList = tMng.getHistory();
        assertEquals(2, testTaskList.size());
        assertEquals(task2, testTaskList.get(0));
    }

    @Test
    void orderOfAddedTasksAndAddedTwice() {
        tMng.createTask(task3);
        tMng.getTaskById(task3.getId());
        tMng.getTaskById(task1.getId());
        List<Task> testTaskList = tMng.getHistory();
        assertEquals(3, testTaskList.size());
        assertEquals(task2, testTaskList.get(0));
        assertEquals(task3, testTaskList.get(1));
        assertEquals(task1, testTaskList.get(2));
    }
}