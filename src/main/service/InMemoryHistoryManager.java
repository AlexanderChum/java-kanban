package main.service;

import main.models.Task;
import main.models.Node;

import java.util.List;
import java.util.ArrayList;

import java.util.Map;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> viewedTasks = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }

    @Override
    public void add(Task task) {
        viewedTasks.put(task.getId(), linkLast(task));
    }

    @Override
    public void remove(int id) {
        Node node = viewedTasks.get(id);
        if (node != null) {
            viewedTasks.remove(id);
            Node prev = node.getPrev();
            Node next = node.getNext();
            if (prev != null) {
                prev.setNext(next);
            }
            if (next != null) {
                next.setPrev(prev);
            }
            if (head == node) {
                head = node.getNext();
            }
            if (tail == node) {
                tail = node.getPrev();
            }
        }
    }

    private Node linkLast(Task task) {
        Node newNode = new Node(task);
        if (viewedTasks.containsKey(task.getId())) {
            remove(task.getId());
        }
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        }
        return newNode;
    }

    private List<Task> getTasks() {
        List<Task> resultList = new ArrayList<>();
        Node node = head;
        while (node.getNext() != null) {
            resultList.add(node.getTask());
            node = node.getNext();
        }
        resultList.add(node.getTask());
        return resultList;
    }
}