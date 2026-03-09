package com.mis.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity // Đánh dấu lớp là một thực thể JPA
@Table(name = "attendances") // Ánh xạ trực tiếp tới bảng attendances trong CSDL
public class Attendance {

    @Id // Khai báo khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID (Auto Increment)
    @Column(name = "attendance_id") // Ánh xạ cột ID
    private Long attendanceId;

    @Column(name = "student_id", nullable = false) // ID học viên được điểm danh
    private Long studentId;

    @Column(name = "class_id", nullable = false) // ID lớp học tương ứng
    private Long classId;

    @Column(name = "attend_date", nullable = false) // Ngày điểm danh
    @Temporal(TemporalType.DATE) // Chỉ lưu trữ định dạng ngày
    private Date attendDate;

    @Column(name = "status", nullable = false) // Trạng thái: Present (Có mặt), Absent (Vắng), Late (Trễ)
    private String status = "Present";

    @Column(name = "note") // Ghi chú thêm 
    private String note;

    // Constructor rỗng bắt buộc cho Hibernate
    public Attendance() {}

    // Constructor đầy đủ để khởi tạo nhanh từ UI
    public Attendance(Long studentId, Long classId, Date date, String status, String note) {
        this.studentId = studentId;
        this.classId = classId;
        this.attendDate = date;
        this.status = status;
        this.note = note;
    }

    // --- Các hàm Getter và Setter giúp Hibernate truy xuất dữ liệu ---
    public Long getAttendanceId() { return attendanceId; }
    public void setAttendanceId(Long id) { this.attendanceId = id; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long id) { this.studentId = id; }

    public Long getClassId() { return classId; }
    public void setClassId(Long id) { this.classId = id; }

    public Date getAttendDate() { return attendDate; }
    public void setAttendDate(Date d) { this.attendDate = d; }

    public String getStatus() { return status; }
    public void setStatus(String s) { this.status = s; }

    public String getNote() { return note; }
    public void setNote(String n) { this.note = n; }
}
