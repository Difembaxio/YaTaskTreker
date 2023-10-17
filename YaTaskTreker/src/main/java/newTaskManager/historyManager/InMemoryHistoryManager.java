package newTaskManager.historyManager;

import newTaskManager.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {


    private Map<Integer, Node> idToNode = new HashMap<>();
    private Node head;
    private Node tail;
    private int size;



    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            idToNode.put(task.getId(), linkLast(new Node(task)));
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> historicList = new ArrayList<>();
        Node node = head;
        if (head != null) {
            while (node.next != null) {
                historicList.add(node.data);
                node = node.next;
            }
            historicList.add(node.data);
        }
        return Collections.unmodifiableList(historicList);
    }

    @Override
    public void remove(int id) {
        if (idToNode.containsKey(id)) {
            removeNode(idToNode.get(id));
            idToNode.remove(id);
        }
    }

    private Node linkLast(Node newNode){
        if (size == 0) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        newNode.prev = tail;
        tail = newNode;
        size++;
        return newNode;
    }


    private void removeNode(Node node) {
        if (size == 1) {
            head = null;
            tail = null;

        } else if (node.prev == null) {
            Node nextNode = node.next;
            nextNode.prev = null;
            head = nextNode;
        } else if (node.next == null) {
          Node prevNode = node.prev;
            prevNode.next = null;
            tail = prevNode;
        } else {
           Node prevNode = node.prev;
           Node nextNode = node.next;
           prevNode.next = nextNode;
           nextNode.prev = prevNode;
        }
        size--;
    }

    public class Node{
        public Task data;
        public Node next;
        public Node prev;

        public Node(Task data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}
