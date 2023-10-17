package newTaskManager.memoryManager;

import newTaskManager.model.Epic;
import newTaskManager.model.SimpleTask;
import newTaskManager.model.Subtask;
import newTaskManager.model.Task;

import java.util.List;

public interface TaskManager {

    public int getNextId();
    public void calculateStatusEpic(Epic epic);
    List<Task> getHistory();

    public void addSimpleTask(SimpleTask simpleTask);

    public void addEpicTask(Epic epic);

    public void addSubtask(Subtask subtask);

    public SimpleTask getSimpleTaskId(int id);

    public Epic getEpicId(int id);

    public Subtask getSubtaskId(int id);

    public void getAllTasks();

    public void removeAllTask();

    public void deleteForSimpleTaskId(int taskId);

    public void deleteForEpicId(int taskId);

    public void deleteForSubtaskId(int taskId);

    public void updateSimpleTask(SimpleTask simpleTask);

    public void updateEpic(Epic epic);

    public void updateSubtask(Subtask subtask);


}
