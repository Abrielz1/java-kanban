import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.Managers;
import manager.history.InMemoryHistoryManager;
import manager.task.InMemoryTaskManager;
import manager.task.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = Managers.getDefaultTask();
//================================================================================
        taskManager.add(new Epic(InMemoryTaskManager.getIdCounter(), "Накормить коте", "Важнейшее", TaskStatus.NEW));
        taskManager.add(new SubTask(InMemoryTaskManager.getIdCounter(), "Заставить себя", "Трудно", TaskStatus.NEW, 1));
        taskManager.add(new SubTask(InMemoryTaskManager.getIdCounter(), "Пойти в магазин", "Купить корм", TaskStatus.NEW, 1));
//================================================================================
        taskManager.add(new Epic(InMemoryTaskManager.getIdCounter(), "Накормить Коте", "Проверить есть ли СВЕЖАЯ вода", TaskStatus.NEW));
        taskManager.add(new SubTask(InMemoryTaskManager.getIdCounter(), "Насыпать корм", "Успеть убежать от миски, затопчет", TaskStatus.NEW, 2));
//================================================================================
        taskManager.add(new Task(InMemoryTaskManager.getIdCounter(), "Погладить кота", "", TaskStatus.NEW));
//================================================================================
        taskManager.update(new SubTask(2, "Заставить себя", "Трудно", TaskStatus.IN_PROGRESS, 1));

 

    }
}
