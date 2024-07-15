package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static DatabaseHandler dbHandler = new DatabaseHandler();
    private static LinkedListDSA<Drug> drugList = new LinkedListDSA<>();
    private static LinkedListDSA<Customer> customerList = new LinkedListDSA<>();
    private static LinkedListDSA<Cart> cartList = new LinkedListDSA<>();
    private static int cartIdCounter = 1;

    public static void main(String[] args) {
        dbHandler.connect();
        loadInitialData();

        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addDrug(); //Use to add drug details to drugs table(DBMS) and drugList(DSA)
                    break;
                case 2:
                    updateDrug();   //Use to update details of drug in drugs table(DBMS) and drugList(DSA)
                    break;
                case 3:
                    deleteDrug(); // Delete drug from drugList and drugs table.
                    break;
                case 4:
                    viewDrugInventory(); // Use to print the list of all drugs on terminal.
                    break;
                case 5:
                    listExpiredDrugs(); // Print all the expired drug on terminal
                    break;
                case 6:
                    registerCustomer(); // Use to resister new customer using their email id as primary key.
                    break;
                case 7:
                    deleteCustomer(); // Use to delete customer by passing their registered email id.
                    break;
                case 8:
                    manageCart(); // Use to login into cart session where admin can add drugs to cart, generate invoice for purchase.
                    break;
                case 9:
                    System.out.println("Exiting...");
                    dbHandler.closeConnection();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("\n--- Pharmacy Store Management System ---");
        System.out.println("1. Add Drug");
        System.out.println("2. Update Drug");
        System.out.println("3. Delete Drug");
        System.out.println("4. View Drug Inventory");
        System.out.println("5. List Expired Drugs");
        System.out.println("6. Register Customer");
        System.out.println("7. Delete Customer");
        System.out.println("8. Manage Cart");
        System.out.println("9. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addDrug() {
        System.out.print("Enter Drug ID: ");
        int drugId = scanner.nextInt();
        scanner.nextLine(); // consume newline
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
        scanner.nextLine(); // consume newline

        try {
            Date expiryDate = new SimpleDateFormat("yyyy-MM-dd").parse(expiryDateStr);
            Drug drug = new Drug(drugId, drugName, manufacturer, expiryDate, quantity, price);
            drugList.add(drug);
            // Save to database
            dbHandler.executeQuery("INSERT INTO Drugs VALUES (" + drugId + ", '" + drugName + "', '" + manufacturer + "', '" + expiryDateStr + "', " + quantity + ", " + price + ")");
            System.out.println("Drug added successfully!");
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
        }
    }

    private static void updateDrug() {
        System.out.print("Enter Drug ID to update: ");
        int drugId = scanner.nextInt();
        scanner.nextLine(); // consume newline

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
        scanner.nextLine(); // consume newline

        try {
            Date expiryDate = new SimpleDateFormat("yyyy-MM-dd").parse(expiryDateStr);
            drug.setDrugName(drugName);
            drug.setManufacturer(manufacturer);
            drug.setExpiryDate(expiryDate);
            drug.setQuantity(quantity);
            drug.setPrice(price);
            // Update database
            dbHandler.executeQuery("UPDATE Drugs SET drug_name='" + drugName + "', manufacturer='" + manufacturer + "', expiry_date='" + expiryDateStr + "', quantity=" + quantity + ", price=" + price + " WHERE drug_id=" + drugId);
            System.out.println("Drug updated successfully!");
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please try again.");
        }
    }

    private static void deleteDrug() {
        System.out.print("Enter Drug ID to delete: ");
        int drugId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Drug drug = findDrugById(drugId);
        if (drug == null) {
            System.out.println("Drug not found.");
            return;
        }

        drugList.remove(drug);
        // Delete from database
        dbHandler.executeQuery("DELETE FROM Drugs WHERE drug_id=" + drugId);
        System.out.println("Drug deleted successfully!");
    }

    private static void viewDrugInventory() {
        System.out.println("\n--- Drug Inventory ---");
        for (Drug drug : drugList) {
            System.out.println(drug);
        }
    }

    private static void listExpiredDrugs() {
        System.out.println("\n--- Expired Drugs ---");
        Date currentDate = new Date();
        for (Drug drug : drugList) {
            if (drug.getExpiryDate().before(currentDate)) {
                System.out.println(drug);
            }
        }
    }

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
        // Save to database
        dbHandler.executeQuery("INSERT INTO Customers VALUES ('" + email + "', '" + name + "', '" + address + "', '" + phoneNumber + "')");
        System.out.println("Customer registered successfully!");
    }

    private static void deleteCustomer() {
        System.out.print("Enter Customer Email to delete: ");
        String email = scanner.nextLine();

        Customer customer = findCustomerByEmail(email);
        if (customer == null) {
            System.out.println("Customer not found.");
            return;
        }

        customerList.remove(customer);
        // Delete from database
        dbHandler.executeQuery("DELETE FROM Customers WHERE email='" + email + "'");
        System.out.println("Customer deleted successfully!");
    }

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
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

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
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void showCartMenu() {
        System.out.println("\n--- Cart Management ---");
        System.out.println("1. Add Drug to Cart");
        System.out.println("2. View Cart");
        System.out.println("3. Checkout");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addDrugToCart(Cart cart) {
        System.out.print("Enter Drug ID to add to cart: ");
        int drugId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Drug drug = findDrugById(drugId);
        if (drug == null) {
            System.out.println("Drug not found.");
            return;
        }

        if (drug.getExpiryDate().before(new Date())) {
            System.out.println("Cannot add expired drug to cart.");
            return;
        }

        CartItem cartItem = new CartItem(drug, quantity);
        cart.addToCart(cartItem);
        System.out.println("Drug added to cart successfully!");
    }

    private static void viewCart(Cart cart) {
        System.out.println("\n--- Cart Items ---");
        cart.viewCart();
    }

    private static void checkout(Cart cart) {
        // Calculate total amount
        double totalAmount = 0;
        for (CartItem item : cart.getItems()) {
            totalAmount += item.getDrug().getPrice() * item.getQuantity();
        }

        // Generate order
        Order order = new Order(cartIdCounter++, cart.getEmail(), new Date(), totalAmount);
        order.generateInvoice();
        order.saveOrder();

        // Clear cart
        cart.getItems().clear();
        System.out.println("Checkout successful! Invoice generated.");
    }

    private static Drug findDrugById(int drugId) {
        for (Drug drug : drugList) {
            if (drug.getDrugId() == drugId) {
                return drug;
            }
        }
        return null;
    }

    private static Customer findCustomerByEmail(String email) {
        for (Customer customer : customerList) {
            if (customer.getEmail().equals(email)) {
                return customer;
            }
        }
        return null;
    }

    private static Cart findCartByEmail(String email) {
        for (Cart cart : cartList) {
            if (cart.getEmail().equals(email)) {
                return cart;
            }
        }
        return null;
    }

    private static void loadInitialData() {
        // Load initial data from the database if needed
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Drug[] drugs = {
                    new Drug(1001, "Aspirin", "PharmaCorp", sdf.parse("2025-12-31"), 100, 5.99),
                    new Drug(1002, "Ibuprofen", "HealthPlus", sdf.parse("2026-11-30"), 200, 10.99),
                    new Drug(1003, "Paracetamol", "MediLife", sdf.parse("2025-10-15"), 150, 3.99),
                    new Drug(1004, "Amoxicillin", "Antibiotix", sdf.parse("2025-09-20"), 75, 15.49),
                    new Drug(1005, "Ciprofloxacin", "BioPharma", sdf.parse("2026-01-25"), 80, 12.89),
                    new Drug(1006, "Lisinopril", "HeartCare", sdf.parse("2025-08-30"), 60, 8.75),
                    new Drug(1007, "Metformin", "Diabeat", sdf.parse("2025-07-14"), 90, 4.50),
                    new Drug(1008, "Omeprazole", "StomachEase", sdf.parse("2025-11-25"), 120, 7.20),
                    new Drug(1009, "Atorvastatin", "CholestrolFix", sdf.parse("2025-12-01"), 110, 9.30),
                    new Drug(1010, "Levothyroxine", "ThyroMed", sdf.parse("2026-02-14"), 100, 6.40),
                    new Drug(1011, "Amlodipine", "CardioSafe", sdf.parse("2025-06-20"), 95, 5.10),
                    new Drug(1012, "Simvastatin", "LipControl", sdf.parse("2026-03-10"), 85, 6.90),
                    new Drug(1013, "Clopidogrel", "BloodThinner", sdf.parse("2025-08-22"), 70, 11.00),
                    new Drug(1014, "Losartan", "HyperTensionRelief", sdf.parse("2025-09-14"), 65, 7.75),
                    new Drug(1015, "Gabapentin", "NerveRelief", sdf.parse("2025-10-05"), 130, 14.20),
                    new Drug(1016, "ExpiredDrug1", "ExpiredMeds", sdf.parse("2023-12-31"), 50, 5.50),
                    new Drug(1017, "ExpiredDrug2", "OldPharma", sdf.parse("2022-11-30"), 40, 6.60),
                    new Drug(1018, "ExpiredDrug3", "PastMeds", sdf.parse("2021-10-15"), 30, 7.70),
                    new Drug(1019, "ExpiredDrug4", "OutdatedPharma", sdf.parse("2022-09-20"), 25, 8.80),
                    new Drug(1020, "ExpiredDrug5", "OldStock", sdf.parse("2023-01-25"), 20, 9.90),
                    new Drug(1021, "Doxycycline", "Antibiotix", sdf.parse("2025-12-31"), 100, 13.45),
                    new Drug(1022, "Metoprolol", "HeartCare", sdf.parse("2025-11-30"), 200, 10.75),
                    new Drug(1023, "Albuterol", "BreathEasy", sdf.parse("2026-10-15"), 150, 12.00),
                    new Drug(1024, "Pantoprazole", "GastroCare", sdf.parse("2025-09-20"), 75, 14.55),
                    new Drug(1025, "Zolpidem", "SleepWell", sdf.parse("2025-01-25"), 80, 8.25)
            };
            for (Drug drug : drugs) {
                drugList.add(drug);
            }

            Customer[] initialCustomers = {
                    new Customer("ankushgupta1806@gmail.com","Ankush Gupta","A-1201","7202906881"),
                    new Customer("veer@gmail.com","Veer","A-1204","1234567890")
            };
            for(Customer customer : initialCustomers){
                customerList.add(customer);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
