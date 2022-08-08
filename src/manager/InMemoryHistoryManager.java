package manager;

import constructor.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {

        if (history.size() == 10) {
            history.remove(0);

            history.add(0, task);
        } else {
            history.add(task);
        }
    }


    @Override
    public void printAll() {
        for (Task counter : history) {
            System.out.println(counter.getDescription());
        }
    }

    @Override
    public List<Task> history() {
        return history;
    }

    @Override
    public HistoryManager getHistory() {
        return (HistoryManager) history;
    }

}
