-- liquibase formatted sql
-- changeset catalin:test-data

-- Test Data for Library Management System
-- This file contains sample data for testing all endpoints via Postman

-- ========================================
-- AUTHORS (8 records)
-- ========================================
INSERT INTO author (first_name, last_name, biography, birth_date, nationality) VALUES
('George', 'Orwell', 'English novelist and essayist, journalist and critic', '1903-06-25', 'British'),
('Jane', 'Austen', 'English novelist known primarily for her six major novels', '1775-12-16', 'British'),
('Mark', 'Twain', 'American writer, humorist, entrepreneur, publisher, and lecturer', '1835-11-30', 'American'),
('Agatha', 'Christie', 'English writer known for her detective novels', '1890-09-15', 'British'),
('Isaac', 'Asimov', 'American writer and professor of biochemistry, prolific science fiction author', '1920-01-02', 'American'),
('Stephen', 'Hawking', 'English theoretical physicist, cosmologist, and author', '1942-01-08', 'British'),
('J.K.', 'Rowling', 'British author, philanthropist, and film producer', '1965-07-31', 'British'),
('Malcolm', 'Gladwell', 'Canadian journalist, author, and public speaker', '1963-09-03', 'Canadian');

-- ========================================
-- BOOKS (10 records)
-- ========================================
INSERT INTO book (title, description, publication_year, available_copies, book_category) VALUES
('1984', 'Dystopian social science fiction novel and cautionary tale', 1949, 3, 'FICTION'),
('Animal Farm', 'Allegorical novella reflecting events leading up to the Russian Revolution', 1945, 2, 'FICTION'),
('Pride and Prejudice', 'Romantic novel of manners following the character development of Elizabeth Bennet', 1813, 4, 'FICTION'),
('The Adventures of Tom Sawyer', 'Novel about a boy growing up along the Mississippi River', 1876, 1, 'CHILDREN'),
('Murder on the Orient Express', 'Detective novel featuring Hercule Poirot', 1934, 2, 'MYSTERY'),
('Foundation', 'Science fiction novel about the collapse and rebirth of galactic civilization', 1951, 0, 'SCIENCE'),
('A Brief History of Time', 'Popular science book on cosmology', 1988, 5, 'SCIENCE'),
('Harry Potter and the Philosopher Stone', 'First novel in the Harry Potter series', 1997, 0, 'CHILDREN'),
('Outliers: The Story of Success', 'Examination of factors that contribute to high levels of success', 2008, 3, 'NON_FICTION'),
('The Innovators Dilemma', 'Business analysis book about disruptive innovation', 1997, 2, 'TECHNOLOGY');

-- ========================================
-- BOOK_AUTHOR (Many-to-Many Relationships)
-- ========================================
INSERT INTO book_author (book_id, author_id) VALUES
(1, 1),  -- 1984 by George Orwell
(2, 1),  -- Animal Farm by George Orwell
(3, 2),  -- Pride and Prejudice by Jane Austen
(4, 3),  -- Tom Sawyer by Mark Twain
(5, 4),  -- Murder on the Orient Express by Agatha Christie
(6, 5),  -- Foundation by Isaac Asimov
(7, 6),  -- A Brief History of Time by Stephen Hawking
(8, 7),  -- Harry Potter by J.K. Rowling
(9, 8),  -- Outliers by Malcolm Gladwell
(10, 8); -- The Innovators Dilemma by Malcolm Gladwell (co-author scenario)

-- ========================================
-- MEMBERS (7 records)
-- ========================================
INSERT INTO member (first_name, last_name, email, phone_no, address, membership_date, membership_type, status) VALUES
('John', 'Doe', 'john.doe@email.com', '+1-555-0101', '123 Main St, New York, NY 10001', '2024-01-15', 'PREMIUM', 'ACTIVE'),
('Jane', 'Smith', 'jane.smith@email.com', '+1-555-0102', '456 Oak Ave, Los Angeles, CA 90001', '2024-03-20', 'STANDARD', 'ACTIVE'),
('Robert', 'Johnson', 'robert.j@email.com', '+1-555-0103', '789 Pine Rd, Chicago, IL 60601', '2023-11-10', 'STUDENT', 'ACTIVE'),
('Emily', 'Davis', 'emily.davis@email.com', '+1-555-0104', '321 Elm St, Houston, TX 77001', '2024-02-05', 'STANDARD', 'ACTIVE'),
('Michael', 'Wilson', 'michael.w@email.com', '+1-555-0105', '654 Maple Dr, Phoenix, AZ 85001', '2023-08-22', 'PREMIUM', 'SUSPENDED'),
('Sarah', 'Brown', 'sarah.brown@email.com', '+1-555-0106', '987 Cedar Ln, Philadelphia, PA 19019', '2024-04-12', 'STUDENT', 'ACTIVE'),
('David', 'Martinez', 'david.m@email.com', '+1-555-0107', '147 Birch Blvd, San Antonio, TX 78201', '2022-12-30', 'STANDARD', 'EXPIRED');

