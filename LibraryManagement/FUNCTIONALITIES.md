# Library Management System - Functionalities Documentation

## Overview
The Library Management System is a RESTful API application built with Spring Boot that manages library operations including books, authors, members, loans, reservations, and fines.

---

## 1. Book Management

### Core Operations
- **Create Book**: Register a new book in the system with title, description, publication year, available copies, category, and associated authors
- **Update Book**: Modify book details and reassign authors
- **Delete Book**: Remove a book from the system (cascades to book-author associations)
- **Get Book by ID**: Retrieve detailed information about a specific book including its authors
- **Get All Books**: List all books in the library with their authors

### Search & Filter
- **Search by Title**: Find books by title using case-insensitive partial matching
- **Filter by Category**: Retrieve books belonging to a specific category (Fiction, Non-Fiction, Technical, etc.)
- **Find Available Books**: List only books with available copies for lending

### Business Rules
- Each book must have at least one author
- Book-author relationships are validated before creation/update
- Available copies are automatically managed during loan operations

---

## 2. Author Management

### Core Operations
- **Create Author**: Add a new author with first name, last name, biography, birth date, and nationality
- **Update Author**: Modify author information
- **Delete Author**: Remove an author from the system
- **Get Author by ID**: Retrieve detailed information about a specific author
- **Get All Authors**: List all authors in the system

### Search & Relationships
- **Search by Name**: Find authors by first or last name using case-insensitive partial matching
- **Get Books by Author**: Retrieve all books written by a specific author

---

## 3. Member Management

### Core Operations
- **Register Member**: Create a new library member with personal details, membership type, and status
- **Update Member**: Modify member information including membership type and status
- **Delete Member**: Remove a member from the system
- **Get Member by ID**: Retrieve detailed information about a specific member
- **Get All Members**: List all registered members

### Search & Filter
- **Search by Name**: Find members by name using case-insensitive partial matching
- **Search by Email**: Find members by email using case-insensitive partial matching
- **Filter by Membership Type**: List members by type (Standard, Premium, Student)
- **Filter by Status**: List members by status (Active, Suspended, Expired)

### Member Activity
- **Get Member Loans**: View all loan transactions for a specific member
- **Get Member Reservations**: View all book reservations for a specific member
- **Get Member Fines**: View all fines associated with a specific member

---

## 4. Loan Management

### Core Operations
- **Create Loan**: Issue a book to a member with automatic due date calculation (14 days)
- **Update Loan**: Modify loan details (dates, status)
- **Delete Loan**: Remove a loan record from the system
- **Get Loan by ID**: Retrieve detailed information about a specific loan
- **Get All Loans**: List all loan transactions

### Loan Actions
- **Return Loan**: Process book return, update status, and increment available copies
- **Renew Loan**: Extend loan duration by 14 days (only for active loans)

### Search & Filter
- **Filter by Status**: List loans by status (Active, Returned, Overdue)
- **Get Loans by Member**: View all loans for a specific member
- **Get Loans by Book**: View loan history for a specific book
- **Count Active Loans**: Get the number of active loans for a member

### Business Rules
- Books must have available copies to be loaned
- Available copies are decremented when a loan is created
- Available copies are incremented when a loan is returned
- Only active loans can be renewed
- Loans cannot be returned twice

---

## 5. Reservation Management

### Core Operations
- **Create Reservation**: Reserve a book that is currently unavailable with automatic expiry date (7 days)
- **Update Reservation**: Modify reservation details
- **Delete Reservation**: Remove a reservation from the system
- **Get Reservation by ID**: Retrieve detailed information about a specific reservation
- **Get All Reservations**: List all reservations

### Reservation Actions
- **Cancel Reservation**: Cancel a pending reservation
- **Fulfill Reservation**: Mark a reservation as fulfilled when the book becomes available
- **Expire Reservation**: Mark a reservation as expired when not fulfilled in time

### Search & Filter
- **Filter by Status**: List reservations by status (Pending, Fulfilled, Cancelled, Expired)
- **Get Reservations by Member**: View all reservations for a specific member
- **Get Reservations by Book**: View reservation queue for a specific book
- **Count Active Reservations**: Get the number of pending reservations for a member

### Business Rules
- Reservations can only be created for unavailable books (0 available copies)
- Only pending reservations can be cancelled, fulfilled, or expired
- Reservations automatically expire after 7 days

---

## 6. Fine Management

### Core Operations
- **Create Fine**: Manually create a fine for a loan with custom amount and reason
- **Update Fine**: Modify fine details (amount, reason, status)
- **Delete Fine**: Remove a fine record from the system
- **Get Fine by ID**: Retrieve detailed information about a specific fine
- **Get All Fines**: List all fines in the system

### Fine Actions
- **Pay Fine**: Mark a fine as paid with automatic payment date
- **Waive Fine**: Forgive a fine (change status to waived)

### Automated Fine Generation
- **Generate Fine for Loan**: Automatically calculate and create a fine for an overdue loan (0.50 per day)
- **Generate Overdue Fines**: Bulk generate fines for all overdue active loans

### Search & Filter
- **Filter by Status**: List fines by status (Pending, Paid, Waived)
- **Get Fines by Member**: View all fines for a specific member
- **Get Fine by Loan**: Retrieve the fine associated with a specific loan
- **Get Unpaid Fines**: List all pending fines

### Financial Reports
- **Total Unpaid by Member**: Calculate total outstanding fine amount for a member
- **Total Paid by Member**: Calculate total paid fine amount for a member
- **Total Collected**: Get total amount of all paid fines system-wide
- **Total Outstanding**: Get total amount of all pending fines system-wide

### Business Rules
- Only one fine can exist per loan
- Fines are automatically calculated as: days overdue × 0.50
- Only pending fines can be paid or waived
- Fines can only be generated for active overdue loans

---

## Technical Features

### Database
- PostgreSQL database with Liquibase migrations
- Many-to-many relationships managed through junction tables

---

## API Endpoints Summary

| Entity | Base Path | Key Operations |
|--------|-----------|----------------|
| Books | `/books` | CRUD, search, filter by category, find available |
| Authors | `/authors` | CRUD, search, get books by author |
| Members | `/members` | CRUD, search, filter, get loans/reservations/fines |
| Loans | `/loans` | CRUD, return, renew, filter by status/member/book |
| Reservations | `/reservations` | CRUD, cancel, fulfill, expire, filter by status |
| Fines | `/fines` | CRUD, pay, waive, generate, financial reports |
