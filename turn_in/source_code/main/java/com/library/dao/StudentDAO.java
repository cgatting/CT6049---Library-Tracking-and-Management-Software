package com.library.dao;

import com.library.model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Student Data Access Object for Oracle Database
 */
public class StudentDAO {
    
    private final OracleConnectionManager connectionManager;
    
    public StudentDAO() {
        this.connectionManager = OracleConnectionManager.getInstance();
    }
    
    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<Student>();
        String sql = "SELECT student_id, name, email, phone, registration_date FROM Students ORDER BY name";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                
                Date regDate = rs.getDate("registration_date");
                if (regDate != null) {
                    student.setRegistrationDate(regDate.toLocalDate());
                }
                
                students.add(student);
            }
        }
        
        return students;
    }
    
    public Student getStudentById(int studentId) throws SQLException {
        String sql = "SELECT student_id, name, email, phone, registration_date FROM Students WHERE student_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setStudentId(rs.getInt("student_id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    student.setPhone(rs.getString("phone"));
                    
                    Date regDate = rs.getDate("registration_date");
                    if (regDate != null) {
                        student.setRegistrationDate(regDate.toLocalDate());
                    }
                    
                    return student;
                }
            }
        }
        
        return null;
    }
    
    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO Students (student_id, name, email, phone, registration_date) " +
                    "VALUES (seq_student_id.NEXTVAL, ?, ?, ?, ?)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getPhone());
            stmt.setDate(4, Date.valueOf(student.getRegistrationDate() != null ? 
                                       student.getRegistrationDate() : LocalDate.now()));
            
            stmt.executeUpdate();
        }
    }
    
    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE Students SET name = ?, email = ?, phone = ? WHERE student_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getPhone());
            stmt.setInt(4, student.getStudentId());
            
            stmt.executeUpdate();
        }
    }
    
    public void deleteStudent(int studentId) throws SQLException {
        String sql = "DELETE FROM Students WHERE student_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        }
    }
}