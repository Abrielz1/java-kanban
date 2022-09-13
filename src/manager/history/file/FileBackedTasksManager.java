package manager.history.file;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.history.HistoryManager;
import manager.task.InMemoryTaskManager;
import manager.task.TaskManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static void main(String[] args) {
        TaskManager manager = new FileBackedTasksManager();
        FileBackedTasksManager managers = new FileBackedTasksManager();
//================================================================================
        manager.add(new Task("Погладить кота",TaskStatus.NEW,  "поймать его"));
        manager.add(new Task("Убраться в доме", TaskStatus.IN_PROGRESS, "заставить себя"));
//================================================================================
        manager.add(new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее"));
        manager.add(new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 1));
        manager.add(new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 1));
        manager.add(new SubTask("Купить корм",TaskStatus.IN_PROGRESS, "Выбрать корм", 1));
//================================================================================
        manager.add(new Epic("Накормить Коте", TaskStatus.NEW, "Проверить есть ли СВЕЖАЯ вода"));
        manager.add(new SubTask("Насыпать корм", TaskStatus.NEW, "Успеть убежать от миски затопчет", 2));
//================================================================================
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getEpicById(3);
        manager.getSubtaskById(4);
        manager.getSubtaskById(5);
        System.out.println(manager);
     //   managers.loadFromFile();
    }

    private FileBackedTasksManager() {

    }

    @Override
    public int add(Task task) {
        int id = super.add(task);
        save();
        return id;

    }

    @Override
    public int add(Epic epic) {
        int id = super.add(epic);
        save();
        return id;
    }

    @Override
    public int add(SubTask subtask) {
        int id = super.add(subtask);
        save();
        return id;
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void update(SubTask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return  epic;
    }

    @Override
    public SubTask getSubtaskById(int id) {
        SubTask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllEpicsAndSubtasks() {
        super.removeAllEpicsAndSubtasks();
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();

    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        super.removeSubtaskById(id);
        save();
    }


    public void save() {
        try {
            Path path = Path.of("resources\\file.csv");
            final String head = "id,type,name,status,description,epic" + System.lineSeparator();

            String data = head +
                    taskToString(this) + System.lineSeparator() +
                    HistoryManager.historyToString(historyManager);
            Files.writeString(path, data);
        } catch (IOException e) {
                throw new ManagerSaveException("Ошибка, при записи файла произошел сбой!");
        }
    }

    public void loadFromFile() {
        String content = readFileContentsOrNull("resources\\file.csv");
        if (content != null) {
            String[] lines = content.split("\r?\n");
            for (int j = 1; j < lines.length; j++) {
                String line = lines[j];
                if (line.length() != 0 & line.length() > 3) {
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    Types type = Types.valueOf(parts[1]);
                    String name = parts[2];
                    TaskStatus status = TaskStatus.valueOf(parts[3]);
                    String description = parts[4];
                    int idEpic = 0;
                    if (line.length() > 3) {
                        idEpic = Integer.parseInt(parts[5]);
                    }
                    if (type.equals("Task")) {
                        Task task1 = new Task(name, status, description);
                        task1.setId(id);
                        task1.setStatus(status);
                        taskArray.put(id, task1);
                    }
                    if (type.equals("Epic")) {
                        Epic epic1 = new Epic(name, status, description);
                        epic1.setId(id);
                        epic1.setStatus(status);
                        epicHash.put(id, epic1);
                    }
                    if (type.equals("SubTask")) {
                        SubTask subTask1 = new SubTask(name, status, description, idEpic);
                        subTask1.setId(id);
                        subTask1.setStatus(status);
                        subEpicHash.put(id, subTask1);
                    }
                } else {
                    String[] parts = line.split(",");
                    for (String str : parts) {
                        int tempId = Integer.parseInt(str);
                        historyManager.addHistory(taskArray.get(tempId));
                    }
                }
            }
        }
    }

//    private static String historyToString(HistoryManager manager) {
//        List<Task> list = manager.getHistory();
//        if (list.isEmpty()) {
//            return "";
//        }
//        StringBuilder builder = new StringBuilder();
//        for (Task task : list) {
//            builder.append(task.getId()).append(",");
//        }
//        if (builder.length() > 0) {
//            builder.setLength(builder.length() - 1);
//        }
//        return builder.toString();
//    }

    private String toString(Task task) {
        return String.join(",",
                String.valueOf(task.getId()),
                task.getClass().getSimpleName().toUpperCase(),
                task.getTitle(),
                String.valueOf(task.getStatus()),
                task.getDescription(),
                String.valueOf(task instanceof SubTask ? ((SubTask) task).getEpicId() : "")
        );
    }

    private String taskToString(TaskManager  taskmanager) {
       List<Task> taskDump = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        taskDump.addAll(taskArray.values());
        taskDump.addAll(epicHash.values());
        taskDump.addAll(subEpicHash.values());
        for (Task task1 : taskDump) {
            //sb.append(task1.toString()).append(",").append(System.lineSeparator());
            sb.append(toString(task1)).append(System.lineSeparator());
        }
        return sb.toString();
    }

    private String readFileContentsOrNull(String path) {
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл!");
            return null;
        }
    }

    static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(final String message) {
            super(message);
        }
    }
}
