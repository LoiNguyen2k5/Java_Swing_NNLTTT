package com.mis.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    // Biến lưu trữ SessionFactory, khởi tạo 1 lần duy nhất (Singleton pattern)
    private static final SessionFactory sessionFactory = buildSessionFactory();

    // Hàm xây dựng SessionFactory từ file cấu hình xml
    private static SessionFactory buildSessionFactory() {
        try {
            // Khởi tạo cấu hình và build SessionFactory
            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            // Ném lỗi nếu quá trình khởi tạo thất bại (Sai pass, sai tên DB...)
            System.err.println("Khởi tạo SessionFactory thất bại: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Hàm gọi để lấy SessionFactory sử dụng cho các lớp DAO
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Hàm đóng SessionFactory khi ứng dụng kết thúc
    public static void shutdown() {
        getSessionFactory().close();
    }
}