package manager.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

public class KVTaskClient {

    private static final Gson gson = new GsonBuilder().
            registerTypeAdapter(LocalDateTime.class, new LocalDateAdapter()).
            registerTypeAdapter(Duration.class, new DurationAdapter()).
            create();

protected URI url;

protected String apiToken;

HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient(String path) {
        this.url = URI.create(path);
        this.apiToken = register();
    }

    public String register () {
        URI uri =  URI.create(this.url + "/register");
        HttpRequest request =  HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return apiToken = response.body();
            } else System.out.println("Не удалрось получить API_TOKEN");
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return "wrong API_TOKEN";
    }

    public void put(String key, String json)  {
        if (apiToken == null) {
            System.out.println("API_TOKEN не присвоен");
            return;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save" + key + "?API_TOKEN=" + apiToken))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        if (apiToken == null) {
            return "API_TOKEN не присвоен";
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/load" + key + "?API_TOKEN=" + apiToken))
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return "Ошбика получения запроса";
    }

}



/*
При создании KVTaskClient учтите следующее:
Конструктор принимает URL к серверу хранилища и регистрируется. При регистрации выдаётся токен (API_TOKEN), который нужен при работе с сервером.
Метод void put(String key, String json) должен сохранять состояние менеджера задач через запрос POST /save/<ключ>?API_TOKEN=.
Метод String load(String key) должен возвращать состояние менеджера задач через запрос GET /load/<ключ>?API_TOKEN=.
 */