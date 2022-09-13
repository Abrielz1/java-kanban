package constructor;

import constructor.status.TaskStatus;
import manager.history.file.Types;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTaskId;


    public Epic(String name, TaskStatus status, String description) {
        super(name, status, description);
        subTaskId = new ArrayList<>();
    }

    public Epic(int id, Types taskType, String name, TaskStatus status, String description) {
        super(id, taskType, name, status, description);
        subTaskId = new ArrayList<>();
    }

    public List<Integer> getSubtaskId() {
        return subTaskId;
    }


    @Override
    public String toString() {
        return id + "," +
                Types.EPIC + "," +
                name + "," +
                taskStatus + "," +
                description;
    }
}

