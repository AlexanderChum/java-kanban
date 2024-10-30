package models;

public class Task {
    protected String name;
    protected String description;
    protected Status status;

    public Task() {
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Имя: " + this.name + "\nОписание: " + this.description + "\nСтатус: " + this.status + "\n";
    }
}
