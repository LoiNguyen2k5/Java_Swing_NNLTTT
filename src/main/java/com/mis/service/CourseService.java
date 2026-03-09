package com.mis.service;

import com.mis.dao.CourseDAO;
import com.mis.entity.Course;
import java.util.List;
import java.util.stream.Collectors;

public class CourseService {
    private CourseDAO courseDAO = new CourseDAO();

    public List<Course> getAllCourses() { return courseDAO.getAllCourses(); }
    public void addCourse(Course c) { courseDAO.addCourse(c); }
    public void updateCourse(Course c) { courseDAO.updateCourse(c); }
    public void deleteCourse(Long id) { courseDAO.deleteCourse(id); }

     // Lambda 1: Tìm kiếm khóa học theo tên
    public List<Course> searchCoursesByName(String keyword) {
        return courseDAO.getAllCourses().stream()
                .filter(c -> c.getCourseName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
    // Lambda 2: Lọc khóa học theo cấp độ (VD: Beginner, Intermediate)
    public List<Course> filterCoursesByLevel(String level) {
        return courseDAO.getAllCourses().stream()
                .filter(c -> c.getLevel() != null && c.getLevel().equalsIgnoreCase(level))
                .collect(Collectors.toList());
    }
 // Lambda 3: Tìm khóa học có học phí nhỏ hơn một mức giá nhất định
    public List<Course> getCoursesCheaperThan(double maxFee) {
        return courseDAO.getAllCourses().stream()
                .filter(c -> c.getFee() != null && c.getFee().doubleValue() <= maxFee)
                .collect(Collectors.toList());
    }
}