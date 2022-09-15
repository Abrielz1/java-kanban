package manager.task;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.Managers;
import manager.history.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    private static int idCounter = 1;
    public HistoryManager historyManager = Managers.getDefaultHistory();
    public Map<Integer, Task> taskArray = new HashMap<>();
    public Map<Integer, Epic> epicHash = new HashMap<>();
    public Map<Integer, SubTask> subEpicHash = new HashMap<>();


    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        InMemoryTaskManager.idCounter = idCounter;
    }

    @Override
    public Map<Integer, Task> getTasks() {
        return taskArray;
    }

    @Override
    public Map<Integer, Epic> getEpics() {
        return epicHash;
    }

    @Override
    public Map<Integer, SubTask> getSubtasks() {
        return subEpicHash;
    }

    public Task getTaskById(int id) {
        historyManager.addHistory(taskArray.get(id));
        return taskArray.get(id);
    }

    public Epic getEpicById(int id) {
        historyManager.addHistory(epicHash.get(id));
        return epicHash.get(id);
    }

    public SubTask getSubtaskById(int id) {
        historyManager.addHistory(subEpicHash.get(id));
        return subEpicHash.get(id);
    }

    @Override
    public int add(Task task) {
        task.setId(idCounter++);
        taskArray.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int add(Epic epic) {
        epic.setId(idCounter++);
        epicHash.put(epic.getId(), epic);
        for (SubTask subtask : subEpicHash.values()) {
            if (epic.getId() == subtask.getEpicId()) {
                epic.getSubtaskId().add(subtask.getId());
            }
        }
        return epic.getId();
    }

    @Override
    public int add(SubTask subtask) {
        subtask.setId(idCounter++);
        subEpicHash.put(subtask.getId(), subtask);
        for (Epic epic : epicHash.values()) {
            if (epic.getId() == subtask.getEpicId()) {
                epic.getSubtaskId().add(subtask.getId());
            }
        }
        if (epicHash.containsKey(subtask.getEpicId())) {
            update(epicHash.get(subtask.getEpicId()));
        }
        return subtask.getId();
    }

    @Override
    public void update(Task task) {
        taskArray.put(task.getId(), task);
    }

    @Override
    public void update(Epic epic) {
        epic.setStatus(TaskStatus.NEW);
        epicHash.put(epic.getId(), epic);
        List<Integer> epicList = epic.getSubtaskId();
        if (epic.getSubtaskId().isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        }
        for (int id : epicList) {
            if (subEpicHash.get(id).getStatus() == TaskStatus.DONE) {
                epic.setStatus(TaskStatus.DONE);
                break;
            } else if (subEpicHash.get(id).getStatus() == TaskStatus.IN_PROGRESS) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            } else if (epic.getStatus() == TaskStatus.DONE && subEpicHash.get(id).getStatus() == TaskStatus.NEW) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }

    @Override
    public void update(SubTask subtask) {
        subEpicHash.put(subtask.getId(), subtask);
        update(epicHash.get(subtask.getEpicId()));

    }

    @Override
    public void removeAllTasks() {
        taskArray.clear();
    }

    @Override
    public void removeAllEpicsAndSubtasks() {
        epicHash.clear();
        subEpicHash.clear();
    }

    @Override
    public void removeTaskById(int id) {
        historyManager.remove(id);
        taskArray.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        List<Integer> subtasksInEpic = epicHash.get(id).getSubtaskId();
        for (int subtaskId : subtasksInEpic) {
            subEpicHash.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
        historyManager.remove(id);
        epicHash.remove(id);
    }

    @Override
    public void removeSubtaskById(Integer id) {
        int epicID = subEpicHash.get(id).getEpicId();
        epicHash.get(epicID).getSubtaskId().remove(id);
        update(epicHash.get(epicID));
        historyManager.remove(subEpicHash.get(id).getId());
        subEpicHash.remove(id);
    }

    @Override
    public List<SubTask> getAllSubtasksByEpic(int id) {
        List<Integer> numbers = epicHash.get(id).getSubtaskId();
        List<SubTask> subtaskArrayList = new ArrayList<>();
        for (int item : numbers) {
            subtaskArrayList.add(subEpicHash.get(item));
        }
        return subtaskArrayList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "taskArray=" + taskArray +
                ", epicHash=" + epicHash +
                ", subEpicHash=" + subEpicHash +
                '}';
    }
}
