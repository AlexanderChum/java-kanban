package main.service;

import main.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    File testFile;
    FileBackedTaskManager tMng;

    Task task1 = new Task("Первый таск", "Описание1");
    Task task2 = new Task("Второй таск", "Описание2");

    @BeforeEach
    void setTest() {
        try {
            testFile = File.createTempFile("test", "csv");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        tMng = FileBackedTaskManager.loadFromFile(testFile);
    }

    @Test
    void emptySaveAndLoad() {
        tMng.saveToFile();
        tMng.loadFromFile(testFile);
        assertTrue(tMng.getAllTasks().isEmpty());
        assertTrue(tMng.getAllEpics().isEmpty());
    }

    @Test
    void saveTasks() {
        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.saveToFile();
        assertEquals(2, tMng.getAllTasks().size());
    }

    @Test
    void loadTasks() {
        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.saveToFile();
        FileBackedTaskManager newTmng = FileBackedTaskManager.loadFromFile(testFile);
        assertEquals("Первый таск", newTmng.getTaskById(task1.getId()).getName());
        assertEquals("Второй таск", newTmng.getTaskById(task2.getId()).getName());
    }


}
