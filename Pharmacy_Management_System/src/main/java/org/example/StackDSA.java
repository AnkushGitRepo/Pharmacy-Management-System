package org.example;

public class StackDSA {
    private String[] stackArray;
    private int top;
    private int maxSize;

    // Constructor to initialize the stack
    public StackDSA(int size) {
        maxSize = size;
        stackArray = new String[maxSize];
        top = -1;
    }

    // Method to push an element onto the stack
    public void push(String value) {
        if (isFull()) {
            System.out.println("Stack is full. Cannot push " + value);
        } else {
            stackArray[++top] = value;
        }
    }

    // Method to pop an element from the stack
    public String pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty. Cannot pop");
            return null;
        } else {
            return stackArray[top--];
        }
    }

    // Method to peek at the top element of the stack
    public String peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty. Cannot peek");
            return null;
        } else {
            return stackArray[top];
        }
    }

    public void printStack() {
        if (isEmpty()) {
            System.out.println("Stack is empty.");
        } else {
            System.out.println("\nAction Stack contents:");
            for (int i = top; i >= 0; i--) {
                System.out.println(stackArray[i]);
            }
        }
    }

    // Method to check if the stack is empty
    public boolean isEmpty() {
        return (top == -1);
    }

    // Method to check if the stack is full
    public boolean isFull() {
        return (top == maxSize - 1);
    }
}
