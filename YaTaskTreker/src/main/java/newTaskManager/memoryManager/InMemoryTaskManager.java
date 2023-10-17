package newTaskManager.memoryManager;

import newTaskManager.historyManager.HistoryManager;
import newTaskManager.model.*;
import newTaskManager.historyManager.InMemoryHistoryManager;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {


    private final HistoryManager historyManager;
    private int nextId = 1;
    public Map<Integer, SimpleTask> simpleTaskMap;
    public Map<Integer, Epic> epicMap;
    public Map<Integer, Subtask> subtaskMap;



    public InMemoryTaskManager() {
        simpleTaskMap = new HashMap<>();
        epicMap = new HashMap<>();
        subtaskMap = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();


    }

    public int getNextId() {
        return nextId++;
    }
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


    public void calculateStatusEpic(Epic epic) {
        if (epic.getSubtasks().isEmpty()) {
            epic.setStatus(Status.NEW);
        } else {
            Subtask subFirst = subtaskMap.get(epic.getSubtasks().get(0));
            for (Integer subtaskId : epic.getSubtasks()) {
                Subtask subNext = subtaskMap.get(subtaskId);
                if (subFirst.getStatus().equals(subNext.getStatus())) {
                    epic.setStatus(subFirst.getStatus());
                } else {
                    epic.setStatus(Status.IN_PROGRESS);
                    return;
                }
            }
        }
    }


    public void addSimpleTask(SimpleTask simpleTask) {
        simpleTask.setId(getNextId());
        simpleTaskMap.put(simpleTask.getId(), simpleTask);

    }

    public void addEpicTask(Epic epic) {
        epic.setId(getNextId());
        epicMap.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtaskMap.put(subtask.getId(), subtask);
        epicMap.get(subtask.getEpicId()).addSubtask(subtask.getId());
        calculateStatusEpic(epicMap.get(subtask.getEpicId()));
    }


    public SimpleTask getSimpleTaskId(int id) {
        historyManager.add(simpleTaskMap.get(id));
        return simpleTaskMap.get(id);

    }

    public Epic getEpicId(int id) {
        historyManager.add(epicMap.get(id));
        return epicMap.get(id);
    }

    public Subtask getSubtaskId(int id) {
        historyManager.add(subtaskMap.get(id));
        return subtaskMap.get(id);
    }

    public void getAllTasks() {
        for (SimpleTask simpleTask : simpleTaskMap.values()) {
            System.out.println(simpleTask);
        }
        for (Epic epic : epicMap.values()) {
            System.out.println(epic);
        }
        for (Subtask subtask : subtaskMap.values()) {
            System.out.println(subtask);
        }
    }

    public void removeAllTask() {
        simpleTaskMap.clear();
        epicMap.clear();
        subtaskMap.clear();
        if (simpleTaskMap.isEmpty() && epicMap.isEmpty() && subtaskMap.isEmpty()) {
            System.out.println("Все задачи удалены");
        } else {
            getAllTasks();
        }

    }


    public void deleteForSimpleTaskId(int taskId) {
        simpleTaskMap.remove(taskId);
        System.out.println("Задача Task № " + taskId + " удалена");

    }

    public void deleteForEpicId(int taskId) {
        epicMap.remove(taskId);
        System.out.println("Задача Epic № " + taskId + " удалена");
    }

    public void deleteForSubtaskId(int taskId) {
        subtaskMap.remove(taskId);
        System.out.println("Задача Subtask № " + taskId + " удалена");
    }

    public void updateSimpleTask(SimpleTask simpleTask) {
        simpleTaskMap.put(simpleTask.getId(), simpleTask);

    }

    public void updateEpic(Epic epic) {
        epicMap.put(epic.getId(), epic);
    }


    public void updateSubtask(Subtask subtask) {
        Epic epic = (Epic) epicMap.get(subtask.getEpicId());
        if (epic == null) {
            return;
        }
        subtaskMap.put(subtask.getId(), subtask);
        calculateStatusEpic(epic);
    }

}

