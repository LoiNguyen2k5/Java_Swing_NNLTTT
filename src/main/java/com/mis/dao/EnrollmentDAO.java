package com.mis.dao;

import com.mis.entity.Enrollment;
import com.mis.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EnrollmentDAO {

    // Lấy toàn bộ danh sách đăng ký từ Database
    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> list = new ArrayList<>();
        // Mở một phiên làm việc (Session) mới từ Hibernate Factory
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Truy vấn lấy dữ liệu bằng HQL (Hibernate Query Language)
            list = session.createQuery("FROM Enrollment", Enrollment.class).list();
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console nếu có ngoại lệ
        }
        return list;
    }

    // Thêm mới hoặc Cập nhật thông tin đăng ký
    public void saveOrUpdateEnrollment(Enrollment enrollment) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction(); // Bắt đầu giao dịch
            session.merge(enrollment); // Sử dụng merge để xử lý cả thêm mới và sửa
            transaction.commit(); // Xác nhận lưu dữ liệu xuống DB
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Hoàn tác nếu xảy ra lỗi
            e.printStackTrace();
        }
    }
}
