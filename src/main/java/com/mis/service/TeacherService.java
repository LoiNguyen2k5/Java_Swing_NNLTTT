package com.mis.service;

import com.mis.dao.TeacherDAO;
import com.mis.entity.Teacher;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherService {
    private TeacherDAO teacherDAO = new TeacherDAO();

    public List<Teacher> getAllTeachers() { return teacherDAO.getAllTeachers(); }
    public void addTeacher(Teacher t) { teacherDAO.addTeacher(t); }
    public void updateTeacher(Teacher t) { teacherDAO.updateTeacher(t); }
    public void deleteTeacher(Long id) { teacherDAO.deleteTeacher(id); }
    
// Lambda 4: Tìm kiếm giáo viên theo tên (Không phân biệt hoa thường)
    public List<Teacher> searchTeachersByName(String keyword) {
        return teacherDAO.getAllTeachers().stream()
                .filter(t -> t.getFullName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
 // Lambda 5: Lọc giáo viên theo chuyên môn (Ví dụ: IELTS, TOEIC)
    public List<Teacher> getTeachersBySpecialty(String specialty) {
        return teacherDAO.getAllTeachers().stream()
                .filter(t -> t.getSpecialty() != null && t.getSpecialty().equalsIgnoreCase(specialty))
                .collect(Collectors.toList());
    }
 // Lambda 6: Đếm số lượng giáo viên đang còn hoạt động (Active)
    public long countActiveTeachers() {
        return teacherDAO.getAllTeachers().stream()
                .filter(t -> "Active".equalsIgnoreCase(t.getStatus()))
                .count();
    }
}