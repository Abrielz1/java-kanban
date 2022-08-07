package SomeMangers;

import Constructors.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Task> history = new ArrayList<>();

    private final int MAX_LENGHT = 10;

    @Override
    public void add(Task task) {
        int count = 0;
        if (history.size() == 10) {
            history.remove(0);
            count++;
        history.add(0,task);
        } else {
            history.add(task);
            count++;
        }
        if (count == 10){
            for (Task counter : history) {
                System.out.println(counter.getDescription());
            }
        }
    }

    @Override
    public ArrayList<Task> history() {
        return history; //historyViews
    }
}
