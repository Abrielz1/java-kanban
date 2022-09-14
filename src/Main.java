import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.Managers;
import manager.history.file.FileBackedTasksManager;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager taskManager = Managers.getDefaultFileManager();

        //taskManager.loadFromFile();
    }
}
