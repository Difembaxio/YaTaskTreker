package newTaskManager.historyManager;

import newTaskManager.model.Task;

import java.util.List;

public interface HistoryManager {
    public void add(Task task);
    public List<Task> getHistory();
    void remove(int id);
}
