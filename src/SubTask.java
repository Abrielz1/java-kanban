import java.util.*;

public class SubTask extends Task {

    protected int epicId;

    public SubTask(String name, String description, String status, int epicID) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
