package main.service;

import main.models.Task;

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
        List<Task> resultList = new ArrayList<>();
        Node node = head;
        while (node.getNext() != null) {
            resultList.add((Task) node.getTask());
            node = node.getNext();
        }
        resultList.add((Task) node.getTask());
        return resultList;
    }

    @Override
    public void add(Task task) {
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
        viewedTasks.put(task.getId(), newNode);
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

    //------------------------------------------------------

    private class Node {
        public Task task;
        public Node next;
        public Node prev;

        public Node(Task task) {
            this.task = task;
            this.next = null;
            this.prev = null;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }
    }
}