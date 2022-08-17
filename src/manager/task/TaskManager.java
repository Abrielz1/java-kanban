package manager.task;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import manager.history.HistoryManager;

import java.util.List;
import java.util.Map;

public interface TaskManager {
    Map<Integer, Epic> getEpicHash();

    Map<Integer, SubTask> getSubEpicHash();

    Map<Integer, Task> getTaskArray();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubtaskById();

    List<Epic> getEpicHashValues();

    List<Task> getValuesSubTask();

    List<Task> getTaskValues();

    void add(Task task);

    void add(Epic epic);

    void add(SubTask subtask);

    void update(Task task);

    void update(Epic epic);

    void updateAllStatuses(Epic epic);

    void update(SubTask subtask);

    List<SubTask> getAllSubtasksFromEpic(int id);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubTask(int id, SubTask subtask);

    void purgeTask();

    void purgeEpic();

    void purgeAllTask();

    List<Task> getHistory();

    HistoryManager getHistoryManager();
}
