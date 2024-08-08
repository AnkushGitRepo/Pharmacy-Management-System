package org.example;

public class LinkedListDSA<T> {

    class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private int size;

    // Constructor
    public LinkedListDSA() {
        this.head = null;
        this.size = 0;
    }

    // Add an element to the end of the linked list
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    // Remove an element from the linked list
    public void remove(T data) {
        if (head == null) {
            return;
        }

        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return;
        }

        Node<T> current = head;
        Node<T> previous = null;

        while (current != null && !current.data.equals(data)) {
            previous = current;
            current = current.next;
        }

        if (current != null) {
            previous.next = current.next;
            size--;
        }
    }

    // Get the size of the linked list
    public int size() {
        return size;
    }

    // Check if the linked list is empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Get the element at a specific index
    public T get(int index) {
        checkIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Set the element at a specific index
    public void set(int index, T data) {
        checkIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        current.data = data;
    }

    // Clear the linked list
    public void clear() {
        head = null;
        size = 0;
    }

    // Check if the linked list contains a specific element
    public boolean contains(T data) {
        Node<T> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    // Check if the index is within bounds
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    // Override toString() method for better visualization of the linked list
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data).append(" -> ");
            current = current.next;
        }
        sb.append("null");
        return sb.toString();
    }
}
