import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.TaskStatus;
import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager inMemoryTaskManager = new InMemoryTaskManager();
        HistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

        Epic e1 = new Epic(1, "Накормить коте", "Важнейшее", TaskStatus.NEW);
        SubTask s1 = new SubTask(2, "Заставить себя", "Трудно", TaskStatus.NEW, 1);
        SubTask s2 = new SubTask(3, "Пойти в магазин", "Очиння дорого", TaskStatus.NEW, 1);
        Epic e2 = new Epic(4, "Накормить Коте", "Проверить есть ли СВЕЖАЯ вода", TaskStatus.NEW);
        SubTask s3 = new SubTask(5, "Насыпать корм", "Успеть убежать от миски, затопчет", TaskStatus.NEW, 2);
        Task t1 = new Task(0, "", "", TaskStatus.NEW);

        inMemoryTaskManager.add(t1);
        inMemoryTaskManager.getHistoryManager();
        inMemoryHistoryManager.getHistory();

        inMemoryTaskManager.add(e1);
        inMemoryTaskManager.getHistoryManager();
        inMemoryHistoryManager.getHistory();

        inMemoryTaskManager.add(s1);
        inMemoryTaskManager.getHistoryManager();
        inMemoryHistoryManager.getHistory();

        inMemoryTaskManager.getTaskById(t1.getId());
        inMemoryTaskManager.purgeAllTask();

    }
}
