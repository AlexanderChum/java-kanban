package main.service;

import main.models.Epic;
import main.models.Subtask;
import main.models.Task;

import java.util.ArrayList;
import java.util.LinkedList;

public interface TaskManager {

    Task createTask(Task task);

    Epic createEpic(Epic epic);

    Subtask createSubtask(Subtask subtask);

    void deleteTaskById(int taskId);

    void deleteEpicById(int epicId);

    void deleteSubtaskById(int subtaskId);

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpics();

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    Subtask getSubtaskById(int subtaskId);

    ArrayList<Subtask> getSubtasksByEpicID(int epicId);

    Task updateTask(Task newTask);

    Epic updateEpic(Epic newEpic);

    Subtask updateSubtask(Subtask newSubtask);

    LinkedList<Task> getHistory();
}
