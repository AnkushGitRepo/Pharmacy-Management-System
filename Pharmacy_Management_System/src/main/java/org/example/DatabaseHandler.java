package org.example;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

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
        }
    }

    // Return Nothing [Only Use for Insert, Update, Delete]
    public void executeQuery(String query) {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Error executing query: " + e.getMessage());
        }
    }

    // Return ResultSet [Only Use with Select Query]
    public ResultSet executeSelectQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error executing select query: " + e.getMessage());
            return null;
        }
    }

    public void deleteCustomerByEmail(String email) {
        String deleteOrderItemsQuery = "DELETE FROM OrderItems WHERE order_id IN (SELECT order_id FROM Orders WHERE email = ?)";
        String deleteOrdersQuery = "DELETE FROM Orders WHERE email = ?";
        String deleteCartQuery = "DELETE FROM Cart WHERE email = ?";
        String deleteCustomerQuery = "DELETE FROM Customers WHERE email = ?";

        try (PreparedStatement pstOrderItems = connection.prepareStatement(deleteOrderItemsQuery);
             PreparedStatement pstOrders = connection.prepareStatement(deleteOrdersQuery);
             PreparedStatement pstCart = connection.prepareStatement(deleteCartQuery);
             PreparedStatement pstCustomer = connection.prepareStatement(deleteCustomerQuery)) {

            pstOrderItems.setString(1, email);
            pstOrderItems.executeUpdate();

            pstOrders.setString(1, email);
            pstOrders.executeUpdate();

            pstCart.setString(1, email);
            pstCart.executeUpdate();

            pstCustomer.setString(1, email);
            pstCustomer.executeUpdate();

            System.out.println("Customer and associated data deleted successfully for email: " + email);
        } catch (SQLException e) {
            System.out.println("Error deleting customer data: " + e.getMessage());
        }
    }

    public void deleteCartByEmail(String email) {
        String deleteCartQuery = "DELETE FROM Cart WHERE email = ?";

        try (PreparedStatement pst = connection.prepareStatement(deleteCartQuery)) {
            pst.setString(1, email);
            pst.executeUpdate();

            System.out.println("Cart data deleted successfully for email: " + email);
        } catch (SQLException e) {
            System.out.println("Error deleting cart data: " + e.getMessage());
        }
    }

    public void deleteDrugById(int drugId) {
        String deleteOrderItemsQuery = "DELETE FROM OrderItems WHERE drug_id = ?";
        String deleteCartQuery = "DELETE FROM Cart WHERE drug_id = ?";
        String deleteDrugQuery = "DELETE FROM Drugs WHERE drug_id = ?";

        try (PreparedStatement pstOrderItems = connection.prepareStatement(deleteOrderItemsQuery);
             PreparedStatement pstCart = connection.prepareStatement(deleteCartQuery);
             PreparedStatement pstDrug = connection.prepareStatement(deleteDrugQuery)) {

            pstOrderItems.setInt(1, drugId);
            pstOrderItems.executeUpdate();

            pstCart.setInt(1, drugId);
            pstCart.executeUpdate();

            pstDrug.setInt(1, drugId);
            pstDrug.executeUpdate();

            System.out.println("Drug and associated data deleted successfully for drug ID: " + drugId);
        } catch (SQLException e) {
            System.out.println("Error deleting drug data: " + e.getMessage());
        }
    }

    public void generateSalesReport(java.util.Date startDate, java.util.Date endDate) {
        String query = "SELECT oi.drug_id, d.drug_name, SUM(oi.quantity) AS total_quantity, SUM(oi.quantity * oi.price) AS total_earnings " +
                "FROM OrderItems oi " +
                "JOIN Orders o ON oi.order_id = o.order_id " +
                "JOIN Drugs d ON oi.drug_id = d.drug_id " +
                "WHERE o.order_date BETWEEN ? AND ? " +
                "GROUP BY oi.drug_id, d.drug_name";

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/pharmacy", "postgres", "1806");
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setDate(1, new java.sql.Date(startDate.getTime()));
            statement.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet resultSet = statement.executeQuery();

            String filePath = "sales_report_" + new SimpleDateFormat("yyyyMMdd").format(endDate) + ".txt";
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write("Sales Report\n");
                writer.write("Start Date: " + new SimpleDateFormat("yyyy-MM-dd").format(startDate) + "\n");
                writer.write("End Date: " + new SimpleDateFormat("yyyy-MM-dd").format(endDate) + "\n\n");
                writer.write(String.format("%-10s %-20s %-15s %-15s\n", "Drug ID", "Drug Name", "Total Quantity", "Total Earnings"));

                double totalEarnings = 0;
                while (resultSet.next()) {
                    int drugId = resultSet.getInt("drug_id");
                    String drugName = resultSet.getString("drug_name");
                    int totalQuantity = resultSet.getInt("total_quantity");
                    double earnings = resultSet.getDouble("total_earnings");
                    totalEarnings += earnings;

                    writer.write(String.format("%-10d %-20s %-15d %-15.2f\n", drugId, drugName, totalQuantity, earnings));
                }
                writer.write(String.format("\nTotal Earnings: $%.2f\n", totalEarnings));

                System.out.println("Sales report generated: " + filePath);
            } catch (IOException e) {
                System.out.println("Error writing sales report: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error generating sales report: " + e.getMessage());
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed!");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}
