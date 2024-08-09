# Database Table and Triggers
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

CREATE TABLE OrderItems (
    order_item_id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    drug_id INT NOT NULL,
    quantity INT NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (drug_id) REFERENCES Drugs(drug_id)
);

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


CREATE OR REPLACE FUNCTION log_drug_changes() RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO Drugs_Audit (
            operation, drug_id, old_drug_name, old_manufacturer, old_expiry_date, old_quantity, old_price, old_description, old_tags,
            new_drug_name, new_manufacturer, new_expiry_date, new_quantity, new_price, new_description, new_tags, operation_time
        ) VALUES (
            'DELETE', OLD.drug_id, OLD.drug_name, OLD.manufacturer, OLD.expiry_date, OLD.quantity, OLD.price, OLD.description, OLD.tags,
            NULL, NULL, NULL, NULL, NULL, NULL, NULL,
            TO_CHAR(CURRENT_TIMESTAMP, 'DD/MM/YYYY HH12:MI:SS AM')
        );
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO Drugs_Audit (
            operation, drug_id, old_drug_name, old_manufacturer, old_expiry_date, old_quantity, old_price, old_description, old_tags,
            new_drug_name, new_manufacturer, new_expiry_date, new_quantity, new_price, new_description, new_tags, operation_time
        ) VALUES (
            'UPDATE', OLD.drug_id, OLD.drug_name, OLD.manufacturer, OLD.expiry_date, OLD.quantity, OLD.price, OLD.description, OLD.tags,
            NEW.drug_name, NEW.manufacturer, NEW.expiry_date, NEW.quantity, NEW.price, NEW.description, NEW.tags,
            TO_CHAR(CURRENT_TIMESTAMP, 'DD/MM/YYYY HH12:MI:SS AM')
        );
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_log_drug_changes
AFTER UPDATE OR DELETE ON Drugs
FOR EACH ROW
EXECUTE FUNCTION log_drug_changes();
```
