package manager.history;

class Node<T> {
    T value;
    Node<T> tail;
    Node<T> next;

    Node(T value, Node<T> tail, Node<T> head) {
        this.value = value;
        this.tail = tail;
        this.next = head;
    }

}