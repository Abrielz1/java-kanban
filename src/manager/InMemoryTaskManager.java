package manager;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    private int id = 0;

    private HashMap<Integer, Epic> epicHash = new HashMap<Integer, Epic>();
    private HashMap<Integer, SubTask> subEpicHash = new HashMap<Integer, SubTask>();
    private HashMap<Integer, Task> taskArray = new HashMap<Integer, Task>();

    public HashMap<Integer, Epic> getEpicHash() {
        return epicHash;
    }

    public HashMap<Integer, SubTask> getSubEpicHash() {
        return subEpicHash;
    }

    public HashMap<Integer, Task> getTaskArray() {
        return taskArray;
    }

    public TaskManager getTaskById(int id) {
        return (TaskManager) taskArray.get(id);
    }

    public TaskManager getEpicById(int id) {
        return (TaskManager) epicHash.get(id);
    }

    public TaskManager getSubtaskById() {
        return (TaskManager) subEpicHash.get(id);
    }


    public List<Epic> getEpicHashValues() {
        return new ArrayList<>(epicHash.values());
    }

    public List<Task> getValuesSubTask() {
        return new ArrayList<>(subEpicHash.values());
    }

    public List<Task> getTaskValues() {
        return new ArrayList<>(taskArray.values());
    }

    @Override
    public void add(Task task) {
        task.setId(id++);
        taskArray.put(task.getId(), task);
    }

    @Override
    public void addEpicTask(Epic epic) {
        epic.setId(id++);
        epicHash.put(epic.getId(), epic);
    }

    @Override
    public void addSubEpicTask(SubTask subtask) {
        subtask.setId(id++);
        subEpicHash.put(subtask.getId(), subtask);
        updateAllStatuses((Epic) getEpicById(subtask.getEpicId()));
    }

    @Override
    public void updateSubEpic(SubTask subtask) {
        subEpicHash.put(subtask.getId(), subtask);
        updateAllStatuses((Epic) getEpicById(subtask.getEpicId()));
    }

    @Override
    public void removeSubTask(int id, SubTask subtask) {
        int Ids = subEpicHash.get(id).getEpicId();
        epicHash.get(Ids).getSubtaskId().remove(id);
        updateAllStatuses((Epic) getEpicById(subtask.getEpicId()));
        subEpicHash.remove(id);
    }

    @Override
    public void updateAllStatuses(Epic epic) {
        epic.setStatus(TaskStatus.NEW);
        List<Integer> subTaskId = epic.getSubtaskId();
        for (int id : subTaskId) {
            if (subEpicHash.get(id).getStatus().equals(TaskStatus.DONE)) {
                epic.setStatus(TaskStatus.DONE);
                break;
            } else if (subEpicHash.get(id).getStatus().equals(TaskStatus.IN_PROGRESS)) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            } else if (epic.getStatus().equals(TaskStatus.DONE) && taskArray.get(id).getStatus().equals(TaskStatus.NEW)) {
                epic.setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }
    }

    @Override
    public void update(Task task) {
        taskArray.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        epicHash.put(epic.getId(), epic);
    }

    @Override
    public List<SubTask> getAllSubtasksFromEpic(int id) {
        List<Integer> numbers = epicHash.get(id).getSubtaskId();
        List<SubTask> subtaskArrayList = new ArrayList<>();
        for (int item : numbers) {
            subtaskArrayList.add(subEpicHash.get(item));
        }
        return subtaskArrayList;
    }

    @Override
    public void removeTask(int id) {
        taskArray.remove(id);
    }

    @Override
    public void removeEpic(int id) {
        List<Integer> containerSubTasksIDs = epicHash.get(id).getSubtaskId();
        for (Integer Ids : containerSubTasksIDs) {
            epicHash.remove(Ids);
        }
        epicHash.remove(id);
    }

    @Override
    public void purgeTask() {
        taskArray.clear();
    }

    @Override
    public void purgeEpic() {
        epicHash.clear();
    }

    @Override
    public void purgeAllTask() {
        epicHash.clear();
        subEpicHash.clear();
    }

    @Override
    public TaskManager getTask(int id) {
        Managers.getDefaultHistory().add(taskArray.get(id));
        return (TaskManager) taskArray.get(id);
    }

    @Override
    public TaskManager getEpic(int id) {
        Managers.getDefaultTask().add(epicHash.get(id));
        return (TaskManager) epicHash.get(id);
    }

    @Override
    public TaskManager getSubTask(int id) {
        Managers.getDefaultTask().add(subEpicHash.get(id));
        return (TaskManager) subEpicHash.get(id);
    }

    @Override
    public String toString() {
        return super.toString();
    }


}
