import java.util.*; //от греха решил перепроверить и не зря :(

public class Manager {
    protected int id = 0;

    protected HashMap<Integer, Epic> epicHash = new HashMap<Integer, Epic>();
    protected HashMap<Integer, SubTask> subEpicHash = new HashMap<Integer, SubTask>();
    protected HashMap<Integer, Task> taskArray = new HashMap<Integer, Task>();

    public HashMap<Integer, Epic> getEpicHash() {
        return epicHash;
    }

    public HashMap<Integer, SubTask> getSubEpicHash() {
        return subEpicHash;
    }

    public HashMap<Integer, Task> getTaskArray() {
        return taskArray;
    }

    public Task getTaskById(int id) {
        return taskArray.get(id);
    }

    public Epic getEpicById(int id) {
        return epicHash.get(id);
    }

    public SubTask getSubtaskById() {
        return subEpicHash.get(id);
    }

    public ArrayList<Epic> getEpicHashValues() {
        return new ArrayList<>(epicHash.values());
    }

    public ArrayList<Task> getValuesSubTask() {
        return new ArrayList<>(subEpicHash.values());
    }

    public ArrayList<Task> getTaskValues() {
        return new ArrayList<>(taskArray.values());
    }

    public void add(Task task) {
        task.setId(id++);
        taskArray.put(task.getId(), task);

    }

    public void addEpicTask(Epic epic) {
        epic.setId(id++);
        epicHash.put(epic.getId(), epic);
    }

    public void addSubEpicTask(SubTask subtask) {
        subtask.setId(id++);
        subEpicHash.put(subtask.getId(), subtask);
        johnTheRipper(getEpicById(subtask.getEpicId()));
    }

    public void update(Task task) {
        taskArray.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epicHash.put(epic.getId(), epic);
    }

    public void johnTheRipper(Epic epic){
        epic.setStatus("NEW");
        ArrayList<Integer> subTaskId = epic.getSubtaskId();
        for (int id : subTaskId) {
            if (subEpicHash.get(id).getStatus().equals("DONE")) {
                epic.setStatus("DONE");
                break;
            } else if (subEpicHash.get(id).getStatus().equals("IN_PROGRESS")) {
                epic.setStatus("IN_PROGRESS");
                return;
            } else if (epic.getStatus().equals("DONE") && taskArray.get(id).getStatus().equals("NEW")) {
                epic.setStatus("IN_PROGRESS");
                return;
            }
        }
    }

    public void updateSubEpic(SubTask subtask) {
        subEpicHash.put(subtask.getId(), subtask);
        johnTheRipper(getEpicById(subtask.getEpicId()));
    }

    public ArrayList<SubTask> getAllSubtasksFromEpic(int id) {
        ArrayList<Integer> numbers = epicHash.get(id).getSubtaskId();
        ArrayList<SubTask> subtaskArrayList = new ArrayList<>();
        for (int item : numbers) {
            subtaskArrayList.add(subEpicHash.get(item));
        }
        return subtaskArrayList;
    }

    public void removeTask(int id) {
        taskArray.remove(id);
    }

    public void removeEpic(int id) {
        ArrayList<Integer> containerSubTasksIDs = epicHash.get(id).getSubtaskId();
        for (Integer Ids : containerSubTasksIDs) {
            epicHash.remove(Ids);
        }
        epicHash.remove(id);
    }

    public void removeSubTask(int id, SubTask subtask) {
        int Ids = subEpicHash.get(id).getEpicId();
        epicHash.get(Ids).subTaskId.remove(id);
        johnTheRipper(getEpicById(subtask.getEpicId()));
        subEpicHash.remove(id);
    }

    public void purgeTask() {
        taskArray.clear();
    }

    public void purgeEpic() {
        epicHash.clear();
    }

    public void purgeAllTask() {
        epicHash.clear();
        subEpicHash.clear();
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", epicHash=" + epicHash +
                ", subEpicHash=" + subEpicHash +
                ", taskArray=" + taskArray +
                '}';
    }
}


