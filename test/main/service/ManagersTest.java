package main.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    Managers mng = new Managers();

    @Test
    void getDefaultTaskManager() {
        TaskManager taskManager = mng.getDefaultTaskManager();
        assertTrue(taskManager instanceof InMemoryTaskManager);
    }

    @Test
    void getDefaultHistory() {
        HistoryManager historyManager = mng.getDefaultHistory();
        assertTrue(historyManager instanceof InMemoryHistoryManager);
    }
}