package main.service;

import main.exceptions.*;
import main.models.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File fileForWork;
    private final String startTimeDoesntExist = "Время начала не задано";
    private final String endTimeDoesntExist = "Время конца не задано";

    public FileBackedTaskManager(File file) {
        this.fileForWork = file;
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
        TypesOfTasks taskType = stringToType(separatedLine[1]);
        String name = separatedLine[2];
        String stringStatus = separatedLine[3];
        String description = separatedLine[4];
        String duration = separatedLine[5];
        String startTime = separatedLine[6];
        String endTime = separatedLine[7];
        if (startTimeDoesntExist.equals(startTime)) startTime = null;
        if (endTimeDoesntExist.equals(endTime)) endTime = null;

        switch (taskType) {
            case TypesOfTasks.TASK:
                Task task = new Task(name, description, duration, startTime, endTime);
                super.createTask(task);
                task.setStatus(stringToStatus(stringStatus));
                return task;
            case TypesOfTasks.EPIC:
                Epic epic = new Epic(name, description);
                super.createEpic(epic);
                epic.setStatus((stringToStatus(stringStatus)));
                return epic;
            case TypesOfTasks.SUBTASK:
                Subtask subtask = new Subtask(name, description, duration, startTime, endTime,
                        Integer.parseInt(separatedLine[8]));
                super.createSubtask(subtask);
                //перепроверить почему при создании сабтаски в этом методе не присваивается id
                subtask.setId(Integer.parseInt(separatedLine[0]));
                subtask.setStatus(stringToStatus(stringStatus));
                super.epicTimeUpdates(subtask.getEpicId());
                return subtask;
            default:
                throw new TypeMismatchException("Такого типа задач нет");
        }
    }

    private Status stringToStatus(String line) {
        return switch (line) {
            case "NEW" -> Status.NEW;
            case "IN_PROGRESS" -> Status.IN_PROGRESS;
            case "DONE" -> Status.DONE;
            default -> throw new TypeMismatchException("Такого типа статусов нет");
        };
    }

    private TypesOfTasks stringToType(String line) {
        return switch (line) {
            case "TASK" -> TypesOfTasks.TASK;
            case "EPIC" -> TypesOfTasks.EPIC;
            case "SUBTASK" -> TypesOfTasks.SUBTASK;
            default -> throw new TypeMismatchException("Такого типа задач нет");
        };
    }

    public void saveToFile() {
        save();
    }

    private void save() {
        try (BufferedWriter bw = Files.newBufferedWriter(fileForWork.toPath(), StandardCharsets.UTF_8)) {
            bw.write("id,type,name,status,description,duration(mins),startTime,endTime,epicId");
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
        String localStartTime;
        String localEndTime;
        if (task.getStatus().equals(Status.NEW)) {
            localStatus = "NEW";
        } else if (task.getStatus().equals(Status.IN_PROGRESS)) {
            localStatus = "IN_PROGRESS";
        } else {
            localStatus = "DONE";
        }
        String localDuration = String.valueOf(task.getDuration());
        if (task.getStartTime() != null) {
            localStartTime = task.getStartTime().format(Task.FORMATTER);
        } else localStartTime = startTimeDoesntExist;
        if (task.getEndTime() != null) {
            localEndTime = task.getEndTime().format(Task.FORMATTER);
        } else localEndTime = endTimeDoesntExist;

        if (super.tasks.containsKey(localId)) {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s", localId, TypesOfTasks.TASK, localName,
                    localStatus, localDescription, localDuration, localStartTime, localEndTime);
        } else if (super.epics.containsKey(localId)) {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s", localId, TypesOfTasks.EPIC, localName,
                    localStatus, localDescription, localDuration, localStartTime, localEndTime);
        } else {
            return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", localId, TypesOfTasks.SUBTASK, localName,
                    localStatus, localDescription, localDuration, localStartTime, localEndTime,
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
