package tests;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.Managers;
import manager.history.file.FileBackedTasksManager;
import manager.http.HTTPTaskManager;
import manager.http.KVServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoadFromKVServerTest {
    HTTPTaskManager httpTaskManager;

    @BeforeAll
    static void constructFileForeTests() {
        var fileManager = Managers.getDefaultFileManager();
        FileBackedTasksManager.setIdCounter(1);

        fileManager.add(new Task("Погладить кота", TaskStatus.NEW,  "поймать его", LocalDateTime.of(2022, 9, 25, 13, 30, 15), 30));

        fileManager.add(new Task("Потратиться на себя", TaskStatus.NEW, "Сделать себе приятно", LocalDateTime.of(2022, 9, 26, 12, 0, 15), 30));

        fileManager.add(new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее"));

        fileManager.add(new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 3, LocalDateTime.of(2022,9,25,9,0,10), 30));

        fileManager.add(new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 3, LocalDateTime.of(2022, 9, 25, 10, 0, 15), 45));

        fileManager.add(new SubTask("Поесть", TaskStatus.NEW, "Важная задача", 3, LocalDateTime.of(2022, 9, 25, 11, 0, 15),  120));



        fileManager.getTaskById(1);
        fileManager.getEpicById(3);
    }
    @Test
    void saveAndLoadTasksKVServerTest() throws IOException {
        KVServer kvServer = new KVServer();
        kvServer.start();
        httpTaskManager = Managers.loadedHTTPTasksManager();
        httpTaskManager.getToken();
        httpTaskManager.saveTasks();
        HTTPTaskManager loadedFromServerManager = new HTTPTaskManager("http://localhost:8078");
        loadedFromServerManager.getToken();
        loadedFromServerManager.loadTasks();
        kvServer.stop();
    }
}
