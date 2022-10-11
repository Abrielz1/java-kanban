package tests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpServer;
import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.Managers;
import manager.history.file.Types;
import manager.http.*;
import manager.task.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpTaskServerTest {
    private static Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
            registerTypeAdapter(Duration.class, new DurationAdapter()).
            create();

    HttpTaskServer httpTaskServer;

    HTTPTaskManager httpTaskManager;

    HTTPTaskManager loadedTaskManager;

    HttpClient httpClient = HttpClient.newHttpClient();

    KVServer kvServer;

    String path = "http://localhost:8080";

    Task task1 = new Task("Погладить кота", TaskStatus.NEW,  "поймать его", LocalDateTime.of(2022, 9, 25, 13, 30, 15), Duration.ofMinutes(30));

    Task task2 = new Task("Потратиться на себя", TaskStatus.NEW, "Сделать себе приятно", LocalDateTime.of(2022, 9, 26, 12, 0, 15), Duration.ofMinutes(30));

    Epic epic1 = new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее");

    SubTask subTask1 = new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 7, LocalDateTime.of(2022,9,25,9,0,10), Duration.ofMinutes(30));

    SubTask subTask2 = new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 7, LocalDateTime.of(2022, 9, 25, 10, 0, 15), Duration.ofMinutes(45));

    SubTask subtask3 = new SubTask("Поесть", TaskStatus.NEW, "Важная задача", 7, LocalDateTime.of(2022, 9, 25, 11, 0, 15),  Duration.ofMinutes(120));

    Epic epic2 = new Epic("Английский", TaskStatus.NEW, "дойти до уровня Native");

    public HttpTaskServerTest() {
        loadedTaskManager = Managers.loadedHTTPTasksManager();
    }

    @BeforeEach
    void starServer() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();
        httpTaskManager = Managers.loadedHTTPTasksManager();
        httpTaskManager.getToken();
        httpTaskManager.saveTasks();
    }

    @AfterEach
    void stopStart() {
        httpTaskServer.stop();
        kvServer.stop();
    }

    @Test
    void getAllTasksAndEpicsAndSubtasks() throws IOException {
        httpTaskManager = loadedTaskManager;
        httpTaskManager.saveTasks();
        URI url = URI.create(path + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
//            String json = response.body();
//            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
//            List<Task> tasksList = gson.fromJson(json, type);
//            List<Task> expectedList = new ArrayList<>(httpTaskManager.getAllTasks().values());
//            assertEquals(expectedList.size(), tasksList.size());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }




//
//    private HttpTaskServer server;
//
//    private TaskManager taskManager;
//
//    private Task task;
//
//    private Epic epic;
//
//    private SubTask subTask;
//
//    private SubTask subTask2;
//
//    public HttpClient client;
//    public String path = "http://localhost:8080";
//
//    private static final int PORT = 8080;
//    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
//
//    HttpServer httpServer;
//
//    void startServer() throws IOException {
//        HttpServer httpServer = HttpServer.create();
//        httpServer.bind(new InetSocketAddress(PORT), 0);
//        httpServer.createContext("/tasks", new HttpTaskServer.TasksHandler());
//        httpServer.start();
//    }
//
//
//
//    @BeforeEach
//    void shouldStartServer() throws IOException {
////        taskManager = Managers.getDefaultTask();
////        gson = Managers.getGson();
////      server = new HttpTaskServer();
////        server = new HttpTaskServer();
////        client = HttpClient.newHttpClient();
//        @AfterEach
//                void shouldStopServer(){
//            httpServer.stop();
//        }
//
//        epic = new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее");
//
//        subTask = new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 2, LocalDateTime.of(2022,9,25,9,0,10), Duration.ofMinutes(30));
//        subTask2 = new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 2, LocalDateTime.of(2022, 9, 25, 10, 0, 15), Duration.ofMinutes(45));
//
//        task = new Task("Погладить кота", TaskStatus.NEW,  "поймать его", LocalDateTime.of(2022, 9, 25, 13, 30, 15), Duration.ofMinutes(30));
//
//        taskManager.add(epic);
//        taskManager.add(task);
//        taskManager.add(subTask);
//        taskManager.add(subTask2);
//        taskManager.getTaskById(task.getId());
//        taskManager.getEpicById(epic.getId());
//
//        startServer();
//    }



}
