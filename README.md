# Library Database System (CT6049)

A Java-based library management application featuring a Swing GUI and support for dual database backends: **Oracle (Relational)** and **MongoDB (NoSQL)**. This project demonstrates the implementation of both relational and document-oriented persistence layers within a single application.

## Features

- **Dual Backend Support**: Seamlessly switch between Oracle and MongoDB at runtime.
- **Swing GUI**: Modern, clean interface using [FlatLaf](https://www.formdev.com/flatlaf/).
- **Library Operations**: Comprehensive management of books, students, loans, and fines.
- **Data Persistence**: Full CRUD operations tailored for both relational (SQL) and document (NoSQL) data models.

## Prerequisites

- **Java JDK 8+**
- **Maven** (for building from source)
- **Oracle Database** (12c or later)
- **MongoDB** (Server and Shell)

## Setup

### 1. Database Initialization

Before running the application, you must initialize the databases. Scripts are located in the `main run/database/` directory.

**Oracle Setup:**
1.  Navigate to `main run/database/oracle`.
2.  Run `scripts/create_oracle_user.sql` (requires `sysdba` or administrative privileges) to create the application user.
3.  Run `schema/schema.sql` (as the newly created user) to create the tables.
4.  (Optional) Run `data/sample_data.sql` to seed the database with initial records.

**MongoDB Setup:**
1.  Ensure your MongoDB server is running.
2.  Navigate to `main run/database/mongodb`.
3.  Run the seed script using `mongosh` or `node` to initialize the collections and data:
    ```bash
    mongosh < seed.js
    ```

### 2. Building the Project

Navigate to the source directory and build with Maven:

```bash
cd "main run/source_code"
mvn clean package
```

This will compile the code, run tests, and generate the application artifacts in `main run/source_code/target/`, including:
- `library-database-system-1.0.0.jar`: The main executable JAR.
- `library_oracle.exe`: Windows executable pre-configured for Oracle.
- `library_mongo.exe`: Windows executable pre-configured for MongoDB.

## Running the Application

### Method 1: Using Maven (From Source)
You can run the application directly from the source code folder:

```bash
cd "main run/source_code"
mvn exec:java
```

### Method 2: Using Generated Executables (Windows)
After building, execute one of the generated wrappers in the `target` folder:

- **`library_oracle.exe`**: Launches the application connected to the Oracle database.
- **`library_mongo.exe`**: Launches the application connected to MongoDB.

### Method 3: Using the JAR File
Run the shaded JAR file directly. You will be prompted to select a backend if not specified.

```bash
java -jar "main run/source_code/target/library-database-system-1.0.0.jar"
```

### Command Line Arguments
You can bypass the selection dialog by specifying the backend via the `--backend` flag:

```bash
java -jar application.jar --backend=oracle
java -jar application.jar --backend=mongo
```

## Project Structure

For a detailed breakdown of the codebase, folders, and files, please refer to [CODEBASE_STRUCTURE.md](main%20run/CODEBASE_STRUCTURE.md).
