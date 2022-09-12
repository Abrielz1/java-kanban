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

public class FileBackedTasksManager extends InMemoryTaskManager implements TaskManager {
    public static void main(String[] args) {
        TaskManager manager = new FileBackedTasksManager();
        //================================================================================
        manager.add(new Task("Погладить кота", "поймать его", TaskStatus.NEW));
        manager.add(new Task("Убраться в доме", "заставить себя", TaskStatus.IN_PROGRESS));
        //================================================================================
        manager.add(new Epic("Накормить коте", "Важнейшее", TaskStatus.NEW));
        manager.add(new SubTask("Заставить себя", "Трудно", TaskStatus.NEW, 1));
        manager.add(new SubTask("Пойти в магазин", "Купить корм", TaskStatus.NEW, 1));
        manager.add(new SubTask("Купить корм", "Выбрать корм", TaskStatus.IN_PROGRESS, 1));
        //================================================================================
        manager.add(new Epic("Накормить Коте", "Проверить есть ли СВЕЖАЯ вода", TaskStatus.NEW));
        manager.add(new SubTask("Насыпать корм", "Успеть убежать от миски, затопчет", TaskStatus.NEW, 2));
        //================================================================================

        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getEpicById(3);
        manager.getEpicById(3);
        manager.getSubtaskById(4);
        manager.getSubtaskById(5);
        System.out.println(manager);

    }

    private FileBackedTasksManager() {

    }

    @Override
    public int add(Task task) {
        save();
        return super.add(task);

    }

    @Override
    public int add(Epic epic) {
        save();
        return super.add(epic);
    }

    @Override
    public int add(SubTask subtask) {
        save();
        return super.add(subtask);
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


    private void save() {
        try {
            Path path = Path.of("resources\\file.csv");
            String head = "id,type,name,status,description,epic" + System.lineSeparator();
            String data = head +
                    taskToString(this) + System.lineSeparator() +
                    historyToString(historyManager);
            Files.writeString(path, data);
        } catch (IOException e) {
                throw new ManagerSaveException("Ошибка, при восстановлении из файла произошел сбой!");
        }
    }

    private void loadFromFile() {
        String content = readFileContentsOrNull("resources\\file.csv");
        if (content != null) {
            String[] lines = content.split("\r?\n");
            for (int j = 1; j < lines.length; j++) {
                String line = lines[j];
                if (line.length() != 0 & line.length() > 3) {
                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0]);
                    String type = parts[1];
                    String name = parts[2];
                    TaskStatus status = TaskStatus.valueOf(parts[3]);
                    String description = parts[4];
                    int idEpic = 0;
                    if (line.length() > 4) {
                        idEpic = Integer.parseInt(parts[5]);
                    }
                    if (type.equals("Task")) {
                        Task task1 = new Task(name, description, status);
                        task1.setId(id);
                        task1.setStatus(status);
                        taskArray.put(id, task1);
                    }
                    if (type.equals("Epic")) {
                        Epic epic1 = new Epic(name, description, status);
                        epic1.setId(id);
                        epic1.setStatus(status);
                        epicHash.put(id, epic1);
                    }
                    if (type.equals("SubTask")) {
                        SubTask subTask1 = new SubTask(name, description, status, idEpic);
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

    private static String historyToString(HistoryManager manager) {
        List<Task> list = manager.getHistory();
        StringBuilder builder = new StringBuilder();
        for (Task task : list) {
            builder.append(task.getId()).append(",");
        }
        if (builder.length() > 0) {
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();
    }

    private String taskToString(TaskManager  taskmanager) {
       List<Task> taskDump = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        taskDump.addAll(taskArray.values());
        taskDump.addAll(epicHash.values());
        taskDump.addAll(subEpicHash.values());
        for (Task task1 : taskDump) {
            sb.append(task1.toString()).append(",").append(System.lineSeparator());
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

    static class ManagerSaveException extends Error {
        public ManagerSaveException(final String message) {
            super(message);
        }
    }
}