-- ========================================
-- LOANS (8 records - mix of ACTIVE, RETURNED, OVERDUE)
-- ========================================
-- ACTIVE loans (current)
INSERT INTO loan (loan_date, due_date, return_date, status, member_id, book_id) VALUES
('2025-12-20 10:00:00+00', '2026-01-03 10:00:00+00', NULL, 'ACTIVE', 1, 1),  -- John borrowed 1984
('2025-12-28 14:30:00+00', '2026-01-11 14:30:00+00', NULL, 'ACTIVE', 2, 3),  -- Jane borrowed Pride and Prejudice
('2025-12-15 09:00:00+00', '2025-12-29 09:00:00+00', NULL, 'OVERDUE', 3, 4), -- Robert borrowed Tom Sawyer (OVERDUE for fine generation)

-- RETURNED loans (completed)
('2025-11-01 11:00:00+00', '2025-11-15 11:00:00+00', '2025-11-14 16:00:00+00', 'RETURNED', 1, 2),  -- John returned Animal Farm
('2025-11-10 13:00:00+00', '2025-11-24 13:00:00+00', '2025-11-23 10:00:00+00', 'RETURNED', 4, 5),  -- Emily returned Murder on the Orient Express
('2025-10-05 10:00:00+00', '2025-10-19 10:00:00+00', '2025-10-25 15:00:00+00', 'RETURNED', 2, 7),  -- Jane returned A Brief History (LATE - fine issued)

-- More OVERDUE loans for testing fine generation
('2025-11-20 12:00:00+00', '2025-12-04 12:00:00+00', NULL, 'OVERDUE', 5, 9),  -- Michael borrowed Outliers (OVERDUE and SUSPENDED member)
('2025-12-01 15:00:00+00', '2025-12-15 15:00:00+00', NULL, 'OVERDUE', 6, 10); -- Sarah borrowed The Innovators Dilemma (OVERDUE)

-- ========================================
-- RESERVATIONS (5 records - mix of statuses)
-- ========================================
INSERT INTO reservation (reservation_date, expiry_date, status, notification_sent, member_id, book_id) VALUES
-- PENDING reservations (for books with 0 available copies)
('2025-12-29 10:00:00+00', '2026-01-05 10:00:00+00', 'PENDING', FALSE, 4, 6),  -- Emily reserved Foundation
('2025-12-30 14:00:00+00', '2026-01-06 14:00:00+00', 'PENDING', TRUE, 1, 8),   -- John reserved Harry Potter

-- FULFILLED reservation
('2025-11-15 09:00:00+00', '2025-11-22 09:00:00+00', 'FULFILLED', TRUE, 2, 3), -- Jane's reservation was fulfilled

-- CANCELLED reservation
('2025-12-10 11:00:00+00', '2025-12-17 11:00:00+00', 'CANCELLED', FALSE, 3, 6), -- Robert cancelled Foundation reservation

-- EXPIRED reservation
('2025-11-01 13:00:00+00', '2025-11-08 13:00:00+00', 'EXPIRED', TRUE, 6, 8);   -- Sarah's reservation expired

-- ========================================
-- FINES (5 records - mix of PENDING, PAID, WAIVED)
-- ========================================
INSERT INTO fine (amount, reason, issued_date, paid_date, status, loan_id, member_id) VALUES
-- PENDING fines (unpaid)
(3.00, 'Overdue loan - 6 days late', '2025-12-10 10:00:00+00', NULL, 'PENDING', 3, 3),      -- Robert's overdue Tom Sawyer
(16.50, 'Overdue loan - 33 days late', '2025-12-23 12:00:00+00', NULL, 'PENDING', 7, 5),    -- Michael's overdue Outliers

-- PAID fines
(3.00, 'Overdue loan - 6 days late', '2025-10-26 09:00:00+00', '2025-11-05 14:30:00+00', 'PAID', 6, 2), -- Jane paid for late return

-- WAIVED fines
(1.50, 'Overdue loan - 3 days late', '2025-11-28 10:00:00+00', NULL, 'WAIVED', 4, 1),       -- John's fine was waived

-- Another PENDING fine
(10.50, 'Overdue loan - 21 days late', '2025-12-22 11:00:00+00', NULL, 'PENDING', 8, 6);    -- Sarah's overdue book

-- ========================================
-- Summary for Testing:
-- ========================================
-- Authors: 8 records
-- Books: 10 records (2 with 0 available copies for reservation testing)
-- Members: 7 records (1 SUSPENDED, 1 EXPIRED for edge case testing)
-- Book-Author: 10 mappings
-- Loans: 8 records (2 ACTIVE, 3 RETURNED, 3 OVERDUE)
-- Reservations: 5 records (2 PENDING, 1 FULFILLED, 1 CANCELLED, 1 EXPIRED)
-- Fines: 5 records (3 PENDING, 1 PAID, 1 WAIVED)
--
-- Test Scenarios Covered:
-- 1. Active loans for member lookup
-- 2. Overdue loans for fine generation endpoint testing
-- 3. Books with no copies for reservation testing
-- 4. Mix of member statuses
-- 5. Various fine statuses for payment/waive endpoints
-- 6. Mix of reservation statuses for workflow testing
-- 7. Books with multiple copies vs. out of stock
-- 8. Different book categories for filtering
-- 9. Multiple loans per member for counting endpoints
-- 10. Aggregation testing (total fines, total outstanding, etc.)
