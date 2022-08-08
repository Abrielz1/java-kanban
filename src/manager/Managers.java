package manager;

public class Managers {
    private final HistoryManager historyManager = Managers.getDefaultHistory();


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTask() {
        return new InMemoryTaskManager();
    }
}

