package manager;

public class Managers {
    private final HistoryManager historyManager;

    Managers() {
        historyManager = Managers.getDefaultHistory();
    }

    public void add() {
        getDefaultHistory();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTask() {
        return new InMemoryTaskManager();
    }
}

