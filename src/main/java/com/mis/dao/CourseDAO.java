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

    // =========================================================================
    // JAVA LAMBDAS CHO MODULE COURSE (ĐÁP ỨNG YÊU CẦU GIÁO VIÊN)
    // =========================================================================

    // Lambda 1: Tìm kiếm khóa học theo tên
    public List<Course> searchCoursesByName(String keyword) {
        List<Course> allCourses = getAllCourses();
        // Dùng stream() để lọc các khóa học có tên chứa từ khóa
        return allCourses.stream()
                .filter(c -> c.getCourseName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Lambda 2: Lọc khóa học theo cấp độ (VD: Beginner, Intermediate)
    public List<Course> filterCoursesByLevel(String level) {
        List<Course> allCourses = getAllCourses();
        return allCourses.stream()
                .filter(c -> c.getLevel() != null && c.getLevel().equalsIgnoreCase(level))
                .collect(Collectors.toList());
    }

    // Lambda 3: Tìm khóa học có học phí nhỏ hơn một mức giá nhất định
    public List<Course> getCoursesCheaperThan(double maxFee) {
        List<Course> allCourses = getAllCourses();
        return allCourses.stream()
                // Chuyển BigDecimal sang double để so sánh
                .filter(c -> c.getFee() != null && c.getFee().doubleValue() <= maxFee)
                .collect(Collectors.toList());
    }
}