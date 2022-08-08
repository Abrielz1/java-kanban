package constructor;

import manager.TaskManager;

import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskId;

    public Epic(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }


    public List<Integer> getSubtaskId() {
        return subTaskId;
    }

    public void setSubtaskId(List<Integer> subtaskId) {
        this.subTaskId = subtaskId;
    }

    @Override
    public String toString() {
        return "Constructors.Epic{" +
                "subTaskId=" + getSubtaskId() +
                ", id=" + getId() +
                ", name='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}

