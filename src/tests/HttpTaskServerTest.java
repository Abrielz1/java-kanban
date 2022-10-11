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
import manager.history.file.FileBackedTasksManager;
import manager.history.file.Types;
import manager.http.*;
import manager.task.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;


public class HttpTaskServerTest {

    HttpTaskServer httpTaskServer;
    HTTPTaskManager httpTaskManager;
    HTTPTaskManager loadedTaskManager;
    HttpClient httpClient = HttpClient.newHttpClient();
    KVServer kvServer;
    String path = "http://localhost:8080";
    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
            create();


    Task task1 = new Task("Погладить кота", TaskStatus.NEW,  "поймать его", LocalDateTime.of(2022, 9, 25, 13, 30, 15), 30);

    Task task2 = new Task("Потратиться на себя", TaskStatus.NEW, "Сделать себе приятно", LocalDateTime.of(2022, 9, 26, 12, 0, 15), 30);

    Epic epic1 = new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее");

    SubTask subTask1 = new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 7, LocalDateTime.of(2022,9,25,9,0,10), 30);

    SubTask subTask2 = new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 7, LocalDateTime.of(2022, 9, 25, 10, 0, 15), 45);

    SubTask subtask3 = new SubTask("Поесть", TaskStatus.NEW, "Важная задача", 7, LocalDateTime.of(2022, 9, 25, 11, 0, 15),  120);

    Epic epic2 = new Epic("Купить стриралку", TaskStatus.NEW, "Надо новую");


    public HttpTaskServerTest() {
        loadedTaskManager = Managers.loadedHTTPTasksManager();
    }
        @BeforeAll
        static void constructFileForeTests() {
            var fileManager = Managers.getDefaultFileManager();
            FileBackedTasksManager.setIdCounter(1);
            //создаем объекты и закидываем в файл
            fileManager.add(new Task("Погладить кота", TaskStatus.NEW,  "поймать его", LocalDateTime.of(2022, 9, 25, 13, 30, 15), 30));

            fileManager.add(new Task("Потратиться на себя", TaskStatus.NEW, "Сделать себе приятно", LocalDateTime.of(2022, 9, 26, 12, 0, 15), 30));

            fileManager.add(new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее"));

            fileManager.add(new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 7, LocalDateTime.of(2022,9,25,9,0,10), 30));

            fileManager.add(new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 7, LocalDateTime.of(2022, 9, 25, 10, 0, 15), 45));

            fileManager.add(new SubTask("Поесть", TaskStatus.NEW, "Важная задача", 7, LocalDateTime.of(2022, 9, 25, 11, 0, 15),  120));

            fileManager.add(new Epic("Купить стриралку", TaskStatus.NEW, "Надо новую"));

            fileManager.getTaskById(1);
            fileManager.getEpicById(7);
            fileManager.getEpicById(3);
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
            String json = response.body();
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> tasksList = gson.fromJson(json, type);
            List<Task> expectedList = new ArrayList<>(httpTaskManager.getAllTasks().values());
            assertEquals(expectedList.size(), tasksList.size());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }





    }
















