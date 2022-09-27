
import manager.Managers;
import manager.history.file.FileBackedTasksManager;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager manager = Managers.getDefaultFileManager();

////================================================================================
//        manager.add(new Task("Погладить кота",TaskStatus.NEW,  "поймать его", LocalDateTime.of(2022, 9, 25, 13, 30, 15), Duration.ofMinutes(30)));
////        manager.add(new Task("Убраться в доме", TaskStatus.IN_PROGRESS, "заставить себя"));
////================================================================================
//        manager.add(new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее"));
//        manager.add(new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 3, LocalDateTime.now(), Duration.ofMinutes(30)));
//        manager.add(new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 3, LocalDateTime.of(2022, 9, 24, 10, 0, 15), Duration.ofMinutes(45)));
//        manager.add(new SubTask("Купить корм",TaskStatus.IN_PROGRESS, "Выбрать корм", 3, LocalDateTime.of(2022, 9, 25, 11, 0, 15), Duration.ofMinutes(120)));
////================================================================================
//        manager.add(new Epic("Накормить Коте", TaskStatus.NEW, "Проверить есть ли СВЕЖАЯ вода"));
//        manager.add(new SubTask("Насыпать корм", TaskStatus.NEW, "Успеть убежать от миски затопчет", 7));
////================================================================================
//        manager.getTaskById(1);
//        manager.getTaskById(1);
//        manager.getTaskById(2);
//        manager.getEpicById(3);
//        manager.getEpicById(3);
//        manager.getSubtaskById(4);
//        manager.getSubtaskById(5);
//        manager.getSubtaskById(6);
//        manager.getEpicById(7);
//        manager.getSubtaskById(8);
//        manager.getEpicById(3);
//        manager.removeTaskById(1);//
//        manager.add(new Task("Погладить кота",TaskStatus.NEW,  "поймать его"));
//        manager.removeEpicById(7);
//        manager.removeEpicById(3);
//        manager.removeSubtaskById(4);
//       manager.removeSubtaskById(5);
//        manager.removeSubtaskById(6);
//        manager.update(new SubTask("Пойти в магазин", TaskStatus.DONE, "Купить корм", 3));
//       System.out.println(manager);
//           manager.loadFromFile();

    }
}
