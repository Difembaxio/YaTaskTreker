package newTaskManager.fileManager;

import newTaskManager.exceptions.ManagerSaveException;
import newTaskManager.historyManager.HistoryManager;
import newTaskManager.historyManager.InMemoryHistoryManager;
import newTaskManager.memoryManager.InMemoryTaskManager;
import newTaskManager.model.*;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class FileBackedTasksManager extends InMemoryTaskManager {

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();



    private final Path autoSaveFile;

    public FileBackedTasksManager(Path autoSaveFile) {
        super();
        this.autoSaveFile = autoSaveFile;
    }


    @Override
    public void calculateStatusEpic(Epic epic) {
        save();
        super.calculateStatusEpic(epic);
    }

    @Override
    public void getAllTasks() {
        save();
        super.getAllTasks();
    }

    @Override
    public void removeAllTask() {
        save();
        super.removeAllTask();
    }

    @Override
    public List<Task> getHistory() {
        save();
        return super.getHistory();
    }

    @Override
    public void addSimpleTask(SimpleTask simpleTask) {
        super.addSimpleTask(simpleTask);
        save();
    }

    @Override
    public void addEpicTask(Epic epic) {
        super.addEpicTask(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public SimpleTask getSimpleTaskId(int id) {
        SimpleTask simpleTask = super.getSimpleTaskId(id);
        save();
        return simpleTask;

    }

    @Override
    public Epic getEpicId(int id) {
        Epic epic = super.getEpicId(id);
        save();
        return epic;
    }

    @Override
    public Subtask getSubtaskId(int id) {
        Subtask subtask = super.getSubtaskId(id);
        save();
        return subtask;
    }

    @Override
    public void deleteForSimpleTaskId(int taskId) {
        super.deleteForSimpleTaskId(taskId);
        save();
    }

    @Override
    public void deleteForEpicId(int taskId) {
        super.deleteForEpicId(taskId);
        save();
    }

    @Override
    public void deleteForSubtaskId(int taskId) {
        super.deleteForSubtaskId(taskId);
        save();
    }

    @Override
    public void updateSimpleTask(SimpleTask simpleTask) {
        super.updateSimpleTask(simpleTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    private void save() {
        List<String> saveList = new ArrayList<>(List.of("id,type,name,status,description,epic"));
        if (!saveList.isEmpty()) {
            for (Task task : simpleTaskMap.values()) {
                saveList.add(task.toString());
            }

            for (Task task : epicMap.values()) {
                saveList.add(task.toString());
            }

            for (Task task : subtaskMap.values()) {
                saveList.add(task.toString());
            }

            saveList.add(" ");
            saveList.add(historyToString());


            try (BufferedWriter writer = Files.newBufferedWriter(autoSaveFile, StandardCharsets.UTF_8)) {
                for (String line : saveList) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new ManagerSaveException(e.getMessage());
            }
        }
    }
    private String historyToString() {
        List<Task> history = super.getHistory();
        StringBuilder historyToString = new StringBuilder();
        for (Task t: history) {
            historyToString.append(String.format("%d,", t.getId()));
        }
        return historyToString.toString();
    }


    private Task fromString(String taskLine) {
        String[] taskElements = taskLine.split(",");
        if (taskElements[1].equals(NameTasks.SIMPLETASK.name())) {
            return new SimpleTask(Integer.parseInt(taskElements[0]),  taskElements[2], taskElements[4], Status.valueOf(taskElements[3]));
        } else if ((taskElements[1].equals(NameTasks.EPIC.name()))) {
            return new Epic (Integer.parseInt(taskElements[0]), taskElements[2], taskElements[4], Status.valueOf(taskElements[3]));
        } else {
            return new Subtask(Integer.parseInt(taskElements[0]), taskElements[2], taskElements[4],
                    Integer.parseInt(taskElements[5]), Status.valueOf(taskElements[3]));
        }
    }

    private List<Integer> historyFromString(String historyLine) {
        if (!historyLine.isBlank()) {
            String[] historyElements = historyLine.split(",");
            List<Integer> historyIds = new ArrayList<>();
            for (String e : historyElements) {
                historyIds.add(Integer.parseInt(e));
            }
            return historyIds;
        } else {
            return new ArrayList<>();
        }
    }

    public static FileBackedTasksManager loadFromFile(Path fileBacked) {
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefault(fileBacked);

        try {
            List<String> lines = new ArrayList<>(Files.readAllLines(fileBacked, StandardCharsets.UTF_8));
            for (int i = 1; i < lines.size() -2; i++) {
                Task task = fileBackedTasksManager.fromString(lines.get(i));
                if (task.getClass().getSimpleName().equals("SimpleTask")) {
                    SimpleTask simple = (SimpleTask) task;
                    fileBackedTasksManager.simpleTaskMap.put(simple.getId(), simple);
                }
                if (task.getClass().getSimpleName().equals("Epic")) {
                    Epic epic = (Epic) task;
                    fileBackedTasksManager.epicMap.put(epic.getId(), epic);
                }
                if (task.getClass().getSimpleName().equals("Subtask")) {
                    Subtask subtask = (Subtask) task;
                    fileBackedTasksManager.subtaskMap.put(subtask.getId(), subtask);
                    Epic epic = fileBackedTasksManager.epicMap.get(subtask.getEpicId());
                    epic.getSubtasks().add(subtask.getId());
                }
            }
                if (!lines.isEmpty()) {
                    List<Integer> history = fileBackedTasksManager.historyFromString(lines.get(lines.size() - 1));
                    for (Integer id : history) {
                        if (fileBackedTasksManager.simpleTaskMap.get(id) != null){
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.simpleTaskMap.get(id));
                        } else if (fileBackedTasksManager.epicMap.get(id) != null) {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.epicMap.get(id));
                        } else {
                            fileBackedTasksManager.historyManager.add(fileBackedTasksManager.subtaskMap.get(id));
                        }
                    }
                }
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public static void main(String[] args) {


       Path autoSaveFile = Paths.get("..\\history.csv");

       FileBackedTasksManager tasksManager = new FileBackedTasksManager(autoSaveFile);

       tasksManager.addSimpleTask(new SimpleTask(1,"SimpleTask", "Description1",Status.NEW));

       tasksManager.addEpicTask(new Epic("Epic", "Description of Epic 1"));

       tasksManager.addSubtask(new Subtask("Subtask", "Description of Subtask 1", 2, Status.IN_PROGRESS));

        tasksManager.getSimpleTaskId(1);
        tasksManager.getEpicId(2);
        tasksManager.getSubtaskId(3);

      FileBackedTasksManager fileBackedTasksManager = loadFromFile(autoSaveFile);

       System.out.println(fileBackedTasksManager.getSimpleTaskId(1));
       System.out.println(fileBackedTasksManager.getEpicId(2));
       System.out.println(fileBackedTasksManager.getSubtaskId(3));
       System.out.println(fileBackedTasksManager.getHistory());






   }
}

