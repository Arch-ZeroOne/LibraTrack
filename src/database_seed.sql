-- ===========================================
-- LIBRATRACK DATABASE SEED DATA
-- ===========================================
-- Sample data for testing LibraTrack Library Management System

USE libratrack_db;

-- ===========================================
-- 1. INSERT SAMPLE AUTHORS
-- ===========================================
INSERT INTO authors (author_name) VALUES
('J.K. Rowling'),
('George R.R. Martin'),
('J.R.R. Tolkien'),
('Agatha Christie'),
('Stephen King'),
('Haruki Murakami'),
('Margaret Atwood'),
('Neil Gaiman'),
('Terry Pratchett'),
('Ursula K. Le Guin'),
('Isaac Asimov'),
('Arthur C. Clarke'),
('Philip K. Dick'),
('Frank Herbert'),
('William Gibson'),
('Octavia Butler'),
('N.K. Jemisin'),
('Chimamanda Ngozi Adichie'),
('Zadie Smith'),
('Sally Rooney');

-- ===========================================
-- 2. INSERT SAMPLE CATEGORIES
-- ===========================================
INSERT INTO categories (category_name) VALUES
('Fantasy'),
('Science Fiction'),
('Mystery'),
('Horror'),
('Romance'),
('Literary Fiction'),
('Young Adult'),
('Historical Fiction'),
('Biography'),
('Self-Help'),
('Philosophy'),
('Poetry'),
('Drama'),
('Adventure'),
('Thriller'),
('Comedy');

-- ===========================================
-- 3. INSERT SAMPLE BOOKS
-- ===========================================
INSERT INTO books (title, author_id, publisher, isbn, publication_date, status_id) VALUES
-- Fantasy Books
('Harry Potter and the Philosopher\'s Stone', 1, 'Bloomsbury', '9780747532699', '1997-06-26', 1),
('Harry Potter and the Chamber of Secrets', 1, 'Bloomsbury', '9780747538493', '1998-07-02', 1),
('A Game of Thrones', 2, 'Bantam Books', '9780553103540', '1996-08-01', 1),
('The Hobbit', 3, 'George Allen & Unwin', '9780547928227', '1937-09-21', 1),
('The Lord of the Rings: The Fellowship of the Ring', 3, 'George Allen & Unwin', '9780547928210', '1954-07-29', 1),

-- Science Fiction
('Dune', 14, 'Chilton Books', '9780441172719', '1965-08-01', 1),
('Neuromancer', 15, 'Ace Books', '9780441569595', '1984-07-01', 1),
('The Left Hand of Darkness', 11, 'Ace Books', '9780441478125', '1969-03-01', 1),
('2001: A Space Odyssey', 12, 'New American Library', '9780451457993', '1968-04-01', 1),

-- Mystery
('Murder on the Orient Express', 4, 'Collins Crime Club', '9780007119318', '1934-01-01', 1),
('The Murder of Roger Ackroyd', 4, 'Collins Crime Club', '9780007120680', '1926-06-01', 1),
('And Then There Were None', 4, 'Collins Crime Club', '9780007136834', '1939-11-06', 1),

-- Horror
('The Shining', 5, 'Doubleday', '9780307743657', '1977-01-28', 1),
('It', 5, 'Viking Press', '9781501142970', '1986-09-15', 1),

-- Literary Fiction
('The Handmaid\'s Tale', 7, 'McClelland and Stewart', '9780771008795', '1985-08-01', 1),
('Americanah', 18, 'Alfred A. Knopf', '9780307455925', '2013-05-14', 1),
('Normal People', 20, 'Faber & Faber', '9780571334650', '2018-08-28', 1),

-- Young Adult
('The Graveyard Book', 8, 'HarperCollins', '9780060530921', '2008-09-30', 1),
('Neverwhere', 8, 'BBC Books', '9780060557812', '1996-09-16', 1);

-- ===========================================
-- 4. INSERT BOOK-CATEGORY RELATIONSHIPS
-- ===========================================
INSERT INTO book_categories (book_id, category_id) VALUES
-- Harry Potter books (Fantasy)
(1, 1), (1, 7), -- Fantasy, Young Adult
(2, 1), (2, 7), -- Fantasy, Young Adult

-- A Game of Thrones (Fantasy)
(3, 1), (3, 14), -- Fantasy, Adventure

-- The Hobbit & Lord of the Rings (Fantasy, Adventure)
(4, 1), (4, 14), -- Fantasy, Adventure
(5, 1), (5, 14), -- Fantasy, Adventure

-- Dune (Science Fiction)
(6, 2), (6, 14), -- Science Fiction, Adventure

-- Neuromancer (Science Fiction)
(7, 2), (7, 15), -- Science Fiction, Thriller

