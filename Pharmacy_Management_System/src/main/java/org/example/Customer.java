package org.example;

public class Customer {
    private String email;
    private String name;
    private String address;
    private String phoneNumber;

    public Customer(String email, String name, String address, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "Customer [email=" + email + ", name=" + name + ", address=" + address + ", phoneNumber=" + phoneNumber + "]";
    }
}
