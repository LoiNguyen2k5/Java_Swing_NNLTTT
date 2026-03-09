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
}