-- The Left Hand of Darkness (Science Fiction)
(8, 2), (8, 6), -- Science Fiction, Literary Fiction

-- 2001: A Space Odyssey (Science Fiction)
(9, 2), (2, 11), -- Science Fiction, Philosophy

-- Agatha Christie books (Mystery)
(10, 3), (10, 15), -- Mystery, Thriller
(11, 3), (11, 15), -- Mystery, Thriller
(12, 3), (12, 15), -- Mystery, Thriller

-- Stephen King books (Horror, Thriller)
(13, 4), (13, 15), -- Horror, Thriller
(14, 4), (14, 15), -- Horror, Thriller

-- Literary Fiction
(15, 6), (15, 8), -- Literary Fiction, Historical Fiction
(16, 6), (16, 5), -- Literary Fiction, Romance
(17, 6), (17, 5), -- Literary Fiction, Romance

-- Neil Gaiman books (Fantasy, Young Adult)
(18, 1), (18, 7), -- Fantasy, Young Adult
(19, 1), (19, 7); -- Fantasy, Young Adult

-- ===========================================
-- 5. INSERT SAMPLE BOOK COPIES
-- ===========================================
INSERT INTO book (book_id, isAvailable, condition_status) VALUES
-- Multiple copies of popular books
(1, 'Available', 'Good'), (1, 'Available', 'Good'), (1, 'Borrowed', 'Good'), -- Harry Potter 1 (2 available, 1 borrowed)
(2, 'Available', 'Excellent'), (2, 'Available', 'Good'), -- Harry Potter 2 (2 available)
(3, 'Available', 'Good'), (3, 'Available', 'Fair'), (3, 'Borrowed', 'Good'), -- Game of Thrones (2 available, 1 borrowed)
(4, 'Available', 'Good'), (4, 'Available', 'Excellent'), -- The Hobbit (2 available)
(5, 'Available', 'Good'), -- LOTR Fellowship (1 available)

-- Science Fiction books
(6, 'Available', 'Good'), (6, 'Available', 'Excellent'), -- Dune (2 available)
(7, 'Available', 'Good'), -- Neuromancer (1 available)
(8, 'Available', 'Fair'), -- Left Hand of Darkness (1 available)
(9, 'Available', 'Good'), -- 2001 (1 available)

-- Mystery books
(10, 'Available', 'Good'), (10, 'Borrowed', 'Good'), -- Murder on Orient Express (1 available, 1 borrowed)
(11, 'Available', 'Excellent'), -- Roger Ackroyd (1 available)
(12, 'Available', 'Good'), -- And Then There Were None (1 available)

-- Horror books
(13, 'Available', 'Good'), -- The Shining (1 available)
(14, 'Available', 'Fair'), -- It (1 available)

-- Literary Fiction
(15, 'Available', 'Good'), (15, 'Borrowed', 'Excellent'), -- Handmaid's Tale (1 available, 1 borrowed)
(16, 'Available', 'Good'), -- Americanah (1 available)
(17, 'Available', 'Excellent'), -- Normal People (1 available)

-- Young Adult Fantasy
(18, 'Available', 'Good'), -- Graveyard Book (1 available)
(19, 'Available', 'Excellent'); -- Neverwhere (1 available)

-- ===========================================
-- 6. INSERT SAMPLE STUDENTS
-- ===========================================
INSERT INTO student (firstname, middlename, lastname, school_id, isActive, course) VALUES
-- Computer Science Students
('Juan', 'Carlos', 'Dela Cruz', '2021001', 'active', 'Computer Science'),
('Maria', 'Santos', 'Garcia', '2021002', 'active', 'Computer Science'),
('Jose', 'Antonio', 'Reyes', '2021003', 'active', 'Computer Science'),
('Ana', 'Maria', 'Torres', '2021004', 'active', 'Computer Science'),
('Miguel', 'Angel', 'Flores', '2021005', 'active', 'Computer Science'),

-- Information Technology Students
('Carmen', 'Luna', 'Vasquez', '2021006', 'active', 'Information Technology'),
('Roberto', 'Miguel', 'Herrera', '2021007', 'active', 'Information Technology'),
('Isabella', 'Rose', 'Morales', '2021008', 'active', 'Information Technology'),
('Diego', 'Antonio', 'Gutierrez', '2021009', 'active', 'Information Technology'),
('Sofia', 'Elena', 'Ortega', '2021010', 'active', 'Information Technology'),

-- Business Administration Students
('Luis', 'Fernando', 'Castillo', '2021011', 'active', 'Business Administration'),
('Gabriela', 'Isabel', 'Santiago', '2021012', 'active', 'Business Administration'),
('Carlos', 'Manuel', 'Vargas', '2021013', 'active', 'Business Administration'),
('Valentina', 'Maria', 'Mendoza', '2021014', 'active', 'Business Administration'),
('Andres', 'Jose', 'Delgado', '2021015', 'active', 'Business Administration'),

