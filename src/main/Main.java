package main;

import main.models.Epic;
import main.models.Subtask;
import main.models.Task;
import main.service.*;

public class Main {

    public static void main(String[] args) {

        InMemoryTaskManager tMng = (InMemoryTaskManager) Managers.getDefault();
        InMemoryHistoryManager hMng = (InMemoryHistoryManager) tMng.getHistoryManager();

        Task task1 = new Task("Первый таск", "Описание1");
        Task task2 = new Task("Второй таск", "Описание2");
        Epic epic1 = new Epic("Первый эпик", "Описание3");
        Subtask subtask1 = new Subtask("Первый подтаск", "Описание4", 3);
        Subtask subtask2 = new Subtask("Второй подтаск", "Описание5", 3);
        Subtask subtask3 = new Subtask("Третий подтаск", "Описание6", 3);
        Epic epic2 = new Epic("Первый эпик", "Описание7");

        tMng.createTask(task1);
        tMng.createTask(task2);
        tMng.createEpic(epic1);
        tMng.createSubtask(subtask1);
        tMng.createSubtask(subtask2);
        tMng.createSubtask(subtask3);
        tMng.createEpic(epic2);

        tMng.getTaskById(task2.getId());
        tMng.getTaskById(task1.getId());
        tMng.getSubtaskById(subtask1.getId());
        System.out.println(tMng.getHistory());

        tMng.getTaskById(task2.getId());
        System.out.println(tMng.getHistory());

        hMng.remove(task1.getId());
        System.out.println(tMng.getHistory());

        tMng.getEpicById(epic1.getId());
        tMng.getSubtaskById(subtask2.getId());
        System.out.println(tMng.getHistory());

        tMng.deleteAllEpics();
        System.out.println(tMng.getHistory());
    }
}
