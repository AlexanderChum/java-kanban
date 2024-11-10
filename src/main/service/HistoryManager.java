package main.service;

import main.models.Task;

import java.util.ArrayList;

public interface HistoryManager {

    ArrayList<Task> getHistory();

    void addTaskToViewed(Task task);
}
