/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;

/**
 *
 * @author Windyl
 */
public class BookRowView {

    public BookRowView(int book_id, String title, String author, String publisher, String isbn, LocalDate publication_date, String status) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.publication_date = publication_date;
        this.status = status;
        this.isBorrowed = false; // Default to not borrowed
    }

    public BookRowView(int book_id, String title, String author, String publisher, String isbn, LocalDate publication_date, String status, boolean isBorrowed) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.publication_date = publication_date;
        this.status = status;
        this.isBorrowed = isBorrowed;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(LocalDate publication_date) {
        this.publication_date = publication_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean isBorrowed) {
        this.isBorrowed = isBorrowed;
    }

    public String getDisplayStatus() {
        return isBorrowed ? "Borrowed" : "Available";
    }

    private int book_id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private LocalDate publication_date;
    private String status;
    private boolean isBorrowed;
    
}
