package TaskManager;

public class Task {
    private String name;
    private String description;
    private Condition condition;

    public Task() {

    }

    public Task(String name, String description, Condition condition) {
        this.name = name;
        this.description = description;
        this.condition = condition;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Condition getCondition() {
        return condition;
    }
}
