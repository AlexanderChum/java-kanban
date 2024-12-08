package main.service;

import main.models.Epic;
import main.models.Status;
import main.models.Subtask;
import main.models.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    InMemoryTaskManager tMng = new InMemoryTaskManager();

    Task task1 = new Task("Первый таск", "Описание1");
    Task task2 = new Task("Второй таск", "Описание2");
    Epic epic1 = new Epic("Первый эпик", "Описание3");
    Subtask subtask1;
    Subtask subtask2;
    Subtask subtask3;
    Epic epic2;
    Task taskForUpdate;

    @BeforeEach
    void setTest() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        subtask1 = new Subtask("Первый подтаск", "Описание4", epic1.getId());
        subtask2 = new Subtask("Второй подтаск", "Описание5", epic1.getId());
        epic2 = new Epic(epic1.getId(), "Первый эпик", "Описание7");
        taskForUpdate = new Task(task1.getId(), "Третий таск", "Описание8", Status.IN_PROGRESS);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        subtask3 = new Subtask(subtask1.getId(), "Первый подтаск", "Описание6", Status.DONE,
                epic1.getId());
        tMng.createTask(task2);
    }

    @Test
    void createTask() {
        Task testTask = tMng.getAllTasks().getFirst();
        assertEquals(task1, testTask);
    }

    @Test
    void createEpic() {
        Epic testEpic = tMng.getAllEpics().getFirst();
        assertEquals(epic1, testEpic);
    }

    @Test
    void createSubtask() {
        Subtask testSubtask = tMng.getAllSubtasks().getFirst();
        assertEquals(subtask1, testSubtask);
    }

    @Test
    void createSubtaskIfZeroEpics() {
        tMng.deleteAllEpics();
        tMng.createSubtask(subtask1);
        assertTrue(tMng.getAllSubtasks().isEmpty());
    }

    @Test
    void deleteTaskById() {
        tMng.deleteTaskById(task1.getId());
        assertEquals(1, tMng.getAllTasks().size());
        Task testTask = tMng.getTaskById(task2.getId());
        assertEquals(task2, testTask);
        assertFalse(tMng.getAllTasks().contains(task1));
    }

    @Test
    void deleteEpicById() {
        tMng.createEpic(epic2);
        tMng.deleteEpicById(epic1.getId());
        assertEquals(1, tMng.getAllEpics().size());
        assertFalse(tMng.getAllEpics().contains(epic1));
        assertTrue(tMng.getAllEpics().contains(epic2));
    }

    @Test
    void deleteSubtaskById() {
        tMng.deleteSubtaskById(subtask1.getId());
        assertEquals(1, tMng.getAllSubtasks().size());
        Subtask testSubtask = tMng.getSubtaskById(subtask2.getId());
        assertEquals(subtask2, testSubtask);
        assertFalse(tMng.getAllSubtasks().contains(subtask1));
    }

    @Test
    void deleteSubtaskByIdEpicStatusCheck() {
        tMng.updateSubtask(subtask3);
        Epic testEpic = tMng.getEpicById(epic1.getId());
        assertEquals(Status.IN_PROGRESS, testEpic.getStatus());
        tMng.deleteSubtaskById(subtask3.getId());
        assertEquals(Status.NEW, testEpic.getStatus());
    }

    @Test
    void deleteAllTasks() {
        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.deleteAllTasks();
        assertEquals(0, tMng.getAllTasks().size());
        assertFalse(tMng.getAllTasks().contains(task2));
    }

    @Test
    void deleteAllSubTasks() {
        assertEquals(2, tMng.getAllSubtasks().size());
        tMng.deleteAllSubTasks();
        assertTrue(tMng.getAllSubtasks().isEmpty());
    }

    @Test
    void deleteAllSubtasksEpicStatusCheck() {
        tMng.updateSubtask(subtask3);
        Epic testEpic = tMng.getEpicById(epic1.getId());
        assertEquals(Status.IN_PROGRESS, testEpic.getStatus());
        tMng.deleteAllSubTasks();
        assertEquals(Status.NEW, testEpic.getStatus());
    }

    @Test
    void deleteAllEpics() {
        tMng.createEpic(epic2);
        assertEquals(2, tMng.getAllEpics().size());
        tMng.deleteAllEpics();
        assertTrue(tMng.getAllEpics().isEmpty());
    }

    @Test
    void deleteAllEpicsWithSubtasks() {
        assertEquals(2, tMng.getAllSubtasks().size());
        tMng.deleteAllEpics();
        assertTrue(tMng.getAllSubtasks().isEmpty());
    }

    @Test
    void getAllTasks() {
        List<Task> listOfTestTasks = tMng.getAllTasks();
        assertTrue(listOfTestTasks.contains(task1));
        assertTrue(listOfTestTasks.contains(task2));
        assertFalse(listOfTestTasks.contains(epic1));
    }

    @Test
    void getAllEpics() {
        tMng.createEpic(epic2);
        assertEquals(2, tMng.getAllEpics().size());
        List<Epic> listOfTestEpics = tMng.getAllEpics();
        assertTrue(listOfTestEpics.contains(epic1));
        assertTrue(listOfTestEpics.contains(epic2));
    }

    @Test
    void getAllSubtasks() {
        assertEquals(2, tMng.getAllSubtasks().size());
        List<Subtask> listOfTestSubtasks = tMng.getAllSubtasks();
        assertTrue(listOfTestSubtasks.contains(subtask1));
        assertTrue(listOfTestSubtasks.contains(subtask2));
    }

    @Test
    void getTaskById() {
        Task testTask = tMng.getTaskById(task2.getId());
        assertEquals(task2, testTask);
    }

    @Test
    void getTaskByWrongId() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        Task testTask = tMng.getTaskById(epic1.getId());
        assertNull(testTask);
    }

    @Test
    void getEpicById() {
        tMng.createEpic(epic2);
        Epic testEpic = tMng.getEpicById(epic2.getId());
        assertEquals(epic2, testEpic);
    }

    @Test
    void getSubtaskById() {
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        Subtask testSubtask = tMng.getSubtaskById(subtask1.getId());
        assertEquals(subtask1, testSubtask);
    }

    @Test
    void getSubtasksByEpicID() {
        List<Subtask> listOfTestSubtasks = tMng.getSubtasksByEpicID(epic1.getId());
        assertEquals(2, listOfTestSubtasks.size());
        assertTrue(listOfTestSubtasks.contains(subtask1));
        assertTrue(listOfTestSubtasks.contains(subtask2));
        assertFalse(listOfTestSubtasks.contains(task1));
    }

    @Test
    void updateTask() {
        tMng.updateTask(taskForUpdate);
        Task testTask = tMng.getTaskById(task1.getId());
        assertEquals(Status.IN_PROGRESS, testTask.getStatus());
        assertEquals("Третий таск", testTask.getName());
    }

    @Test
    void updateEpic() {
        tMng.updateEpic(epic2);
        Epic testEpic = tMng.getEpicById(epic1.getId());
        ArrayList<Integer> subtasksId = testEpic.getSubtasksId();
        assertEquals(2, subtasksId.size());
        assertTrue(subtasksId.contains(subtask1.getId()));
        assertTrue(subtasksId.contains(subtask2.getId()));
        assertEquals("Описание7", testEpic.getDescription());
    }

    @Test
    void updateSubtaskWithEpicStatusCheck() {
        Epic testEpic = tMng.getEpicById(epic1.getId());
        Subtask testSubtask = tMng.getSubtaskById(subtask3.getId());
        assertEquals(Status.NEW, testEpic.getStatus());
        tMng.updateSubtask(subtask3);
        assertEquals(Status.IN_PROGRESS, testEpic.getStatus());
        assertEquals(Status.DONE, testSubtask.getStatus());
    }

    @Test
    void getTaskByWrongIdGetNull() {
        Task task = tMng.getTaskById(epic1.getId());
        assertNull(task);
    }

    @Test
    void updateTaskByWrongId() {
        tMng.deleteAllTasks();
        assertNull(tMng.updateTask(taskForUpdate));
    }
}