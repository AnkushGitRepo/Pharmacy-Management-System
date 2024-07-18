# Pharmacy Store Management System

The Pharmacy Store Management System is a console-based application designed to facilitate the efficient management of drug inventory and customer transactions in a pharmacy. The system allows an admin to perform various tasks, including managing drugs and customer records, processing sales, and generating invoices.

## Table of Contents

- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Database Schema](#database-schema)
- [StackDSA Usage](#stackdsa-usage)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Drug Management**: Add, update, delete, view, and list expired drugs.
- **Customer Management**: Register, delete, and update customer information.
- **Cart Management**: Add drugs to cart, view cart, and checkout.
- **Help**: Get information about drugs using descriptions, tags, names, or IDs.
- **Action Logging**: All user actions are stored in a stack data structure for review.

## System Requirements

### Software Requirements

- Java Development Kit (JDK) 8 or above
- Database Management System (PostgreSQL)
- Java IDE (Eclipse, IntelliJ IDEA, or NetBeans)

### Hardware Requirements

- Processor: Intel Core i3 or above
- RAM: 4 GB or above
- Storage: 10 GB of free disk space

## Installation

1. **Clone the repository**:
    ```sh
    git clone https://github.com/yourusername/pharmacy-store-management-system.git
    ```

2. **Navigate to the project directory**:
    ```sh
    cd pharmacy-store-management-system
    ```

3. **Set up the database**:
    - Create a PostgreSQL database named `pharmacy`.
    - Run the SQL scripts in the `sql/` directory to create the necessary tables.

4. **Update database connection settings**:
    - Update the database connection settings in the `DatabaseHandler` class to match your PostgreSQL configuration.

5. **Compile and run the application**:
    ```sh
    javac -d bin src/org/example/*.java
    java -cp bin org.example.Main
    ```

## Usage

### Main Menu

1. **Drug Management**: Manage drug inventory.
2. **Customer Management**: Manage customer records.
3. **Manage Cart**: Add drugs to cart, view cart, and checkout.
4. **Help**: Get information about drugs.
5. **Print Actions Stack**: View logged user actions.
6. **Exit**: Exit the application.

### Drug Management

- **Add Drug**: Add a new drug to the inventory.
- **Update Drug**: Update details of an existing drug.
- **Delete Drug**: Remove a drug from the inventory.
- **View Drug Inventory**: Display all drugs in the inventory.
- **List Expired Drugs**: List drugs that have expired.
- **Help**: Get information about drugs.

### Customer Management

- **Register Customer**: Register a new customer.
- **Delete Customer**: Remove a customer.
- **Update Customer**: Update customer information.
- **Manage Cart**: Add drugs to cart, view cart, and checkout for a customer.

## Database Schema

### Tables

```sql
CREATE TABLE Drugs (
    drug_id INT PRIMARY KEY CHECK (drug_id >= 1000 AND drug_id <= 9999),
    drug_name VARCHAR(255) NOT NULL,
    manufacturer VARCHAR(255) NOT NULL,
    expiry_date DATE NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    description TEXT,
    tags VARCHAR(255)
);



CREATE TABLE Customers (
    email VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL CHECK (name !~ '[0-9]'),
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL
);


CREATE TABLE Cart (
    cart_id SERIAL PRIMARY KEY,
    email VARCHAR(255),
    drug_id INT,
    quantity INT NOT NULL,
    FOREIGN KEY (email) REFERENCES Customers(email),
    FOREIGN KEY (drug_id) REFERENCES Drugs(drug_id),
    UNIQUE (email, drug_id)
);


CREATE TABLE Orders (
    order_id SERIAL PRIMARY KEY,
    email VARCHAR(255),
    order_date DATE NOT NULL,
    total_amount DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (email) REFERENCES Customers(email)
);

CREATE TABLE CartItems (
    cart_item_id SERIAL PRIMARY KEY,
    cart_id INT,
    drug_id INT,
    quantity INT NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES Cart(cart_id),
    FOREIGN KEY (drug_id) REFERENCES Drugs(drug_id)
);
```

```sh
    ![Activity Diagram of Pharmacy Management System](Pharmacy-Management-System/ActivityDiagram.png)
```

