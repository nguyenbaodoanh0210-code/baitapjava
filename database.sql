CREATE DATABASE IF NOT EXISTS fastfood_db;
USE fastfood_db;

CREATE TABLE account (
    useId INT AUTO_INCREMENT PRIMARY KEY,
    useName VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    fullName VARCHAR(255) NOT NULL,
    role TINYINT(1) DEFAULT 0
);

CREATE TABLE categories (
    categoryId INT AUTO_INCREMENT PRIMARY KEY,
    categoryName VARCHAR(255) NOT NULL
);

CREATE TABLE products (
    productId INT AUTO_INCREMENT PRIMARY KEY,
    productName VARCHAR(255) NOT NULL,
    price DECIMAL(18,2) NOT NULL,
    categoryId INT,
    FOREIGN KEY (categoryId) REFERENCES categories(categoryId)
);

CREATE TABLE orders (
    orderId INT AUTO_INCREMENT PRIMARY KEY,
    orderDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    priceTotal DECIMAL(18,2) NOT NULL,
    useId INT,
    FOREIGN KEY (useId) REFERENCES account(useId)
);

CREATE TABLE detailOrder (
    id INT AUTO_INCREMENT PRIMARY KEY,
    productId INT,
    orderId INT,
    quantity INT NOT NULL,
    priceAtSale DECIMAL(18,2),
    FOREIGN KEY (productId) REFERENCES products(productId),
    FOREIGN KEY (orderId) REFERENCES orders(orderId)
);