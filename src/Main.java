
import manager.Managers;
import manager.history.file.FileBackedTasksManager;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager taskManager = Managers.getDefaultFileManager();

        taskManager.loadFromFile();
    }
}
