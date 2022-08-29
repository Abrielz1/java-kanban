package manager.task;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import java.util.List;


public interface TaskManager {

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubtaskById(int id);

    int add(Task task);

    int add(Epic epic);

    int add(SubTask subtask);

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
