package main.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class TaskTest {

    Task taskTest = new Task(1,"Таск", "Описание", Status.NEW);

    @Test
    void getName() {
        String name = "Таск";
        assertEquals(name, taskTest.getName());
    }

    @Test
    void setName() {
        String name = "ТаскТест";
        taskTest.setName(name);
        assertEquals(name, taskTest.getName());
    }

    @Test
    void getDescription() {
        String description = "Описание";
        assertEquals(description, taskTest.getDescription());
    }

    @Test
    void setDescription() {
        String description = "ОписаниеТест";
        taskTest.setDescription(description);
        assertEquals(description, taskTest.getDescription());
    }

    @Test
    void getStatus() {
        Status status = Status.NEW;
        assertEquals(status, taskTest.getStatus());
    }

    @Test
    void setStatus() {
        Status status = Status.IN_PROGRESS;
        taskTest.setStatus(status);
        assertEquals(status, taskTest.getStatus());
    }

    @Test
    void getId() {
        Integer id = 1;
        assertEquals(id, taskTest.getId());
    }

    @Test
    void setId() {
        Integer id = 5;
        taskTest.setId(id);
        assertEquals(id, taskTest.getId());
    }
}