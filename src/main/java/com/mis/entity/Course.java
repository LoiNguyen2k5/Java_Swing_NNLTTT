package com.mis.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "courses")
public class Course {

    // Đánh dấu đây là Khóa chính (Primary Key)
    @Id
    // Tự động tăng ID (AUTO_INCREMENT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Ánh xạ tới cột course_id trong bảng
    @Column(name = "course_id")
    private Long courseId;

    // Ánh xạ cột tên khóa học, không được để trống (nullable = false)
    @Column(name = "course_name", nullable = false, length = 200)
    private String courseName;

    // Ánh xạ cột mô tả khóa học
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Ánh xạ cột cấp độ (Beginner, Intermediate, Advanced)
    @Column(name = "level")
    private String level;

    // Ánh xạ cột thời lượng học
    @Column(name = "duration")
    private Integer duration;

    // Ánh xạ cột học phí, sử dụng BigDecimal để tính toán tiền tệ chính xác
    @Column(name = "fee", nullable = false)
    private BigDecimal fee;

    // Ánh xạ cột trạng thái, giá trị mặc định là Active
    @Column(name = "status", nullable = false)
    private String status = "Active";

    // Hàm khởi tạo rỗng (Bắt buộc phải có để Hibernate hoạt động)
    public Course() {}

    // Hàm khởi tạo đầy đủ tham số để tiện truyền dữ liệu từ Giao diện
    public Course(String courseName, String description, String level, Integer duration, BigDecimal fee, String status) {
        this.courseName = courseName;
        this.description = description;
        this.level = level;
        this.duration = duration;
        this.fee = fee;
        this.status = status;
    }

    // --- CÁC HÀM GETTER VÀ SETTER (Bắt buộc để lấy và gán dữ liệu) ---
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}