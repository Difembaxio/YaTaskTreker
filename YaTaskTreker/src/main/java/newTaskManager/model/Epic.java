package newTaskManager.model;





import java.util.ArrayList;


public class Epic extends Task {

    private ArrayList<Integer> subtasksId;


    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
        this.subtasksId = new ArrayList<>();
    }

    public Epic(String name, String description) {
        super(name, description);
        this.subtasksId = new ArrayList<>();
    }

    public void addSubtask(int subtaskId) {
        subtasksId.add(subtaskId);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasksId;
    }

    public void setSubtasks(ArrayList<Integer> subtasks) {
        this.subtasksId = subtasks;
    }


    public String toString() {
        return String.format("%d,%s,%s,%s,%s", getId(), NameTasks.EPIC, getName(), getStatus(), getDescription());
    }
}
