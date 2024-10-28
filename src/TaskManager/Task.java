package TaskManager;

import java.util.ArrayList;
import java.util.Scanner;

public class Task {
    private String name;
    private String description;
    private Condition condition;
    Scanner scanner;

    //-----------------------------------Constructors------------------------------------------------------------------

    public Task(Scanner scanner) {
        this.scanner = scanner;
    }

    public Task(String name, String description, Condition condition) {
        this.name = name;
        this.description = description;
        this.condition = condition;
    }

    //-------------------------------------Getters and Setters---------------------------------------------------------

    public String getName() {
        return name;
    }

    public Condition getCondition(Task task) {
        return task.condition;
    }

    //--------------------------------------Class Methods--------------------------------------------------------------

    public void printListOfTasks(ArrayList<Task> tasks) {
        int index = 0;
        for (Task task : tasks) {
            index++;
            System.out.println(index + ". " + task.getName());
        }
    }

    public void deleteListOfTasks(ArrayList<Task> tasks) {
        tasks.clear();
    }

    public Task createTask() {
        System.out.println("Название:");
        String name = scanner.nextLine();
        System.out.println("Описание");
        String description = scanner.nextLine();
        System.out.println("Статус задачи");
        do {
            String conditionType = scanner.next();
            switch (conditionType) {
                case "NEW":
                    condition = Condition.NEW;
                    break;
                case "IN_PROGRESS":
                    condition = Condition.IN_PROGRESS;
                    break;
                case "DONE":
                    condition = Condition.DONE;
                    break;
                default:
                    System.out.println("Введен неверный статус");
                    break;
            }
        } while (condition == null);
        return new Task(name, description, condition);
    }

    public Task updateTask() {
        System.out.println("Введите новое название:");
        String name = scanner.nextLine();
        System.out.println("Введите новое описание");
        String description = scanner.nextLine();
        System.out.println("Введите новый статус задачи");
        do {
            String conditionType = scanner.next();
            switch (conditionType) {
                case "NEW":
                    condition = Condition.NEW;
                    break;
                case "IN_PROGRESS":
                    condition = Condition.IN_PROGRESS;
                    break;
                case "DONE":
                    condition = Condition.DONE;
                    break;
                default:
                    System.out.println("Введен неверный статус");
                    break;
            }
        } while (condition == null);
        return new Task(name, description, condition);
    }
}
