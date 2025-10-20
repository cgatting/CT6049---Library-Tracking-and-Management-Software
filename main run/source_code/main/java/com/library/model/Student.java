package com.library.model;

import java.time.LocalDate;

/**
 * Student model class
 */
public class Student {
    private int studentId;
    private String name;
    private String email;
    private String phone;
    private String address;
    private LocalDate registrationDate;
    
    public Student() {}
    
    public Student(int studentId, String name, String email, String phone, String address, LocalDate registrationDate) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.registrationDate = registrationDate;
    }
    
    // Getters and Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { this.registrationDate = registrationDate; }
    
    @Override
    public String toString() {
        return String.format("Student{id=%d, name='%s', email='%s'}", studentId, name, email);
    }
}