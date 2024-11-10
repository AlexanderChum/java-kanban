package main.service;

import main.models.Epic;
import main.models.Status;
import main.models.Subtask;
import main.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
class InMemoryTaskManagerTest {

    InMemoryTaskManager tMng = new InMemoryTaskManager();

    Task task1 = new Task("Первый таск", "Описание1");
    Task task2 = new Task("Второй таск","Описание2");
    Epic epic1 = new Epic("Первый эпик","Описание3");
    Subtask subtask1 = new Subtask("Первый подтаск", "Описание4", 2);
    Subtask subtask2 = new Subtask("Второй подтаск", "Описание5", 2);
    Subtask subtask3 = new Subtask(4,"Первый подтаск", "Описание6", Status.DONE, 2);
    Epic epic2 = new Epic(2,"Первый эпик","Описание7");
    Subtask subtask4 = new Subtask(5, "Второй подтаск", "Описание8", Status.DONE, 2);
    Task taskForUpdate = new Task(1,"Третий таск", "Описание8",Status.IN_PROGRESS);

    @BeforeEach
    void tMngHashMapsRefresh() {
        InMemoryTaskManager tMng = new InMemoryTaskManager();
    }

    @Test
    void createTask() {
        tMng.createTask(task1);
        Task testTask = tMng.getTaskById(1);
        assertEquals(task1, testTask);
    }

    @Test
    void createEpic() {
        tMng.createEpic(epic1);
        Epic testEpic = tMng.getEpicById(1);
        assertEquals(epic1, testEpic);
    }

