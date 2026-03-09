package com.mis.service;

import com.mis.dao.StudentDAO;
import com.mis.entity.Student;
import java.util.List;
import java.util.stream.Collectors;

public class StudentService {
    private StudentDAO studentDAO = new StudentDAO();

    public List<Student> getAllStudents() { return studentDAO.getAllStudents(); }
    public void addStudent(Student s) { studentDAO.addStudent(s); }
    public void updateStudent(Student s) { studentDAO.updateStudent(s); }
    public void deleteStudent(Long id) { studentDAO.deleteStudent(id); }

    // --- CÁC HÀM LAMBDA ---
   // Yêu cầu Lambda 2: Tìm kiếm học viên theo Tên (Tìm gần đúng)
    public List<Student> searchStudentsByName(String keyword) {
        return studentDAO.getAllStudents().stream()
                .filter(s -> s.getFullName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
   // Yêu cầu Lambda 1: Lọc học viên theo trạng thái (Ví dụ: "Active")
    public List<Student> getStudentsByStatus(String status) {
        return studentDAO.getAllStudents().stream()
                .filter(s -> s.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
// Yêu cầu Lambda 3: Đếm số lượng học viên Nam (Male)
    public long countMaleStudents() {
        return studentDAO.getAllStudents().stream()
                .filter(s -> "Male".equalsIgnoreCase(s.getGender()))
                .count();
    }
}