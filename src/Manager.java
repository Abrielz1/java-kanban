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
        Task task = taskArray.get(id);
        return task;
    }

    public Epic getEpicById(int id) {
        Epic epic = epicHash.get(id);
        return epic;
    }

    public SubTask getSubtaskById() {
        SubTask subTask = subEpicHash.get(id);
        return subTask;
    }

    public void add(Task task) {
        if (taskArray.isEmpty()) {
            System.out.println("Данные, простой задачи отсутствуют");
        } else {
            System.out.println("Данные, простой задачи присутствуют");
        }
        task.setId(id++);
        taskArray.put(task.getId(), task);
    }

    public void addEpicTask(Epic epic) {
        if (epicHash.isEmpty()) {
            System.out.println("Данные, комплексной задачи отсутствуют");
        } else {
            System.out.println("Данные, комплексной задачи присутствуют");
        }
        epic.setId(id++);
        epicHash.put(epic.getId(), epic);

        for (SubTask value : subEpicHash.values()) {
            if (epic.getId() == value.getEpicId()) {
                epic.subTaskId.add(value.getId());
            }
        }
    }

    public void addSubEpicTask(SubTask subtask) {
        if (subEpicHash.isEmpty()) {
            System.out.println("Данные, комплексной подзадачи отсутствуют");
        } else {
            System.out.println("Данные, комплексной подзадачи присутствуют");
        }
        subtask.setId(id++);
        subEpicHash.put(subtask.getId(), subtask);
        for (Epic value : epicHash.values()) {
            if (value.getId() == subtask.getId()) {
                value.subTaskId.add(value.getId());
            }
        }

    }

    public void update(Task task) {
        taskArray.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        if (epicHash.isEmpty()) {
            System.out.println("Данные, комплексной подзадачи отсутствуют");
        } else {
            System.out.println("Данные, комплексной подзадачи присутствуют");
        }
        epicHash.put(epic.getId(), epic);
        ArrayList<Integer> subTaskId = epic.getSubtaskId();
        for (int id : subTaskId) {
            if (subEpicHash.get(id).getStatus().equals("DONE")) {
                epic.setStatus("DONE");
                break;
            } else if (subEpicHash.get(id).getStatus().equals("IN_PROGRESS")) {
                epic.setStatus("IN_PROGRESS");
                return;
            }
        }
    }

    public void updateSubEpic(SubTask subtask) {
        if (subEpicHash.isEmpty()) {
            System.out.println("Данные, комплексной задачи отсутствуют");
        } else {
            System.out.println("Данные, комплексной задачи присутствуют");
        }

        subEpicHash.put(subtask.getId(), subtask);
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

    public void removeSubTask(int id) {
        if (subEpicHash.isEmpty()) {
            System.out.println("Данные, комплексной подзадачи отсутствуют");
        } else {
            System.out.println("Данные, комплексной подзадачи присутствуют");
        }
        int Ids = subEpicHash.get(id).getEpicId();
        epicHash.get(Ids).subTaskId.remove(id);
        subEpicHash.remove(id);
    }

    public void purgeTask() {
        taskArray.clear();
    }

    public void purgeAllTask() {
        epicHash.clear();
        subEpicHash.clear();
    }
}


