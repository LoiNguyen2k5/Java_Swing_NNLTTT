package com.mis.service;

import com.mis.dao.ClassDAO;
import com.mis.dao.CourseDAO;
import com.mis.dao.EnrollmentDAO;
import com.mis.dao.PaymentDAO;
import com.mis.entity.Course;
import com.mis.entity.Payment;
import com.mis.entity.SchoolClass;

import java.util.List;
import java.util.stream.Collectors;

public class PaymentService {
    private PaymentDAO paymentDAO = new PaymentDAO();
    
    // Khai báo thêm DAO khác để phục vụ logic tính nợ liên bảng
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private ClassDAO classDAO = new ClassDAO();
    private CourseDAO courseDAO = new CourseDAO();

    // Các hàm CRUD cơ bản
    public List<Payment> getAllPayments() { return paymentDAO.getAllPayments(); }
    public void addPayment(Payment p) { paymentDAO.addPayment(p); }

    // --- CÁC HÀM LAMBDA ĐÃ CHUYỂN TỪ DAO SANG ---

    // Lambda 1: Lọc các giao dịch theo phương thức thanh toán
    public List<Payment> filterByMethod(String method) {
        return paymentDAO.getAllPayments().stream()
            .filter(p -> p.getPaymentMethod().equalsIgnoreCase(method))
            .collect(Collectors.toList());
    }

    // Lambda 2: Tìm các giao dịch có số tiền lớn hơn mức quy định
    public List<Payment> getPaymentsAbove(double minAmount) {
        return paymentDAO.getAllPayments().stream()
            .filter(p -> p.getAmount().doubleValue() >= minAmount)
            .collect(Collectors.toList());
    }

    // Lambda 3: Tính tổng doanh thu từ các giao dịch thành công
    public double getTotalRevenue() {
        return paymentDAO.getAllPayments().stream()
            .filter(p -> "Completed".equalsIgnoreCase(p.getStatus()))
            .mapToDouble(p -> p.getAmount().doubleValue())
            .sum(); 
    }

    // --- LOGIC NGHIỆP VỤ TÍNH CÔNG NỢ ---

    public double getTotalDebt(Long studentId) {
        return enrollmentDAO.getAllEnrollments().stream()
                .filter(en -> en.getStudentId().equals(studentId))
                .mapToDouble(en -> {
                    SchoolClass sc = classDAO.getAllClasses().stream()
                            .filter(c -> c.getClassId().equals(en.getClassId()))
                            .findFirst().orElse(null);
                    if (sc != null) {
                        Course crs = courseDAO.getAllCourses().stream()
                                .filter(co -> co.getCourseId().equals(sc.getCourseId()))
                                .findFirst().orElse(null);
                        if (crs != null && crs.getFee() != null) {
                            return crs.getFee().doubleValue();
                        }
                    }
                    return 0.0;
                }).sum();
    }

    public double getTotalPaid(Long studentId) {
        return paymentDAO.getAllPayments().stream()
                .filter(p -> p.getStudentId().equals(studentId) && "Completed".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(p -> p.getAmount().doubleValue())
                .sum();
    }
}