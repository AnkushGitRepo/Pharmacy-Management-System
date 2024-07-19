package org.example;

import java.io.FileWriter;
import java.io.IOException;
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

    public Order(int orderId, String email, Date orderDate, double totalAmount, LinkedList<CartItem> items) {
        this.orderId = orderId;
        this.email = email;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.items = items != null ? items : new LinkedList<>();
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

    public LinkedList<CartItem> getItems() {
        return items;
    }

    public void setItems(LinkedList<CartItem> items) {
        this.items = items;
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
            System.out.println("Error generating invoice: " + e.getMessage());
        }
    }

    public void saveOrder() throws SQLException {
        Connection connection = null;
        PreparedStatement orderStatement = null;
        PreparedStatement itemStatement = null;
        PreparedStatement updateDrugStatement = null;

        String maxOrderIdSQL = "SELECT COALESCE(MAX(order_id), 0) FROM Orders";
        String insertOrderSQL = "INSERT INTO Orders (order_id, email, order_date, total_amount) VALUES (?, ?, ?, ?)";
        String itemSQL = "INSERT INTO CartItems (cart_id, drug_id, quantity) VALUES (?, ?, ?)";
        String updateDrugSQL = "UPDATE Drugs SET quantity = quantity - ? WHERE drug_id = ?";

        try {
            // Establish database connection
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pharmacy", "postgres", "1806");
            connection.setAutoCommit(false); // Enable transaction

            // Retrieve the current maximum order ID
            Statement maxOrderIdStatement = connection.createStatement();
            ResultSet rs = maxOrderIdStatement.executeQuery(maxOrderIdSQL);
            if (rs.next()) {
                this.orderId = rs.getInt(1) + 1; // Increment the max order ID or start from 1 if there are no orders
            }

            // Insert order details
            orderStatement = connection.prepareStatement(insertOrderSQL);
            orderStatement.setInt(1, orderId);
            orderStatement.setString(2, email);
            orderStatement.setDate(3, new java.sql.Date(orderDate.getTime()));
            orderStatement.setDouble(4, totalAmount);
            orderStatement.executeUpdate();

            // Retrieve the cart ID for the customer
            String getCartIdSQL = "SELECT cart_id FROM Cart WHERE email = ?";
            PreparedStatement getCartIdStatement = connection.prepareStatement(getCartIdSQL);
            getCartIdStatement.setString(1, email);
            ResultSet cartRs = getCartIdStatement.executeQuery();
            int cartId = -1;
            if (cartRs.next()) {
                cartId = cartRs.getInt("cart_id");
            }

            if (cartId == -1) {
                throw new SQLException("Cart ID not found for customer email: " + email);
            }

            // Insert order items
            itemStatement = connection.prepareStatement(itemSQL);
            updateDrugStatement = connection.prepareStatement(updateDrugSQL);
            for (CartItem item : items) {
                itemStatement.setInt(1, cartId); // Use the retrieved cart_id
                itemStatement.setInt(2, item.getDrug().getDrugId());
                itemStatement.setInt(3, item.getQuantity());
                itemStatement.addBatch();

                // Update drug quantity
                updateDrugStatement.setInt(1, item.getQuantity());
                updateDrugStatement.setInt(2, item.getDrug().getDrugId());
                updateDrugStatement.addBatch();
            }
            itemStatement.executeBatch();
            updateDrugStatement.executeBatch();

            connection.commit(); // Commit transaction
            System.out.println("Order saved successfully!");

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback(); // Rollback transaction on error
                } catch (SQLException ex) {
                    System.out.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            // Close resources
            try {
                if (orderStatement != null) orderStatement.close();
                if (itemStatement != null) itemStatement.close();
                if (updateDrugStatement != null) updateDrugStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }


    @Override
    public String toString() {
        return "Order [orderId=" + orderId + ", email=" + email + ", orderDate=" + orderDate + ", totalAmount=" + totalAmount + "]";
    }
}

