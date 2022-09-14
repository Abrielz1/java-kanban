package manager.history.file;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.Managers;
import manager.history.HistoryManager;
import manager.history.ManagerSaveException;
import manager.task.InMemoryTaskManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    public static void main(String[] args) {
        FileBackedTasksManager manager = Managers.getDefaultFileManager();

////================================================================================
//        manager.add(new Task("Погладить кота",TaskStatus.NEW,  "поймать его"));
//        manager.add(new Task("Убраться в доме", TaskStatus.IN_PROGRESS, "заставить себя"));
////================================================================================
//        manager.add(new Epic("Накормить коте", TaskStatus.NEW, "Важнейшее"));
//        manager.add(new SubTask("Заставить себя", TaskStatus.NEW, "Трудно", 3));
//        manager.add(new SubTask("Пойти в магазин", TaskStatus.NEW, "Купить корм", 3));
//        manager.add(new SubTask("Купить корм",TaskStatus.IN_PROGRESS, "Выбрать корм", 3));
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
//        manager.getEpicById(3);
//
//        System.out.println(manager);
       manager.loadFromFile();
    }


    private final static String PATH = "resources\\data.csv";

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
        return epic;
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
            Path path = Path.of(PATH);
            final String head = "id,type,name,status,description,epic" + System.lineSeparator();

            String data = head +
                    taskToString() + System.lineSeparator() +
                    HistoryManager.historyToString(historyManager);
            Files.writeString(path, data);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка, при записи файла произошел сбой!");
        }
    }


    public void loadFromFile() {

        Map<Integer, Task> allTasks = new HashMap<>();
        try {
            String[] lines = Files.readString(Path.of(PATH), StandardCharsets.UTF_8).split(System.lineSeparator());
            if (lines.length < 2) return;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                String[] lineContents = line.split(",");
                if (lineContents.length >= 5) {
                    if (lineContents[1].equals("TASK")) {
                        int id = Integer.parseInt(lineContents[0]);
                        Types types = Enum.valueOf(Types.class, lineContents[1]);
                        String title = lineContents[2];
                        TaskStatus status = Enum.valueOf(TaskStatus.class, lineContents[3]);
                        String description = lineContents[4];
                        this.taskArray.put(id, new Task(id, types, title, status, description));
                        if (getIdCounter() <= id) setIdCounter(++id);
                    }
                    if (lineContents[1].equals("EPIC")) {
                        int id = Integer.parseInt(lineContents[0]);
                        Types types = Enum.valueOf(Types.class, lineContents[1]);
                        String title = lineContents[2];
                        TaskStatus status = Enum.valueOf(TaskStatus.class, lineContents[3]);
                        String description = lineContents[4];
                        this.epicHash.put(id, new Epic(id, types, title, status, description));
                        if (getIdCounter() <= id) setIdCounter(++id);
                    }
                    if (lineContents[1].equals("SUBTASK")) {
                        int id = Integer.parseInt(lineContents[0]);
                        Types types = Enum.valueOf(Types.class, lineContents[1]);
                        String title = lineContents[2];
                        TaskStatus status = Enum.valueOf(TaskStatus.class, lineContents[3]);
                        String description = lineContents[4];
                        int epicId = Integer.parseInt(lineContents[5]);
                        this.subEpicHash.put(id, new SubTask(id, types, title, status, description, epicId));
                        if (getIdCounter() <= id) setIdCounter(++id);
                    }
                }

            }
            allTasks.putAll(getTasks());
            allTasks.putAll(getEpics());
            allTasks.putAll(getSubtasks());
            if (lines[lines.length - 2].isBlank() && lines.length >= 4) {
                String historyLine = lines[lines.length - 1];
                String[] historyLineContents = historyLine.split(",");
                for (String s : historyLineContents) {
                    historyManager.addHistory(allTasks.get(Integer.parseInt(s)));
                }
            }
        }
        catch(IOException err) {
            throw new ManagerSaveException("Ошибка при восстановлении данных");
        }
    }

    private String toString(Task task) {
        return String.join(",",
                String.valueOf(task.getId()),
                task.getClass().getSimpleName().toUpperCase(),
                task.getTitle(),
                String.valueOf(task.getStatus()),
                task.getDescription(),
                String.valueOf(task instanceof SubTask ? ((SubTask) task).getEpicId() : ""));
    }

    private String taskToString() {
        List<Task> taskDump = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        taskDump.addAll(taskArray.values());
        taskDump.addAll(epicHash.values());
        taskDump.addAll(subEpicHash.values());
        for (Task task1 : taskDump) {
            sb.append(toString(task1)).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
