# Pharmacy Management System - Database Trigger

## Overview

This document provides detailed information about the trigger implemented in the Pharmacy Management System database schema. The trigger is designed to log changes made to the `Drugs` table, capturing both `UPDATE` and `DELETE` operations.

## Trigger and Function Definition

### Trigger Purpose

The trigger ensures that any changes made to the `Drugs` table are recorded in the `Drugs_Audit` table. This helps in maintaining a history of changes for auditing and tracking purposes.

### Trigger Components

1. **Drugs_Audit Table**
2. **log_drug_changes Function**
3. **trg_log_drug_changes Trigger**

### Drugs_Audit Table
The `Drugs_Audit` table is used to store the history of changes made to the `Drugs` table.

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

## log_drug_changes Function
  - The `log_drug_changes` function is a `PL/pgSQL` function that logs changes to the Drugs table. It handles both `UPDATE` and `DELETE` operations.
### Definition:
```sql
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
```

## trg_log_drug_changes Trigger
  - The `trg_log_drug_changes` trigger is executed after each `UPDATE` or `DELETE` operation on the Drugs table, calling the `log_drug_changes` function.
### Definition:
```sql
CREATE TRIGGER trg_log_drug_changes
AFTER UPDATE OR DELETE ON Drugs
FOR EACH ROW
EXECUTE FUNCTION log_drug_changes();
```

# Summary
  - The trigger and function setup described above ensures that any changes to the `Drugs` table are properly logged. This aids in maintaining a reliable audit trail, which is crucial for tracking changes, performing audits, and ensuring data integrity in the Pharmacy Management System.

  - For further details, refer to the database schema and interaction documentation.
  - This [https://github.com/AnkushGitRepo/Pharmacy-Management-System/blob/main/Pharmacy_Management_System/src/main/java/org/example/DBMS/Database-Tables-and-Interactions.md](Database-Tables-and-Interactions.md)
  - File provides a concise yet comprehensive overview of the trigger used in your database schema, explaining its purpose, components, and definitions with syntax highlighting for better readability.

