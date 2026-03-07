package com.mis.dao;

import com.mis.entity.Attendance;
import com.mis.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceDAO {

    // Lấy toàn bộ danh sách điểm danh từ CSDL
    public List<Attendance> getAllAttendances() {
        List<Attendance> list = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            list = session.createQuery("FROM Attendance", Attendance.class).list(); // Sử dụng HQL
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lưu hoặc cập nhật trạng thái điểm danh
    public void saveOrUpdate(Attendance attendance) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.merge(attendance); // Cập nhật nếu đã tồn tại, thêm mới nếu chưa có
            tr.commit();
        } catch (Exception e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        }
    }

    // --- CÁC HÀM SỬ DỤNG JAVA LAMBDA (Đáp ứng yêu cầu GV) ---

    // Lambda 1: Lọc danh sách điểm danh theo mã lớp
    public List<Attendance> filterByClass(Long classId) {
        return getAllAttendances().stream()
            .filter(a -> a.getClassId().equals(classId))
            .collect(Collectors.toList());
    }

    // Lambda 2: Đếm số buổi vắng mặt (Absent) của một học viên cụ thể
    public long countAbsencesByStudent(Long studentId) {
        return getAllAttendances().stream()
            .filter(a -> a.getStudentId().equals(studentId))
            .filter(a -> "Absent".equalsIgnoreCase(a.getStatus())) // Chỉ lọc những buổi vắng
            .count(); // Sử dụng hàm count() của Stream API
    }

    // Lambda 3: Tìm danh sách học viên đi trễ (Late) trong một ngày cụ thể
    public List<Attendance> getLateStudentsByDate(java.util.Date date) {
        return getAllAttendances().stream()
            .filter(a -> a.getAttendDate().equals(date) && "Late".equalsIgnoreCase(a.getStatus()))
            .collect(Collectors.toList());
    }
}
