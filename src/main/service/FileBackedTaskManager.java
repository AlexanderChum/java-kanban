package main.service;

import main.models.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File fileForWork;
    private final InMemoryTaskManager tMng;

    public FileBackedTaskManager(File file) {
        this.fileForWork = file;
        this.tMng = (InMemoryTaskManager) Managers.getDefault();
    }

    public static FileBackedTaskManager loadFromFile(File fileForWork) {
        FileBackedTaskManager loadedFile = new FileBackedTaskManager(fileForWork);
        loadedFile.load();
        return loadedFile;
    }

    private void load() {
        try (BufferedReader br = Files.newBufferedReader(fileForWork.toPath(), StandardCharsets.UTF_8)) {
            br.readLine();
            while (br.ready()) {
                String line = br.readLine();
                fromString(line);
            }
        } catch (IOException e) {
            throw new ManagerLoadException("Ошибка загрузки файла");
        }
    }

    private Task fromString(String s) {
        String[] separatedLine = s.split(",");
        String taskType = separatedLine[1];
        String name = separatedLine[2];
        String stringStatus = separatedLine[3];
        String description = separatedLine[4];

        switch (taskType) {
            case "TASK":
                Task task = new Task(name, description);
                super.createTask(task);
                task.setStatus(stringToStatus(stringStatus));
                return task;
            case "EPIC":
                Epic epic = new Epic(name, description);
                super.createTask(epic);
                epic.setStatus((stringToStatus(stringStatus)));
                return epic;
            case "SUBTASK":
                Subtask subtask = new Subtask(name, description, Integer.parseInt(separatedLine[5]));
                super.createSubtask(subtask);
                subtask.setStatus(stringToStatus(stringStatus));
                return subtask;
            default:
                throw new TypeMismatchException("Такого типа задач нет");
        }
    }

    private Status stringToStatus(String line) {
        switch (line) {
            case "NEW":
                return Status.NEW;
            case "IN_PROGRESS":
                return Status.IN_PROGRESS;
            case "DONE":
                return Status.DONE;
            default:
                return Status.NEW;
        }
    }

    public void saveToFile() {
        save();
    }

    private void save() {
        try (BufferedWriter bw = Files.newBufferedWriter(fileForWork.toPath(), StandardCharsets.UTF_8)) {
            bw.write("id,type,name,status,description,epic");
            bw.newLine();
            for (int i = 1; i <= super.getMaxCreatedId(); i++) {
                if (super.tasks.containsKey(i)) {
                    bw.write(toString(super.tasks.get(i)));
                    bw.newLine();
                } else if (super.epics.containsKey(i)) {
                    bw.write(toString(super.epics.get(i)));
                    bw.newLine();
                } else if (super.subtasks.containsKey(i)) {
                    bw.write(toString(super.subtasks.get(i)));
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла");
        }
    }

    private String toString(Task task) {
        int localId = task.getId();
        String localName = task.getName();
        String localDescription = task.getDescription();
        String localStatus;
        if (task.getStatus().equals(Status.NEW)) {
            localStatus = "NEW";
        } else if (task.getStatus().equals(Status.IN_PROGRESS)) {
            localStatus = "IN_PROGRESS";
        } else {
            localStatus = "DONE";
        }

        if (super.tasks.containsKey(localId)) {
            return String.format("%s,TASK,%s,%s,%s", localId, localName, localStatus, localDescription);
        } else if (super.epics.containsKey(localId)) {
            return String.format("%s,EPIC,%s,%s,%s", localId, localName, localStatus, localDescription);
        } else {
            return String.format("%s,SUBTASK,%s,%s,%s,%s", localId, localName, localStatus, localDescription,
                    super.subtasks.get(localId).getEpicId());
        }
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    //--------------------------------

    @Override
    public Task updateTask(Task task) {
        super.updateTask(task);
        save();
        return task;
    }

    @Override
    public Epic updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
        return subtask;
    }

    //-----------------------------------

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubtaskById(int subtaskId) {
        super.deleteSubtaskById(subtaskId);
        save();
    }

    //------------------------------------

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }
}
