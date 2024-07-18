package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in); // Scanner for user input
    private static DatabaseHandler dbHandler = new DatabaseHandler(); // Database handler for DB operations
    private static LinkedListDSA<Drug> drugList = new LinkedListDSA<>(); // List to store drugs
    private static LinkedListDSA<Customer> customerList = new LinkedListDSA<>(); // List to store customers
    private static LinkedListDSA<Cart> cartList = new LinkedListDSA<>(); // List to store carts
    private static StackDSA actionStack = new StackDSA(100); // Stack to store user actions
    private static int cartIdCounter = 1; // Counter for cart IDs

    public static void main(String[] args) {
        dbHandler.connect(); // Connect to the database
        loadInitialData(); // Load initial data from the database

        while (true) {
            showMainMenu(); // Show the main menu
            int choice = -1;
            try {
                choice = scanner.nextInt(); // Get user choice
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please try again.");
                scanner.nextLine(); // Consume invalid input
            }

            switch (choice) {
                case 1:
                    drugManagement(); // Manage drugs
                    break;
                case 2:
                    customerManagement(); // Manage customers
                    break;
                case 3:
                    manageCart(); // Manage cart
                    break;
                case 4:
                    help(); // Show help information
                    break;
                case 5:
                    actionStack.printStack(); // Print the actions stack
                    break;
                case 6:
                    System.out.println("Exiting...");
                    dbHandler.closeConnection(); // Close the database connection
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Show the main menu
    private static void showMainMenu() {
        System.out.println("\n--- Pharmacy Store Management System ---");
        System.out.println("1. Drug Management");
        System.out.println("2. Customer Management");
        System.out.println("3. Manage Cart");
        System.out.println("4. Help");
        System.out.println("5. Print Actions Stack");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    // Manage drugs (add, update, delete, view, list expired, help)
    private static void drugManagement() {
        while (true) {
            showDrugMenu(); // Show drug management menu
            int choice = -1;
            try {
                choice = scanner.nextInt(); // Get user choice
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please try again.");
                scanner.nextLine(); // Consume invalid input
            }

            switch (choice) {
                case 1:
                    addDrug(); // Add a new drug
                    break;
                case 2:
                    updateDrug(); // Update an existing drug
                    break;
                case 3:
                    deleteDrug(); // Delete a drug
                    break;
                case 4:
                    viewDrugInventory(); // View all drugs
                    break;
                case 5:
                    listExpiredDrugs(); // List expired drugs
                    break;
                case 6:
                    help(); // Show help information
                    break;
                case 7:
                    return; // Return to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Show drug management menu
    private static void showDrugMenu() {
        System.out.println("\n--- Drug Management ---");
        System.out.println("1. Add Drug");
        System.out.println("2. Update Drug");
        System.out.println("3. Delete Drug");
        System.out.println("4. View Drug Inventory");
        System.out.println("5. List Expired Drugs");
        System.out.println("6. Help");
        System.out.println("7. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    // Manage customers (register, delete, update, manage cart)
    private static void customerManagement() {
        while (true) {
            showCustomerMenu(); // Show customer management menu
            int choice = -1;
            try {
                choice = scanner.nextInt(); // Get user choice
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please try again.");
                scanner.nextLine(); // Consume invalid input
            }

            switch (choice) {
                case 1:
                    registerCustomer(); // Register a new customer
                    break;
                case 2:
                    updateCustomer(); // Update customer information
                    break;
                case 3:
                    manageCart(); // Manage customer's cart
                    break;
                case 4:
                    return; // Return to main menu
            }
        }
    }

    // Show customer management menu
    private static void showCustomerMenu() {
        System.out.println("\n--- Customer Management ---");
        System.out.println("1. Register Customer");
        System.out.println("2. Update Customer");
        System.out.println("3. Manage Cart");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    // Update customer information
    private static void updateCustomer() {
        System.out.print("Enter Customer Email to update: ");
        String email = scanner.nextLine();

        Customer customer = findCustomerByEmail(email);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        System.out.print("Enter new Customer Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new Customer Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter new Customer Phone Number: ");
        String phoneNumber = scanner.nextLine();

        customer.setName(name);
        customer.setAddress(address);
        customer.setPhoneNumber(phoneNumber);

        dbHandler.executeQuery("UPDATE Customers SET name='" + name + "', address='" + address + "', phone_number='" + phoneNumber + "' WHERE email='" + email + "'");
        System.out.println("Customer updated successfully!");
        actionStack.push("Updated customer with email: " + email);
    }

    // Add a new drug
    private static void addDrug() {
        System.out.print("Enter Drug ID: ");
        int drugId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Drug Name: ");
        String drugName = scanner.nextLine();
        System.out.print("Enter Manufacturer: ");
        String manufacturer = scanner.nextLine();
        System.out.print("Enter Expiry Date (yyyy-MM-dd): ");
        String expiryDateStr = scanner.nextLine();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Tags: ");
        String tags = scanner.nextLine();

        try {
            Date expiryDate = new SimpleDateFormat("yyyy-MM-dd").parse(expiryDateStr);
            Drug drug = new Drug(drugId, drugName, manufacturer, expiryDate, quantity, price, description, tags);
            drugList.add(drug);
            dbHandler.executeQuery("INSERT INTO Drugs (drug_id, drug_name, manufacturer, expiry_date, quantity, price, description, tags) VALUES (" + drugId + ", '" + drugName + "', '" + manufacturer + "', '" + expiryDateStr + "', " + quantity + ", " + price + ", '" + description + "', '" + tags + "')");
            System.out.println("Drug added successfully!");
            actionStack.push("Added drug with ID: " + drugId);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
        }
    }

    // Update an existing drug
    private static void updateDrug() {
        System.out.print("Enter Drug ID to update: ");
        int drugId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Drug drug = findDrugById(drugId);
        if (drug == null) {
            System.out.println("Drug not found.");
            return;
        }

        System.out.print("Enter new Drug Name: ");
        String drugName = scanner.nextLine();
        System.out.print("Enter new Manufacturer: ");
        String manufacturer = scanner.nextLine();
        System.out.print("Enter new Expiry Date (yyyy-MM-dd): ");
        String expiryDateStr = scanner.nextLine();
        System.out.print("Enter new Quantity: ");
        int quantity = scanner.nextInt();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter new Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter new Tags: ");
        String tags = scanner.nextLine();

        try {
            Date expiryDate = new SimpleDateFormat("yyyy-MM-dd").parse(expiryDateStr);
            drug.setDrugName(drugName);
            drug.setManufacturer(manufacturer);
            drug.setExpiryDate(expiryDate);
            drug.setQuantity(quantity);
            drug.setPrice(price);
            drug.setDescription(description);
            drug.setTags(tags);
            dbHandler.executeQuery("UPDATE Drugs SET drug_name='" + drugName + "', manufacturer='" + manufacturer + "', expiry_date='" + expiryDateStr + "', quantity=" + quantity + ", price=" + price + ", description='" + description + "', tags='" + tags + "' WHERE drug_id=" + drugId);
            System.out.println("Drug updated successfully!");
            actionStack.push("Updated drug with ID: " + drugId);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
        }
    }

    // Delete a drug
    private static void deleteDrug() {
        System.out.print("Enter Drug ID to delete: ");
        int drugId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        Drug drug = findDrugById(drugId);
        if (drug == null) {
            System.out.println("Drug not found.");
            return;
        }

        drugList.remove(drug);
        dbHandler.executeQuery("DELETE FROM Drugs WHERE drug_id=" + drugId);
        System.out.println("Drug deleted successfully!");
        actionStack.push("Deleted drug with ID: " + drugId);
    }

    // View all drugs in inventory
    private static void viewDrugInventory() {
        System.out.println("\n--- Drug Inventory ---");
        System.out.printf("%-10s %-20s %-15s %-10s %-10s\n", "Drug ID", "Drug Name", "Expiry Date", "Quantity", "Price");

        for (Drug drug : drugList) {
            System.out.printf("%-10d %-20s %-15s %-10d %-10.2f\n",
                    drug.getDrugId(),
                    drug.getDrugName(),
                    new SimpleDateFormat("yyyy-MM-dd").format(drug.getExpiryDate()),
                    drug.getQuantity(),
                    drug.getPrice());
        }
        actionStack.push("Viewed drug inventory");
    }

    // List expired drugs
    private static void listExpiredDrugs() {
        System.out.println("\n--- Expired Drugs ---");
        System.out.printf("%-10s %-20s %-20s %-15s %-10s %-10s\n", "Drug ID", "Drug Name", "Manufacturer", "Expiry Date", "Quantity", "Price");

        Date currentDate = new Date();
        for (Drug drug : drugList) {
            if (drug.getExpiryDate().before(currentDate)) {
                System.out.printf("%-10d %-20s %-20s %-15s %-10d %-10.2f\n",
                        drug.getDrugId(),
                        drug.getDrugName(),
                        drug.getManufacturer(),
                        new SimpleDateFormat("yyyy-MM-dd").format(drug.getExpiryDate()),
                        drug.getQuantity(),
                        drug.getPrice());
            }
        }
        actionStack.push("Listed expired drugs");
    }

    // Register a new customer
    private static void registerCustomer() {
        System.out.print("Enter Customer Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Customer Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Customer Address: ");
        String address = scanner.nextLine();
        System.out.print("Enter Customer Phone Number: ");
        String phoneNumber = scanner.nextLine();

        Customer customer = new Customer(email, name, address, phoneNumber);
        customerList.add(customer);
        dbHandler.executeQuery("INSERT INTO Customers VALUES ('" + email + "', '" + name + "', '" + address + "', '" + phoneNumber + "')");
        System.out.println("Customer registered successfully!");
        actionStack.push("Registered customer with email: " + email);
    }

    // Manage customer's cart (add drug, view cart, checkout, help)
    private static void manageCart() {
        System.out.print("Enter Customer Email to manage cart: ");
        String email = scanner.nextLine();

        Customer customer = findCustomerByEmail(email);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        Cart cart = findCartByEmail(email);
        if (cart == null) {
            cart = new Cart(cartIdCounter++, email);
            cartList.add(cart);
        }

        while (true) {
            showCartMenu();
            int choice = -1;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (InputMismatchException e) {
                System.out.println("Invalid choice. Please try again.");
                scanner.nextLine(); // Consume invalid input
            }

            switch (choice) {
                case 1:
                    addDrugToCart(cart);
                    break;
                case 2:
                    viewCart(cart);
                    break;
                case 3:
                    checkout(cart);
                    return;
                case 4:
                    help();
                    break;
                case 5:
                    return;
            }
        }
    }

    // Show cart management menu
    private static void showCartMenu() {
        System.out.println("\n--- Cart Management ---");
        System.out.println("1. Add Drug to Cart");
        System.out.println("2. View Cart");
        System.out.println("3. Checkout");
        System.out.println("4. Help");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    // Add a drug to the cart
    private static void addDrugToCart(Cart cart) {
        System.out.println("\n--- Available Drugs ---");
        System.out.printf("%-10s %-20s", "Drug ID", "Drug Name");

        int count = 0;

        for (Drug drug : drugList) {
            if (!drug.getExpiryDate().before(new Date())) {
                if (count % 5 == 0) {
                    System.out.println(); // Move to the next line after every 5 drugs
                }
                System.out.printf("%-10d %-20s", drug.getDrugId(), drug.getDrugName());
                count++;
            }
        }
        System.out.println(); // Ensure we move to the next line after the loop

        System.out.print("\nEnter Drug ID or Drug Name to add to cart: ");
        String input = scanner.nextLine();
        Drug drug = null;

        try {
            int drugId = Integer.parseInt(input);
            drug = findDrugById(drugId);
        } catch (NumberFormatException e) {
            drug = findDrugByName(input);
        }

        if (drug == null) {
            System.out.println("Drug not found.");
            return;
        }

        if (drug.getExpiryDate().before(new Date())) {
            System.out.println("Cannot add expired drug to cart.");
            return;
        }

        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        CartItem cartItem = new CartItem(drug, quantity);
        cart.addToCart(cartItem);

        try {
            String email = cart.getEmail();
            dbHandler.executeQuery("INSERT INTO Cart (email, drug_id, quantity) VALUES ('" + email + "', " + drug.getDrugId() + ", " + quantity + ") " +
                    "ON CONFLICT (email, drug_id) DO UPDATE SET quantity = Cart.quantity + EXCLUDED.quantity");

            System.out.println("Drug added to cart successfully!");
            actionStack.push("Added drug to cart: " + drug.getDrugId() + " with quantity: " + quantity);
        } catch (Exception e) {
            System.out.println("Error updating cart: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Find drug by name
    private static Drug findDrugByName(String drugName) {
        for (Drug drug : drugList) {
            if (drug.getDrugName().equalsIgnoreCase(drugName)) {
                return drug;
            }
        }
        return null;
    }

    // Find drug by ID
    private static Drug findDrugById(int drugId) {
        for (Drug drug : drugList) {
            if (drug.getDrugId() == drugId) {
                return drug;
            }
        }
        return null;
    }

    // View items in the cart
    private static void viewCart(Cart cart) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- Cart Items ---");
        if (cart.getItems().isEmpty()) {
            System.out.println("Cart is empty.");
        } else {
            System.out.printf("%-10s %-20s %-10s %-10s\n", "Drug ID", "Drug Name", "Quantity", "Price");

            for (CartItem item : cart.getItems()) {
                Drug drug = item.getDrug();
                System.out.printf("%-10d %-20s %-10d %-10.2f\n",
                        drug.getDrugId(),
                        drug.getDrugName(),
                        item.getQuantity(),
                        drug.getPrice());
            }

            System.out.print("\nDo you want to remove any drug from the cart? (yes/no): ");
            String response = scanner.nextLine();

            if (response.equalsIgnoreCase("yes")) {
                System.out.print("Enter Drug ID to remove: ");
                int drugId = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                CartItem itemToRemove = null;
                for (CartItem item : cart.getItems()) {
                    if (item.getDrug().getDrugId() == drugId) {
                        itemToRemove = item;
                        break;
                    }
                }

                if (itemToRemove != null) {
                    cart.removeFromCart(itemToRemove);
                    System.out.println("Drug removed from cart successfully!");
                    // Optionally update the database to reflect this change
                    try {
                        dbHandler.executeQuery("DELETE FROM Cart WHERE email='" + cart.getEmail() + "' AND drug_id=" + drugId);
                        actionStack.push("Removed drug from cart: " + drugId);
                    } catch (Exception e) {
                        System.out.println("Error updating cart in the database: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Drug not found in cart.");
                }
            }
        }
    }

    // Help method to provide drug information
    private static void help() {
        System.out.print("\nEnter drug description, tag, name, or ID to get information: \n");
        String input = scanner.nextLine();

        Drug drug = null;

        try {
            int drugId = Integer.parseInt(input);
            drug = findDrugById(drugId);
        } catch (NumberFormatException e) {
            drug = findDrugByName(input);
            if (drug == null) {
                drug = findDrugByDescriptionOrTag(input);
            }
        }

        if (drug != null) {
            System.out.println("----Drug Information----");
            System.out.println("ID: " + drug.getDrugId());
            System.out.println("Name: " + drug.getDrugName());
            System.out.println("Manufacturer: " + drug.getManufacturer());
            System.out.println("Expiry Date: " + new SimpleDateFormat("yyyy-MM-dd").format(drug.getExpiryDate()));
            System.out.println("Quantity: " + drug.getQuantity());
            System.out.println("Price: " + drug.getPrice());
            System.out.println("Description: " + drug.getDescription());
            System.out.println("Tags: " + drug.getTags());
            actionStack.push("Viewed information for drug: " + drug.getDrugId());
        } else {
            System.out.println("Drug not found.");
        }
    }

    // Find drug by description or tag
    private static Drug findDrugByDescriptionOrTag(String input) {
        for (Drug drug : drugList) {
            if (drug.getDescription().toLowerCase().contains(input.toLowerCase()) || drug.getTags().toLowerCase().contains(input.toLowerCase())) {
                return drug;
            }
        }
        return null;
    }

    // Checkout method to finalize the cart
    private static void checkout(Cart cart) {
        if (cart.getItems().isEmpty()) {
            System.out.println("Cart is empty. Cannot checkout.");
            return;
        }

        double totalAmount = calculateTotalAmount(cart);

        if (totalAmount == 0) {
            System.out.println("Total amount is zero. Cannot checkout.");
            return;
        }

        Order order = new Order(cart.getCartId(), cart.getEmail(), new Date(), totalAmount, cart.getItems());

        try {
            order.saveOrder();
            order.generateInvoice();

            cart.getItems().clear();
            dbHandler.deleteCartByEmail(cart.getEmail());
            System.out.println("Checkout successful! Invoice generated.");
            actionStack.push("Checked out cart with ID: " + cart.getCartId());
        } catch (Exception e) {
            System.out.println("Error during checkout: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Calculate total amount in the cart
    private static double calculateTotalAmount(Cart cart) {
        double totalAmount = 0;
        for (CartItem item : cart.getItems()) {
            if (item.getDrug() != null && item.getQuantity() > 0) {
                totalAmount += item.getQuantity() * item.getDrug().getPrice();
            } else {
                System.out.println("Invalid item in cart: " + item);
            }
        }
        return totalAmount;
    }

    // Find customer by email
    private static Customer findCustomerByEmail(String email) {
        for (Customer customer : customerList) {
            if (customer.getEmail().equals(email)) {
                return customer;
            }
        }
        return null;
    }

    // Find cart by email
    private static Cart findCartByEmail(String email) {
        for (Cart cart : cartList) {
            if (cart.getEmail().equals(email)) {
                return cart;
            }
        }
        return null;
    }



    // Load initial data from the database
    private static void loadInitialData() {
        loadDrugData();
        loadCustomerData();
        loadCartData();
    }

    // Load drug data from the database
    private static void loadDrugData() {
        String query = "SELECT * FROM Drugs";
        ResultSet resultSet = dbHandler.executeSelectQuery(query);

        try {
            while (resultSet.next()) {
                int drugId = resultSet.getInt("drug_id");
                String drugName = resultSet.getString("drug_name");
                String manufacturer = resultSet.getString("manufacturer");
                Date expiryDate = resultSet.getDate("expiry_date");
                int quantity = resultSet.getInt("quantity");
                double price = resultSet.getDouble("price");
                String description = resultSet.getString("description");
                String tags = resultSet.getString("tags");

                Drug drug = new Drug(drugId, drugName, manufacturer, expiryDate, quantity, price, description, tags);
                drugList.add(drug);
            }
            System.out.println("Initial drug data loaded successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load customer data from the database
    private static void loadCustomerData() {
        String query = "SELECT * FROM Customers";
        ResultSet resultSet = dbHandler.executeSelectQuery(query);

        try {
            while (resultSet.next()) {
                String email = resultSet.getString("email");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String phoneNumber = resultSet.getString("phone_number");

                Customer customer = new Customer(email, name, address, phoneNumber);
                customerList.add(customer);
            }
            System.out.println("Initial customer data loaded successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Load cart data from the database
    private static void loadCartData() {
        String query = "SELECT * FROM Cart";
        ResultSet resultSet = dbHandler.executeSelectQuery(query);

        try {
            while (resultSet.next()) {
                int cartId = resultSet.getInt("cart_id");
                String email = resultSet.getString("email");
                int drugId = resultSet.getInt("drug_id");
                int quantity = resultSet.getInt("quantity");

                Drug drug = findDrugById(drugId);
                if (drug != null) {
                    CartItem cartItem = new CartItem(drug, quantity);
                    Cart cart = findCartByEmail(email);
                    if (cart == null) {
                        cart = new Cart(cartId, email);
                        cartList.add(cart);
                    }
                    cart.addToCart(cartItem);
                }
            }
            System.out.println("Initial cart data loaded successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
