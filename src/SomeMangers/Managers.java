package SomeMangers;

public class Managers {

    private static InMemoryHistoryManager InMemoryHistoryManager;
    private static InMemoryTaskManager InMemoryTaskManager;

    public static HistoryManager getDefaultHistory() {
        return new  InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTask() {
        return new  InMemoryTaskManager();
    }
}

