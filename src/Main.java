import java.util.*;

public class Main {

    public static void main(String[] args) {
        Manager taskManager = new Manager();
        Epic e1 = new Epic("Накормить коте", "Важнейшее", "NEW");
        SubTask s1 = new SubTask("Заставить себя", "Трудно", "IN_PROGRESS", 1);
        SubTask s2 = new SubTask("Пойти в магазин", "Очиння дорого", "NEW", 1);
        Epic e2 = new Epic("Накормить Коте", "Проверить есть ли СВЕЖАЯ вода", "NEW");
        SubTask s3 = new SubTask("Насыпать корм", "Успеть убежать от миски, затопчет", "IN_PROGRESS", 4);

        taskManager.add(e1);
        taskManager.add(s1);
        taskManager.add(s2);
        taskManager.add(e2);
        taskManager.add(e1);
        taskManager.add(s3);

    }
}
