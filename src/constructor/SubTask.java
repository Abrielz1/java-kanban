package constructor;

import constructor.status.TaskStatus;
import manager.history.file.Types;

public class SubTask extends Task {

    private final int epicId;


    public SubTask(String name, String description, TaskStatus status, int epicID) {
        super(name, description, status);
        this.epicId = epicID;
    }

    public int getEpicId() {
        return epicId;
    }


    @Override
    public String toString() {
        return "Constructors.SubTask{" +
                "epicId=" + getId() +
                ", id=" + getId()  +
                ", type='" + Types.SubTask +
                ", name='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                '}';
    }
}
