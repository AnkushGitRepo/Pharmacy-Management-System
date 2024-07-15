package org.example;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Cart {
    private int cartId;
    private String email;
    private LinkedList<CartItem> items;

    public Cart(int cartId, String email) {
        this.cartId = cartId;
        this.email = email;
        this.items = new LinkedList<>();
    }

    // Getters and Setters

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LinkedList<CartItem> getItems() {
        return items;
    }

    public void setItems(LinkedList<CartItem> items) {
        this.items = items;
    }

    public void addToCart(CartItem item) {
        this.items.add(item);
    }

    public void removeFromCart(CartItem item) {
        this.items.remove(item);
    }

    public void viewCart() {
        for (CartItem item : items) {
            System.out.println(item);
        }
    }

    public void checkout() {
        // Calculate total amount
        double totalAmount = 0;
        for (CartItem item : items) {
            totalAmount += item.getQuantity() * item.getDrug().getPrice();
        }

        // Create Order object
        int orderId = cartId; // Assuming order ID is the same as cart ID
        Date orderDate = new Date();
        Order order = new Order(orderId, email, orderDate, totalAmount, items);

        // Save order to the database
        order.saveOrder();

        // Generate invoice
        order.generateInvoice();
    }


    @Override
    public String toString() {
        return "Cart [cartId=" + cartId + ", email=" + email + ", items=" + items + "]";
    }
}
