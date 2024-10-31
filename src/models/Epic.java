package models;

import java.util.ArrayList;

public class Epic extends Task{
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description){
        super(name, description);
        this.setStatus(Status.NEW);
    }

    public ArrayList<Integer> getSubtasksId() {
        return this.subtasksId;
    }

    public void setSubtasksId(ArrayList<Integer> subtasksId) {
        this.subtasksId = subtasksId;
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
}
