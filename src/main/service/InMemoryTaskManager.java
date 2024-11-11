package main.service;

import main.models.Epic;
import main.models.Status;
import main.models.Subtask;
import main.models.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int createdId = 1;
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

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
            } else if (doneCount > 0 | progressCount > 0) {
                epics.get(epicId).setStatus(Status.IN_PROGRESS);
            } else {
                epics.get(epicId).setStatus(Status.NEW);
            }
        } else {
            epics.get(epicId).setStatus(Status.NEW);
        }
    }

    private void idGeneration() {
        createdId++;
    }

    //---------------------------------------------

    @Override
    public Task createTask(Task task) {
        task.setId(createdId);
        tasks.put(createdId, task);
        idGeneration();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(createdId);
        epics.put(createdId, epic);
        idGeneration();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId())) {
            subtask.setId(createdId);
            subtasks.put(createdId, subtask);
            epics.get(subtask.getEpicId()).addSubtaskId(createdId);
            epicStatusUpdate(subtask.getEpicId());
            idGeneration();
            return subtask;
        } else return null;
    }

    //-----------------------------------------------

    @Override
    public void deleteTaskById(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void deleteEpicById(int epicId) {
        for (Integer subtaskId : epics.get(epicId).getSubtasksId()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(epicId);
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        int epicId = subtasks.get(subtaskId).getEpicId();
        epics.get(epicId).removeSubtaskId(subtaskId);
        subtasks.remove(subtaskId);
        epicStatusUpdate(epicId);
    }
    //--------------------------------------------------

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()) {
            epic.clearSubtasksList();
            epic.setStatus(Status.NEW);
        }
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    //--------------------------------------------------

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //----------------------------------------------------

    @Override
    public Task getTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            historyManager.add(tasks.get(taskId));
            return tasks.get(taskId);
        } else return null;
    }

    @Override
    public Epic getEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            historyManager.add(epics.get(epicId));
            return epics.get(epicId);
        } else return null;
    }

    @Override
    public Subtask getSubtaskById(int subtaskId) {
        if (subtasks.containsKey(subtaskId)) {
            historyManager.add(subtasks.get(subtaskId));
            return subtasks.get(subtaskId);
        } else return null;
    }

    @Override
    public ArrayList<Subtask> getSubtasksByEpicID(int epicId) {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Integer subtaskId : epics.get(epicId).getSubtasksId()) {
            subtasksList.add(subtasks.get(subtaskId));
        }
        return subtasksList;
    }

    //-----------------------------------------------------

    @Override
    public Task updateTask(Task newTask) {
        if (tasks.containsKey(newTask.getId())) {
            Task task = tasks.get(newTask.getId());
            task.setName(newTask.getName());
            task.setDescription(newTask.getDescription());
            task.setStatus(newTask.getStatus());
            return tasks.get(newTask.getId());
        } else return null;
    }

    @Override
    public Epic updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getId())) {
            Epic epic = epics.get(newEpic.getId());
            epic.setName(newEpic.getName());
            epic.setDescription(newEpic.getDescription());
            return epics.get(newEpic.getId());
        } return null;
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getId())) {
            Subtask subtask = subtasks.get(newSubtask.getId());
            subtask.setName(newSubtask.getName());
            subtask.setDescription(newSubtask.getDescription());
            subtask.setStatus(newSubtask.getStatus());
            epicStatusUpdate(newSubtask.getEpicId());
            return subtasks.get(newSubtask.getId());
        } else return null;
    }

    @Override
    public LinkedList<Task> getHistory() {
        return historyManager.getHistory();
    }
}