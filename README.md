# Fast Food Store Management System (Hệ thống Quản lý Cửa hàng Thức ăn nhanh)

A desktop-based Fast Food Store Management and Point of Sale (POS) system built using **Java Swing** for the Graphical User Interface (GUI) and **MySQL** as the relational database management system. Designed using a **layered architecture (MVC-like pattern)**, the application facilitates efficient management of products, categories, orders, user authentication/roles, and sales analytics.

---

## 🌟 Key Features

### 1. Role-Based Access Control (RBAC)
*   **Admin (Administrator):** Full administrative privileges. Admins can manage food items, categories, user accounts, perform transactions (POS), view order history, and access detailed business analytics and reports.
*   **User (Cashier / Customer):** Standard privileges. Users can access:
    *   **Welcome / Dashboard screen.**
    *   **Point of Sale (POS):** Browse menus, manage cart items, process checkouts, and generate invoices.
    *   **Order History:** View personal transaction logs and review itemized order details.

### 2. Point of Sale (POS) Screen
*   Browse food items categorized by menu type (Categories).
*   Add items to the cart, with automatic quantity aggregation and real-time total price calculation.
*   Remove items from the cart or clear the cart.
*   **Checkout & Invoicing:** Persist transactional data and itemized details securely in the MySQL database.

### 3. Order History & Management
*   Visual lists of all placed orders.
*   Inspect detailed receipt views including product name, unit price at purchase, and quantity.
*   Delete order records (restricted to Admin accounts).

### 4. Food & Category Management
*   **Product Management:** Admins can view the product list, add new items (name, price, category classification), and delete products.
*   **Category Management:** Admins can create or remove categories (e.g., Burger, Drinks, Fried Chicken, etc.).

### 5. User Account Management
*   Self-registration from the Sign-Up screen (newly registered accounts default to the `User` role).
*   Admin dashboard to review all registered accounts and delete user accounts (with protection preventing self-deletion).

### 6. Reports & Analytics
*   **Key Business Metrics:**
    *   *Today's Revenue* (VND)
    *   *Today's Order Count*
    *   *Current Month's Revenue* (VND)
    *   *Average Order Value (AOV)* (VND)
*   **Custom-Rendered Charts (Custom Swing Graphics):**
    *   Last 7 days revenue trend (Bar Chart).
    *   Last 6 months revenue trend (Bar Chart).
*   **Top 5 Best-Selling Products:** A ranking table displaying top items by units sold and generated revenue.
*   **Cumulative Revenue:** Displays the overall historical revenue of the establishment.

---

## 🛠️ Technology Stack

*   **Programming Language:** Java (JDK 8 or higher).
*   **GUI Framework:** Java Swing (configured to use the system native Look and Feel).
*   **Database:** MySQL (or MariaDB).
*   **Database Driver:** JDBC (MySQL Connector/J).

---

## 📁 Project Directory Structure

```text
baitapjava/
├── .classpath              # Eclipse classpath configuration
├── .project                # Eclipse project details
├── .gitignore              # Git ignore file
├── database.sql            # SQL schema script to initialize MySQL database
└── com/                    # Main application source code
    ├── model/              # Entity models
    │   ├── User.java       # User account details & roles
    │   ├── Product.java    # Food item details
    │   └── Categories.java # Food category details
    ├── dao/                # Data Access Objects (DB query handlers)
    │   ├── UserDAO.java
    │   ├── ProductDAO.java
    │   ├── CategoryDAO.java
    │   └── OrderDAO.java
    ├── util/               # Utility classes
    │   └── DBConnection.java # JDBC MySQL connection configuration
    └── view/               # Swing GUI components
        ├── LoginView.java     # Authentication screen & application entry point (Main)
        ├── RegisterView.java  # User registration screen
        └── MainView.java      # Dashboard & workspace window
```

---

## 🗄️ Database Schema

The database is named `fastfood_db` and contains the following tables:
*   `account`: Stores credentials and user profiles (Admin uses `role = 1`, User uses `role = 0`).
*   `categories`: Stores categories of food items.
*   `products`: Stores food items referencing their respective categories.
*   `orders`: Captures transaction-level summaries (date/time, total price, associated account).
*   `detailOrder`: Stores line-item details for each order (product references, quantity, and price at sale).

---

## 🚀 Installation & Setup

### 1. Prerequisites
*   Install **Java Development Kit (JDK)** version 8 or newer.
*   Set up a **MySQL Server** (using local installations, XAMPP, Laragon, or dockerized instances).

### 2. Database Initialization
*   Connect to your MySQL server using your preferred client (phpMyAdmin, DBeaver, Command Line, etc.).
*   Import the [database.sql](file:///c:/Users/ANH%20HOA/Documents/DUT/baitapjava/database.sql) script to automatically create the database `fastfood_db` and populate mock data.

### 3. Database Connection Configuration
*   Open [DBConnection.java](file:///c:/Users/ANH%20HOA/Documents/DUT/baitapjava/com/util/DBConnection.java).
*   Update the credentials to match your local MySQL configuration:
    ```java
    String url = "jdbc:mysql://localhost:3306/fastfood_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    String user = "root";   // Your MySQL database username
    String pass = "";       // Your MySQL database password
    ```

### 4. Import JDBC Driver
*   This project requires the **MySQL Connector/J** library (e.g., `mysql-connector-j-*.jar`).
*   **Eclipse IDE instructions:**
    1.  Download the MySQL JDBC Connector `.jar` file.
    2.  Right-click on the Project -> **Build Path** -> **Configure Build Path...**.
    3.  Go to the **Libraries** tab.
    4.  Select **Classpath** (if applicable) and click **Add External JARs...**.
    5.  Select the downloaded `.jar` file and click **Apply and Close**.

---

## 🏃 Running the Application

1.  Open your IDE (Eclipse, IntelliJ IDEA, or VS Code) and import the project.
2.  Verify that your MySQL server is running.
3.  Navigate to [LoginView.java](file:///c:/Users/ANH%20HOA/Documents/DUT/baitapjava/com/view/LoginView.java).
4.  Right-click the file and select **Run As** -> **Java Application** (or execute the `main` method).
5.  On the Login window:
    *   You can register a new account on the Register screen. By default, newly registered accounts are assigned the **User** role.
    *   To grant **Admin** privileges, manually update the `role` field to `1` for the desired account row in the `account` table within your database.