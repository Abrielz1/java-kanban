package manager.history;

public class Node<E> {
    private E currentElement;
    private Node<E> previousElement;

    public Node(E currentElement, Node<E> previousElement, Node<E> nextElement) {
        this.currentElement = currentElement;
        this.previousElement = previousElement;
        this.nextElement = nextElement;
    }

    public E getCurrentElement() {
        return currentElement;
    }

    public void setCurrentElement(E currentElement) {
        this.currentElement = currentElement;
    }

    public Node<E> getPreviousElement() {
        return previousElement;
    }

    public void setPreviousElement(Node<E> previousElement) {
        this.previousElement = previousElement;
    }

    public Node<E> getNextElement() {
        return nextElement;
    }

    public void setNextElement(Node<E> nextElement) {
        this.nextElement = nextElement;
    }

    private Node<E> nextElement;
}
