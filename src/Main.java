import models.Epic;
import models.Status;
import models.Subtask;
import models.Task;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Первый таск", "Описание1");
        Task task2 = new Task("Второй таск","Описание2");
        Epic epic1 = new Epic("Первый эпик","Описание3");
        Subtask subtask1 = new Subtask("Первый подтаск", "Описание4", 3);
        Subtask subtask2 = new Subtask("Второй подтаск", "Описание5", 3);

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        System.out.println("-----------");

        Subtask subtask3 = new Subtask(4,"Первый подтаск", "Описание6", Status.DONE, 3);
        Epic epic2 = new Epic(3,"Первый эпик","Описание7");
        Subtask subtask4 = new Subtask(5, "Второй подтаск", "Описание8", Status.DONE, 3);

        taskManager.updateSubtask(subtask3);
        System.out.println(taskManager.getSubtaskById(4));
        System.out.println(taskManager.getEpicById(3));

        System.out.println("-----------");

        taskManager.updateEpic(epic2);
        System.out.println(taskManager.getAllEpics());
        taskManager.updateSubtask(subtask4);
        System.out.println(taskManager.getSubtasksByEpicID(3));
        System.out.println(taskManager.getSubtaskById(4));
        System.out.println(taskManager.getAllEpics());

        taskManager.deleteEpicById(3);
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getAllEpics());

        taskManager.deleteTaskById(1);
        System.out.println(taskManager.getAllTasks());
    }
}
