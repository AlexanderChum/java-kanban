package main.models;

import java.util.ArrayList;

public class Epic extends Task{
    private final ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        this.setStatus(Status.NEW);
    }

    public Epic(Integer id, String name, String description) {
        super(name, description);
        this.setId(id);
    }

    public ArrayList<Integer> getSubtasksId() {
        return this.subtasksId;
    }

    public void addSubtaskId (int subtaskId) {
        subtasksId.add(subtaskId);
    }

    public void removeSubtaskId (Integer subtaskId) {
        subtasksId.remove(subtaskId);
    }

    public void clearSubtasksList () {
        subtasksId.clear();
    }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", id='" + getId() + '\'' +
                ", subtasksId=" + getSubtasksId() +
                '}';
    }
}
