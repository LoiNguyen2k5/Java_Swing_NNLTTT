package com.mis.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "students")
public class Student {

    // Khai báo khóa chính (Primary Key)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    // Ánh xạ cột họ tên
    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    // Ánh xạ cột ngày sinh
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    // Ánh xạ cột giới tính
    @Column(name = "gender")
    private String gender;

    // Ánh xạ cột số điện thoại
    @Column(name = "phone", length = 20, unique = true)
    private String phone;

    // Ánh xạ cột email
    @Column(name = "email", length = 150, unique = true)
    private String email;

    // Ánh xạ cột địa chỉ
    @Column(name = "address")
    private String address;

    // Ánh xạ cột trạng thái
    @Column(name = "status", nullable = false)
    private String status = "Active";

    // Constructor rỗng bắt buộc của Hibernate
    public Student() {}

    // Constructor đầy đủ tham số để tiện khởi tạo đối tượng
    public Student(String fullName, Date dateOfBirth, String gender, String phone, String email, String address, String status) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.status = status;
    }

    // Các hàm Getter và Setter (Bắt buộc để Hibernate có thể gán và lấy dữ liệu)
    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}