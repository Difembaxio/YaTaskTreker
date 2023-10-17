package newTaskManager.model;

public class SimpleTask extends Task {

       public SimpleTask(int id, String name, String description, Status status) {
        super(id, name, description, status);

    }


    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s", getId(), NameTasks.SIMPLETASK, getName(), getStatus(), getDescription(), getName());
    }
}
