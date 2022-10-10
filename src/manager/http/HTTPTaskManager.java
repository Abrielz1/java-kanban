package manager.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import manager.history.file.FileBackedTasksManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static manager.history.HistoryManager.historyToString;

public class HTTPTaskManager extends FileBackedTasksManager {

    protected KVTaskClient kvTaskClient;
    protected String path;

    protected Gson gson = new Gson();

    public HTTPTaskManager(String path) {
        this.path = path;
        kvTaskClient = new KVTaskClient(path);
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
            add(task);
        }
        allTasks.putAll(getTasks());

        json = kvTaskClient.load("/epics");
        type = new TypeToken<ArrayList<Epic>>(){}.getType();
        ArrayList<Epic> epicsList = gson.fromJson(json, type);
        for (Epic epic : epicsList) {
            add(epic);
        }
        allTasks.putAll(getEpics());

        json = kvTaskClient.load("/subtasks");
        type = new TypeToken<ArrayList<SubTask>>(){}.getType();
        ArrayList<SubTask> subtasksList = gson.fromJson(json, type);
        for (SubTask subtask : subtasksList) {
            add(subtask);
        }
        allTasks.putAll(getSubtasks());

        json = kvTaskClient.load("/history");
        String historyLine = json.substring(1, json.length() - 1);
        String[] historyLineContents = historyLine.split(",");
        for (String s : historyLineContents) {
            historyManager.addHistory(allTasks.get(Integer.parseInt(s)));
        }
        save();
    }

    @Override
    public int add(Task task) {
        prioritizedTasks.add(task);
        taskArray.put(task.getId(), task);
        save();
        return task.getId();
    }

    @Override
    public int add(Epic epic) {
        prioritizedTasks.add(epic);
        epicHash.put(epic.getId(), epic);
        save();
        return epic.getId();
    }

    @Override
    public int add(SubTask subtask) {
        prioritizedTasks.add(subtask);
        subEpicHash.put(subtask.getId(), subtask);
        save();
        return subtask.getId();
    }

//    public void getToken() {
//        kvTaskClient = new KVTaskClient(path);
//        kvTaskClient.register();
//    }
}

/*
Конструктор HTTPTaskManager должен будет вместо имени файла принимать URL к серверу KVServer. Также HTTPTaskManager создаёт KVTaskClient, из которого можно получить исходное состояние менеджера. Вам нужно заменить вызовы сохранения состояния в файлах на вызов клиента.
В конце обновите статический метод getDefault() в утилитарном классе Managers, чтобы он возвращал HTTPTaskManager.
*/