package com.mis.service;

import com.mis.dao.AttendanceDAO;
import com.mis.entity.Attendance;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceService {
    private AttendanceDAO attendanceDAO = new AttendanceDAO();

    // Các hàm CRUD cơ bản
    public List<Attendance> getAllAttendances() { return attendanceDAO.getAllAttendances(); }
    public void saveOrUpdate(Attendance a) { attendanceDAO.saveOrUpdate(a); }

    // --- CÁC HÀM LAMBDA ĐÃ CHUYỂN TỪ DAO SANG ---

    // Lambda 1: Lọc danh sách điểm danh theo mã lớp
    public List<Attendance> filterByClass(Long classId) {
        return attendanceDAO.getAllAttendances().stream()
            .filter(a -> a.getClassId().equals(classId))
            .collect(Collectors.toList());
    }

    // Lambda 2: Đếm số buổi vắng mặt (Absent) của một học viên cụ thể
    public long countAbsencesByStudent(Long studentId) {
        return attendanceDAO.getAllAttendances().stream()
            .filter(a -> a.getStudentId().equals(studentId))
            .filter(a -> "Absent".equalsIgnoreCase(a.getStatus())) // Chỉ lọc những buổi vắng
            .count();
    }

    // Lambda 3: Tìm danh sách học viên đi trễ (Late) trong một ngày cụ thể
    public List<Attendance> getLateStudentsByDate(java.util.Date date) {
        return attendanceDAO.getAllAttendances().stream()
            .filter(a -> a.getAttendDate().equals(date) && "Late".equalsIgnoreCase(a.getStatus()))
            .collect(Collectors.toList());
    }
}