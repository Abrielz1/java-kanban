package manager.history;

import constructor.Task;

import java.util.List;

public interface HistoryManager {

    void addHistory(Task task);

    void remove(int id);

    void clear();

    List<Task> getHistory();
}
