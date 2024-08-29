package org.example;

import java.util.LinkedList;

public class Cart {
    private int cartId;
    private String email;
    private LinkedListDSA<CartItem> items;

    public Cart(int cartId, String email) {
        this.cartId = cartId;
        this.email = email;
        this.items = new LinkedListDSA<>();
    }

    // Getters and Setters

    public int getCartId() {
        return cartId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LinkedListDSA<CartItem> getItems() {
        return items;
    }

    public void addToCart(CartItem item) {
        if (item != null && item.getQuantity() > 0 && item.getDrug() != null) {
            this.items.add(item);
        } else {
            System.out.println("Invalid cart item.");
        }
    }

    @Override
    public String toString() {
        return "Cart [cartId=" + cartId + ", email=" + email + ", items=" + items + "]";
    }
}

