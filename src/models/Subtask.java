package models;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask() {
    }

    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.status = Status.NEW;
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Status status, Integer epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Status getStatus() {
        return this.status;
    }

    public int getEpicId() {
        return this.epicId;
    }
}
