/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.sql.Date;
/**
 *
 * @author Windyl
 */
public class Book {
    private int book_id;
    private String title;
    private int author_id;
    private int publisher_id;
    private String isbn;
    private Date publication_date;
    private int category_id;
    private int status_id;

    
    public Book(String title, int author_id, int publisher_id, Date publication_date, int category_id, int status_id, String isbn) {
        this.title = title;
        this.author_id = author_id;
        this.publisher_id = publisher_id;
        this.isbn = isbn;
        this.publication_date = publication_date;
        this.category_id = category_id;
        this.status_id = status_id;
            }

   

    public Book(int book_id, String title, int author_id, int publisher_id, Date publication_date, int category_id, int status_id,String isbn) {
        this.book_id = book_id;
        this.title = title;
        this.author_id = author_id;
        this.publisher_id = publisher_id;
        this.isbn = isbn;
        this.publication_date = publication_date;
        this.category_id = category_id;
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

    public int getPublisher_id() {
        return publisher_id;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public Date getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(Date publication_date) {
        this.publication_date = publication_date;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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
