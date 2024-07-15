package org.example;

import java.util.Date;

public class Drug {
    private int drugId;
    private String drugName;
    private String manufacturer;
    private Date expiryDate;
    private int quantity;
    private double price;

    public Drug(int drugId, String drugName, String manufacturer, Date expiryDate, int quantity, double price) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters

    public int getDrugId() {
        return drugId;
    }

    public void setDrugId(int drugId) {
        this.drugId = drugId;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Drug [drugId=" + drugId + ", drugName=" + drugName + ", manufacturer=" + manufacturer + ", expiryDate="
                + expiryDate + ", quantity=" + quantity + ", price=" + price + "]";
    }
}
