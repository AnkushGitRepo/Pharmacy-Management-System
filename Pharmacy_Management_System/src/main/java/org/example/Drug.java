package org.example;

import java.util.Date;

public class Drug {
    private int drugId;
    private String drugName;
    private String manufacturer;
    private Date expiryDate;
    private int quantity;
    private double price;
    private String description;
    private String tags;

    public Drug(int drugId, String drugName, String manufacturer, Date expiryDate, int quantity, double price, String description, String tags) {
        this.drugId = drugId;
        this.drugName = drugName;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
        this.price = price;
        this.description = description;
        this.tags = tags;
    }

    // Getters and Setters

    public int getDrugId() {
        return drugId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Drug [drugId=" + drugId + ", drugName=" + drugName + ", manufacturer=" + manufacturer + ", expiryDate="
                + expiryDate + ", quantity=" + quantity + ", price=" + price + ", description=" + description + ", tags=" + tags + "]";
    }
}
