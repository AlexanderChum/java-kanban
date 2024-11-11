package main.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    Managers mng = new Managers();

    @Test
    void getDefaultTaskManager() {
        TaskManager taskManager = mng.getDefault();
        assertInstanceOf(InMemoryTaskManager.class, taskManager);
    }

    @Test
    void getDefaultHistory() {
        HistoryManager historyManager = mng.getDefaultHistory();
        assertInstanceOf(InMemoryHistoryManager.class, historyManager);
    }
}