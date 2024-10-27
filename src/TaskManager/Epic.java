package TaskManager;

import java.util.ArrayList;
import java.util.Scanner;

public class Epic {
    private String name;
    private String description;
    private Condition condition;
    private ArrayList<Task> subtasks;
    Scanner scanner;
    Task task = new Task();

    //-----------------------------------Constructors------------------------------------------------------------------

    public Epic(Scanner scanner) {
        this.scanner = scanner;
    }

    public Epic(String name, String description, Condition condition) {
        this.name = name;
        this.description = description;
        this.condition = condition;
    }

    //-------------------------------------Getters and Setters---------------------------------------------------------

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Condition getCondition() {
        return condition;
    }

    public ArrayList<Task> getSubtask() {
        return subtasks;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    //--------------------------------------Class Methods--------------------------------------------------------------

    public void printListOfEpics(ArrayList<Epic> epics) {
        int index = 0;
        for (Epic epic : epics) {
            index++;
            System.out.println(index + ". " + epic.getName());
        }
    }

    public void printListOfSubtasks(Epic epic) {
        task.printListOfTasks(subtasks);
    }

    public void addSubtask(Epic epic) {
        System.out.println("Название:");
        String name = scanner.nextLine();
        System.out.println("Описание");
        String description = scanner.nextLine();
        System.out.println("Статус эпика");
        while (true) {
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
        }
        subtasks.add(new Subtask(name, description, condition));
    }

    public void deleteListOfEpics(ArrayList<Epic> epics) {
        epics.clear();
    }

    public void deleteListOfSubtasks(Epic epic) {
        task.deleteListOfTasks(getSubtask());
    }

    public void getSubtaskById (Epic epic) {
        System.out.println("Введите id подзадачи который хотите получить");
        int subtaskId = scanner.nextInt();
        System.out.println(subtasks.get(subtaskId - 1));
    }
}

