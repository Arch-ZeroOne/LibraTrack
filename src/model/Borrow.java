package model;
public class Borrow {

    private int borrowId;
    private String borrowDate;     // or LocalDate
    private String dueDate;        // or LocalDate
    private int studentId;
    private int accessionNumber;
    private int librarianId;
   

    public Borrow() {
    }

    public Borrow(int borrowId, String borrowDate, String dueDate, int studentId, int accessionNumber, int librarianId) {
        this.borrowId = borrowId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.studentId = studentId;
        this.accessionNumber = accessionNumber;
        this.librarianId = librarianId;
    }
   

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(int accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public int getLibrarianId() {
        return librarianId;
    }

    public void setLibrarianId(int librarianId) {
        this.librarianId = librarianId;
    }
}
