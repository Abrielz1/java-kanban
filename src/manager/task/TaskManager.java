package manager.task;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface TaskManager {

    public Map<Integer, Task> getTasks();
    public Map<Integer, Epic> getEpics();
    public Map<Integer, SubTask> getSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubtaskById(int id);

    int add(Task task);

    int add(Epic epic);

    int add(SubTask subtask);

    void update(Task task);

    int update(Epic epic);

    void update(SubTask subtask);

    void removeAllTasks();

    void removeAllEpicsAndSubtasks();

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubtaskById(Integer id);

    List<SubTask> getAllSubtasksByEpic(int id);

    List<Task> getHistory();

    void intersectionCheck();

    Set<Task> getPrioritizedTasks();

    void getTaskEndTime(Task task);

    void getEpicTimesAndDuration(Epic epic);

    void getSubtaskEndTime(SubTask subtask);

}
