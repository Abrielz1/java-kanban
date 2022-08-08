package manager;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {

    HistoryManager historyManager = Managers.getDefaultHistory();

   private int id = 0;

    private Map<Integer, Epic> epicHash = new HashMap<Integer, Epic>();
    private Map<Integer, SubTask> subEpicHash = new HashMap<Integer, SubTask>();
    private Map<Integer, Task> taskArray = new HashMap<Integer, Task>();

    public Map<Integer, Epic> getEpicHash() {
        return epicHash;
    }

    public Map<Integer, SubTask> getSubEpicHash() {
        return subEpicHash;
    }

    public Map<Integer, Task> getTaskArray() {
        return taskArray;
    }

    public Task getTaskById(int id) {
        historyManager.addHistory(taskArray.get(id));
        return taskArray.get(id);
    }

    public Epic getEpicById(int id) {
        historyManager.addHistory(epicHash.get(id));
        return  epicHash.get(id);
    }

    public SubTask getSubtaskById() {
        historyManager.addHistory(subEpicHash.get(id));
        return subEpicHash.get(id);
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
    public void add(Epic epic) {
        epic.setId(id++);
        epicHash.put(epic.getId(), epic);
    }

    @Override
    public void add(SubTask subtask) {
        subtask.setId(id++);
        subEpicHash.put(subtask.getId(), subtask);
        updateAllStatuses(getEpicById(subtask.getEpicId()));
    }

    @Override
    public void updateSubEpic(SubTask subtask) {
        subEpicHash.put(subtask.getId(), subtask);
        updateAllStatuses(getEpicById(subtask.getEpicId()));
    }

    @Override
    public void removeSubTask(int id, SubTask subtask) {
        int Ids = subEpicHash.get(id).getEpicId();
        epicHash.get(Ids).getSubtaskId().remove(id);
        updateAllStatuses(getEpicById(subtask.getEpicId()));
        subEpicHash.remove(id);
    }

    @Override
    public void updateAllStatuses(Epic epic) {
        epic.setStatus(TaskStatus.NEW);
        List<Integer> subTaskId = epic.getSubtaskId();
        for (Integer id : subTaskId) {
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
    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
