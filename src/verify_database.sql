-- ===========================================
-- LIBRATRACK DATABASE VERIFICATION
-- ===========================================
-- Verify that sample data was inserted correctly

USE libratrack_db;

-- ===========================================
-- QUICK DATA VERIFICATION
-- ===========================================

SELECT '=== DATABASE VERIFICATION RESULTS ===' as Status;

-- Check table counts
SELECT
    'Authors' as Table_Name, COUNT(*) as Records
FROM authors
UNION ALL
SELECT 'Categories', COUNT(*) FROM categories
UNION ALL
SELECT 'Books', COUNT(*) FROM books
UNION ALL
SELECT 'Book Copies', COUNT(*) FROM book
UNION ALL
SELECT 'Students', COUNT(*) FROM student
UNION ALL
SELECT 'Librarians', COUNT(*) FROM librarian
UNION ALL
SELECT 'Borrow Records', COUNT(*) FROM borrow
UNION ALL
SELECT 'Attendance Logs', COUNT(*) FROM log;

SELECT '' as Separator;

-- ===========================================
-- SAMPLE DATA PREVIEW
-- ===========================================

SELECT '=== SAMPLE AUTHORS ===' as Section;
SELECT author_id, author_name FROM authors LIMIT 5;

SELECT '' as Separator;
SELECT '=== SAMPLE BOOKS ===' as Section;
SELECT b.book_id, b.title, a.author_name, b.isbn
FROM books b
LEFT JOIN authors a ON b.author_id = a.author_id
LIMIT 5;

SELECT '' as Separator;
SELECT '=== SAMPLE STUDENTS ===' as Section;
SELECT student_id, CONCAT(firstname, ' ', lastname) as Full_Name, school_id, course
FROM student
WHERE isActive = 'active'
LIMIT 5;

SELECT '' as Separator;
SELECT '=== CURRENT BORROWINGS ===' as Section;
SELECT
    br.borrow_id,
    CONCAT(s.firstname, ' ', s.lastname) as Student,
    bk.title as Book,
    br.borrow_date,
    br.due_date,
    CASE WHEN br.return_date IS NULL THEN 'Borrowed' ELSE 'Returned' END as Status
FROM borrow br
JOIN student s ON br.student_id = s.student_id
JOIN book b ON br.accession_number = b.accession_number
JOIN books bk ON b.book_id = bk.book_id
ORDER BY br.borrow_date DESC
LIMIT 5;

SELECT '' as Separator;
SELECT '=== TODAY''S ATTENDANCE ===' as Section;
SELECT
    COUNT(*) as Today_Attendance,
    DATE_FORMAT(CURDATE(), '%W, %M %e, %Y') as Date
FROM log
WHERE DATE(log_date) = CURDATE();

SELECT '' as Separator;
SELECT '=== DASHBOARD STATISTICS ===' as Section;
SELECT * FROM library_dashboard_totals;

SELECT '' as Separator;
SELECT '=== DATABASE READY FOR USE! ===' as Final_Status;
