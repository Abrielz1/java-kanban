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
import org.junit.jupiter.api.*;


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

    SubTask subTask1 = new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 3, LocalDateTime.of(2022,9,25,9,0,10), 30);

    SubTask subTask2 = new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 3, LocalDateTime.of(2022, 9, 25, 10, 0, 15), 45);

    SubTask subtask3 = new SubTask("Поесть", TaskStatus.NEW, "Важная задача", 3, LocalDateTime.of(2022, 9, 25, 11, 0, 15),  120);



    public HttpTaskServerTest() {
        loadedTaskManager = Managers.loadedHTTPTasksManager();
    }
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

    @Test
    void getTasksTest() {
        URI url = URI.create(path + "/tasks/task");
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
            List<Task> expectedList = new ArrayList<>(httpTaskManager.getTasks().values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), tasksList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getEpicsTest() {
        URI url = URI.create(path + "/tasks/epic");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<Epic>>(){}.getType();
            List<Epic> tasksList = gson.fromJson(json, type);
            List<Epic> expectedList = new ArrayList<>(httpTaskManager.getEpics().values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), tasksList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }


    @Test
    void getSubtasksTest() {
        URI url = URI.create(path + "/tasks/subtask");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            String json = response.body();
            Type type = new TypeToken<ArrayList<SubTask>>(){}.getType();
            List<SubTask> tasksList = gson.fromJson(json, type);
            List<SubTask> expectedList = new ArrayList<>(httpTaskManager.getSubtasks().values());
            for (int i = 0; i < tasksList.size(); i++) {
                assertEquals(expectedList.get(i).getTitle(), tasksList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getTaskByIdTest() {
        URI url = URI.create(path + "/tasks/task?id=1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            String json = response.body();
            System.out.println(response.body());
            Task task = gson.fromJson(json, Task.class);
            Task expectedTask = httpTaskManager.getTasks().get(1);

            assertEquals(expectedTask.getTitle(), task.getTitle());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getSubtaskByIdTest() {
        URI url = URI.create(path + "/tasks/subtask?id=5");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());
            String json = response.body();
            SubTask subtask = gson.fromJson(json, SubTask.class);
            SubTask expectedSubtask = httpTaskManager.getSubtasks().get(5);

            assertEquals(expectedSubtask.getTitle(), subtask.getTitle());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getHistoryTest() {
        URI url = URI.create(path + "/tasks/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            String json = response.body();
            Type type = new TypeToken<ArrayList<Task>>(){}.getType();
            List<Task> historyList = gson.fromJson(json, type);
            List<Task> expectedHistoryList = new ArrayList<>(httpTaskManager.getHistory());
            for (int i = 0; i < historyList.size(); i++) {
                assertEquals(expectedHistoryList.get(i).getTitle(), historyList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void getPrioritizedListTest() {
        URI url = URI.create(path + "/tasks/priority");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(200, response.statusCode());

            String json = response.body();
            Type type = new TypeToken<List<Task>>(){}.getType();
            List<Task> prioritizedList = gson.fromJson(json, type);
            List<Task> expectedPrioritizedList = new ArrayList<>(httpTaskManager.getPrioritizedTasks());
            for (int i = 0; i < prioritizedList.size(); i++) {
                assertEquals(expectedPrioritizedList.get(i).getTitle(), prioritizedList.get(i).getTitle());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }



    @Test
    void deleteTaskByIdFromServerTest() {
        HTTPTaskManager rollback = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/task?id=2");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            HTTPTaskManager taskManager = Managers.loadedHTTPTasksManager();
            assertNull(taskManager.getTasks().get(2));
            httpTaskManager = rollback;
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void deleteEpicByIdAndAllSubtasksByThisEpicFromServerTest() {
        HTTPTaskManager rollback = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/epic?id=3");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            httpTaskManager = Managers.loadedHTTPTasksManager();
            assertNull(httpTaskManager.getEpicById(3));
            httpTaskManager = rollback;
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    @Test
    void deleteSubtaskByIdFromServerTest() {
        HTTPTaskManager rollback = Managers.loadedHTTPTasksManager();
        URI url = URI.create(path + "/tasks/subtask?id=6");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            httpTaskManager = Managers.loadedHTTPTasksManager();
            assertNull(httpTaskManager.getSubtasks().get(6));
            httpTaskManager = rollback;
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }


    @AfterAll
    static void restoreFile() {
        var fileManager = Managers.getDefaultFileManager();
        FileBackedTasksManager.setIdCounter(1);
        //создаем объекты и закидываем в файл
        Task task1 = new Task("Погладить кота", TaskStatus.NEW,  "поймать его", LocalDateTime.of(2022, 9, 25, 13, 30, 15), 30);

        Task task2 = new Task("Потратиться на себя", TaskStatus.NEW, "Сделать себе приятно", LocalDateTime.of(2022, 9, 26, 12, 0, 15), 30);

        Epic epic1 = new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее");

        SubTask subTask1 = new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 3, LocalDateTime.of(2022,9,25,9,0,10), 30);

        SubTask subTask2 = new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 3, LocalDateTime.of(2022, 9, 25, 10, 0, 15), 45);

        SubTask subtask3 = new SubTask("Поесть", TaskStatus.NEW, "Важная задача", 3, LocalDateTime.of(2022, 9, 25, 11, 0, 15),  120);


        fileManager.getTaskById(1);
        fileManager.getEpicById(7);
        fileManager.getEpicById(3);
    }


}

















