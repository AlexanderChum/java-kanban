package main;

import main.models.Epic;
import main.models.Status;
import main.models.Subtask;
import main.models.Task;
import main.service.*;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager tMng = (InMemoryTaskManager) Managers.getDefaultTaskManager();

        Task task1 = new Task("Первый таск", "Описание1");
        Task task2 = new Task("Второй таск","Описание2");
        Epic epic1 = new Epic("Первый эпик","Описание3");
        Subtask subtask1 = new Subtask("Первый подтаск", "Описание4", 3);
        Subtask subtask2 = new Subtask("Второй подтаск", "Описание5", 3);
        Subtask subtask3 = new Subtask(4,"Первый подтаск", "Описание6", Status.DONE, 3);
        Epic epic2 = new Epic(3,"Первый эпик","Описание7");
        Subtask subtask4 = new Subtask(5, "Второй подтаск", "Описание8", Status.DONE, 3);

        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        tMng.updateSubtask(subtask3);
        System.out.println(epic1);
        tMng.deleteSubtaskById(4);
        System.out.println(epic1);



        tMng.getEpicById(3);
        tMng.getTaskById(1);
        tMng.getTaskById(2);
        tMng.getTaskById(1);
        tMng.getSubtaskById(4);

        System.out.println(tMng.getHistory());
    }
}
