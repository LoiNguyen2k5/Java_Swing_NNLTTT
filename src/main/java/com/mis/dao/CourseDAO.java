package com.mis.dao;

import com.mis.entity.Course;
import com.mis.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseDAO {

    // Hàm lấy danh sách toàn bộ khóa học
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        // Mở Session kết nối với Database
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Lấy dữ liệu bằng câu lệnh HQL
            courses = session.createQuery("FROM Course", Course.class).list();
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có
        }
        return courses;
    }

    // Hàm thêm một khóa học mới
    public void addCourse(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction(); // Bắt đầu giao dịch
            session.persist(course); // Lưu đối tượng vào DB
            transaction.commit(); // Hoàn tất giao dịch
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); } // Lỗi thì hoàn tác
            e.printStackTrace();
        }
    }

    // Hàm cập nhật khóa học
    public void updateCourse(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(course); // Cập nhật đối tượng đã có
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }

    // Hàm xóa khóa học theo ID
    public void deleteCourse(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, id); // Tìm kiếm trước
            if (course != null) {
                session.remove(course); // Xóa nếu tìm thấy
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }
}