package manager.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import manager.Managers;
import manager.history.file.FileBackedTasksManager;
import manager.task.TaskManager;


import java.io.*;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
            create();

    static HttpServer httpServer;


   public HttpTaskServer() throws IOException {
            this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
            httpServer.createContext("/tasks", new TasksHandler());
        }

    public void start() {

        System.out.println("Запускаем сервер на порту " + PORT);

        httpServer.start();
    }

    public void stop() {
        System.out.println("Сервер остановлен");
        httpServer.stop(0);
    }

    public static class TasksHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {

            FileBackedTasksManager fileManager = Managers.getDefaultFileManager();
            fileManager.loadFromFile();

            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            String response = "";
            int rCode = 404;

            switch (method) {

                case "GET":
                    if (Pattern.matches("^/tasks$", path)) {
                        response = gson.toJson(fileManager.getAllTasks().values());
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/task$", path) && exchange.getRequestURI().getQuery() != null) {
                        String query = exchange.getRequestURI().getQuery();
                        String[] queryArray = query.split("=");
                        int id = Integer.parseInt(queryArray[1]);
                        response = gson.toJson(fileManager.getTaskById(id));
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/task$", path)) {
                        response = gson.toJson(fileManager.getTasks().values());
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/subtask/epic$", path) && exchange.getRequestURI().getQuery() != null) {
                        String query = exchange.getRequestURI().getQuery();
                        String[] queryArray = query.split("=");
                        int id = Integer.parseInt(queryArray[1]);
                        response = gson.toJson(fileManager.getAllSubtasksByEpic(id));
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/epic$", path) && exchange.getRequestURI().getQuery() != null) {
                        String query = exchange.getRequestURI().getQuery();
                        String[] queryArray = query.split("=");
                        int id = Integer.parseInt(queryArray[1]);
                        response = gson.toJson(fileManager.getEpicById(id));
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        response = gson.toJson(fileManager.getEpics().values());
                        rCode = 200;
                    } else if (path.endsWith("tasks/subtask") && exchange.getRequestURI().getQuery() != null) {
                        String query = exchange.getRequestURI().getQuery();
                        String[] queryArray = query.split("=");
                        int id = Integer.parseInt(queryArray[1]);
                        response = gson.toJson(fileManager.getSubtaskById(id));
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/subtask$", path) && exchange.getRequestURI().getQuery() != null) {
                        response = gson.toJson(fileManager.getSubtasks().values());
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/history$", path)) {
                        response = gson.toJson(fileManager.getHistory());
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/priority$", path)) {
                        response = gson.toJson(fileManager.getPrioritizedTasks());
                        rCode = 200;
                    }

                    break;

                case "POST":
                    //todo
                    if (Pattern.matches("^/tasks/task$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(taskBody, Task.class);
                        fileManager.add(task);
                        rCode = 201;
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(taskBody, Epic.class);
                        fileManager.add(epic);
                        rCode = 201;
                    } else if (Pattern.matches("^/tasks/subtask$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        SubTask subtask = gson.fromJson(taskBody, SubTask.class);
                        fileManager.add(subtask);
                        rCode = 201;
                    }
                    break;
                case "DELETE":

                    if (Pattern.matches("^/tasks/task$", path) && exchange.getRequestURI().getQuery() != null) {
                        String query = exchange.getRequestURI().getQuery();
                        String[] queryArray = query.split("=");
                        int id = Integer.parseInt(queryArray[1]);
                        fileManager.removeTaskById(id);
                        response = gson.toJson("Задача c id:" + id + " удалена!");
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/task$", path)) {
                        fileManager.removeAllTasks();
                        response = gson.toJson("Все задачи удалены");
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/epic$", path) && exchange.getRequestURI().getQuery() != null) {
                        String query = exchange.getRequestURI().getQuery();
                        String[] queryArray = query.split("=");
                        int id = Integer.parseInt(queryArray[1]);
                        fileManager.removeEpicById(id);
                        response = gson.toJson("Комплексная задача c id:" + id + " удалена!");
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        fileManager.removeAllEpicsAndSubtasks();
                        response = gson.toJson("Все комплексные задачи и подзадачи удалены");
                        rCode = 200;
                    } else if (Pattern.matches("^/tasks/subtask$", path) && exchange.getRequestURI().getQuery() != null) {
                        String query = exchange.getRequestURI().getQuery();
                        String[] queryArray = query.split("=");
                        int id = Integer.parseInt(queryArray[1]);
                        fileManager.removeSubtaskById(id); //
                        response = gson.toJson("Подзадача c id:" + id + " удалена!");
                        rCode = 200;

                    }
                    break;

                case "PUT":
                    if (Pattern.matches("^/tasks/task$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Task task = gson.fromJson(taskBody, Task.class);
                        fileManager.update(task);
                        rCode = 202;
                    } else if (Pattern.matches("^/tasks/epic$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        Epic epic = gson.fromJson(taskBody, Epic.class);
                        fileManager.update(epic);
                        rCode = 202;
                    } else if (Pattern.matches("^/tasks/subtask$", path)) {
                        InputStream inputStream = exchange.getRequestBody();
                        String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                        SubTask subtask = gson.fromJson(taskBody, SubTask.class);
                        fileManager.update(subtask);
                        rCode = 202;
                    } else {
                        rCode = 404;
                        response = gson.toJson("Такой команды нет");
                    }
                    break;
            }

            exchange.sendResponseHeaders(rCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
