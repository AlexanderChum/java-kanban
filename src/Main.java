import models.Epic;
import models.Status;
import models.Subtask;
import models.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task = new Task();
        Epic epic = new Epic();
        Subtask subtask = new Subtask();

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

        taskManager.printAllTasks();
        taskManager.printAllEpics();
        taskManager.printSubtasksByEpicID(3);


        Subtask subtask3 = new Subtask("Первый подтаск", "Описание6", Status.DONE, 3);

        taskManager.updateSubtask(4, subtask3);

        taskManager.printAllSubtasks();
        taskManager.printEpicById(3);

    }
}
