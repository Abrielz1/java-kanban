package manager.history;

import constructor.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    public final Map<Integer, Node<Task>> history;

    public final CustomLinkedList<Task> historyList;

    public InMemoryHistoryManager() {
        this.history = new HashMap<>();
        this.historyList = new CustomLinkedList<>();
    }

    @Override
    public void addHistory(Task task) {
        remove(task);
        historyList.linkLast(task);
        history.put(task.getId(), historyList.tail.prev);
    }

    @Override
    public void remove(Task task) {
        if (history.containsKey(task.getId())) {
            historyList.removeNode(history.get(task.getId()));
            history.remove(task.getId());
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList.getTasks();
    }
}

class CustomLinkedList<T> {
    Node<T> head;
    Node<T> tail;

    public void linkLast(T t) {
        if (head == null) {
            Node<T> currentNode = new Node<>(t, null, tail);
            head = currentNode;
            tail = new Node<>(null, currentNode, null);
            return;
        }
        Node<T> currentNode = tail;
        currentNode.value = t;
        tail = new Node<>(null, currentNode, null);
        currentNode.prev.next = currentNode;
        currentNode.next = tail;
    }

    public List<T> getTasks() {
        List<T> tasks = new ArrayList<>();
        Node<T> current = head;

        while (current != null) {
            tasks.add(current.value);
            current = current.next;
        }
        return tasks;
    }

    public void removeNode(Node<T> node) {
        if (node.equals(head)) {
            head = node.next;
            if (node.next != null) {
                node.next.prev = null;
            }
        } else {
            node.prev.next = node.next;
            if (node.next != null) {
                node.next.prev = node.prev;
            }
        }
    }
}