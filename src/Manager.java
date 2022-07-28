import java.util.*;

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
        Epic epic = epicHash.get(id);
        return epic;
    }

    public SubTask getSubtaskById() {
        SubTask subTask = subEpicHash.get(id);
        return subTask;
    }

    public ArrayList<Epic> getEpicHashValues() {
        ArrayList<Epic>valuesEpic = new ArrayList<>();
        for (Integer integer : epicHash.keySet()) {
            for (Epic value : epicHash.values()) {
                getEpicHashValues().add(value);
            }
        }
        return valuesEpic;
    }

    public ArrayList<Task> getValuesSubTask() {
        ArrayList<Task>valuSubTask = new ArrayList<>();
        for (Integer integer : subEpicHash.keySet()) {
            for (SubTask value : subEpicHash.values()) {
                getValuesSubTask().add(value);
            }
        }
        return valuSubTask;
    }

    public ArrayList<Task> getTaskValues(){
        ArrayList<Task>taskValues = new ArrayList<>();
        for (Integer integer : taskArray.keySet()) {
            for (Task value : taskArray.values()) {
                getTaskValues().add(value);
            }
        }
        return taskValues;
    }

    public void add(Task task) {
        task.setId(id++);
        taskArray.put(task.getId(), task);
    }

    public void addEpicTask(Epic epic) {
        epic.setId(id++);
        epicHash.put(epic.getId(), epic);

        for (SubTask value : subEpicHash.values()) {
            if (epic.getId() == value.getEpicId()) {
                epic.subTaskId.add(value.getId());
            }
        }
    }

    public void addSubEpicTask(SubTask subtask) {
        subtask.setId(id++);
        subEpicHash.put(subtask.getId(), subtask);
        for (Epic value : epicHash.values()) {
            if (value.getId() == subtask.getId()) {
                value.getSubtaskId().add(value.getId());
            }
        }
        if (taskArray.containsKey(subtask.getId())){
            updateEpic(epicHash.get(subtask.getEpicId()));
        }
    }

    public void update(Task task) {
        taskArray.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epic.setStatus("NEW");
        epicHash.put(epic.getId(), epic);
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
        if (taskArray.containsKey(subtask.getId())){
            updateEpic(epicHash.get(subtask.getEpicId()));
        }
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
        if (epicHash.isEmpty()) {
            System.out.println("Данные, комплексной задачи отсутствуют");
        } else {
            System.out.println("Данные, комплексной задачи присутствуют");
        }
        ArrayList<Integer> containerSubTasksIDs = epicHash.get(id).getSubtaskId();
        for (Integer Ids : containerSubTasksIDs) {
            epicHash.remove(Ids);
        }
        epicHash.remove(id);
    }

    public void removeSubTask(int id, SubTask subtask) {

        int Ids = subEpicHash.get(id).getEpicId();
        epicHash.get(Ids).subTaskId.remove(id);
        subEpicHash.remove(id);
        updateEpic(epicHash.get(subtask.getEpicId()));;
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


