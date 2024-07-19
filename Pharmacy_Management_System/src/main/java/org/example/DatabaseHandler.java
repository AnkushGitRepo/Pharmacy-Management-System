package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
    private Connection connection;

    public void connect() {
        try {
            String url = "jdbc:postgresql://localhost:5432/pharmacy";
            String user = "postgres";  // Replace with your database username
            String password = "1806";  // Replace with your database password
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection to the database established successfully.");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeSelectQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteCustomerByEmail(String email) {
        try {
            // Delete associated records in CartItems
            String deleteCartItemsQuery = "DELETE FROM CartItems WHERE cart_id IN (SELECT cart_id FROM Cart WHERE email = '" + email + "')";
            executeQuery(deleteCartItemsQuery);

            // Delete associated records in Cart
            String deleteCartQuery = "DELETE FROM Cart WHERE email = '" + email + "'";
            executeQuery(deleteCartQuery);

            // Delete the customer record
            String deleteCustomerQuery = "DELETE FROM Customers WHERE email = '" + email + "'";
            executeQuery(deleteCustomerQuery);

            System.out.println("Customer and associated data deleted successfully for email: " + email);
        } catch (Exception e) {
            System.out.println("Error deleting customer data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteCartByEmail(String email) {
        try {
            String deleteCartItemsQuery = "DELETE FROM CartItems WHERE cart_id IN (SELECT cart_id FROM Cart WHERE email = '" + email + "')";
            executeQuery(deleteCartItemsQuery);

            String deleteCartQuery = "DELETE FROM Cart WHERE email = '" + email + "'";
            executeQuery(deleteCartQuery);

            System.out.println("Cart data deleted successfully for email: " + email);
        } catch (Exception e) {
            System.out.println("Error deleting cart data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deleteDrugById(int drugId) {
        try {
            // Delete associated records in CartItems
            String deleteCartItemsQuery = "DELETE FROM CartItems WHERE drug_id = " + drugId;
            executeQuery(deleteCartItemsQuery);

            // Delete associated records in Cart
            String deleteCartQuery = "DELETE FROM Cart WHERE drug_id = " + drugId;
            executeQuery(deleteCartQuery);

            // Delete associated records in Orders
            String deleteOrderItemsQuery = "DELETE FROM CartItems WHERE drug_id = " + drugId + " AND cart_id IN (SELECT order_id FROM Orders WHERE order_id = cart_id)";
            executeQuery(deleteOrderItemsQuery);

            // Finally, delete the drug record
            String deleteDrugQuery = "DELETE FROM Drugs WHERE drug_id = " + drugId;
            executeQuery(deleteDrugQuery);

            System.out.println("Drug and associated data deleted successfully for drug ID: " + drugId);
        } catch (Exception e) {
            System.out.println("Error deleting drug data: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
