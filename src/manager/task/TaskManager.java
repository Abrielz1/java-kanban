package manager.task;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import manager.history.HistoryManager;

import java.util.List;
import java.util.Map;

public interface TaskManager {

    Task getTaskById(int id);
    Epic getEpicById(int id);
    SubTask getSubtaskById(int id);

    void add(Task task);
    void add(Epic epic);
    void add(SubTask subtask);
    void update(Task task);
    void update(Epic epic);
    void update(SubTask subtask);

    void removeAllTasks();
    void removeAllEpicsAndSubtasks();
    void removeTaskById(int id);
    void removeEpicById(int id);
    void removeSubtaskById(Integer id);

    List<SubTask> getAllSubtasksByEpic(int id);

    List<Task> getHistory();

}
