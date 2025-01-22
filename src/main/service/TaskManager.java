package main.service;

import main.models.Epic;
import main.models.Subtask;
import main.models.Task;

import java.util.List;

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

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    Task getTaskById(int taskId);

    Epic getEpicById(int epicId);

    Subtask getSubtaskById(int subtaskId);

    List<Subtask> getSubtasksByEpicID(int epicId);

    Task updateTask(Task newTask);

    Epic updateEpic(Epic newEpic);

    Subtask updateSubtask(Subtask newSubtask);

    void updateEpicDuration(List<Integer> subtasksId, Integer epicId);

    void updateEpicStartTime(List<Integer> subtasksId, Integer epicId);

    void updateEpicEndTime(List<Integer> subtasksId, Integer epicId);

    boolean timeValidation(Task inputTask);

    List<Task> getHistory();
}
