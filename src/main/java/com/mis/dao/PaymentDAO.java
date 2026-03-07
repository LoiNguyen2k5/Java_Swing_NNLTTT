package com.mis.dao;

import com.mis.entity.Payment;
import com.mis.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PaymentDAO {

    // Lấy toàn bộ lịch sử thanh toán từ CSDL
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            list = session.createQuery("FROM Payment", Payment.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lưu một giao dịch thanh toán mới
    public void addPayment(Payment payment) {
        Transaction tr = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tr = session.beginTransaction();
            session.persist(payment); // Lưu đối tượng vào DB
            tr.commit();
        } catch (Exception e) {
            if (tr != null) tr.rollback();
            e.printStackTrace();
        }
    }

    // --- CÁC HÀM SỬ DỤNG JAVA LAMBDA (Yêu cầu GV) ---

    // Lambda 1: Lọc các giao dịch theo phương thức thanh toán (ví dụ: 'Bank')
    public List<Payment> filterByMethod(String method) {
        return getAllPayments().stream()
            .filter(p -> p.getPaymentMethod().equalsIgnoreCase(method))
            .collect(Collectors.toList());
    }

    // Lambda 2: Tìm các giao dịch có số tiền lớn hơn mức quy định
    public List<Payment> getPaymentsAbove(double minAmount) {
        return getAllPayments().stream()
            .filter(p -> p.getAmount().doubleValue() >= minAmount)
            .collect(Collectors.toList());
    }

    // Lambda 3: Tính tổng doanh thu từ các giao dịch thành công
    public double getTotalRevenue() {
        return getAllPayments().stream()
            .filter(p -> "Completed".equalsIgnoreCase(p.getStatus()))
            .mapToDouble(p -> p.getAmount().doubleValue())
            .sum(); // Sử dụng hàm sum của Stream
    }
}
