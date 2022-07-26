package manager.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import manager.history.file.FileBackedTasksManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static manager.history.HistoryManager.historyToString;

public class HTTPTaskManager extends FileBackedTasksManager {

    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
            create();
    protected KVTaskClient kvTaskClient;

    protected String path;

    public HTTPTaskManager(String path) {
        this.path = path;
    }

    public void getToken() {
        kvTaskClient = new KVTaskClient(path);
        kvTaskClient.register();
    }

    public void saveTasks() throws IOException {
        if (kvTaskClient == null) {
            System.out.println("Требуется регистрация");
            return;
        }

        this.loadFromFile();
        kvTaskClient.put("/tasks", gson.toJson(getTasks().values()));
        kvTaskClient.put("/epics", gson.toJson(getEpics().values()));
        kvTaskClient.put("/subtasks", gson.toJson(getSubtasks().values()));
        kvTaskClient.put("/history", gson.toJson(historyToString(historyManager)));
    }

    public void loadTasks() {
        String json = kvTaskClient.load("/tasks");
        Type type = new TypeToken<ArrayList<Task>>(){}.getType();
        ArrayList<Task> tasksList = gson.fromJson(json, type);
        for (Task task : tasksList) {
            addTaskFromKVServer(task);
        }
        allTasks.putAll(getTasks());

        json = kvTaskClient.load("/epics");
        type = new TypeToken<ArrayList<Epic>>(){}.getType();
        ArrayList<Epic> epicsList = gson.fromJson(json, type);
        for (Epic epic : epicsList) {
            addEpicFromKVServer(epic);
        }
        allTasks.putAll(getEpics());

        json = kvTaskClient.load("/subtasks");
        type = new TypeToken<ArrayList<SubTask>>(){}.getType();
        ArrayList<SubTask> subtasksList = gson.fromJson(json, type);
        for (SubTask subtask : subtasksList) {
            addSubtaskFromKVServer(subtask);
        }
        allTasks.putAll(getSubtasks());

        json = kvTaskClient.load("/history");
        String historyLine = json.substring(1, json.length() - 1);
        if (!historyLine.equals("\"\"")) {
            String[] historyLineContents = historyLine.split(",");
            for (String s : historyLineContents) {
                historyManager.addHistory(allTasks.get(Integer.parseInt(s)));
            }
        }
        save();
    }

    public int addTaskFromKVServer(Task task) {
        task.setId(task.getId());
        prioritizedTasks.add(task);
        taskArray.put(task.getId(), task);
        save();
        return task.getId();
    }

    public int addEpicFromKVServer(Epic epic) {
        epic.setId(epic.getId());
        prioritizedTasks.add(epic);
        epicHash.put(epic.getId(), epic);
        save();
        return epic.getId();
    }

    public int addSubtaskFromKVServer(SubTask subtask) {
        subtask.setId(subtask.getId());
        prioritizedTasks.add(subtask);
        subEpicHash.put(subtask.getId(), subtask);
        save();
        return subtask.getId();
    }

    @Override
    public int add(Task task) {
        task.setId(task.getId());
        getTaskEndTime(task);
        prioritizedTasks.add(task);
        taskArray.put(task.getId(), task);
        save();
        return task.getId();
    }
    @Override
    public int add(Epic epic) {
        epic.setId(epic.getId());
        getEpicTimesAndDuration(epic);
        prioritizedTasks.add(epic);
        epicHash.put(epic.getId(), epic);
        save();
        return epic.getId();
    }
    @Override
    public int add(SubTask subtask) {
        subtask.setId(subtask.getId());
        getSubtaskEndTime(subtask);
        prioritizedTasks.add(subtask);
        subEpicHash.put(subtask.getId(), subtask);
        save();
        return subtask.getId();
    }
}
