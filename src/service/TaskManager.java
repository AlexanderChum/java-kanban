package service;

import models.Epic;
import models.Status;
import models.Subtask;
import models.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int createdId = 1;

    private void epicStatusUpdate(int epicId) {
        int progressCount = 0, doneCount = 0;
        ArrayList<Integer> subtasksId = epics.get(epicId).getSubtasksId();
        for (Integer subtaskId : subtasksId) {
            if (subtasks.get(subtaskId).getStatus() == Status.IN_PROGRESS) progressCount++;
            if (subtasks.get(subtaskId).getStatus() == Status.DONE) doneCount++;
        }
        if (!subtasksId.isEmpty()) {
            if (doneCount == subtasksId.size()) {
                epics.get(epicId).setStatus(Status.DONE);
            } else if (doneCount > 0 || progressCount > 0) {
                epics.get(epicId).setStatus(Status.IN_PROGRESS);
            }
        } else {
            epics.get(epicId).setStatus(Status.NEW);
        }
    }

    private void idGeneration() {
        createdId++;
    }

    //---------------------------------------------

    public void createTask(Task task) {
        task.setId(createdId);
        tasks.put(createdId, task);
        idGeneration();
    }

    public void createEpic(Epic epic) {
        epic.setId(createdId);
        epics.put(createdId, epic);
        idGeneration();
    }

    public void createSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            subtask.setId(createdId);
            subtasks.put(createdId, subtask);
            epics.get(subtask.getEpicId()).addSubtaskId(createdId);
            epicStatusUpdate(subtask.getEpicId());
            idGeneration();
        } else System.out.println("Эпик не найден");
    }

    //-----------------------------------------------

    public void deleteTaskById(int taskId) {
            tasks.remove(taskId);
    }

    public void deleteEpicById(int epicId) {
        for (Integer subtaskId : epics.get(epicId).getSubtasksId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(epicId);
    }

    public void deleteSubtaskById(int subtaskId) {
        int epicId = subtasks.get(subtaskId).getEpicId();
        epics.get(epicId).removeSubtaskId(subtaskId);
        subtasks.remove(subtaskId);
        epicStatusUpdate(epicId);
    }
    //--------------------------------------------------

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasksList();
        }
        subtasks.clear();
        for (Integer epicId : epics.keySet()) {
            epicStatusUpdate(epicId);
        }
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    //--------------------------------------------------

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //----------------------------------------------------

    public Task getTaskById(int taskId) {
        return tasks.get(taskId);
    }

    public Epic getEpicById(int epicId) {
        return epics.get(epicId);
    }

    public Subtask getSubtaskById(int subtaskId) {
        return subtasks.get(subtaskId);
    }

    public ArrayList<Subtask> getSubtasksByEpicID(int epicId) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
       for (Integer subtaskId : epics.get(epicId).getSubtasksId()) {
           subtasksList.add(subtasks.get(subtaskId));
       }
       return  subtasksList;
    }

    //-----------------------------------------------------

    public void updateTask(Task newTask) {
        Task task = tasks.get(newTask.getId());
        task.setName(newTask.getName());
        task.setDescription(newTask.getDescription());
        task.setStatus(newTask.getStatus());
    }

    public void updateEpic(Epic newEpic) {
        Epic epic = epics.get(newEpic.getId());
        epic.setName(newEpic.getName());
        epic.setDescription(newEpic.getDescription());
    }

    public void updateSubtask(Subtask newSubtask) {
        Subtask subtask = subtasks.get(newSubtask.getId());
        subtask.setName(newSubtask.getName());
        subtask.setDescription(newSubtask.getDescription());
        subtask.setStatus(newSubtask.getStatus());
        epicStatusUpdate(newSubtask.getEpicId());
    }

}

