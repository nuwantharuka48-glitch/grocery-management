# 🛒 FreshMart - Online Grocery Order Management System

A full-stack web application for an online grocery store built with **Java (Spring Boot)**, **Thymeleaf**, **HTML5**, **CSS3**, and **JavaScript**, featuring a **local file-based database** and complete **CRUD operations**.

Developed for the **SE1020 - Object-Oriented Programming** course.

---

## 🌟 Key Features

* **User Authentication & Session Management**:
  * User registration (Sign Up) and login (Sign In) system.
  * Form validation handled with client-side JavaScript.
  * Active sessions managed using Spring `HttpSession`.

* **Interactive Product Catalog**:
  * 10 grocery products with high-resolution imagery.
  * Category division: *Fresh Fruits* and *Seasoning & Spices*.
  * Quantity selector (`1, 2, 3...`) for each product.
  * **Real-time Live Total Calculator**: JavaScript dynamically updates the total price on user interaction without reloading the page.

* **Local PC Folder Database**:
  * Zero external database setup needed; uses local file storage.
  * Credentials stored securely in `local_database/users.txt`.
  * Order invoices generated dynamically as individual text receipts in `local_database/orders/ORD-XXXX.txt`.

* **Full CRUD Admin Dashboard (`/admin/orders`)**:
  * **Create**: Customers submit an order at checkout.
  * **Read**: Admin reads and lists all order receipt files from the PC folder.
  * **Update**: Admin can edit and update the delivery address of an existing order.
  * **Delete**: Admin can cancel/delete an order, which deletes the `.txt` receipt from the local hard drive.

---

## 🧬 Object-Oriented Programming (OOP) Concepts

This project explicitly implements core OOP principles:

1. **Inheritance (`extends`)**:
   * Base class: `GroceryItem` (Parent).
   * Subclasses: `FruitItem` (adds `isOrganic`) and `SpiceItem` (adds `spiceLevel`).
   * Eliminates boilerplate and establishes a clean class hierarchy.

2. **Polymorphism**:
   * A single generic collection (`List<GroceryItem>`) holds both `FruitItem` and `SpiceItem` instances dynamically.

3. **Encapsulation**:
   * All entity variables are marked `private`/`protected` and accessed via public getter and setter methods.

---

## 🛠️ Tech Stack

* **Backend**: Java 17+, Spring Boot (Spring Web, Thymeleaf)
* **Frontend**: HTML5, CSS3 (Responsive Grid Layout), Vanilla JavaScript
* **Database**: Local File System I/O (`java.nio.file`)
* **Build Tool**: Apache Maven
* **IDE**: IntelliJ IDEA

---

## 🚀 How to Run Locally

### 1. Prerequisites
* Java JDK 17 or higher installed.
* Git installed.

### 2. Clone the Repository
```bash
git clone https://github.com/nuwantharuka48-glitch/grocery-management.git
cd grocery-management
