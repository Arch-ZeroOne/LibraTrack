package managers;

public class BookManager {

    // Fields
    private int id;
    private int accessionNumber;
    private String title;
    private String author;
    private String publisher;
    private String publicationDate;
    private String isbn;
    private int copies;
    private String isAvailable;

    // ======= Singleton Instance =======
    private static BookManager instance;

    // Private constructor
    private BookManager() {}

    // Thread-safe method to get the instance
    public static synchronized BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    // ======= Getters and Setters =======
    public int getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(int accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }
    
      public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
