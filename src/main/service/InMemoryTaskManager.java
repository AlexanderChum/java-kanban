package main.service;

import main.models.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private int createdId = 1;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> tasksInTimeOrder = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksInTimeOrder);
    }

    public HistoryManager getHistoryManager() {
        return this.historyManager;
    }

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

    public int getMaxCreatedId() {
        return createdId;
    }

    //---------------------------------------------

    @Override
    public Task createTask(Task task) {
        if (timeValidation(task)) {
            task.setId(createdId);
            tasks.put(createdId, task);
            if (task.getDuration() != 0) {
                tasksInTimeOrder.add(task);
            }
            idGeneration();
            return task;
        } else return null;
    }

    @Override
    public Epic createEpic(Epic epic) {
        if (timeValidation(epic)) {
            epic.setId(createdId);
            epics.put(createdId, epic);
            idGeneration();
            return epic;
        } else return null;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        if (epics.containsKey(subtask.getEpicId()) && timeValidation(subtask)) {
            subtask.setId(createdId);
            subtasks.put(createdId, subtask);
            if (subtask.getDuration() != 0) {
                tasksInTimeOrder.add(subtask);
            }
            epics.get(subtask.getEpicId()).addSubtaskId(createdId);
            epicStatusUpdate(subtask.getEpicId());
            epicTimeUpdates(subtask.getEpicId());
            idGeneration();
            return subtask;
        } else return null;
    }

    //-----------------------------------------------

    @Override
    public void deleteTaskById(int taskId) {
        historyManager.remove(taskId);
        tasksInTimeOrder.remove(tasks.get(taskId));
        tasks.remove(taskId);
    }

    @Override
    public void deleteEpicById(int epicId) {
        epics.get(epicId).getSubtasksId().forEach(subtaskId -> {
            historyManager.remove(subtaskId);
            tasksInTimeOrder.remove(subtasks.get(subtaskId));
            subtasks.remove(subtaskId);
        });
        historyManager.remove(epicId);
        tasksInTimeOrder.remove(epics.get(epicId));
        epics.remove(epicId);
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        historyManager.remove(subtaskId);
        int epicId = subtasks.get(subtaskId).getEpicId();
        epics.get(epicId).removeSubtaskId(subtaskId);
        tasksInTimeOrder.remove(subtasks.get(subtaskId));
        subtasks.remove(subtaskId);
        epicStatusUpdate(epicId);
        epicTimeUpdates(epicId);
    }
    //--------------------------------------------------

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(task -> {
            historyManager.remove(task.getId());
            tasksInTimeOrder.remove(task);
        });
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subtasks.values().forEach(subtask -> {
            historyManager.remove(subtask.getId());
            tasksInTimeOrder.remove(subtask);
        });
        epics.values().forEach(epic -> {
            epic.clearSubtasksList();
            epic.setStatus(Status.NEW);
            epicTimeUpdates(epic.getId());
        });
        subtasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.values().forEach(epic -> {
            epic.getSubtasksId().forEach(historyManager::remove);
            historyManager.remove(epic.getId());
            tasksInTimeOrder.remove(epic);
        });
        subtasks.clear();
        epics.clear();
    }

    //--------------------------------------------------

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    //----------------------------------------------------
    //переписать централизованную проверку для add(null)

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
    public List<Subtask> getSubtasksByEpicID(int epicId) {
        return epics.get(epicId).getSubtasksId().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());
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
        }
        return null;
    }

    @Override
    public Subtask updateSubtask(Subtask newSubtask) {
        if (subtasks.containsKey(newSubtask.getId())) {
            Subtask subtask = subtasks.get(newSubtask.getId());
            subtask.setName(newSubtask.getName());
            subtask.setDescription(newSubtask.getDescription());
            subtask.setStatus(newSubtask.getStatus());
            epicStatusUpdate(newSubtask.getEpicId());
            epicTimeUpdates(newSubtask.getEpicId());
            return subtasks.get(newSubtask.getId());
        } else return null;
    }

    //------------------------------------------------------

    private void epicTimeUpdates(Integer epicId) {
        List<Integer> subtasksId = epics.get(epicId).getSubtasksId();
        updateEpicDuration(subtasksId, epicId);
        updateEpicStartTime(subtasksId, epicId);
        updateEpicEndTime(subtasksId, epicId);
    }

    @Override
    public void updateEpicDuration(List<Integer> subtasksId, Integer epicId) {
        int duration = subtasksId.stream()
                .filter(id -> subtasks.get(id).getDuration() > 0)
                .mapToInt(id -> subtasks.get(id).getDuration())
                .sum();

        epics.get(epicId).setEpicDuration(duration);
    }

    @Override
    public void updateEpicStartTime(List<Integer> subtasksId, Integer epicId) {
        LocalDateTime startTime = subtasksId.stream()
                .map(id -> subtasks.get(id).getStartTime())
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        epics.get(epicId).setEpicStartTime(startTime);

    }

    @Override
    public void updateEpicEndTime(List<Integer> subtasksId, Integer epicId) {
        LocalDateTime endTime = subtasksId.stream()
                .map(id -> subtasks.get(id).getEndTime())
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        epics.get(epicId).setEpicEndTime(endTime);

    }

    @Override
    public boolean timeValidation(Task inputTask) {
        if (!getPrioritizedTasks().isEmpty()) {
            LocalDateTime inputTaskStartTime = inputTask.getStartTime();
            LocalDateTime inputTaskEndTime = inputTask.getEndTime();


            LocalDateTime taskToCompareStartTime = getPrioritizedTasks().stream()
                    .map(Task::getStartTime)
                    .min(LocalDateTime::compareTo)
                    .orElse(inputTaskStartTime);

            LocalDateTime taskToCompareEndTime = getPrioritizedTasks().stream()
                    .map(Task::getEndTime)
                    .max(LocalDateTime::compareTo)
                    .orElse(inputTaskEndTime);


            if (inputTaskStartTime.isBefore(taskToCompareStartTime)) {
                return inputTaskEndTime.isBefore(taskToCompareStartTime) ||
                        inputTaskEndTime.isEqual(taskToCompareStartTime);
            }

            if (inputTaskStartTime.isAfter(taskToCompareStartTime) &&
                    inputTaskStartTime.isBefore(taskToCompareEndTime)) {
                return false;
            }

            return inputTaskStartTime.isAfter(taskToCompareEndTime);
        }
        return true;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}