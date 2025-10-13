package com.library.dao;

import com.library.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Book Data Access Object for Oracle Database
 */
public class BookDAO {
    
    private final OracleConnectionManager connectionManager;
    
    public BookDAO() {
        this.connectionManager = OracleConnectionManager.getInstance();
    }
    
    public List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<Book>();
        String sql = "SELECT book_id, title, author, isbn, category, publication_year, " +
                    "available_copies, total_copies FROM Books ORDER BY title";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublisher(""); // Default empty publisher
                book.setCategory(rs.getString("category"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setLocation(""); // Default empty location
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setTotalCopies(rs.getInt("total_copies"));
                
                books.add(book);
            }
        }
        
        return books;
    }
    
    public List<Book> getAvailableBooks() throws SQLException {
        List<Book> books = new ArrayList<Book>();
        String sql = "SELECT book_id, title, author, isbn, category, publication_year, " +
                    "available_copies, total_copies FROM Books WHERE available_copies > 0 ORDER BY title";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getInt("book_id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setIsbn(rs.getString("isbn"));
                book.setPublisher(""); // Default empty publisher
                book.setCategory(rs.getString("category"));
                book.setPublicationYear(rs.getInt("publication_year"));
                book.setLocation(""); // Default empty location
                book.setAvailableCopies(rs.getInt("available_copies"));
                book.setTotalCopies(rs.getInt("total_copies"));
                
                books.add(book);
            }
        }
        
        return books;
    }
    
    public Book getBookById(int bookId) throws SQLException {
        String sql = "SELECT book_id, title, author, isbn, category, publication_year, " +
                    "available_copies, total_copies FROM Books WHERE book_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setBookId(rs.getInt("book_id"));
                    book.setTitle(rs.getString("title"));
                    book.setAuthor(rs.getString("author"));
                    book.setIsbn(rs.getString("isbn"));
                    book.setPublisher(""); // Default empty publisher
                    book.setCategory(rs.getString("category"));
                    book.setPublicationYear(rs.getInt("publication_year"));
                    book.setLocation(""); // Default empty location
                    book.setAvailableCopies(rs.getInt("available_copies"));
                    book.setTotalCopies(rs.getInt("total_copies"));
                    
                    return book;
                }
            }
        }
        
        return null;
    }
    
    public void updateAvailableCopies(int bookId, int newAvailableCount) throws SQLException {
        String sql = "UPDATE Books SET available_copies = ? WHERE book_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newAvailableCount);
            stmt.setInt(2, bookId);
            
            stmt.executeUpdate();
        }
    }
    
    public void addBook(Book book) throws SQLException {
        String sql = "INSERT INTO Books (book_id, title, author, isbn, category, publication_year, " +
                    "available_copies, total_copies) VALUES (seq_book_id.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getCategory());
            stmt.setInt(5, book.getPublicationYear() != null ? book.getPublicationYear() : 0);
            stmt.setInt(6, book.getAvailableCopies());
            stmt.setInt(7, book.getTotalCopies());
            
            stmt.executeUpdate();
        }
    }
    
    public void updateBook(Book book) throws SQLException {
        String sql = "UPDATE Books SET title = ?, author = ?, isbn = ?, category = ?, " +
                    "publication_year = ?, available_copies = ?, total_copies = ? WHERE book_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getCategory());
            stmt.setInt(5, book.getPublicationYear() != null ? book.getPublicationYear() : 0);
            stmt.setInt(6, book.getAvailableCopies());
            stmt.setInt(7, book.getTotalCopies());
            stmt.setInt(8, book.getBookId());
            
            stmt.executeUpdate();
        }
    }
    
    public void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM Books WHERE book_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        }
    }
}