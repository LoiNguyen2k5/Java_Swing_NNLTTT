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
}
