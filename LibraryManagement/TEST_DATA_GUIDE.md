# Test Data Guide for Postman Testing

This guide explains the test data loaded into the database and provides example scenarios for testing with Postman.

## Data Summary

### Authors (8 records)
- IDs: 1-8
- Notable: George Orwell, Jane Austen, J.K. Rowling, Stephen Hawking, Malcolm Gladwell

### Books (10 records)
- IDs: 1-10
- **Books with available copies**: IDs 1,2,3,4,5,7,9,10
- **Books with NO copies (for reservation testing)**: IDs 6 (Foundation), 8 (Harry Potter)

### Members (7 records)
- ID 1: John Doe (PREMIUM, ACTIVE)
- ID 2: Jane Smith (STANDARD, ACTIVE)
- ID 3: Robert Johnson (STUDENT, ACTIVE)
- ID 4: Emily Davis (STANDARD, ACTIVE)
- ID 5: Michael Wilson (PREMIUM, **SUSPENDED**)
- ID 6: Sarah Brown (STUDENT, ACTIVE)
- ID 7: David Martinez (STANDARD, **EXPIRED**)

### Loans (8 records)
- **ACTIVE**: Loan IDs 1, 2
- **OVERDUE**: Loan IDs 3, 7, 8 (ready for fine generation)
- **RETURNED**: Loan IDs 4, 5, 6

### Reservations (5 records)
- **PENDING**: IDs 1, 2
- **FULFILLED**: ID 3
- **CANCELLED**: ID 4
- **EXPIRED**: ID 5

### Fines (5 records)
- **PENDING**: IDs 1, 2, 5 (total: $30.00)
- **PAID**: ID 3
- **WAIVED**: ID 4

## Postman Test Scenarios

### Book Endpoints

#### 1. Get all books
```
GET http://localhost:8080/api/books
```

#### 2. Search books by title
```
GET http://localhost:8080/api/books/search?title=Harry
```

#### 3. Get books by category
```
GET http://localhost:8080/api/books/category/SCIENCE
```

#### 4. Get available books
```
GET http://localhost:8080/api/books/available
```

#### 5. Create a new book
```
POST http://localhost:8080/api/books
Body (JSON):
{
  "title": "The Great Gatsby",
  "description": "Classic American novel",
  "publicationYear": 1925,
  "availableCopies": 3,
  "bookCategory": "FICTION"
}
```

### Member Endpoints

#### 1. Get all members
```
GET http://localhost:8080/api/members
```

#### 2. Get member by ID
```
GET http://localhost:8080/api/members/1
```

#### 3. Create a new member
```
POST http://localhost:8080/api/members
Body (JSON):
{
  "firstName": "Alice",
  "lastName": "Cooper",
  "email": "alice.cooper@email.com",
  "phoneNo": "+1-555-0199",
  "address": "999 Music Lane, Nashville, TN",
  "membershipType": "PREMIUM",
  "status": "ACTIVE"
}
```

### Loan Endpoints

#### 1. Get all loans
```
GET http://localhost:8080/api/loans
```

#### 2. Get active loans by member
```
GET http://localhost:8080/api/loans/member/1
```

#### 3. Get overdue loans
```
GET http://localhost:8080/api/loans/status/OVERDUE
```

#### 4. Create a new loan
```
POST http://localhost:8080/api/loans
Body (JSON):
{
  "book": { "id": 2 },
  "member": { "id": 4 }
}
```

#### 5. Return a loan
```
POST http://localhost:8080/api/loans/1/return
```

#### 6. Renew a loan
```
POST http://localhost:8080/api/loans/2/renew
```

#### 7. Get active loan count for member
```
GET http://localhost:8080/api/loans/member/1/active-count
```

### Reservation Endpoints

#### 1. Get all reservations
```
GET http://localhost:8080/api/reservations
```

#### 2. Create a reservation (for book with 0 copies)
```
POST http://localhost:8080/api/reservations
Body (JSON):
{
  "book": { "id": 6 },
  "member": { "id": 2 }
}
```

#### 3. Get pending reservations
```
GET http://localhost:8080/api/reservations/status/PENDING
```

#### 4. Get reservations by member
```
GET http://localhost:8080/api/reservations/member/1
```

#### 5. Cancel a reservation
```
POST http://localhost:8080/api/reservations/1/cancel
```

#### 6. Fulfill a reservation
```
POST http://localhost:8080/api/reservations/1/fulfill
```

#### 7. Get expired reservations
```
GET http://localhost:8080/api/reservations/expired
```

#### 8. Get active reservation count for member
```
GET http://localhost:8080/api/reservations/member/4/active-count
```

### Fine Endpoints (WITH BUSINESS LOGIC)

#### 1. Get all fines
```
GET http://localhost:8080/api/fines
```

#### 2. Generate fine for specific overdue loan
```
POST http://localhost:8080/api/fines/generate-for-loan/3
```
Expected: Creates fine for loan ID 3 (overdue Tom Sawyer)

