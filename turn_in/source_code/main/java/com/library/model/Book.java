package com.library.model;

/**
 * Book model class
 */
public class Book {
    private int bookId;
    private String title;
    private String author;
    private String isbn;
    private String publisher;
    private String category;
    private Integer publicationYear;
    private String location;
    private int availableCopies;
    private int totalCopies;
    
    public Book() {}
    
    public Book(int bookId, String title, String author, String isbn, String publisher, String category, 
                Integer publicationYear, String location, int availableCopies, int totalCopies) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.category = category;
        this.publicationYear = publicationYear;
        this.location = location;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
    }
    
    // Getters and Setters
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
    
    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
    
    @Override
    public String toString() {
        return String.format("Book{id=%d, title='%s', author='%s', available=%d}", 
                           bookId, title, author, availableCopies);
    }
}