package manager;

import constructor.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> history();

    HistoryManager getHistory();

    void addHistory(Task task);

    void printAll();

}
