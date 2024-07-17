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
        if (item != null && item.getQuantity() > 0 && item.getDrug() != null) {
            this.items.add(item);
        } else {
            System.out.println("Invalid cart item.");
        }
    }

    public void removeFromCart(CartItem item) {
        if (this.items.contains(item)) {
            this.items.remove(item);
        } else {
            System.out.println("Item not found in cart.");
        }
    }

    public void viewCart() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            for (CartItem item : items) {
                System.out.println(item);
            }
        }
    }


    public void checkout() {
        if (items.isEmpty()) {
            System.out.println("Cart is empty. Cannot checkout.");
            return;
        }

        double totalAmount = calculateTotalAmount();

        if (totalAmount == 0) {
            System.out.println("Total amount is zero. Cannot checkout.");
            return;
        }

        Order order = new Order(cartId, email, new Date(), totalAmount, items);

        try {
            order.saveOrder();
            order.generateInvoice();

            items.clear();
            System.out.println("Checkout successful! Invoice generated.");
        } catch (Exception e) {
            System.out.println("Error during checkout: " + e.getMessage());
        }
    }



    private double calculateTotalAmount() {
        double totalAmount = 0;
        for (CartItem item : items) {
            if (item.getDrug() != null && item.getQuantity() > 0) {
                totalAmount += item.getQuantity() * item.getDrug().getPrice();
            } else {
                System.out.println("Invalid item in cart: " + item);
            }
        }
        return totalAmount;
    }

    @Override
    public String toString() {
        return "Cart [cartId=" + cartId + ", email=" + email + ", items=" + items + "]";
    }
}

