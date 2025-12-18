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
        String sql = "SELECT student_id, name, email, phone, address, registration_date FROM Students ORDER BY name";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                student.setPhone(rs.getString("phone"));
                student.setAddress(rs.getString("address"));
                
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
        String sql = "SELECT student_id, name, email, phone, address, registration_date FROM Students WHERE student_id = ?";
        
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
                    student.setAddress(rs.getString("address"));
                    
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
        String sql = "INSERT INTO Students (student_id, name, email, phone, address, registration_date) " +
                    "VALUES (seq_student_id.NEXTVAL, ?, ?, ?, ?, ?)";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getPhone());
            stmt.setString(4, student.getAddress());
            stmt.setDate(5, Date.valueOf(student.getRegistrationDate() != null ? 
                                       student.getRegistrationDate() : LocalDate.now()));
            
            stmt.executeUpdate();
        }
    }
    
    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE Students SET name = ?, email = ?, phone = ?, address = ? WHERE student_id = ?";
        
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getEmail());
            stmt.setString(3, student.getPhone());
            stmt.setString(4, student.getAddress());
            stmt.setInt(5, student.getStudentId());
            
            stmt.executeUpdate();
        }
    }
    
    public void deleteStudent(int studentId) throws SQLException {
        try (Connection conn = connectionManager.getConnection()) {
            conn.setAutoCommit(false);
            try (
                PreparedStatement deleteFines = conn.prepareStatement(
                    "DELETE FROM Fines WHERE student_id = ?"
                );
                PreparedStatement deleteLoans = conn.prepareStatement(
                    "DELETE FROM Loans WHERE student_id = ?"
                );
                PreparedStatement deleteStudent = conn.prepareStatement(
                    "DELETE FROM Students WHERE student_id = ?"
                )
            ) {
                deleteFines.setInt(1, studentId);
                deleteFines.executeUpdate();

                deleteLoans.setInt(1, studentId);
                deleteLoans.executeUpdate();

                deleteStudent.setInt(1, studentId);
                int affected = deleteStudent.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Student not found: " + studentId);
                }

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}
