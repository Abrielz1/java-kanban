package constructor;

import constructor.status.TaskStatus;
import manager.history.file.Types;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    int id;
    final String name;
    final String description;
    TaskStatus taskStatus;
    Types taskType;
    Duration duration;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public Task(String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.taskStatus = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, Types taskType, String name, TaskStatus status, String description, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.description = description;
        this.taskStatus = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return taskStatus;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return id + "," +
                Types.TASK + "," +
                name + "," +
                description + "," +
                startTime + "," +
                duration;
    }
}
