package main.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    Subtask subtask = new Subtask("Сабтаск", "Описание", 1);

    @Test
    void getEpicId() {
        assertEquals(1, subtask.getEpicId());
    }

    @Test
    void setEpicId() {
        subtask.setEpicId(2);
        assertEquals(2, subtask.getEpicId());
    }
}