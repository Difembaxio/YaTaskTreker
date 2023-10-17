package newTaskManager;

import newTaskManager.historyManager.HistoryManager;
import newTaskManager.memoryManager.TaskManager;
import newTaskManager.model.*;


public class Main {
    public static void main(String[] args) {

        TaskManager manager = Managers.getDefault();
        HistoryManager history = Managers.getDefaultHistory();

        manager.addSimpleTask(new SimpleTask(1,"Task1","Task1 description", Status.DONE));
        manager.addEpicTask(new Epic("Epic1","Epic1 description"));
        manager.addEpicTask(new Epic("Epic2","Epic2 description"));

        manager.addSubtask(new Subtask("Subtask1","Subtask description",2,Status.DONE));
        manager.addSubtask(new Subtask("Subtask2","Subtask description",2,Status.DONE));
        manager.addSubtask(new Subtask("Subtask3","Subtask description",2,Status.DONE));


        history.add(manager.getEpicId(2));
        history.add(manager.getSubtaskId(4));
        history.add(manager.getSimpleTaskId(1));
        history.add(manager.getSubtaskId(5));
        history.add(manager.getEpicId(3));
        history.add(manager.getEpicId(2));
        history.add(manager.getSubtaskId(4));
        history.add(manager.getSimpleTaskId(1));
        history.add(manager.getSubtaskId(5));
        history.add(manager.getEpicId(3));


        System.out.println(history.getHistory());

    }
}
