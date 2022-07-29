import java.util.*;

public class Epic extends Task {
    protected ArrayList<Integer> subTaskId;


    Epic(String name, String description, String status) {
        super(name, description, status);
        subTaskId = new ArrayList<>();
    }


    public ArrayList<Integer> getSubtaskId() {
        return subTaskId;
    }

    public void setSubtaskId(ArrayList<Integer> subtaskId) {
        this.subTaskId = subtaskId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskId=" + subTaskId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
