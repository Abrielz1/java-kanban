package tests;

import manager.task.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected InMemoryTaskManager createManager() {
        InMemoryTaskManager.setIdCounter(1);
        return new InMemoryTaskManager();
    }
}
