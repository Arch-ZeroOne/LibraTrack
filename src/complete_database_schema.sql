-- Complete Database Schema for LibraTrack Library Management System
-- This script creates all necessary tables for the library management system

-- ===========================================
-- 1. AUTHORS TABLE
-- ===========================================
CREATE TABLE authors (
    author_id INT PRIMARY KEY AUTO_INCREMENT,
    author_name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===========================================
-- 2. CATEGORIES TABLE (Genres)
-- ===========================================
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ===========================================
-- 3. STATUSES TABLE (Book Status)
-- ===========================================
CREATE TABLE statuses (
    status_id INT PRIMARY KEY AUTO_INCREMENT,
    status_name VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default statuses
INSERT INTO statuses (status_name) VALUES ('Available'), ('Borrowed'), ('Removed');

-- ===========================================
-- 4. BOOKS TABLE (Book Metadata)
-- ===========================================
CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(500) NOT NULL,
    author_id INT NOT NULL,
    publisher VARCHAR(255),
    isbn VARCHAR(20) UNIQUE,
    publication_date DATE,
    status_id INT DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE CASCADE,
    FOREIGN KEY (status_id) REFERENCES statuses(status_id),

    INDEX idx_title (title),
    INDEX idx_isbn (isbn),
    INDEX idx_author (author_id)
);

-- ===========================================
-- 5. BOOK_CATEGORIES TABLE (Many-to-Many relationship)
-- ===========================================
CREATE TABLE book_categories (
    book_id INT NOT NULL,
    category_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (book_id, category_id),
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE
);

-- ===========================================
-- 6. BOOK TABLE (Individual Book Copies)
-- ===========================================
CREATE TABLE book (
    accession_number INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    isAvailable ENUM('Available', 'Borrowed') DEFAULT 'Available',
    condition_status VARCHAR(50) DEFAULT 'Good',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    INDEX idx_book_id (book_id),
    INDEX idx_available (isAvailable)
);

-- ===========================================
-- 7. STUDENT TABLE
-- ===========================================
CREATE TABLE student (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(100) NOT NULL,
    middlename VARCHAR(100),
    lastname VARCHAR(100) NOT NULL,
    school_id VARCHAR(50) UNIQUE NOT NULL,
    isActive ENUM('active', 'inactive') DEFAULT 'active',
    course VARCHAR(100),
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_school_id (school_id),
    INDEX idx_is_active (isActive),
    INDEX idx_course (course)
);

-- ===========================================
-- 8. LIBRARIAN TABLE
-- ===========================================
CREATE TABLE librarian (
    librarian_id INT PRIMARY KEY AUTO_INCREMENT,
    firstName VARCHAR(100) NOT NULL,
    middleName VARCHAR(100),
    lastName VARCHAR(100) NOT NULL,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('librarian', 'super_admin') DEFAULT 'librarian',
    isActive ENUM('active', 'inactive') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role (role)
);

-- ===========================================
-- 9. BORROW TABLE (Borrowing Transactions)
-- ===========================================
CREATE TABLE borrow (
    borrow_id INT PRIMARY KEY AUTO_INCREMENT,
    borrow_date VARCHAR(20) NOT NULL,
    due_date VARCHAR(20) NOT NULL,
    student_id INT NOT NULL,
    accession_number INT NOT NULL,
    librarian_id INT NOT NULL,
    return_date DATETIME NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,
    FOREIGN KEY (accession_number) REFERENCES book(accession_number) ON DELETE CASCADE,
    FOREIGN KEY (librarian_id) REFERENCES librarian(librarian_id) ON DELETE CASCADE,

    INDEX idx_accession_number (accession_number),
    INDEX idx_student_id (student_id),
    INDEX idx_return_date (return_date),
    INDEX idx_due_date (due_date)
);

-- ===========================================
-- 10. LOG TABLE (Student Attendance)
-- ===========================================
CREATE TABLE log (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(100) NOT NULL,
    middlename VARCHAR(100),
    lastname VARCHAR(100) NOT NULL,
    log_date DATE NOT NULL,
    school_id VARCHAR(50) NOT NULL,
    student_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_log_date (log_date),
    INDEX idx_school_id (school_id),
    INDEX idx_student_id (student_id)
);

-- ===========================================
-- VIEWS FOR EASIER QUERIES
-- ===========================================

-- Book row view (combines book metadata with author and status info)
CREATE VIEW book_row AS
SELECT
    b.book_id,
    b.title,
    a.author_name,
    b.publisher,
    b.isbn,
    b.publication_date,
    s.status_name
FROM books b
LEFT JOIN authors a ON b.author_id = a.author_id
LEFT JOIN statuses s ON b.status_id = s.status_id;

-- Get categories by ISBN view
CREATE VIEW get_by_isbn AS
SELECT
    b.isbn,
    c.category_name
FROM books b
JOIN book_categories bc ON b.book_id = bc.book_id
JOIN categories c ON bc.category_id = c.category_id;

-- Library dashboard totals view
CREATE VIEW library_dashboard_totals AS
SELECT
    (SELECT COUNT(*) FROM books) as total_books,
    (SELECT COUNT(*) FROM book WHERE isAvailable = 'Available') as available_books,
    (SELECT COUNT(*) FROM borrow WHERE return_date IS NULL) as borrowed_books,
    (SELECT COUNT(*) FROM student WHERE isActive = 'active') as active_students,
    (SELECT COUNT(*) FROM authors) as total_authors,
    (SELECT COUNT(*) FROM categories) as total_categories;

-- ===========================================
-- STORED PROCEDURES
-- ===========================================

-- Get unavailable borrowed books for a student
DELIMITER $$
CREATE PROCEDURE get_unavailable_borrowed_books(IN studentId INT)
BEGIN
    SELECT
        br.accession_number,
        b.book_id,
        br.borrow_date,
        br.due_date,
        br.return_date,
        bk.title,
        a.author_name,
        DATEDIFF(br.due_date, CURDATE()) as days_remaining,
        CASE
            WHEN DATEDIFF(CURDATE(), br.due_date) > 0 THEN DATEDIFF(CURDATE(), br.due_date)
            ELSE 0
        END as penalty_days
    FROM borrow br
    JOIN book bk ON br.accession_number = bk.accession_number
    JOIN books b ON bk.book_id = b.book_id
    LEFT JOIN authors a ON b.author_id = a.author_id
    WHERE br.student_id = studentId
    AND br.return_date IS NULL
    ORDER BY br.due_date ASC;
END$$
DELIMITER ;

-- ===========================================
-- SAMPLE DATA (Optional - for testing)
-- ===========================================

-- Insert sample categories
INSERT INTO categories (category_name) VALUES
('Fiction'), ('Non-Fiction'), ('Science'), ('History'), ('Biography'),
('Technology'), ('Literature'), ('Mathematics'), ('Medicine'), ('Arts');

-- Insert sample authors
INSERT INTO authors (author_name) VALUES
('J.K. Rowling'), ('George Orwell'), ('Harper Lee'), ('F. Scott Fitzgerald'),
('Jane Austen'), ('Charles Dickens'), ('Mark Twain'), ('Agatha Christie');

-- Insert sample librarian (admin user)
INSERT INTO librarian (firstName, middleName, lastName, username, email, password, role) VALUES
('Admin', '', 'User', 'admin', 'admin@libratrack.com', 'admin123', 'super_admin');

COMMIT;
