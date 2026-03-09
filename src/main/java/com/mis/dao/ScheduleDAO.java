package com.mis.dao;

import com.mis.entity.Schedule;
import com.mis.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

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

    // Hàm XÓA lịch học
    public void delete(Long id) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            Schedule s = session.get(Schedule.class, id);
            if (s != null) {
                session.remove(s);
            }
            tr.commit();
        } catch (Exception e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        }
    }
}