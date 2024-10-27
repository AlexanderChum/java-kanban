package TaskManager;

import java.util.ArrayList;

public class Task {
    private String name;
    private String description;
    private Condition condition;

    //-----------------------------------Constructors------------------------------------------------------------------

    public Task() {

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

    public String getDescription() {
        return description;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
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
}
