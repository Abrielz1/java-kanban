package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.Managers;
import manager.history.file.Types;
import manager.http.DurationAdapter;
import manager.http.HttpTaskServer;
import manager.http.LocalDateAdapter;
import manager.task.TaskManager;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class HttpTaskServerTest {
    private static Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
            registerTypeAdapter(Duration.class, new DurationAdapter()).
            create();

    private HttpTaskServer server;

    private TaskManager taskManager;

    private Task task;

    private Epic epic;

    private SubTask subTask;

    private SubTask subTask2;

    public HttpClient client;
    public String path = "http://localhost:8080";


    @BeforeEach
    void shouldStartServer() throws IOException {
//        taskManager = Managers.getDefaultTask();
//        gson = Managers.getGson();
//      server = new HttpTaskServer();
//        server = new HttpTaskServer();
//        client = HttpClient.newHttpClient();

        epic = new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее");

        subTask = new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 2, LocalDateTime.of(2022,9,25,9,0,10), Duration.ofMinutes(30));
        subTask2 = new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 2, LocalDateTime.of(2022, 9, 25, 10, 0, 15), Duration.ofMinutes(45));

        task = new Task("Погладить кота", TaskStatus.NEW,  "поймать его", LocalDateTime.of(2022, 9, 25, 13, 30, 15), Duration.ofMinutes(30));

        taskManager.add(epic);
        taskManager.add(task);
        taskManager.add(subTask);
        taskManager.add(subTask2);
        taskManager.getTaskById(task.getId());
        taskManager.getEpicById(epic.getId());


    }



}
