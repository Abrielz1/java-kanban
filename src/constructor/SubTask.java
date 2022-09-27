package constructor;

import constructor.status.TaskStatus;
import manager.history.file.Types;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(String name, TaskStatus status, String description, int epicId, LocalDateTime startTime, Duration duration) {
        super(name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(int id, Types taskType, String name, TaskStatus status, String description, int epicId, LocalDateTime startTime, Duration duration) {
        super(id, taskType, name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return id + "," +
                Types.SUBTASK + "," +
                name + "," +
                taskStatus + "," +
                description + "," +
                startTime + "," +
                duration + "," +
                epicId;
    }
}
