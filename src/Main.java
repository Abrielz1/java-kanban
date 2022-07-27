import java.util.*;

public class Main {

    public static void main(String[] args) {
        Manager taskManager = new Manager();
        //    Epic epic_1 = new Epic(0, "Переезд", "Переезд", "NEW");
        //    SubTask subTask = new SubTask(man.id,0,"Заказать", "Позвонить в транспортную компанию", "NEW");
        //   Task task = new Task(0, "Забираем Коте", "Самое главное", "NEW");
        Epic e1 = new Epic("Накормить коте", "Важнейшее", "NEW");
        SubTask s1 = new SubTask("Заставить себя", "Трудно", "IN_PROGRESS", 1);
        SubTask s2 = new SubTask("Пойти в магазин", "Очиння дорого", "NEW", 1);
        Epic e2 = new Epic("Накормить Коте", "Проверить есть ли СВЕЖАЯ вода", "NEW");
        SubTask s3 = new SubTask("Насыпать корм", "Успеть убежать от миски, затопчет", "IN_PROGRESS", 4);

    }
}
