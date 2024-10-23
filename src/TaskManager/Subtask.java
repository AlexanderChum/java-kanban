package TaskManager;

public class Subtask {
    private String name;
    private String description;
    private Condition condition;

    public Subtask(String name, String description, Condition condition) {
        this.name = name;
        this.description = description;
        this.condition = condition;
    }
}
