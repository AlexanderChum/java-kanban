package main.models;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(Integer id, String name, String description, Status status, Integer epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer duration, Integer epicId) {
        super(name, description, duration);
        this.setStartTime(null);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer duration, String startTime, Integer epicId) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, String duration, String startTime, String endTime, Integer epicId) {
        super(name, description, duration, startTime, endTime);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return this.epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", epicId='" + getEpicId() + '\'' +
                ", startTime='" + getStartTime() + '\'' +
                ", endTime=" + getEndTime() +
                '}';
    }

}
