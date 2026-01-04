-- liquibase formatted sql
-- changeset catalin:202604012136_create-tables

CREATE TABLE IF NOT EXISTS author
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    biography  VARCHAR(500),
    birth_date DATE,
    nationality VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS book
(
    id               BIGSERIAL PRIMARY KEY,
    title            VARCHAR(255),
    description      VARCHAR(500),
    publication_year INTEGER,
    available_copies INTEGER,
    book_category    VARCHAR(50),
    CONSTRAINT chk_available_copies CHECK (available_copies >= 0)
);

CREATE TABLE IF NOT EXISTS book_author
(
    id        BIGSERIAL PRIMARY KEY,
    book_id   BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    CONSTRAINT fk_book_author_book FOREIGN KEY (book_id)
        REFERENCES book (id),
    CONSTRAINT fk_book_author_author FOREIGN KEY (author_id)
        REFERENCES author (id),
    CONSTRAINT uk_book_author UNIQUE (book_id, author_id)
);

CREATE TABLE IF NOT EXISTS member
(
    id              BIGSERIAL PRIMARY KEY,
    first_name      VARCHAR(255),
    last_name       VARCHAR(255),
    email           VARCHAR(255),
    phone_no        VARCHAR(50),
    address         VARCHAR(255),
    membership_date DATE,
    membership_type VARCHAR(50),
    status          VARCHAR(50),
    CONSTRAINT uk_member_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS loan
(
    id          BIGSERIAL PRIMARY KEY,
    loan_date   TIMESTAMP WITH TIME ZONE,
    due_date    TIMESTAMP WITH TIME ZONE,
    return_date TIMESTAMP WITH TIME ZONE,
    status      VARCHAR(50),
    member_id   BIGINT NOT NULL,
    book_id     BIGINT NOT NULL,
    CONSTRAINT fk_loan_member FOREIGN KEY (member_id)
        REFERENCES member (id),
    CONSTRAINT fk_loan_book FOREIGN KEY (book_id)
        REFERENCES book (id)
);

CREATE TABLE IF NOT EXISTS reservation
(
    id                BIGSERIAL PRIMARY KEY,
    reservation_date  TIMESTAMP WITH TIME ZONE,
    expiry_date       TIMESTAMP WITH TIME ZONE,
    status            VARCHAR(50),
    notification_sent BOOLEAN DEFAULT FALSE,
    member_id         BIGINT NOT NULL,
    book_id           BIGINT NOT NULL,
    CONSTRAINT fk_reservation_member FOREIGN KEY (member_id)
        REFERENCES member (id),
    CONSTRAINT fk_reservation_book FOREIGN KEY (book_id)
        REFERENCES book (id)
);

CREATE TABLE IF NOT EXISTS fine
(
    id          BIGSERIAL PRIMARY KEY,
    amount      DECIMAL(10, 2),
    reason      VARCHAR(255),
    issued_date TIMESTAMP WITH TIME ZONE,
    paid_date   TIMESTAMP WITH TIME ZONE,
    status      VARCHAR(50),
    loan_id     BIGINT NOT NULL,
    member_id   BIGINT NOT NULL,
    CONSTRAINT fk_fine_loan FOREIGN KEY (loan_id)
        REFERENCES loan (id),
    CONSTRAINT fk_fine_member FOREIGN KEY (member_id)
        REFERENCES member (id)
);

-- Book indexes
CREATE INDEX idx_book_title ON book (title);
CREATE INDEX idx_book_category ON book (book_category);
CREATE INDEX idx_book_available ON book (available_copies);

-- Member indexes
CREATE INDEX idx_member_email ON member (email);
CREATE INDEX idx_member_status ON member (status);

-- Loan indexes
CREATE INDEX idx_loan_member_id ON loan (member_id);
CREATE INDEX idx_loan_book_id ON loan (book_id);
CREATE INDEX idx_loan_status ON loan (status);
CREATE INDEX idx_loan_due_date ON loan (due_date);

-- Reservation indexes
CREATE INDEX idx_reservation_member_id ON reservation (member_id);
CREATE INDEX idx_reservation_book_id ON reservation (book_id);
CREATE INDEX idx_reservation_status ON reservation (status);

-- Fine indexes
CREATE INDEX idx_fine_member_id ON fine (member_id);
CREATE INDEX idx_fine_status ON fine (status);

-- BookAuthor indexes
CREATE INDEX idx_book_author_book_id ON book_author (book_id);
CREATE INDEX idx_book_author_author_id ON book_author (author_id);