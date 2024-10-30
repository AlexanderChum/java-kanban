package models;

import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic() {
    }

    public Epic(String name, String description){
        super(name, description);
        this.status = Status.NEW;
    }

    public void setStatus (Status status) {
        this.status = status;
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
}
