package com.mis.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.sql.Time;

@Entity
@Table(name = "schedules") // Ánh xạ tới bảng lịch học
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "class_id", nullable = false)
    private Long classId;

    @Column(name = "study_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date studyDate; // Ngày học

    @Column(name = "start_time", nullable = false)
    private Time startTime; // Giờ bắt đầu (Kiểu java.sql.Time)

    @Column(name = "end_time", nullable = false)
    private Time endTime; // Giờ kết thúc (Kiểu java.sql.Time)

    public Schedule() {} // Constructor rỗng cho Hibernate

    public Schedule(Long classId, Date studyDate, Time startTime, Time endTime) {
        this.classId = classId;
        this.studyDate = studyDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters và Setters
    public Long getScheduleId() { return scheduleId; }
    public void setScheduleId(Long scheduleId) { this.scheduleId = scheduleId; }
    public Long getClassId() { return classId; }
    public void setClassId(Long classId) { this.classId = classId; }
    public Date getStudyDate() { return studyDate; }
    public void setStudyDate(Date studyDate) { this.studyDate = studyDate; }
    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }
    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }
}
