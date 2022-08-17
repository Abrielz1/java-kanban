import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.*;
import manager.history.HistoryManager;
import manager.history.InMemoryHistoryManager;
import manager.task.InMemoryTaskManager;
import manager.task.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new InMemoryTaskManager();
        TaskManager inMemoryTaskManager = Managers.getDefaultTask();
        HistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        TaskManager task = new InMemoryTaskManager();

        task.add(new Epic(InMemoryTaskManager.getIdCounter(), "Накормить коте", "Важнейшее", TaskStatus.NEW));
        task.add(new Epic(InMemoryTaskManager.getIdCounter(), "Накормить коте", "Важнейшее", TaskStatus.NEW));

        task.add(new SubTask(InMemoryTaskManager.getIdCounter(), "Заставить себя", "Трудно", TaskStatus.NEW, 1));
        task.add(new SubTask(InMemoryTaskManager.getIdCounter(), "Пойти в магазин", "Купить корм", TaskStatus.NEW, 1));

        task.add(new Epic(InMemoryTaskManager.getIdCounter(), "Накормить Коте", "Проверить есть ли СВЕЖАЯ вода", TaskStatus.NEW));
        task.add(new SubTask(InMemoryTaskManager.getIdCounter(), "Насыпать корм", "Успеть убежать от миски, затопчет", TaskStatus.NEW, 2));

        task.add(new Task(InMemoryTaskManager.getIdCounter(), "Погладить кота", "", TaskStatus.NEW));

        task.update(new SubTask(InMemoryTaskManager.getIdCounter(), "Заставить себя", "Трудно", TaskStatus.IN_PROGRESS, 1));
    }
}
