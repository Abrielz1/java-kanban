package manager;

import constructor.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> history();

    HistoryManager getHistory();

    void add(Task task);

    void printAll();

}
