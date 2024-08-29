package org.example;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Order {
    private int orderId;
    private String email;
    private Date orderDate;
    private double totalAmount;
    private LinkedListDSA<CartItem> items;

    public Order(int orderId, String email, Date orderDate, double totalAmount, LinkedListDSA<CartItem> items) {
        this.orderId = orderId;
        this.email = email;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.items = items != null ? items : new LinkedListDSA<>();
    }

    // Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

            for (int i = 0; i < items.size(); i++) {
                CartItem item = items.get(i);
                writer.write(String.format("%-10d %-20s %-15s %-10d %-10.2f\n",
                        item.getDrug().getDrugId(),
                        item.getDrug().getDrugName(),
                        item.getDrug().getManufacturer(),
                        item.getQuantity(),
                        item.getDrug().getPrice()));
            }

            writer.write(String.format("\nTotal Amount: $%.2f\n", totalAmount));

            System.out.println(String.format("Checkout Amount: $%.2f", totalAmount));
            System.out.println("Invoice generated: " + filePath);

        } catch (IOException e) {
            System.out.println("Error generating invoice: " + e.getMessage());
        }
    }


    /*
      Saves the order details and associated items to the database.

      This method performs the following steps:
      1. Establishes a connection to the database.
      2. Retrieves the current maximum order ID from the Orders table to determine the next order ID.
      3. Inserts the order details into the Orders table.
      4. Inserts each item in the order into the OrderItems table.
      5. Updates the quantity of each drug in the Drugs table to reflect the purchase.
      6. Commits the transaction to ensure all changes are saved.
      7. Rolls back the transaction in case of any error to maintain data integrity.

     Throws SQLException If there is an error while interacting with the database.
     */
    public void saveOrder() throws SQLException {
        Connection connection = null;
        PreparedStatement orderStatement = null;
        PreparedStatement itemStatement = null;
        PreparedStatement updateDrugStatement = null;

        String maxOrderIdSQL = "SELECT COALESCE(MAX(order_id), 0) FROM Orders";
        String insertOrderSQL = "INSERT INTO Orders (order_id, email, order_date, total_amount) VALUES (?, ?, ?, ?)";
        String insertOrderItemSQL = "INSERT INTO OrderItems (order_id, drug_id, quantity, price) VALUES (?, ?, ?, ?)";

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

            // Insert each cart item into OrderItems table and update drug quantities
            for (int i = 0; i < items.size(); i++) {
                CartItem item = items.get(i);
                itemStatement = connection.prepareStatement(insertOrderItemSQL);
                itemStatement.setInt(1, orderId);
                itemStatement.setInt(2, item.getDrug().getDrugId());
                itemStatement.setInt(3, item.getQuantity());
                itemStatement.setDouble(4, item.getDrug().getPrice());
                itemStatement.executeUpdate();
            }
            connection.commit(); // Commit transaction

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

