package main.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    Epic testEpic = new Epic(1, "Эпик", "Описание");
    Subtask subtask = new Subtask(2 ,"Сабтаск", "Описание", Status.NEW, 1);
    Subtask subtask2 = new Subtask(3, "Сабтаск2", "Описание2", Status.NEW, 1);
    ArrayList<Integer> listOfSubtasks = new ArrayList<>();
    int testSubtaskId;
    Subtask subtaskForId = new Subtask(4, "Сабтаск3", "Описание3", Status.NEW, 1);

    @BeforeEach
    void setTest() {
        testSubtaskId = subtaskForId.getEpicId();
        listOfSubtasks.add(subtask.getId());
        listOfSubtasks.add(subtask2.getId());
    }

    @Test
    void getSubtasksId() {
        ArrayList<Integer> listForTest = new ArrayList<>();
        listForTest.add(2);
        listForTest.add(3);
        assertEquals(listForTest, listOfSubtasks);
    }

    @Test
    void addSubtaskId() {
        listOfSubtasks.add(testSubtaskId);
        assertEquals(3, listOfSubtasks.size());
    }

    @Test
    void removeSubtaskId() {
        listOfSubtasks.add(testSubtaskId);
        listOfSubtasks.removeLast();
        ArrayList<Integer> listForTest = new ArrayList<>();
        listForTest.add(2);
        listForTest.add(3);
        assertEquals(listForTest, listOfSubtasks);
    }

    @Test
    void clearSubtasksList() {
        listOfSubtasks.clear();
        assertTrue(listOfSubtasks.isEmpty());
    }
}