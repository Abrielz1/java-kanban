package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.history.file.FileBackedTasksManager;
import manager.http.HTTPTaskManager;
import manager.http.LocalDateAdapter;
import manager.task.InMemoryTaskManager;
import manager.task.TaskManager;

import java.time.LocalDateTime;

public class Managers {

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTask() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getDefaultFileManager() {
        return new FileBackedTasksManager();
    }

    public static Gson getGson(){
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter())
                .create();
    }

    public static HTTPTaskManager loadedHTTPTasksManager() {
        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078");
        //httpTaskManager.loadFromFile();
        httpTaskManager.loadedFromFileTasksManager();
        return httpTaskManager;
    }

}