-- Engineering Students
('Fernanda', 'Lucia', 'Guerrero', '2021016', 'active', 'Mechanical Engineering'),
('Pablo', 'Esteban', 'Moreno', '2021017', 'active', 'Electrical Engineering'),
('Camila', 'Sofia', 'Jimenez', '2021018', 'active', 'Civil Engineering'),
('Mateo', 'Alejandro', 'Perez', '2021019', 'active', 'Chemical Engineering'),
('Lucia', 'Gabriela', 'Romero', '2021020', 'active', 'Computer Engineering'),

-- Inactive Students (for testing)
('Alejandro', 'Luis', 'Navarro', '2020001', 'inactive', 'Computer Science'),
('Daniela', 'Carolina', 'Silva', '2020002', 'inactive', 'Information Technology');

-- ===========================================
-- 7. INSERT SAMPLE LIBRARIANS
-- ===========================================
INSERT INTO librarian (firstName, middleName, lastName, username, email, password) VALUES
('Admin', '', 'User', 'admin', 'admin@libratrack.edu', 'admin123'),
('Maria', 'Elena', 'Rodriguez', 'mrodriguez', 'maria.rodriguez@libratrack.edu', 'lib123'),
('Carlos', 'Alberto', 'Martinez', 'cmartinez', 'carlos.martinez@libratrack.edu', 'lib456');

-- ===========================================
-- 8. INSERT SAMPLE BORROWING RECORDS
-- ===========================================
-- Get some book accession numbers for borrowing
INSERT INTO borrow (borrow_date, due_date, student_id, accession_number, librarian_id) VALUES
-- Juan Dela Cruz borrowing Harry Potter books
('2024-12-20', '2025-01-20', 1, 1, 1), -- Harry Potter 1 (borrowed)
('2024-12-18', '2025-01-18', 1, 2, 1), -- Harry Potter 2 (available)

-- Maria Garcia borrowing Game of Thrones
('2024-12-22', '2025-01-22', 2, 3, 2), -- Game of Thrones (borrowed)

-- Jose Reyes borrowing Murder on Orient Express
('2024-12-25', '2025-01-25', 3, 4, 1), -- Murder on Orient Express (borrowed)

-- Ana Torres borrowing The Handmaid's Tale
('2024-12-24', '2025-01-24', 4, 7, 2); -- The Handmaid's Tale (borrowed)

-- ===========================================
-- 9. INSERT SAMPLE ATTENDANCE LOGS
-- ===========================================
INSERT INTO log (firstname, middlename, lastname, log_date, school_id, student_id) VALUES
-- Today's attendance (December 28, 2024)
('Juan', 'Carlos', 'Dela Cruz', '2024-12-28', '2021001', 1),
('Maria', 'Santos', 'Garcia', '2024-12-28', '2021002', 2),
('Jose', 'Antonio', 'Reyes', '2024-12-28', '2021003', 3),
('Ana', 'Maria', 'Torres', '2024-12-28', '2021004', 4),
('Miguel', 'Angel', 'Flores', '2024-12-28', '2021005', 5),

-- Yesterday's attendance
('Carmen', 'Luna', 'Vasquez', '2024-12-27', '2021006', 6),
('Roberto', 'Miguel', 'Herrera', '2024-12-27', '2021007', 7),
('Isabella', 'Rose', 'Morales', '2024-12-27', '2021008', 8),

-- This week's attendance for a few students
('Juan', 'Carlos', 'Dela Cruz', '2024-12-26', '2021001', 1),
('Juan', 'Carlos', 'Dela Cruz', '2024-12-25', '2021001', 1),
('Juan', 'Carlos', 'Dela Cruz', '2024-12-24', '2021001', 1),
('Maria', 'Santos', 'Garcia', '2024-12-26', '2021002', 2),
('Maria', 'Santos', 'Garcia', '2024-12-25', '2021002', 2);

COMMIT;

-- ===========================================
-- VERIFICATION QUERIES
-- ===========================================

-- Check data insertion
SELECT 'Authors inserted:' as info, COUNT(*) as count FROM authors
UNION ALL
SELECT 'Categories inserted:', COUNT(*) FROM categories
UNION ALL
SELECT 'Books inserted:', COUNT(*) FROM books
UNION ALL
SELECT 'Book copies inserted:', COUNT(*) FROM book
UNION ALL
SELECT 'Students inserted:', COUNT(*) FROM student
UNION ALL
SELECT 'Librarians inserted:', COUNT(*) FROM librarian
UNION ALL
SELECT 'Borrow records inserted:', COUNT(*) FROM borrow
UNION ALL
SELECT 'Attendance logs inserted:', COUNT(*) FROM log;
