package com.mis.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "phone", length = 20, unique = true)
    private String phone;

    @Column(name = "email", length = 150, unique = true)
    private String email;

    @Column(name = "specialty", length = 100)
    private String specialty;

    @Column(name = "hire_date")
    @Temporal(TemporalType.DATE)
    private Date hireDate;

    @Column(name = "status", nullable = false)
    private String status = "Active";

    // Constructor rỗng (Bắt buộc cho Hibernate)
    public Teacher() {}

    // Constructor đầy đủ để dễ thêm mới
    public Teacher(String fullName, String phone, String email, String specialty, Date hireDate, String status) {
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.specialty = specialty;
        this.hireDate = hireDate;
        this.status = status;
    }

    // --- Getters và Setters ---
    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public Date getHireDate() { return hireDate; }
    public void setHireDate(Date hireDate) { this.hireDate = hireDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}