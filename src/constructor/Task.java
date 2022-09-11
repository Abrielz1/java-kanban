package constructor;

import constructor.status.TaskStatus;
import manager.history.file.Types;

public class Task {

    private int id;
    private final String name;
    private final String description;
    private TaskStatus taskStatus;

    public Task(String name, String description, TaskStatus status) {

        this.name = name;
        this.description = description;
        this.taskStatus = status;
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

    @Override
    public String toString() {
        return "Constructors.Task{" +
                "id=" + id +
                ", type='" + Types.Task +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + taskStatus + '\'' +
                '}';
    }
}
