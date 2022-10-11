package tests;

import constructor.Epic;
import constructor.SubTask;
import constructor.Task;
import constructor.status.TaskStatus;
import manager.history.ManagerSaveException;
import manager.history.file.Types;
import manager.task.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    T manager;

    protected abstract T createManager();

    @BeforeEach
    void getManager() {
        manager = createManager();
    }

    @Test
    void addTaskAndGetTaskByIdTest() {
        Task task = new Task(1, Types.TASK.TASK, "Test task", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        manager.add(task);
        assertFalse(manager.getTasks().isEmpty());
        assertEquals(1, manager.getTasks().size());
        assertEquals(task, manager.getTaskById(1));
    }

    @Test
    void addEpicAndGetEpicByIdTest() {
        Epic epic = new Epic(1, Types.EPIC, "Epic", TaskStatus.NEW, "test description");
        manager.add(epic);
        assertFalse(manager.getEpics().isEmpty());
        assertEquals(1, manager.getEpics().size());
        assertEquals(epic, manager.getEpicById(1));
    }

    @Test
    void addSubtaskAndGetSubtaskIdTest() {
        SubTask subtask = new SubTask(1, Types.SUBTASK, "Test task", TaskStatus.NEW, "test description",
                0, LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        manager.add(subtask);
        assertFalse(manager.getSubtasks().isEmpty());
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(subtask, manager.getSubtaskById(1));
    }

    @Test
    void getAllTasksEpicsSubtasksTest() {
        Task task = new Task(1, Types.TASK, "Test task", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        Epic epic = new Epic(2, Types.EPIC, "Epic", TaskStatus.NEW, "test description");
        SubTask subtask = new SubTask(3, Types.SUBTASK, "Test task", TaskStatus.NEW, "test description",
                0, LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        manager.add(task);
        manager.add(epic);
        manager.add(subtask);
        assertEquals(1, manager.getTasks().size());
        assertEquals(1, manager.getEpics().size());
        assertEquals(1, manager.getSubtasks().size());
        assertEquals(task, manager.getTasks().get(1));
        assertEquals(epic, manager.getEpics().get(2));
        assertEquals(subtask, manager.getSubtasks().get(3));
    }

    @Test
    void updateTaskTest() {
        Task taskOld = new Task(1, Types.TASK, "TaskOld", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        Task taskNew = new Task(1, Types.TASK, "TaskNew", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        manager.add(taskOld);
        manager.update(taskNew);
        assertEquals("TaskNew", manager.getTaskById(1).getTitle());
    }

    @Test
    void updateEpicTest() {
        Epic epicOld = new Epic(1, Types.EPIC, "old epic", TaskStatus.NEW, "test description");
        Epic epicNew = new Epic(1, Types.EPIC, "new epic", TaskStatus.NEW, "test description");
        manager.add(epicOld);
        manager.update(epicNew);
        assertEquals("new epic", manager.getEpicById(1).getTitle());
    }

    @Test
    void updateSubtaskTest() {
        Epic epic = new Epic(1, Types.EPIC, "new epic", TaskStatus.NEW, "test description");
        SubTask subtaskOld = new SubTask(2, Types.SUBTASK, "old subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        SubTask subtaskNew = new SubTask(2, Types.SUBTASK, "new subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        manager.add(epic);
        manager.add(subtaskOld);
        manager.update(subtaskNew);
        assertEquals("new subtask", manager.getSubtaskById(2).getTitle());
    }

    @Test
    void updateEpicStatusBySubtasksTest() {
        Epic epic = new Epic(1, Types.EPIC, "new epic", TaskStatus.NEW, "test description");
        manager.add(epic);
        assertEquals(TaskStatus.NEW, manager.getEpics().get(1).getStatus());

        manager.add(new SubTask(2, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.add(new SubTask(3, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30));

        assertEquals(TaskStatus.NEW, manager.getEpics().get(1).getStatus());

        manager.add(new SubTask(4, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.update(new SubTask(2, Types.SUBTASK, "new subtask", TaskStatus.IN_PROGRESS, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpics().get(1).getStatus());

        manager.update(new SubTask(2, Types.SUBTASK, "new subtask", TaskStatus.DONE, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.update(new SubTask(3, Types.SUBTASK, "new subtask", TaskStatus.DONE, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        assertEquals(TaskStatus.IN_PROGRESS, manager.getEpics().get(1).getStatus());

        manager.update(new SubTask(4, Types.SUBTASK, "subtask", TaskStatus.DONE, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        assertEquals(TaskStatus.DONE, manager.getEpics().get(1).getStatus());
    }

    @Test
    void removeAllTasksTest() {
        manager.add(new Task(1, Types.TASK, "Task1", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.add(new Task(2, Types.TASK, "Task2", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.removeAllTasks();
        assertTrue(manager.getTasks().isEmpty());
    }

    @Test
    void removeAllEpicsAndSubtasksTest() {
        manager.add(new Epic(1, Types.EPIC, "new epic", TaskStatus.NEW, "test description"));
        manager.add(new SubTask(2, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.add(new SubTask(3, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.add(new SubTask(4, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.removeAllEpicsAndSubtasks();
        assertTrue(manager.getEpics().isEmpty() && manager.getSubtasks().isEmpty());
    }

    @Test
    void removeTaskByIdAndEpicByIdAndSubtaskById() {
        manager.add(new Task(1, Types.TASK, "Task1", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.add(new Epic(2, Types.EPIC, "new epic", TaskStatus.NEW, "test description"));
        manager.add(new SubTask(3, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                2, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.removeTaskById(1);
        assertTrue(manager.getTasks().isEmpty());
        manager.removeSubtaskById(3);
        assertTrue(manager.getSubtasks().isEmpty());
        manager.removeEpicById(2);
        assertTrue(manager.getEpics().isEmpty());
    }

    @Test
    void getAllSubtasksByEpicTest() {
        manager.add(new Epic(1, Types.EPIC, "new epic", TaskStatus.NEW, "test description"));
        SubTask s1 = new SubTask(2, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        SubTask s2 = new SubTask(3, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        SubTask s3 = new SubTask(4, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        manager.add(s1);
        manager.add(s2);
        manager.add(s3);
        List<SubTask> testList = List.of(s1, s2, s3);
        for (int i = 0; i < manager.getAllSubtasksByEpic(1).size(); i++) {
            assertEquals(testList.get(i), manager.getAllSubtasksByEpic(1).get(i));
        }
    }

    @Test
    void getHistoryAndDuplicatesInHistoryTest() {
        manager.add(new Task(1, Types.TASK, "Task1", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.add(new Epic(2, Types.EPIC, "new epic", TaskStatus.NEW, "test description"));
        manager.add(new SubTask(3, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                2, LocalDateTime.of(2022, 9, 26, 18, 0), 30));

        manager.getTaskById(1);
        manager.getSubtaskById(3);
        manager.getEpicById(2);
        manager.getSubtaskById(3);
        assertEquals(3, manager.getHistory().size());
        for (int i = 0; i < manager.getHistory().size(); i++) {
            assertEquals(i + 1, manager.getHistory().get(i).getId());
        }
    }

    @Test
    void intersectionCheckTest() {
        manager.add(new Task(1, Types.TASK, "Task1", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        manager.add(new Epic(2, Types.EPIC, "new epic", TaskStatus.NEW, "test description"));
        manager.add(new SubTask(3, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                2, LocalDateTime.of(2022, 9, 26, 18, 0), 30));
        ManagerSaveException ex = Assertions.assertThrows(
                ManagerSaveException.class,
                () -> manager.intersectionCheck()
        );
        Assertions.assertEquals("Найдено пересечение времени задач, проверьте корректность данных", ex.getMessage());
    }

    @Test
    void getPrioritizedTasksTest() {
        Task task1 = new Task(1, Types.TASK, "task1", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        Task task2 = new Task(1, Types.TASK, "task1", TaskStatus.NEW, "test description",
                null, 0);
        SubTask subtask1 = new SubTask(3, Types.SUBTASK, "subtask1", TaskStatus.NEW, "test description",
                2, LocalDateTime.of(2022, 9, 27, 19, 0), 30);
        SubTask subtask2 = new SubTask(4, Types.SUBTASK, "subtask2", TaskStatus.NEW, "test description",
                2, LocalDateTime.of(2022, 9, 25, 20, 0), 30);
        manager.add(task1);
        manager.add(task2);
        manager.add(subtask1);
        manager.add(subtask2);
        List<Task> prioritizedTasksTest = new ArrayList<>();
        prioritizedTasksTest.add(subtask2);
        prioritizedTasksTest.add(task1);
        prioritizedTasksTest.add(subtask1);
        prioritizedTasksTest.add(task2);
        assertEquals(4, manager.getPrioritizedTasks().size());
        int i = 0;
        for (Task task : manager.getPrioritizedTasks()) {
            assertEquals(task, prioritizedTasksTest.get(i++));
        }
    }

    @Test
    void getTaskEndTimeTest() {
        Task task = new Task(1, Types.TASK, "task1", TaskStatus.NEW, "test description",
                LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        manager.getTaskEndTime(task);
        assertEquals(task.getStartTime().plusMinutes(30), task.getEndTime());
    }

    @Test
    void getSubtaskEndTimeTest() {
        SubTask subtask = new SubTask(3, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                2, LocalDateTime.of(2022, 9, 27, 19, 0), 30);
        manager.getSubtaskEndTime(subtask);
        assertEquals(subtask.getStartTime().plusMinutes(30), subtask.getEndTime());
    }

    @Test
    void getEpicTimesAndDurationTest() {
        manager.add(new Epic(1, Types.EPIC, "new epic", TaskStatus.NEW, "test description"));
        SubTask s1 = new SubTask(2, Types.SUBTASK, "epic end", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 20, 0), 30);
        SubTask s2 = new SubTask(3, Types.SUBTASK, "epic start", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        SubTask s3 = new SubTask(4, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 19, 0), 45);
        manager.add(s1);
        manager.add(s2);
        manager.add(s3);

        assertEquals(manager.getEpics().get(1).getStartTime(), manager.getSubtasks().get(3).getStartTime());
        assertEquals(manager.getEpics().get(1).getEndTime(), manager.getSubtasks().get(2).getEndTime());
        assertEquals(150, manager.getEpics().get(1).getDuration());
    }

    @Test
    void getHistoryTest() {
        Epic epic = new Epic(1, Types.EPIC, "new epic", TaskStatus.NEW, "test description");
        SubTask s1 = new SubTask(2, Types.SUBTASK, "epic end", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 20, 0), 30);
        SubTask s2 = new SubTask(3, Types.SUBTASK, "epic start", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 18, 0), 30);
        SubTask s3 = new SubTask(4, Types.SUBTASK, "subtask", TaskStatus.NEW, "test description",
                1, LocalDateTime.of(2022, 9, 26, 19, 0), 45);
        manager.add(epic);
        manager.add(s1);
        manager.add(s2);
        manager.add(s3);

        manager.getEpicById(1);
        manager.getSubtaskById(4);
        manager.getEpicById(1);
        manager.getSubtaskById(2);
        Task[] historyTest = {s3, epic, s1};

        Assertions.assertArrayEquals(historyTest, manager.getHistory().toArray());

    }
}