#### 3. Batch generate fines for ALL overdue loans
```
POST http://localhost:8080/api/fines/generate-overdue
```
Expected: Generates fines for all OVERDUE loans without existing fines

#### 4. Get unpaid fines
```
GET http://localhost:8080/api/fines/unpaid
```

#### 5. Pay a fine
```
POST http://localhost:8080/api/fines/1/pay
```
Expected: Sets status to PAID and paidDate to now

#### 6. Waive a fine
```
POST http://localhost:8080/api/fines/2/waive
```
Expected: Sets status to WAIVED

#### 7. Get total unpaid amount for member
```
GET http://localhost:8080/api/fines/member/3/total-unpaid
```
Expected: Returns sum of all PENDING fines for member 3

#### 8. Get total paid amount for member
```
GET http://localhost:8080/api/fines/member/2/total-paid
```
Expected: Returns sum of all PAID fines for member 2

#### 9. Get total collected (all paid fines)
```
GET http://localhost:8080/api/fines/total-collected
```
Expected: Returns total revenue from paid fines

#### 10. Get total outstanding (all pending fines)
```
GET http://localhost:8080/api/fines/total-outstanding
```
Expected: Returns total amount owed across all members

#### 11. Get fines by member
```
GET http://localhost:8080/api/fines/member/3
```

#### 12. Get fine by loan
```
GET http://localhost:8080/api/fines/loan/3
```

### Author Endpoints

#### 1. Get all authors
```
GET http://localhost:8080/api/authors
```

#### 2. Get author by ID
```
GET http://localhost:8080/api/authors/1
```

#### 3. Create a new author
```
POST http://localhost:8080/api/authors
Body (JSON):
{
  "firstName": "Ernest",
  "lastName": "Hemingway",
  "biography": "American novelist and short-story writer",
  "birthDate": "1899-07-21",
  "nationality": "American"
}
```

## Complex Test Scenarios

### Scenario 1: Complete Loan Workflow
1. Check available books: `GET /api/books/available`
2. Create a loan: `POST /api/loans` with book ID and member ID
3. Verify book available_copies decreased: `GET /api/books/{bookId}`
4. Check member's active loans: `GET /api/loans/member/{memberId}`
5. Return the loan: `POST /api/loans/{loanId}/return`
6. Verify book available_copies increased: `GET /api/books/{bookId}`

### Scenario 2: Reservation Workflow
1. Check books with 0 copies: `GET /api/books` (IDs 6 and 8)
2. Create reservation: `POST /api/reservations` for book ID 6
3. Get pending reservations: `GET /api/reservations/status/PENDING`
4. Cancel reservation: `POST /api/reservations/{id}/cancel`

### Scenario 3: Fine Management Workflow
1. Get all overdue loans: `GET /api/loans/status/OVERDUE`
2. Generate fines: `POST /api/fines/generate-overdue`
3. View unpaid fines: `GET /api/fines/unpaid`
4. Check member's total unpaid: `GET /api/fines/member/{memberId}/total-unpaid`
5. Pay a fine: `POST /api/fines/{id}/pay`
6. Verify total collected increased: `GET /api/fines/total-collected`

### Scenario 4: Analytics Testing
1. Get total outstanding fines: `GET /api/fines/total-outstanding`
2. Get total collected: `GET /api/fines/total-collected`
3. Get active loan count per member: `GET /api/loans/member/{memberId}/active-count`
4. Get active reservation count: `GET /api/reservations/member/{memberId}/active-count`

## Expected Test Results

### Fine Calculation Logic
- Daily rate: **$0.50 per day**
- Loan ID 3: 8+ days overdue (as of 2026-01-06) = $4.00+
- Loan ID 7: 33+ days overdue = $16.50+
- Loan ID 8: 22+ days overdue = $11.00+

### Member Status Impact
- Member ID 5 (SUSPENDED): Can still have loans but might need special handling
- Member ID 7 (EXPIRED): Should not create new loans

### Book Availability
- Books 6 and 8: 0 copies - only reservations allowed
- Other books: Available for loans

## Notes

- All timestamps are in UTC timezone
- Fine amounts are calculated at the time of generation
- The test data automatically loads on application startup via Liquibase
- To reset data: restart the application (drop and recreate database)

## Quick Reference IDs

**Useful Member IDs:**
- 1 (John) - Premium, has active loans
- 3 (Robert) - Student, has overdue loan
- 5 (Michael) - Suspended, has overdue loan

**Useful Book IDs:**
- 1 (1984) - Available, currently on loan
- 6 (Foundation) - 0 copies, for reservation testing
- 8 (Harry Potter) - 0 copies, for reservation testing

**Overdue Loan IDs for Fine Testing:**
- 3, 7, 8

**Pending Fine IDs for Payment Testing:**
- 1, 2, 5
