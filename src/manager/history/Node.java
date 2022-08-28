package manager.history;

class Node<T> {
    T value;
    Node<T> prev;
    Node<T> next;

    Node(T value, Node<T> prev, Node<T> next) {
        this.value = value;
        this.prev = prev;
        this.next = next;
    }

}