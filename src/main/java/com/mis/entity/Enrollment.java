package com.mis.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity // Đánh dấu lớp này là một thực thể được Hibernate quản lý
@Table(name = "enrollments") // Ánh xạ chính xác tới tên bảng trong MySQL
public class Enrollment {

    @Id // Khai báo khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Thiết lập ID tự động tăng (Auto Increment)
    @Column(name = "enrollment_id") // Ánh xạ tới cột enrollment_id trong database
    private Long enrollmentId;

    @Column(name = "student_id", nullable = false) // Ánh xạ cột student_id, không được để trống
    private Long studentId;

    @Column(name = "class_id", nullable = false) // Ánh xạ cột class_id, không được để trống
    private Long classId;

    @Column(name = "enrollment_date", nullable = false) // Ánh xạ cột ngày đăng ký
    @Temporal(TemporalType.DATE) // Chỉ định kiểu dữ liệu là ngày tháng
    private Date enrollmentDate = new Date(); // Mặc định lấy ngày hiện tại

    @Column(name = "status", nullable = false) // Ánh xạ cột trạng thái
    private String status = "Enrolled"; // Giá trị mặc định khi tạo mới là 'Enrolled'

    // Constructor rỗng (Bắt buộc phải có để Hibernate hoạt động)
    public Enrollment() {}

    // Constructor đầy đủ tham số để khởi tạo nhanh đối tượng
    public Enrollment(Long studentId, Long classId, Date enrollmentDate, String status) {
        this.studentId = studentId;
        this.classId = classId;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
    }

    // --- Các hàm Getter và Setter giúp lấy và gán giá trị cho các thuộc tính ---
    public Long getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(Long enrollmentId) { this.enrollmentId = enrollmentId; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public Date getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(Date enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
