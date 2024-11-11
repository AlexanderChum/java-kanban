package main.service;

import main.models.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> viewedTasks = new LinkedList<>();

    @Override
    public LinkedList<Task> getHistory() {
        return new LinkedList<>(viewedTasks);
    }

    public void add(Task task) {
        int maxListSize = 10; /*один из вариантов размещения переменной макс размера, насколько лучше/хуже такой вариант
        чем размещение как поле класса? В будущем из минусов вижу только то, что данную переменную не применить в других
        методах, какие могут быть еще?*/
        if (viewedTasks.size() == maxListSize) {
            viewedTasks.removeFirst();
            viewedTasks.add(task);
        } else {
            viewedTasks.add(task);
        }
    }
}
