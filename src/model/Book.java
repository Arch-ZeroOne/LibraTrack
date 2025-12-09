/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;

/**
 *
 * @author Windyl
 */
public class Book {

   
   
    private int id;
    private String title;
    private String author;
    private String publisher;
    private String publicationDate;
    private String isbn;
    private int copies;
    private String isAvailable;
    private String genre;
    public Book(int id, String title, String author, String publisher, String publicationDate, String isbn, int copies, String isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.copies = copies;
        this.isAvailable = isAvailable;
       
    }
    public Book( int id ,String title, String author, String publisher, String publicationDate, String isbn, String isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
       
    }
      public Book( String title, String author, String publisher, String publicationDate, String isbn, String isAvailable) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
       
    }
        public Book( String title, String author, String publisher, String publicationDate, String isbn,int copies, String isAvailable) {
        this.title = title;
        this.copies = copies;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
       
    }
   
    
       public Book( String title, String author, String publisher, String publicationDate, String isbn,int copies, String isAvailable,String genre) {
        this.title = title;
        this.copies = copies;
        this.author = author;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.isbn = isbn;
        this.isAvailable = isAvailable;
        this.genre = genre;
       
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
 public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }


    
}
