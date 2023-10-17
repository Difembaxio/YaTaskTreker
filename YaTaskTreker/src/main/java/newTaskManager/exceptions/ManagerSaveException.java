package newTaskManager.exceptions;

public class ManagerSaveException extends RuntimeException {
    String message;

    public ManagerSaveException(String message) {
        super(message);
    }
}
