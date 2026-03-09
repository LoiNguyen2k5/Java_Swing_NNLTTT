package com.mis.service;

import com.mis.dao.ClassDAO;
import com.mis.entity.SchoolClass;
import java.util.List;
import java.util.stream.Collectors;

public class ClassService {
    private ClassDAO classDAO = new ClassDAO();

    public List<SchoolClass> getAllClasses() { return classDAO.getAllClasses(); }
    public void addClass(SchoolClass sc) { classDAO.addClass(sc); }
    public void updateClass(SchoolClass sc) { classDAO.updateClass(sc); }
    public void deleteClass(Long id) { classDAO.deleteClass(id); }

     // Lambda Tìm kiếm tên lớp
    public List<SchoolClass> searchClassByName(String keyword) {
        return classDAO.getAllClasses().stream()
                .filter(c -> c.getClassName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
  // Lambda lọc lớp học theo Trạng thái (VD: Đang diễn ra "Ongoing")
    public List<SchoolClass> getClassesByStatus(String status) {
        return classDAO.getAllClasses().stream()
                .filter(c -> c.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }
}