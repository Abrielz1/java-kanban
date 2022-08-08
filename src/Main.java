import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.TaskStatus;
import manager.InMemoryHistoryManager;
import manager.InMemoryTaskManager;
import manager.TaskManager;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();

        Epic e1 = new Epic ("Накормить коте", "Важнейшее", TaskStatus.NEW);
        SubTask s1 = new SubTask("Заставить себя", "Трудно", TaskStatus.NEW, 1);
        SubTask s2 = new SubTask("Пойти в магазин", "Очиння дорого", TaskStatus.NEW, 1);
        Epic e2 = new Epic("Накормить Коте", "Проверить есть ли СВЕЖАЯ вода", TaskStatus.NEW);
        SubTask s3 = new SubTask("Насыпать корм", "Успеть убежать от миски, затопчет", TaskStatus.NEW, 4);
        Task t1 = new Task("","", TaskStatus.NEW);
        inMemoryTaskManager.add(e1);
        inMemoryTaskManager.add(s1);
        inMemoryTaskManager.add(s2);
        inMemoryTaskManager.add(e2);
        inMemoryTaskManager.add(e1);
        inMemoryTaskManager.add(s3);
        inMemoryTaskManager.add(t1);

        inMemoryTaskManager.add(e1);
        inMemoryTaskManager.add(e2);

        System.out.println();

    }
}
