package manager.history;

import constructor.Task;

import java.util.List;

public interface HistoryManager {

    void addHistory(Task task);

    void remove(int id);

    List<Task> getHistory();

    static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();

        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }

        return sb.toString();
    }
}
