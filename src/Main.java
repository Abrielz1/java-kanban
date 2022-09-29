
import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.Managers;
import manager.history.file.FileBackedTasksManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager manager = Managers.getDefaultFileManager();

////================================================================================
//        manager.add(new Task("Погладить кота", TaskStatus.NEW,  "поймать его", LocalDateTime.of(2022, 9, 25, 13, 30, 15), Duration.ofMinutes(30)));
////================================================================================
//        manager.add(new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее"));
//        manager.add(new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 2, LocalDateTime.now(), Duration.ofMinutes(30)));
//        manager.add(new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 2, LocalDateTime.of(2022, 9, 24, 10, 0, 15), Duration.ofMinutes(45)));
//        manager.add(new SubTask("Купить корм",TaskStatus.IN_PROGRESS, "Выбрать корм", 2, LocalDateTime.of(2022, 9, 25, 11, 0, 15), Duration.ofMinutes(120)));
////================================================================================

           manager.loadFromFile();

    }
}
