## Drugs Table

**Schema:**
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
```

**Purpose:**
- This table stores information about all drugs available in the pharmacy.
- Each drug has a unique `drug_id`, along with details such as `drug_name`, `manufacturer`, `expiry_date`, `quantity`, `price`, `description`, and `tags`.

**Usage in Project:**
- The `Drug` class maps to this table.
- Operations like adding, updating, deleting, and listing drugs directly interact with this table.
- During checkout, drug quantities are updated in this table.

### Customers Table

**Schema:**
```sql
CREATE TABLE Customers (
    email VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL CHECK (name !~ '[0-9]'),
    address VARCHAR(255) NOT NULL,
    phone_number VARCHAR(15) NOT NULL
);
```

**Purpose:**
- This table stores information about customers.
- Each customer is identified by a unique `email`, along with `name`, `address`, and `phone_number`.

**Usage in Project:**
- The `Customer` class maps to this table.
- Operations like registering, updating, and deleting customers interact with this table.
- Customer data is referenced in `Cart` and `Orders` tables.

### Cart Table

**Schema:**
```sql
CREATE TABLE Cart (
    cart_id SERIAL PRIMARY KEY,
    email VARCHAR(255),
    drug_id INT,
    quantity INT NOT NULL,
    FOREIGN KEY (email) REFERENCES Customers(email),
    FOREIGN KEY (drug_id) REFERENCES Drugs(drug_id),
    UNIQUE (email, drug_id)
);
```

**Purpose:**
- This table stores the items that customers add to their cart before checkout.
- Each cart entry is linked to a customer via `email` and a drug via `drug_id`.
- The `quantity` field indicates how many units of the drug are added to the cart.

**Usage in Project:**
- The `Cart` and `CartItem` classes map to this table.
- Operations like adding drugs to the cart, viewing the cart, and removing items from the cart interact with this table.
- The cart is cleared once an order is placed.

### Orders Table

**Schema:**
```sql
CREATE TABLE Orders (
    order_id SERIAL PRIMARY KEY,
    email VARCHAR(255),
    order_date DATE NOT NULL,
    total_amount DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (email) REFERENCES Customers(email)
);
```

**Purpose:**
- This table stores information about completed orders.
- Each order is linked to a customer via `email` and includes `order_date` and `total_amount`.

**Usage in Project:**
- The `Order` class maps to this table.
- Operations like placing an order and generating invoices interact with this table.
- Order details are stored here for historical reference.

### OrderItems Table

**Schema:**
```sql
CREATE TABLE OrderItems (
    order_item_id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    drug_id INT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (drug_id) REFERENCES Drugs(drug_id)
);
```

**Purpose:**
The OrderItems table is designed to store detailed information about each item in an order.
Each row in this table represents a single item from a specific order.
This allows the system to record multiple items for a single order, facilitating detailed order tracking and historical analysis.

**Fields:**

- order_item_id:
  - Type: SERIAL PRIMARY KEY
  - Description: A unique identifier for each row in the OrderItems table.
  - This is an auto-incrementing field that ensures each order item has a unique ID.

- order_id: 
  - Type: INT NOT NULL
  - Description: A foreign key that links to the Orders table
  - This field indicates which order this item belongs to.


- drug_id: 
  - Type: INT NOT NULL
  -  Description: A foreign key that links to the Drugs table.
  - This field indicates which drug has been ordered.
  - It helps in identifying the drug details such as name, manufacturer, and price at the time of the order.

- quantity:
  - Type: INT NOT NULL
  - Description: The number of units of the drug that have been ordered.
  - This helps in calculating the total cost of the order and managing inventory.

- price: 
  - Type: DOUBLE PRECISION NOT NULL
  - Description: The price per unit of the drug at the time of the order.
  - This is necessary to maintain historical pricing information, which is important for generating accurate invoices and financial reports.

**Interactions:**
  - Adding Items to OrderItems:
  - When a customer `checkout` their cart, the items in the cart are `transferred` to the OrderItems table.



**Usage in of Tables in Project:**
- The `Order` class interacts with this table when saving order details.
- During checkout, each cart item is moved to this table as part of the order.

### Drugs_Audit Table

**Schema:**
```sql
CREATE TABLE Drugs_Audit (
    audit_id SERIAL PRIMARY KEY,
    operation VARCHAR(10) NOT NULL,
    drug_id INT NOT NULL,
    old_drug_name VARCHAR(255),
    old_manufacturer VARCHAR(255),
    old_expiry_date DATE,
    old_quantity INT,
    old_price DOUBLE PRECISION,
    old_description TEXT,
    old_tags VARCHAR(255),
    new_drug_name VARCHAR(255),
    new_manufacturer VARCHAR(255),
    new_expiry_date DATE,
    new_quantity INT,
    new_price DOUBLE PRECISION,
    new_description TEXT,
    new_tags VARCHAR(255),
    operation_time VARCHAR(50)
);
```

**Purpose:**
- This table stores information about any update and delete operation that occurs on the drugs table.
- Each Operation data is stored on the table such as `date and time`, `operation type`, and `what has been changed`.

**Usage in Project:**
- Operations like `Updating or Deleting` drug details interact with this table using a `trigger` in the database.
- Drug details are stored here for historical reference.


## Detailed Interactions

### 1. Adding a Drug

- When a new drug is added using the `addDrug` method in the `Main` class, a new entry is created in the `Drugs` table.
- The `DatabaseHandler` executes an `INSERT` query to add the new drug details.

### 2. Registering a Customer

- When a new customer registers using the `registerCustomer` method in the `Main` class, a new entry is created in the `Customers` table.
- The `DatabaseHandler` executes an `INSERT` query to add the customer details.

### 3. Adding a Drug to Cart

- When a drug is added to a customer's cart using the `addDrugToCart` method in the `Main` class, an entry is created or updated in the `Cart` table.
- The `DatabaseHandler` executes an `INSERT` or `UPDATE` query to reflect the drug and quantity in the cart.

### 4. Checking Out

- When a customer checks out using the `checkout` method in the `Main` class:
  1. A new entry is created in the `Orders` table with the order details.
  2. Each cart item is transferred to the `OrderItems` table with the respective order ID.
  3. The `Drugs` table is updated to reflect the reduced quantities of the drugs.
  4. The `Cart` table is cleared for the customer.

### 5. Deleting a Customer

- When a customer is deleted using the `deleteCustomer` method in the `Main` class:
  1. All associated records in the `Cart` and `Orders` tables are deleted.
  2. The `DatabaseHandler` executes `DELETE` queries to remove the customer's data from the `Customers`, `Cart`, `Orders`, and `OrderItems` tables.

### 6. Deleting a Drug

- When a drug is deleted using the `deleteDrug` method in the `Main` class:
  1. All associated records in the `Cart` and `OrderItems` tables are deleted.
  2. The `DatabaseHandler` executes `DELETE` queries to remove the drug's data from the `Drugs`, `Cart`, and `OrderItems` tables.
  3. All data-related drugs such as `drug_name`, `manufacturer`, `expiry_date`, `quantity`, `price`, `description`, `tags` and `operation` get `inserted` into `Drugs_Audit` table with `TimeStamp` by using `TRIGGERS` stored in database.

## Summary

  - The tables in the database schema are tightly interlinked to maintain data integrity and support the functionalities of the `Pharmacy Management System`.
  - The relationships are established through foreign keys, ensuring that the data in related tables remains consistent.
  - The application uses these tables to perform various operations like adding drugs, managing customers, handling carts, and processing orders, leveraging the database to store and retrieve the necessary information efficiently.
