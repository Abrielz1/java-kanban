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

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    //private static final Gson gson = new Gson();
    //static URI url = URI.create("http://localhost:8080/tasks/task/");
    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
            registerTypeAdapter(Duration.class, new DurationAdapter()).
            create();

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler ());
        httpServer.start();


        //FileBackedTasksManager fileManager = FileBackedTasksManager.loadedFromFileTasksManager();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }


    static class TasksHandler  implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            FileBackedTasksManager fileManager = Managers.getDefaultFileManager();
            fileManager.loadFromFile();
           // var manager = fileManager;

            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            String response = "";
            int rCode = 404;

                        switch (method) {
                    case "GET":
                        if (path.endsWith("/tasks")) {
                            response = gson.toJson(fileManager.getAllTasks().values());
                            rCode = 200;
                        }
                        else if (path.endsWith("tasks/task") && exchange.getRequestURI().getQuery() != null) {
                          String query = exchange.getRequestURI().getQuery();
                            String[] queryArray = query.split("=");
                            int id = Integer.parseInt(queryArray[1]);
                            response = gson.toJson(fileManager.getTaskById(id));
                            rCode = 200;
                        }
                        else if(path.endsWith("tasks/task")){
                            response = gson.toJson(fileManager.getTasks().values());
                            rCode = 200;
                        }
                        else if (path.endsWith("/tasks/subtask/epic") && exchange.getRequestURI().getQuery() != null) {
                            String query = exchange.getRequestURI().getQuery();
                            String[] queryArray = query.split("=");
                            int id = Integer.parseInt(queryArray[1]);
                            response = gson.toJson(fileManager.getAllSubtasksByEpic(id));
                            rCode = 200;
                        }
                        else if (path.endsWith("/tasks/epic") && exchange.getRequestURI().getQuery() != null) {
                            String query = exchange.getRequestURI().getQuery();
                            String[] queryArray = query.split("=");
                            int id = Integer.parseInt(queryArray[1]);
                            response = gson.toJson(fileManager.getEpicById(id));
                            rCode = 200;
                        }
                        else if(path.endsWith("tasks/epic")){
                            response = gson.toJson(fileManager.getEpics().values());
                            rCode = 200;
                        }
                        else if (path.endsWith("tasks/subtask") && exchange.getRequestURI().getQuery() != null) {
                            String query = exchange.getRequestURI().getQuery();
                            String[] queryArray = query.split("=");
                            int id = Integer.parseInt(queryArray[1]);
                            response = gson.toJson(fileManager.getSubtaskById(id));
                            rCode = 200;
                       }
                        else if(path.endsWith("tasks/subtask")){
                            response = gson.toJson(fileManager.getSubtasks().values());
                            rCode = 200;
                        }
                     else if (path.endsWith("tasks/history")) {
                            response = gson.toJson(fileManager.getHistory());
                            rCode = 200;
                        }
                        else if (path.endsWith("tasks/priority")) {
                            response = gson.toJson(fileManager.getPrioritizedTasks());
                            rCode = 200;
                        }

                    break;

                    case "POST":
                    //todo
                        if (path.endsWith("tasks/task")) {
                            InputStream inputStream = exchange.getRequestBody();
                            String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            Task task = gson.fromJson(taskBody, Task.class);
                            fileManager.add(task);
                            rCode = 202;
                        }
                        else  if (path.endsWith("tasks/epic")) {
                            InputStream inputStream = exchange.getRequestBody();
                            String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            Task epic = gson.fromJson(taskBody, Epic.class);
                            fileManager.add(epic);
                        }
                        else if (path.endsWith("tasks/subtask")) {
                            InputStream inputStream = exchange.getRequestBody();
                            String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            Task subtask = gson.fromJson(taskBody, SubTask.class);
                            fileManager.add(subtask);
                        }
                    break;
                    case "DELETE":

                         if (path.endsWith("tasks/task") && exchange.getRequestURI().getQuery() != null) {
                        String query = exchange.getRequestURI().getQuery();
                        String[] queryArray = query.split("=");
                        int id = Integer.parseInt(queryArray[1]);
                             fileManager.removeTaskById(id);
                             response = gson.toJson("Задача c id:" + id + " удалена!");
                        rCode = 200;
                    }
                         else if (path.endsWith("tasks/task")) {
                        fileManager.removeAllTasks();
                        response = gson.toJson("Все задачи удалены");
                        rCode = 200;
                    }
                        else if (path.endsWith("tasks/subtask") && exchange.getRequestURI().getQuery() != null) {
                             String query = exchange.getRequestURI().getQuery();
                             String[] queryArray = query.split("=");
                             int id = Integer.parseInt(queryArray[1]);
                             fileManager.removeSubtaskById(id); //
                             response = gson.toJson("Подзадача c id:" + id + " удалена!");
                             rCode = 200;

                    }
                        else if (path.endsWith("/tasks/epic") && exchange.getRequestURI().getQuery() != null) {
                             String query = exchange.getRequestURI().getQuery();
                             String[] queryArray = query.split("=");
                             int id = Integer.parseInt(queryArray[1]);
                             fileManager.removeEpicById(id);
                             response = gson.toJson("Комплексная задача c id:" + id + " удалена!");
                             rCode = 200;
                   }
                        else if (path.endsWith("/tasks/epic")) {
                             fileManager.removeAllEpicsAndSubtasks();
                             response = gson.toJson("Все комплексные задачи и подзадачи удалены");
                             rCode = 200;
                   }
                        break;

                            case "PUT":
                        if (path.endsWith("tasks/task") && exchange.getRequestURI().getQuery() != null) {
                            InputStream inputStream = exchange.getRequestBody();
                            String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            Task task = gson.fromJson(taskBody, Task.class);
                            fileManager.update(task);
                            rCode = 202;
                   }    else if (path.endsWith("/tasks/epic") && exchange.getRequestURI().getQuery() != null) {
                            InputStream inputStream = exchange.getRequestBody();
                            String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            Task task = gson.fromJson(taskBody, Epic.class);
                            fileManager.update(task);
                            rCode = 202;
                   }    else if (path.endsWith("tasks/subtask") && exchange.getRequestURI().getQuery() != null) {
                            InputStream inputStream = exchange.getRequestBody();
                            String taskBody = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                            Task task = gson.fromJson(taskBody, SubTask.class);
                            fileManager.update(task);
                            rCode = 202;
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

//      if (path.contains("/task") && exchange.getRequestURI().getQuery() != null) {
//              response = gson.toJson(fileManager.getTasks().values());
//              rCode = 200;
//              exchange.sendResponseHeaders(rCode, 0);
//
//              }

//if (path.contains("/task") && method.equals("GET")) {


//            switch (method) {
//                    case "GET":
//                    if (path.endsWith("/subtask") && exchange.getRequestURI().getQuery() != null) {
//                    response = gson.toJson(fileManager.subEpicHash.entrySet());
//                    rCode = 200;
//
//                    }
//                    break;
//
//                    case "POST":
//                    //todo
//                    break;
//                    case "DELETE":
//                    //todo
//                    break;
//                    }




/*
            Подсказка: как создать задачу
	String json = gson.toJson(newTask);
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


GET http://сервер-с-фотографиями/фотографии вернёт все фотографии;

POST http://сервер-с-фотографиями/фотографии сохранит фотографию, переданную в теле запроса;

DELETE http://сервер-с-фотографиями/фотографии удалит все фотографии.



            if (path.endsWith("/task")) {
                response = gson.toJson(fileBackedTasksManager.getTasks().values());
                rCode = 202;
            } else if (path.endsWith("/epic")) {
                response = gson.toJson(fileBackedTasksManager.getEpics().values());
                rCode = 202;
            } else if (path.endsWith("/subtask")) {
                response = gson.toJson(fileBackedTasksManager.getSubtasks().values());
                rCode = 202;
            }
            if (path.contains("task") && !exchange.getRequestURI().getQuery().isEmpty()) {
                String query = exchange.getRequestURI().getQuery();
                String[] queryArray = query.split("=");
                int id = Integer.parseInt(queryArray[1]);
                response = gson.toJson(fileBackedTasksManager.getTaskById(id));
                rCode = 202;
            }

     switch (method) {
                case "GET" :
break;

                case "POST":
                    //todo
                    break;
                case "DELETE" :
                    //todo
                    break;


            }





                    if (path.endsWith("/task")&& exchange.getRequestURI().getQuery() != null) {
                        response = gson.toJson(fileBackedTasksManager.getTasks().values());
                        rCode = 202;
                    } else if (path.endsWith("/epic")&& exchange.getRequestURI().getQuery() != null) {
                        response = gson.toJson(fileBackedTasksManager.getEpics().values());
                        rCode = 202;
                    } else if (path.endsWith("/subtask")&& exchange.getRequestURI().getQuery() != null) {
                        response = gson.toJson(fileBackedTasksManager.getSubtasks().values());
                        rCode = 202;
                    }
                    if (path.contains("task") && exchange.getRequestURI().getQuery() != null) {
                        String query = exchange.getRequestURI().getQuery();
                        String[] queryArray = query.split("=");
                        int id = Integer.parseInt(queryArray[1]);
                        response = gson.toJson(fileBackedTasksManager.getTaskById(id));
                        rCode = 202;
                    }
 */