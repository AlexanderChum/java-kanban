package Main.service;

import Main.models.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> viewedTasks = new ArrayList<>();

    @Override
    public ArrayList<Task> getHistory() {
        return viewedTasks;
    }

    public void addTaskToViewed(Task task) {
        if (viewedTasks.size() == 10) {
            viewedTasks.removeFirst();
            viewedTasks.add(task);
        } else {
            viewedTasks.add(task);
        }
    }
}
