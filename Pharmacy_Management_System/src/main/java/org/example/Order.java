package org.example;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Order {
    private int orderId;
    private String email;
    private Date orderDate;
    private double totalAmount;
    private LinkedList<CartItem> items;

    public Order(int orderId, String email, Date orderDate, double totalAmount) {
        this.orderId = orderId;
        this.email = email;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public Order(int orderId, String email, Date orderDate, double totalAmount, LinkedList<CartItem> items) {
        this.orderId = orderId;
        this.email = email;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.items = items;
    }

    // Getters and Setters

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void generateInvoice() {
        String filePath = "invoice_" + orderId + ".txt";

        try (FileWriter writer = new FileWriter(filePath)) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            writer.write("Invoice\n");
            writer.write("Order ID: " + orderId + "\n");
            writer.write("Customer Email: " + email + "\n");
            writer.write("Order Date: " + sdf.format(orderDate) + "\n\n");

            writer.write(String.format("%-10s %-20s %-15s %-10s %-10s\n", "Drug ID", "Drug Name", "Manufacturer", "Quantity", "Price"));
            for (CartItem item : items) {
                writer.write(String.format("%-10d %-20s %-15s %-10d %-10.2f\n",
                        item.getDrug().getDrugId(),
                        item.getDrug().getDrugName(),
                        item.getDrug().getManufacturer(),
                        item.getQuantity(),
                        item.getDrug().getPrice()));
            }

            writer.write("\nTotal Amount: $" + totalAmount + "\n");

            System.out.println("Checkout Amount: $" + totalAmount);
            System.out.println("Invoice generated: " + filePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void saveOrder() {
        Connection connection = null;
        PreparedStatement orderStatement = null;
        PreparedStatement itemStatement = null;

        String orderSQL = "INSERT INTO Orders (order_id, email, order_date, total_amount) VALUES (?, ?, ?, ?)";
        String itemSQL = "INSERT INTO CartItems (cart_id, drug_id, quantity) VALUES (?, ?, ?)";

        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pharmacy", "username", "password");
            connection.setAutoCommit(false); // Enable transaction

            // Insert order details
            orderStatement = connection.prepareStatement(orderSQL);
            orderStatement.setInt(1, orderId);
            orderStatement.setString(2, email);
            orderStatement.setDate(3, new java.sql.Date(orderDate.getTime()));
            orderStatement.setDouble(4, totalAmount);
            orderStatement.executeUpdate();

            // Insert order items
            itemStatement = connection.prepareStatement(itemSQL);
            for (CartItem item : items) {
                itemStatement.setInt(1, orderId); // Assuming cart_id is same as order_id
                itemStatement.setInt(2, item.getDrug().getDrugId());
                itemStatement.setInt(3, item.getQuantity());
                itemStatement.addBatch();
            }
            itemStatement.executeBatch();

            connection.commit(); // Commit transaction
            System.out.println("Order saved successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            // Close resources
            try {
                if (orderStatement != null) orderStatement.close();
                if (itemStatement != null) itemStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", email=" + email + ", orderDate=" + orderDate + ", totalAmount=" + totalAmount + "]";
    }
}
