package com.mis.entity;

import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "classes")
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "class_id")
    private Long classId;

    @Column(name = "class_name", nullable = false, length = 150)
    private String className;


    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "teacher_id")
    private Long teacherId;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "max_student", nullable = false)
    private Integer maxStudent;

    @Column(name = "status", nullable = false)
    private String status = "Planned";

    // Constructor rỗng
    public SchoolClass() {}

    // Constructor có tham số
    public SchoolClass(String className, Long courseId, Long teacherId, Date startDate, Date endDate, Integer maxStudent, String status) {
        this.className = className;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.maxStudent = maxStudent;
        this.status = status;
    }

    // --- Getters và Setters ---
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Long getTeacherId() { return teacherId; }
    public void setTeacherId(Long teacherId) { this.teacherId = teacherId; }

    public Date getStartDate() { return startDate; }
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    public Date getEndDate() { return endDate; }
    public void setEndDate(Date endDate) { this.endDate = endDate; }

    public Integer getMaxStudent() { return maxStudent; }
    public void setMaxStudent(Integer maxStudent) { this.maxStudent = maxStudent; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}