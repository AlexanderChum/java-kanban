package main.service;

import main.models.Task;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> viewedTasks = new LinkedList<>();
    private final static int maxHistorySize = 10;

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(viewedTasks);
    }

    @Override
    public void add(Task task) {
        if (viewedTasks.size() == maxHistorySize) {
            viewedTasks.removeFirst();
            viewedTasks.add(task);
        } else {
            viewedTasks.add(task);
        }
    }
}
