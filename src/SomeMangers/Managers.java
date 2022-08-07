package SomeMangers;

public class Managers {

    private static InMemoryHistoryManager InMemoryHistoryManager = new InMemoryHistoryManager();
    private static InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

    public static HistoryManager getDefaultHistory() {
        return InMemoryHistoryManager;
    }

    public static TaskManager getDefaultTask() {
        return inMemoryTaskManager;
    }
}

