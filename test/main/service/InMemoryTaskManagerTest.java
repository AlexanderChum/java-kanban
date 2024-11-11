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
    Task task2 = new Task("Второй таск","Описание2");
    Epic epic1 = new Epic("Первый эпик","Описание3");
    Subtask subtask1 = new Subtask("Первый подтаск", "Описание4", 2);
    Subtask subtask2 = new Subtask("Второй подтаск", "Описание5", 2);
    Subtask subtask3 = new Subtask(4,"Первый подтаск", "Описание6", Status.DONE, 2);
    Epic epic2 = new Epic(2,"Первый эпик","Описание7");
    Task taskForUpdate = new Task(1,"Третий таск", "Описание8",Status.IN_PROGRESS);

    @BeforeEach
    void tMngHashMapsRefresh() {
        tMng.deleteAllTasks();
        tMng.deleteAllEpics();
        tMng.deleteAllSubTasks();
        tMng.createTask(task1);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
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
        tMng.deleteSubtaskById(subtask2.getId());
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
        ArrayList<Task> listOfTestTasks = tMng.getAllTasks();
        assertTrue(listOfTestTasks.contains(task1));
        assertTrue(listOfTestTasks.contains(task2));
        assertFalse(listOfTestTasks.contains(epic1));
    }

    @Test
    void getAllEpics() {
        tMng.createEpic(epic2);
        assertEquals(2, tMng.getAllEpics().size());
        ArrayList<Epic> listOfTestEpics = tMng.getAllEpics();
        assertTrue(listOfTestEpics.contains(epic1));
        assertTrue(listOfTestEpics.contains(epic2));
    }

    @Test
    void getAllSubtasks() {
        assertEquals(2, tMng.getAllSubtasks().size());
        ArrayList<Subtask> listOfTestSubtasks = tMng.getAllSubtasks();
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
        Subtask testSubtask = tMng.getSubtaskById(3);
        assertEquals(subtask1, testSubtask);
    }

    @Test
    void getSubtasksByEpicID() {
        List<Subtask> listOfTestSubtasks = tMng.getSubtasksByEpicID(2);
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