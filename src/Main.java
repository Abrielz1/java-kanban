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
////================================================================================
//        taskManager.add(new Epic("Накормить коте", "Важнейшее", TaskStatus.NEW));
//        taskManager.add(new SubTask("Заставить себя", "Трудно", TaskStatus.NEW, 1));
//        taskManager.add(new SubTask("Пойти в магазин", "Купить корм", TaskStatus.NEW, 1));
//        taskManager.add(new SubTask("Купить корм", "Выбрать корм", TaskStatus.IN_PROGRESS, 1));
////================================================================================
//        taskManager.add(new Epic("Накормить Коте", "Проверить есть ли СВЕЖАЯ вода", TaskStatus.NEW));
//        taskManager.add(new SubTask("Насыпать корм", "Успеть убежать от миски, затопчет", TaskStatus.NEW, 2));
////================================================================================
//        taskManager.add(new Epic("Пойти в магазин", "Покупки", TaskStatus.NEW));
////================================================================================
//        taskManager.add(new Task("Погладить кота", "поймать его", TaskStatus.NEW));
//        taskManager.add(new Task("Убраться в доме", "заставить себя", TaskStatus.IN_PROGRESS));
////================================================================================
//        taskManager.update(new SubTask("Заставить себя", "Трудно", TaskStatus.IN_PROGRESS, 1));
//
//        taskManager.getEpicById(1);
//        taskManager.getSubtaskById(2);
//        taskManager.getSubtaskById(2);
//        taskManager.getSubtaskById(3);
//        taskManager.getEpicById(5);
//        taskManager.getEpicById(7);
//        taskManager.getTaskById(8);
//        taskManager.getTaskById(8);
//        taskManager.getTaskById(9);
//        taskManager.removeEpicById(7);
//        taskManager.removeEpicById(1);
//        taskManager.removeEpicById(5);
    }
}
