package com.mis.service;

import com.mis.dao.EnrollmentDAO;
import com.mis.entity.Enrollment;
import java.util.List;
import java.util.stream.Collectors;

public class EnrollmentService {
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    // Các hàm CRUD cơ bản
    public List<Enrollment> getAllEnrollments() { return enrollmentDAO.getAllEnrollments(); }
    public void saveOrUpdateEnrollment(Enrollment en) { enrollmentDAO.saveOrUpdateEnrollment(en); }

    // --- CÁC HÀM LAMBDA ĐÃ CHUYỂN TỪ DAO SANG ---
    
    // Lambda: Lọc danh sách đăng ký theo mã lớp học
    public List<Enrollment> filterByClass(Long classId) {
        return enrollmentDAO.getAllEnrollments().stream()
            .filter(e -> e.getClassId().equals(classId))
            .collect(Collectors.toList());
    }

    // Lambda: Tìm danh sách các đăng ký có trạng thái cụ thể
    public List<Enrollment> getEnrollmentsByStatus(String status) {
        return enrollmentDAO.getAllEnrollments().stream()
            .filter(e -> e.getStatus().equalsIgnoreCase(status)) // So sánh chuỗi không phân biệt hoa thường
            .collect(Collectors.toList());
    }

    // Lambda: Đếm tổng số lượng đăng ký đã hoàn thành
    public long countCompletedEnrollments() {
        return enrollmentDAO.getAllEnrollments().stream()
            .filter(e -> "Completed".equalsIgnoreCase(e.getStatus()))
            .count();
    }

    // Lambda: Lọc danh sách các lớp mà một Sinh viên đã đăng ký
    public List<Enrollment> filterByStudent(Long studentId) {
        return enrollmentDAO.getAllEnrollments().stream()
            .filter(e -> e.getStudentId().equals(studentId))
            .collect(Collectors.toList());
    }
}