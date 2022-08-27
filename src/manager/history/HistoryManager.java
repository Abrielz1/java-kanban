package manager.history;

import constructor.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    public void remove(int id );

    void addHistory(Task task);
}
