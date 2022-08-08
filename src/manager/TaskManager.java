package manager;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    HashMap<Integer, Epic> epicHash = new HashMap<Integer, Epic>();
    HashMap<Integer, SubTask> subEpicHash = new HashMap<Integer, SubTask>();
    HashMap<Integer, Task> taskArray = new HashMap<Integer, Task>();

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

    void updateEpic(Epic epic);

    void updateAllStatuses(Epic epic);

    void updateSubEpic(SubTask subtask);

    List<SubTask> getAllSubtasksFromEpic(int id);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubTask(int id, SubTask subtask);

    void purgeTask();

    void purgeEpic();

    void purgeAllTask();

    HistoryManager getHistoryManager();

}
