package model;

import java.time.LocalDate;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class BorrowedBook {
    private final SimpleIntegerProperty accessionNumber;
    private final SimpleIntegerProperty studentId;
    private final SimpleStringProperty studentName;
    private final SimpleStringProperty title;
    private final SimpleStringProperty author;
    private final SimpleObjectProperty<LocalDate> borrowDate;
    private final SimpleObjectProperty<LocalDate> dueDate;
    private final SimpleIntegerProperty penaltyAmount;

    public BorrowedBook(int accessionNumber, int studentId, String studentName, String title, String author,
                        LocalDate borrowDate, LocalDate dueDate, int penaltyAmount) {

        this.accessionNumber = new SimpleIntegerProperty(accessionNumber);
        this.studentId = new SimpleIntegerProperty(studentId);
        this.studentName = new SimpleStringProperty(studentName);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.borrowDate = new SimpleObjectProperty<>(borrowDate);
        this.dueDate = new SimpleObjectProperty<>(dueDate);
        this.penaltyAmount = new SimpleIntegerProperty(penaltyAmount);
    }

    public int getAccessionNumber() { return accessionNumber.get(); }
    public int getStudentId() { return studentId.get(); }
    public String getStudentName() { return studentName.get(); }
    public String getTitle() { return title.get(); }
    public String getAuthor() { return author.get(); }
    public LocalDate getBorrowDate() { return borrowDate.get(); }
    public LocalDate getDueDate() { return dueDate.get(); }
    public int getPenaltyAmount() { return penaltyAmount.get(); }
}
