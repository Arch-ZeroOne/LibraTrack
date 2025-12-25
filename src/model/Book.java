/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDate;
/**
 *
 * @author Windyl
 */
public class Book {
    private int book_id;
    private String title;
    private int author_id;
    private String publisher;
    private String isbn;
    private LocalDate publication_date;
    private int status_id;
    
    
    public Book(){}

    
    public Book(String title, int author_id, String publisher, LocalDate publication_date, int status_id, String isbn) {
        this.title = title;
        this.author_id = author_id;
        this.publisher = publisher;
        this.isbn = isbn;
        this.publication_date = publication_date;
      
        this.status_id = status_id;
            }

    public Book(int book_id,String title, int author_id, String publisher, LocalDate publication_date, int status_id) {
        this.book_id = book_id;
        this.title = title;
        this.author_id = author_id;
        this.publisher = publisher;
        this.publication_date = publication_date;
        this.status_id = status_id;
    }

   

    public Book(int book_id, String title, int author_id, String publisher, LocalDate publication_date, int status_id,String isbn) {
        this.book_id = book_id;
        this.title = title;
        this.author_id = author_id;
        this.publisher = publisher;
        this.isbn = isbn;
        this.publication_date = publication_date;
       
        this.status_id = status_id;
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

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public LocalDate getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(LocalDate publication_date) {
        this.publication_date = publication_date;
    }

  
    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }
     public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
   
   

    
  


    
}