    @Test
    void createSubtask() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        Subtask testSubtask = tMng.getSubtaskById(3);
        assertEquals(subtask1, testSubtask);
    }

    @Test
    void createSubtaskIfZeroEpics() {
        tMng.createSubtask(subtask1);
        assertTrue(tMng.subtasks.isEmpty());
    }

    @Test
    void deleteTaskById() {
        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.deleteTaskById(1);
        assertEquals(1, tMng.tasks.size());
        Task testTask = tMng.getTaskById(2);
        assertEquals(task2, testTask);
        assertFalse(tMng.tasks.containsValue(task1));
    }

    @Test
    void deleteEpicById() {
        tMng.createEpic(epic1);
        tMng.createEpic(epic2);
        tMng.deleteEpicById(1);
        assertEquals(1, tMng.epics.size());
        Epic testEpic = tMng.epics.get(2);
        assertEquals(epic2, testEpic);
        assertFalse(tMng.epics.containsValue(epic1));
    }

    @Test
    void deleteSubtaskById() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        tMng.deleteSubtaskById(3);
        assertEquals(1, tMng.subtasks.size());
        Subtask testSubtask = tMng.getSubtaskById(4);
        assertEquals(subtask2, testSubtask);
        assertFalse(tMng.subtasks.containsValue(subtask1));
    }

    @Test
    void deleteSubtaskByIdEpicStatusCheck() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        tMng.updateSubtask(subtask3);
        Epic testEpic = tMng.getEpicById(2);
        assertEquals(Status.IN_PROGRESS, testEpic.getStatus());
        tMng.deleteSubtaskById(4);
        assertEquals(Status.NEW, testEpic.getStatus());
    }

    @Test
    void deleteAllTasks() {
        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.deleteAllTasks();
        assertEquals(0, tMng.tasks.size());
        assertFalse(tMng.tasks.containsValue(task2));
    }

    @Test
    void deleteAllSubTasks() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        assertEquals(2, tMng.subtasks.size());
        tMng.deleteAllSubTasks();
        assertTrue(tMng.subtasks.isEmpty());
    }

    @Test
    void deleteAllSubtasksEpicStatusCheck() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        tMng.updateSubtask(subtask3);
        Epic testEpic = tMng.getEpicById(2);
        assertEquals(Status.IN_PROGRESS, testEpic.getStatus());
        tMng.deleteAllSubTasks();
        assertEquals(Status.NEW, testEpic.getStatus());
    }

    @Test
    void deleteAllEpics() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createEpic(epic2);
        assertEquals(2, tMng.epics.size());
        tMng.deleteAllEpics();
        assertTrue(tMng.epics.isEmpty());
    }

    @Test
    void deleteAllEpicsWithSubtasks() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        assertEquals(2, tMng.subtasks.size());
        tMng.deleteAllEpics();
        assertTrue(tMng.subtasks.isEmpty());
    }

    @Test
    void getAllTasks() {
        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.createEpic(epic1);
        ArrayList<Task> listOfTestTasks = tMng.getAllTasks();
        assertTrue(listOfTestTasks.contains(task1));
        assertTrue(listOfTestTasks.contains(task2));
        assertFalse(listOfTestTasks.contains(epic1));
    }

    @Test
    void getAllEpics() {
        tMng.createEpic(epic1);
        tMng.createEpic(epic2);
        assertEquals(2, tMng.epics.size());
        ArrayList<Epic> listOfTestEpics = tMng.getAllEpics();
        assertTrue(listOfTestEpics.contains(epic1));
        assertTrue(listOfTestEpics.contains(epic2));
    }

    @Test
    void getAllSubtasks() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        assertEquals(2, tMng.subtasks.size());
        ArrayList<Subtask> listOfTestSubtasks = tMng.getAllSubtasks();
        assertTrue(listOfTestSubtasks.contains(subtask1));
        assertTrue(listOfTestSubtasks.contains(subtask2));
    }

    @Test
    void getTaskById() {
        tMng.createTask(task1);
        tMng.createTask(task2);
        Task testTask = tMng.getTaskById(2);
        assertEquals(task2, testTask);
    }

    @Test
    void getTaskByWrongId() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        Task testTask = tMng.getTaskById(2);
        assertNull(testTask);
    }

    @Test
    void getEpicById() {
        tMng.createEpic(epic1);
        tMng.createEpic(epic2);
        Epic testEpic = tMng.getEpicById(2);
        assertEquals(epic2, testEpic);
    }

    @Test
    void getSubtaskById() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        Subtask testSubtask = tMng.getSubtaskById(3);
        assertEquals(subtask1, testSubtask);
    }

    @Test
    void getSubtasksByEpicID() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        ArrayList<Subtask> listOfTestSubtasks = tMng.getSubtasksByEpicID(2);
        assertEquals(2, listOfTestSubtasks.size());
        assertTrue(listOfTestSubtasks.contains(subtask1));
        assertTrue(listOfTestSubtasks.contains(subtask2));
        assertFalse(listOfTestSubtasks.contains(task1));
    }

    @Test
    void updateTask() {
        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.updateTask(taskForUpdate);
        Task testTask = tMng.getTaskById(1);
        assertEquals(Status.IN_PROGRESS, testTask.getStatus());
        assertEquals("Третий таск", testTask.getName());
    }

    @Test
    void updateEpic() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        tMng.updateEpic(epic2);
        Epic testEpic = tMng.getEpicById(2);
        ArrayList<Integer> subtasksId = testEpic.getSubtasksId();
        assertEquals(2, subtasksId.size());
        assertTrue(subtasksId.contains(subtask1.getId()));
        assertTrue(subtasksId.contains(subtask2.getId()));
        assertEquals("Описание7", testEpic.getDescription());
    }

    @Test
    void updateSubtaskWithEpicStatusCheck() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        Epic testEpic = tMng.getEpicById(2);
        Subtask testSubtask = tMng.getSubtaskById(4);
        assertEquals(Status.NEW, testEpic.getStatus());
        tMng.updateSubtask(subtask3);
        assertEquals(Status.IN_PROGRESS, testEpic.getStatus());
        assertEquals(Status.DONE, testSubtask.getStatus());
    }
}