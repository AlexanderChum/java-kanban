package TaskManager;

import java.util.ArrayList;
import java.util.Scanner;

public class Epic {
    private String name;
    private String description;
    private Condition condition;
    private ArrayList<Task> subtasks = null;
    Scanner scanner;
    Task task = new Task(scanner);

    //-----------------------------------Constructors------------------------------------------------------------------

    public Epic(Scanner scanner) {
        this.scanner = scanner;
    }

    public Epic(String name, String description, Condition condition) {
        this.name = name;
        this.description = description;
        this.condition = condition;
    }

    public Epic (String name, String description, Condition condition, ArrayList<Task> subtasks) {
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.subtasks = subtasks;
    }

    //-------------------------------------Getters and Setters---------------------------------------------------------

    public String getName() {
        return name;
    }

    public ArrayList<Task> getSubtask(Epic epic) {
        return epic.subtasks;
    }

    public void setCondition(Epic epic) {
        subtasks = getSubtask(epic);
        int progressCount = 0, doneCount = 0;
        for (Task subtask : subtasks) {
            if (task.getCondition(subtask).equals(Condition.IN_PROGRESS)) {
                progressCount++;
            }
            if (task.getCondition(subtask).equals((Condition.DONE))) {
                doneCount++;
            }
            if (progressCount == 0 && doneCount == 0) {
                this.condition = Condition.NEW;
            } else if (progressCount > 0 || doneCount >0) {
                this.condition = Condition.IN_PROGRESS;
                if (doneCount == subtasks.size()) {
                    this.condition = Condition.DONE;
                }
            }
        }
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
        task.printListOfTasks(getSubtask(epic));
    }

    //---------------------------------------

    public void deleteListOfEpics(ArrayList<Epic> epics) {
        epics.clear();
    }

    public void deleteListOfSubtasks(Epic epic) {
        task.deleteListOfTasks(getSubtask(epic));
    }

    //------------------------------------------

    public void getSubtaskById (Epic epic) {
        System.out.println("Введите id подзадачи который хотите получить");
        int subtaskId = scanner.nextInt();
        System.out.println(getSubtask(epic).get(subtaskId - 1));
    }

    //------------------------------------------

    public Epic createEpic() {
        System.out.println("Название:");
        String name = scanner.nextLine();
        System.out.println("Описание");
        String description = scanner.nextLine();
        System.out.println("Статус эпика");
        condition = Condition.NEW;
        return new Epic(name, description, condition);
    }

    public void addSubtask(Epic epic) {
        getSubtask(epic).add(task.createTask());
        setCondition(epic);
    }

    //-----------------------------------------------

    public Epic updateEpic(Epic epic) {
        System.out.println("Введите новое название:");
        this.name = scanner.nextLine();
        System.out.println("Введите новое описание");
        this.description = scanner.nextLine();
        System.out.println("Смена статуса вручную для эпика не предусмотрена");
        Epic newEpic = new Epic(name, description, Condition.NEW, getSubtask(epic));
        setCondition(newEpic);
        return newEpic;
    }

    public void updateSubtask(Epic epic) {
        System.out.println("Введите id подзадачи которую желаете обновить:");
        int subtaskId = scanner.nextInt();
        getSubtask(epic).set(subtaskId - 1, task.updateTask());
        setCondition(epic);
    }

    //-----------------------------------------------

    public void deleteById(Epic epic) {
        System.out.println("Введите id подзадачи для удаления");
        int subTaskId = scanner.nextInt();
        getSubtask(epic).remove(subTaskId - 1);
        setCondition(epic);
    }
}

