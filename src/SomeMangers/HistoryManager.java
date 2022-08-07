package SomeMangers;

import Constructors.Task;

import java.util.ArrayList;

public interface HistoryManager {

    ArrayList<Task> history();   //List<Constructors.Task> getHistory();

    void add(Task task);

}
