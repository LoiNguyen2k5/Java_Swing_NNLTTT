package com.mis.dao;

import com.mis.entity.Teacher;
import com.mis.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherDAO {

    // 1. Lấy toàn bộ danh sách giáo viên
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            teachers = session.createQuery("FROM Teacher", Teacher.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return teachers;
    }

    // 2. Thêm giáo viên mới
    public void addTeacher(Teacher teacher) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(teacher);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }

    // 3. Cập nhật thông tin giáo viên
    public void updateTeacher(Teacher teacher) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(teacher);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }

    // 4. Xóa giáo viên
    public void deleteTeacher(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Teacher teacher = session.get(Teacher.class, id);
            if (teacher != null) {
                session.remove(teacher);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }


    
}