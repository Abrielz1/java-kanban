package SomeMangers;

import Constructors.Epic;
import Constructors.SubTask;
import Constructors.Task;

import java.util.ArrayList;
import java.util.HashMap;

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

    ArrayList<Epic> getEpicHashValues();

    ArrayList<Task> getValuesSubTask();

    ArrayList<Task> getTaskValues();

    void add(Task task);

    void addEpicTask(Epic epic);

    void addSubEpicTask(SubTask subtask);

    void update(Task task);

    void updateEpic(Epic epic);

    void johnTheRipper(Epic epic);

    void updateSubEpic(SubTask subtask);

    ArrayList<SubTask> getAllSubtasksFromEpic(int id);

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
