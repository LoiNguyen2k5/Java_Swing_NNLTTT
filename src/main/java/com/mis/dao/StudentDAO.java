package com.mis.dao;

import com.mis.entity.Student;
import com.mis.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentDAO {

    // 1. Hàm lấy danh sách toàn bộ học viên từ CSDL
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        // Mở một session (phiên làm việc) với database
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Truy vấn lấy toàn bộ dữ liệu bảng Student sử dụng HQL
            students = session.createQuery("FROM Student", Student.class).list();
        } catch (Exception e) {
            e.printStackTrace(); // In lỗi nếu có ngoại lệ
        }
        return students;
    }

    // 2. Hàm Thêm mới (Save) một học viên
    public void addStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Bắt đầu một giao dịch (transaction)
            transaction = session.beginTransaction();
            // Lưu đối tượng học viên xuống CSDL
            session.persist(student);
            // Xác nhận (commit) giao dịch thành công
            transaction.commit();
        } catch (Exception e) {
            // Nếu có lỗi thì rollback (hoàn lại) trạng thái ban đầu
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }

    // 3. Hàm Cập nhật (Update) học viên
    public void updateStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Lệnh merge để cập nhật dữ liệu của entity đã tồn tại
            session.merge(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }

    // 4. Hàm Xóa (Delete) học viên theo ID
    public void deleteStudent(Long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            // Tìm đối tượng bằng ID trước khi xóa
            Student student = session.get(Student.class, id);
            if (student != null) {
                // Thực hiện xóa khỏi CSDL
                session.remove(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) { transaction.rollback(); }
            e.printStackTrace();
        }
    }


    // Yêu cầu Lambda 1: Lọc học viên theo trạng thái (Ví dụ: "Active")
    public List<Student> getStudentsByStatus(String status) {
        List<Student> allStudents = getAllStudents();
        // Dùng Stream API để filter dữ liệu và thu thập lại thành List
        return allStudents.stream()
                .filter(s -> s.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    // Yêu cầu Lambda 2: Tìm kiếm học viên theo Tên (Tìm gần đúng)
    public List<Student> searchStudentsByName(String keyword) {
        List<Student> allStudents = getAllStudents();
        // Lọc các học viên có Tên chứa từ khóa keyword (bỏ qua chữ hoa chữ thường)
        return allStudents.stream()
                .filter(s -> s.getFullName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Yêu cầu Lambda 3: Đếm số lượng học viên Nam (Male)
    public long countMaleStudents() {
        List<Student> allStudents = getAllStudents();
        // Đếm (count) các đối tượng có gender là "Male"
        return allStudents.stream()
                .filter(s -> "Male".equalsIgnoreCase(s.getGender()))
                .count();
    }
}