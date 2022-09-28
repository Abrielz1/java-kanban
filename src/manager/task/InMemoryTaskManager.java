package manager.task;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.Managers;
import manager.history.HistoryManager;
import manager.history.ManagerSaveException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected Set<Task> prioritizedTasks = new TreeSet<>((o1, o2) -> {
        if (o1.getStartTime() == null) return 1;
        if (o2.getStartTime() == null) return -1;
        if (o1.getStartTime().isAfter(o2.getStartTime())) return 1;
        if (o1.getStartTime().isBefore(o2.getStartTime())) return -1;
        if (o1.getStartTime().isEqual(o2.getStartTime())) return o1.getId() - o2.getId();
        return 0;
    });

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
        getTaskEndTime(task);
        prioritizedTasks.add(task);
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
        getSubtaskEndTime(subtask);
        prioritizedTasks.add(subtask);
        subEpicHash.put(subtask.getId(), subtask);
        for (Epic epic : epicHash.values()) {
            if (epic.getId() == subtask.getEpicId()) {
                epic.getSubtaskId().add(subtask.getId());
            }
        }
        if (epicHash.containsKey(subtask.getEpicId())) {
            update(epicHash.get(subtask.getEpicId()));
            getEpicTimesAndDuration(epicHash.get(subtask.getEpicId()));
        }
        return subtask.getId();
    }

    @Override
    public void update(Task task) {
        taskArray.put(task.getId(), task);
    }

    @Override
    public int update(Epic epic) {
        epic.setStatus(TaskStatus.NEW);
        epicHash.put(epic.getId(), epic);
        List<Integer> epicList = epic.getSubtaskId();
        if (epic.getSubtaskId().isEmpty()) {
            return epic.getId();
        }
        TaskStatus status = TaskStatus.NEW;
        for (int id : epicList) {
            if (subEpicHash.get(id).getStatus() == TaskStatus.DONE) {
                status = status.DONE;
            } else {
                status = TaskStatus.NEW;
                break;
            }
        }
        if (status == TaskStatus.DONE) {
            epic.setStatus(status);
            return epic.getId();
        }
        for (int id : epicList) {
            if (subEpicHash.get(id).getStatus() == TaskStatus.NEW) {
                status = status.NEW;
            } else {
                status = TaskStatus.IN_PROGRESS;
                break;
            }
        }
        epic.setStatus(status);
        return epic.getId();
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
            historyManager.remove(subtaskId);
            subEpicHash.remove(subtaskId);
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
    public void intersectionCheck() {
        LocalDateTime checkTime = null;
        boolean flagCheckTimeIsEmpty = true;
        for (Task task : prioritizedTasks) {
            if (flagCheckTimeIsEmpty) {
                checkTime = task.getEndTime();
                flagCheckTimeIsEmpty = false;
            } else if (task.getStartTime() != null) {
                if (task.getStartTime().isBefore(checkTime)) {
                    throw new ManagerSaveException("Найдено пересечение времени задач, проверьте корректность данных");
                }
                if (task.getStartTime().isAfter(checkTime) || task.getStartTime().isEqual(checkTime)) {
                    checkTime = task.getEndTime();
                }
            }
        }
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        intersectionCheck();
        return prioritizedTasks;
    }

    @Override
    public void getTaskEndTime(Task task) {
        if (task.getStartTime() == null || task.getDuration() == null) return;
        LocalDateTime endTime = task.getStartTime().plus(task.getDuration());
        task.setEndTime(endTime);
    }

    @Override
    public void getEpicTimesAndDuration(Epic epic) {
        if (epic.getSubtaskId().isEmpty()) {
            return;
        }
        LocalDateTime start;
        LocalDateTime end;
        start = subEpicHash.get(epic.getSubtaskId().get(0)).getStartTime();
        end = subEpicHash.get(epic.getSubtaskId().get(0)).getEndTime();
        epic.setStartTime(start);
        epic.setEndTime(end);
        for (Integer id : epic.getSubtaskId()) {
            if (subEpicHash.get(id).getStartTime() != null && subEpicHash.get(id).getStartTime().isBefore(start)) {
                start = subEpicHash.get(id).getStartTime();
            }
            if (subEpicHash.get(id).getStartTime() != null && subEpicHash.get(id).getEndTime().isAfter(end)) {
                end = subEpicHash.get(id).getEndTime();
            }
        }
        epic.setStartTime(start);
        epic.setEndTime(end);
        epic.setDuration(Duration.between(epic.getStartTime(), epic.getEndTime()));
    }

    @Override
    public void getSubtaskEndTime(SubTask subtask) {
        if (subtask.getStartTime() == null || subtask.getDuration() == null) return;
        LocalDateTime endTime = subtask.getStartTime().plus(subtask.getDuration());
        subtask.setEndTime(endTime);
        if (epicHash.containsKey(subtask.getEpicId())) {
            getEpicTimesAndDuration(epicHash.get(subtask.getEpicId()));
        }
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
