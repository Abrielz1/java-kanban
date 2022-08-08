package SomeMangers;

import Constructors.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> history();

    Task getHistory();

    void add(Task task);

    void  printAll();

}
