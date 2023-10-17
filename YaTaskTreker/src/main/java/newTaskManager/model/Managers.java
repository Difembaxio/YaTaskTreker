package newTaskManager.model;


import newTaskManager.fileManager.FileBackedTasksManager;
import newTaskManager.memoryManager.InMemoryTaskManager;
import newTaskManager.historyManager.HistoryManager;
import newTaskManager.historyManager.InMemoryHistoryManager;
import newTaskManager.memoryManager.TaskManager;

import java.nio.file.Path;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefault(Path autoSaveFile) {
        return new FileBackedTasksManager(autoSaveFile);
    }
}

