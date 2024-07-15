package org.example;

public class CartItem {
    private Drug drug;
    private int quantity;

    public CartItem(Drug drug, int quantity) {
        this.drug = drug;
        this.quantity = quantity;
    }

    // Getters and Setters

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CartItem [drug=" + drug + ", quantity=" + quantity + "]";
    }
}
