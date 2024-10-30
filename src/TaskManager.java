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

    Epic epic = new Epic();
    Subtask subtask = new Subtask();

    public void epicStatusUpdate(int epicId) {
        int progressCount = 0, doneCount = 0;
        ArrayList<Integer> subtasksId = epics.get(epicId).getSubtasksId();
        for (Integer subtaskId : subtasksId) {
            if (subtasks.get(subtaskId).getStatus() == Status.IN_PROGRESS) progressCount++;
            if (subtasks.get(subtaskId).getStatus() == Status.DONE) doneCount++;
        }
        if (doneCount == subtasksId.size()) {
            epics.get(epicId).setStatus(Status.DONE);
        } else if (doneCount > 0 || progressCount > 0) {
            epics.get(epicId).setStatus(Status.IN_PROGRESS);
        }
    }

    //---------------------------------------------

    public void createTask(Task task) {
        tasks.put(createdId++, task);
    }

    public void createEpic(Epic epic) {
        epics.put(createdId++, epic);
    }

    public void createSubtask(Subtask subtask) {
        if (!epics.isEmpty()) {
            subtasks.put(createdId, subtask);
            epics.get(subtask.getEpicId()).addSubtaskId(createdId++);
        } else System.out.println("Лист эпиков пуст");
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
        subtasks.clear();
    }

    public void deleteAllEpics() {
        deleteAllSubTasks();
        epics.clear();
    }

    //--------------------------------------------------

    public void printAllTasks() {
        for (Task task : tasks.values()) System.out.println(task);
    }

    public void printAllEpics() {
        for (Epic epic : epics.values()) System.out.println(epic);
    }

    public void printAllSubtasks() {
        for (Subtask subtask : subtasks.values()) System.out.println(subtask);
    }

    //----------------------------------------------------

    public void printTaskById(int taskId) {
        System.out.println(tasks.get(taskId));
    }

    public void printEpicById(int epicId) {
        System.out.println(epics.get(epicId));
    }

    public void printSubtaskById(int subtaskId) {
        System.out.println(subtasks.get(subtaskId));
    }

    public void printSubtasksByEpicID(int epicId) {
       for (Integer subtaskId : epics.get(epicId).getSubtasksId()) {
           System.out.println(subtasks.get(subtaskId));
       }
    }

    //-----------------------------------------------------

    public void updateTask(int taskId, Task task) {
        deleteTaskById(taskId);
        createTask(task);
    }

    public void updateEpic(int epicId, Epic epic) {
        deleteEpicById(epicId);
        createEpic(epic);
    }

    public void updateSubtask(int subtaskId, Subtask subtask) {
        createSubtask(subtask);
        deleteSubtaskById(subtaskId);
        epicStatusUpdate(subtask.getEpicId());
    }

}

