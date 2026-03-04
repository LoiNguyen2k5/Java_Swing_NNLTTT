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


    // Lambda 4: Tìm kiếm giáo viên theo tên (Không phân biệt hoa thường)
    public List<Teacher> searchTeachersByName(String keyword) {
        List<Teacher> allTeachers = getAllTeachers();
        return allTeachers.stream()
                .filter(t -> t.getFullName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Lambda 5: Lọc giáo viên theo chuyên môn (Ví dụ: IELTS, TOEIC)
    public List<Teacher> getTeachersBySpecialty(String specialty) {
        List<Teacher> allTeachers = getAllTeachers();
        return allTeachers.stream()
                .filter(t -> t.getSpecialty() != null && t.getSpecialty().equalsIgnoreCase(specialty))
                .collect(Collectors.toList());
    }

    // Lambda 6: Đếm số lượng giáo viên đang còn hoạt động (Active)
    public long countActiveTeachers() {
        List<Teacher> allTeachers = getAllTeachers();
        return allTeachers.stream()
                .filter(t -> "Active".equalsIgnoreCase(t.getStatus()))
                .count();
    }
}