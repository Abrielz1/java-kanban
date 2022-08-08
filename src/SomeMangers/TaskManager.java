package SomeMangers;

import Constructors.Epic;
import Constructors.SubTask;
import Constructors.Task;

import java.util.HashMap;
import java.util.List;

public interface TaskManager {

    int id = 0;

    HashMap<Integer, Epic> epicHash = new HashMap<Integer, Epic>();
    HashMap<Integer, SubTask> subEpicHash = new HashMap<Integer, SubTask>();
    HashMap<Integer, Task> taskArray = new HashMap<Integer, Task>();

    HashMap<Integer, Epic> getEpicHash();

    HashMap<Integer, SubTask> getSubEpicHash();

    HashMap<Integer, Task> getTaskArray();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubtaskById();

    List<Epic> getEpicHashValues();

    List<Task> getValuesSubTask();

    List<Task> getTaskValues();

    void add(Task task);

    void addEpicTask(Epic epic);

    void addSubEpicTask(SubTask subtask);

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

    Task getTask(int id);

    SubTask getSubTask(int id);

    Epic getEpic(int id);

    String toString();



}
