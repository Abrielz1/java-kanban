package constructor;

import constructor.status.TaskStatus;
import manager.history.file.Types;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subTaskId;


    public Epic(String name, String description, TaskStatus status) {
        super(name, description, status);
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

