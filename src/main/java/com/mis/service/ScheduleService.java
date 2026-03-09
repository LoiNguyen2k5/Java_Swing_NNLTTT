package com.mis.service;

import com.mis.dao.ScheduleDAO;
import com.mis.entity.Schedule;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleService {
    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    public List<Schedule> getAll() { return scheduleDAO.getAll(); }
    public void saveOrUpdate(Schedule s) { scheduleDAO.saveOrUpdate(s); }
    
    // Gọi hàm xóa từ DAO
    public void delete(Long id) { scheduleDAO.delete(id); }

    // Lambda: Lọc lịch học theo mã lớp
    public List<Schedule> filterByClass(Long classId) {
        return scheduleDAO.getAll().stream()
            .filter(s -> s.getClassId().equals(classId))
            .collect(Collectors.toList());
    }
}