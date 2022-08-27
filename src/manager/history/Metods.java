package manager.history;

public class Metods<E> extends LinkedList{

    public void addLast (E e) {
        Node<E> prev = getLstNode();
        prev.setCurrentElement(e);
        setLstNode(new Node<E>(null, prev, null ));
        prev.setNextElement(getLstNode());
        setSize(getSize() + 1);
    }

    public E getElementByIndex (int counter) {
        Node<E> result = null;
        Node<E> target = getFstNode().getNextElement();
        for (int i = 0; i < counter; i++) {
            result = getNextElement(target);
        }
        return result.getCurrentElement();
    }

    public Node<E> getNextElement (Node<E> current) {
        return current.getNextElement();
    }

    public int size() {
        return getSize();
    }
}
