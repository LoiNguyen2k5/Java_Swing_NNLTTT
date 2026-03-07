package com.mis.dao;

import com.mis.entity.Schedule;
import com.mis.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleDAO {
    // Lấy toàn bộ danh sách lịch học
    public List<Schedule> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Schedule", Schedule.class).list();
        }
    }

    // Lưu hoặc cập nhật lịch học
    public void saveOrUpdate(Schedule s) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.merge(s);
            tr.commit();
        } catch (Exception e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        }
    }

    // Lambda: Lọc lịch học theo mã lớp
    public List<Schedule> filterByClass(Long classId) {
        return getAll().stream()
            .filter(s -> s.getClassId().equals(classId))
            .collect(Collectors.toList());
    }
}
