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

    // --- CÁC HÀM SỬ DỤNG JAVA LAMBDA (Yêu cầu của giáo viên) ---

    // Lambda 1: Lọc danh sách đăng ký theo mã lớp học
    public List<Enrollment> filterByClass(Long classId) {
        List<Enrollment> all = getAllEnrollments(); // Lấy danh sách tổng
        return all.stream() // Chuyển sang luồng dữ liệu (Stream)
            .filter(e -> e.getClassId().equals(classId)) // Lọc những phần tử khớp mã lớp
            .collect(Collectors.toList()); // Thu thập kết quả về dạng danh sách List
    }

    // Lambda 2: Tìm danh sách các đăng ký có trạng thái cụ thể (ví dụ: 'Dropped')
    public List<Enrollment> getEnrollmentsByStatus(String status) {
        return getAllEnrollments().stream()
            .filter(e -> e.getStatus().equalsIgnoreCase(status)) // So sánh chuỗi không phân biệt hoa thường
            .collect(Collectors.toList());
    }

    // Lambda 3: Đếm tổng số lượng đăng ký đã hoàn thành (Status = 'Completed')
    public long countCompletedEnrollments() {
        return getAllEnrollments().stream()
            .filter(e -> "Completed".equalsIgnoreCase(e.getStatus())) // Lọc trạng thái 'Completed'
            .count(); // Trả về số lượng đếm được
    }
}
