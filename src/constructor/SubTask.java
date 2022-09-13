package constructor;

import constructor.status.TaskStatus;
import manager.history.file.Types;

public class SubTask extends Task {

    private final int epicId;


    public SubTask(String name, TaskStatus status, String description, int epicID) {
        super(name, status, description);
        this.epicId = epicID;
    }

    public SubTask(int id, Types taskType, String name, TaskStatus status, String description, int epicId) {
        super(id, taskType, name, status, description);
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
                epicId;
    }
}
