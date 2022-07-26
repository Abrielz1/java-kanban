import java.util.*;

public class Main {

    public static void main(String[] args) {
        Manager man = new Manager();
        Epic epic = new Epic(0, "Переезд", "Переезд", "NEW");
        SubTask subTask = new SubTask(man.id,0,"Заказать", "Позвонить в транспортную компанию", "NEW");
        Task task = new Task(0, "Забираем Коте", "Самое главное", "NEW");

        //   man.addEpic();
    man.printEpic();


    man.addEpic(epic);
    man.addSubEpic(subTask);
    man.addTask(task);
    }
}
