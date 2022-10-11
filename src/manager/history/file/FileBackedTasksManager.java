package manager.history.file;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.history.HistoryManager;
import manager.history.ManagerSaveException;
import manager.task.InMemoryTaskManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {

       private final static String PATH = "resources\\data.csv";
    private final static String HEAD = "id,type,title,description,status,duration,startTime,endTime,epic\n";
    public static FileBackedTasksManager loadedFromFileTasksManager () {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager();
        fileBackedTasksManager.loadFromFile();
        return fileBackedTasksManager;
    }

    public Map<Integer, Task> allTasks = new HashMap<>();

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
    public int update(Epic epic) {
        int id = super.update(epic);
        save();
        return id;
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
        List<Integer> subtasksInEpic = epicHash.get(id).getSubtaskId();
        for (int subtaskId : subtasksInEpic) {
            prioritizedTasks.remove(subEpicHash.get(subtaskId));
        }
        prioritizedTasks.remove(epicHash.get(id));
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubtaskById(Integer id) {
        prioritizedTasks.remove(subEpicHash.get(id));
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



    public Map<Integer, Task> getAllTasks() {
        return allTasks;
    }


    public void loadFromFile() {
//Map<Integer, Task> allTasks = new HashMap<>();

        try {
            String[] lines = Files.readString(Path.of(PATH), StandardCharsets.UTF_8).split(System.lineSeparator());
            if (lines.length < 2) return;
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                String[] lineContents = line.split(",");
                if (lineContents.length >= 5) {
                    if (lineContents[1].equals("TASK")) {
                        int id = Integer.parseInt(lineContents[0]);
                        String title = lineContents[2];
                        TaskStatus status = Enum.valueOf(TaskStatus.class, lineContents[3]);
                        String description = lineContents[4];
                        long duration = Long.parseLong(lineContents[6]);
                        LocalDateTime startTime = null;
                        if (!lineContents[5].equals("null")) startTime = LocalDateTime.parse(lineContents[5]);
                        Task task = new Task(id, Types.TASK, title, status, description, startTime, duration);
                        if (!lineContents[6].equals("null")) task.setEndTime(task.getStartTime().plusMinutes(duration));
                        taskArray.put(id, task);
                        if (getIdCounter() <= id) setIdCounter(++id);
                        prioritizedTasks.add(task);
                    }
                    if (lineContents[1].equals("EPIC")) {
                        int id = Integer.parseInt(lineContents[0]);
                        String title = lineContents[2];
                        TaskStatus status = Enum.valueOf(TaskStatus.class, lineContents[3]);
                        String description = lineContents[4];
                        Epic epic = new Epic(id, Types.EPIC, title, status, description);
                        epicHash.put(id, epic);
                        for (SubTask subtask : subEpicHash.values()) {
                            if (epicHash.containsKey(subtask.getEpicId())) {
                                epicHash.get(subtask.getEpicId()).getSubtaskId().add(subtask.getId());
                            }
                        }
                        getEpicTimesAndDuration(epicHash.get(id));
                        if (getIdCounter() <= id) setIdCounter(++id);
                    }
                    if (lineContents[1].equals("SUBTASK")) {
                        int id = Integer.parseInt(lineContents[0]);
                        String title = lineContents[2];
                        TaskStatus status = Enum.valueOf(TaskStatus.class, lineContents[3]);
                        String description = lineContents[4];
                        long duration = Long.parseLong(lineContents[6]);
                        LocalDateTime startTime = null;
                        if (!lineContents[5].equals("null")) startTime = LocalDateTime.parse(lineContents[5]);
                        int epicId = Integer.parseInt(lineContents[8]);
                        SubTask subtask = new SubTask(id, Types.SUBTASK, title, status, description, epicId, startTime, duration);
                        if (!lineContents[6].equals("null")) subtask.setEndTime(LocalDateTime.parse(lineContents[7]));
                        subEpicHash.put(id, subtask);
                        if (epicHash.containsKey(epicId)) {
                            epicHash.get(epicId).getSubtaskId().add(id);
                        }
                        if (getIdCounter() <= id) setIdCounter(++id);
                        prioritizedTasks.add(subtask);
                        if (epicHash.containsKey(subtask.getEpicId())) {
                            getEpicTimesAndDuration(epicHash.get(subtask.getEpicId()));
                        }
                    }

                }
                allTasks.putAll(getTasks());
                allTasks.putAll(getEpics());
                allTasks.putAll(getSubtasks());
                if (lines.length < 4) return;
                if (lines[lines.length - 2].isBlank()) {
                    String historyLine = lines[lines.length - 1];
                    String[] historyLineContents = historyLine.split(",");
                    for (String s : historyLineContents) {

                        historyManager.addHistory(allTasks.get(Integer.parseInt(s)));
                    }
                }
            }
        } catch (IOException err) {
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
                String.valueOf(task.getStartTime()),
                String.valueOf(task.getDuration()),
                String.valueOf(task.getEndTime()),
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
