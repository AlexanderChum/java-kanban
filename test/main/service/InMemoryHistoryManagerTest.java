package main.service;

import main.models.Epic;
import main.models.Subtask;
import main.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager hMng = new InMemoryHistoryManager();

    Task task1 = new Task("Первый таск", "Описание1");
    Task task2 = new Task("Второй таск","Описание2");
    Task task3 = new Task("Третий таск","Описание3");
    Task task4 = new Task("Четвертый таск","Описание4");
    Epic epic1 = new Epic("Первый эпик","Описание5");
    Epic epic2 = new Epic("Второй эпик","Описание6");
    Subtask subtask1 = new Subtask("Первый подтаск", "Описание4", 3);
    Subtask subtask2 = new Subtask("Второй подтаск", "Описание5", 3);
    Subtask subtask3 = new Subtask("Первый подтаск", "Описание6", 3);
    Subtask subtask4 = new Subtask("Второй подтаск", "Описание8", 3);

    @BeforeEach
    void setTest() {
        InMemoryHistoryManager hMng = new InMemoryHistoryManager();
    }

    @Test
    void getHistory() {
        hMng.addTaskToViewed(task1);
        hMng.addTaskToViewed(epic1);
        ArrayList<Task> testTaskList= hMng.getHistory();
        assertNotNull(testTaskList);
        assertEquals(2, testTaskList.size());
    }

    @Test
    void addTaskToViewedCheckMaxSize() {
        hMng.addTaskToViewed(task1);
        hMng.addTaskToViewed(task2);
        hMng.addTaskToViewed(task3);
        hMng.addTaskToViewed(task4);
        hMng.addTaskToViewed(epic1);
        hMng.addTaskToViewed(epic2);
        hMng.addTaskToViewed(subtask1);
        hMng.addTaskToViewed(subtask2);
        hMng.addTaskToViewed(subtask3);
        hMng.addTaskToViewed(subtask4);
        hMng.addTaskToViewed(task1);
        hMng.addTaskToViewed(task1);
        hMng.addTaskToViewed(task1);
        ArrayList<Task> testList = hMng.getHistory();
        assertEquals(10, testList.size());
    }
}