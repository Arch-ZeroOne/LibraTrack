-- Create borrow table for LibraTrack Library Management System
-- This table tracks book borrowing transactions

CREATE TABLE borrow (
    borrow_id INT PRIMARY KEY AUTO_INCREMENT,
    borrow_date VARCHAR(20) NOT NULL,
    due_date VARCHAR(20) NOT NULL,
    student_id INT NOT NULL,
    accession_number INT NOT NULL,
    librarian_id INT NOT NULL,
    return_date DATETIME NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign key constraint to student table
    FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE,

    -- Index for faster queries by accession number and student
    INDEX idx_accession_number (accession_number),
    INDEX idx_student_id (student_id),
    INDEX idx_return_date (return_date),

    -- Ensure a book can only be borrowed once at a time (no overlapping active borrows)
    UNIQUE KEY unique_active_borrow (accession_number, return_date)
);

-- Add comments for documentation
ALTER TABLE borrow COMMENT = 'Tracks book borrowing transactions between students and library books';
