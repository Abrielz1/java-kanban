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
    long duration;
    LocalDateTime startTime;
    LocalDateTime endTime;

    public Task(String name, TaskStatus status, String description, LocalDateTime startTime, long duration) {
        this.name = name;
        this.description = description;
        this.taskStatus = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(int id, Types taskType, String name, TaskStatus status, String description, LocalDateTime startTime, long duration) {
        this.id = id;
        this.taskType = taskType;
        this.name = name;
        this.description = description;
        this.taskStatus = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Types getTaskType() {
        return taskType;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
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
//@Override
//public String toString() {
//    return "Task{" +
//            "id=" + id +
//            ", type=" + taskType +
//            ", title='" + name + '\'' +
//            ", description='" + description + '\'' +
//            ", status=" + taskStatus +
//            ", duration=" + duration +
//            ", startTime=" + startTime +
//            ", endTime=" + endTime +
//            '}';
//}

}
