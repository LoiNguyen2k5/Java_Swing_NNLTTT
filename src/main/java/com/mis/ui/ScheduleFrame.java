package com.mis.ui;

import com.mis.entity.Schedule;
import com.mis.service.ScheduleService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

public class ScheduleFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtClassId, txtDate, txtStartTime, txtEndTime, txtSearch;
    private ScheduleService scheduleService = new ScheduleService(); // Dùng Service
    private SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");

    public ScheduleFrame() {
        setTitle("Quản lý Lịch học");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadData(scheduleService.getAll());
    }

    private void initComponents() {
        // Panel nhập liệu
        JPanel pnlInput = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin lịch học"));

        pnlInput.add(new JLabel("Mã Lớp:"));
        txtClassId = new JTextField(); pnlInput.add(txtClassId);

        pnlInput.add(new JLabel("Ngày (yyyy-MM-dd):"));
        txtDate = new JTextField(); pnlInput.add(txtDate);

        pnlInput.add(new JLabel("Bắt đầu (HH:mm:ss):"));
        txtStartTime = new JTextField(); pnlInput.add(txtStartTime);

        pnlInput.add(new JLabel("Kết thúc (HH:mm:ss):"));
        txtEndTime = new JTextField(); pnlInput.add(txtEndTime);

        add(pnlInput, BorderLayout.NORTH);

        // Bảng dữ liệu
        model = new DefaultTableModel(new String[]{"ID", "Lớp", "Ngày học", "Bắt đầu", "Kết thúc"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Sự kiện CLICK VÀO BẢNG để lấy dữ liệu ngược lên các ô nhập
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    txtClassId.setText(model.getValueAt(row, 1).toString());
                    txtDate.setText(model.getValueAt(row, 2).toString());
                    txtStartTime.setText(model.getValueAt(row, 3).toString());
                    txtEndTime.setText(model.getValueAt(row, 4).toString());
                }
            }
        });

        // Panel chức năng 
        JPanel pnlSouth = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm lịch học");
        JButton btnDelete = new JButton("Xóa lịch học"); // Nút XÓA mới
        JButton btnRefresh = new JButton("Làm mới");
        
        txtSearch = new JTextField(10);
        JButton btnFilter = new JButton("Lọc theo lớp (Lambda)");

        pnlSouth.add(btnAdd);
        pnlSouth.add(btnDelete); // Đưa nút xóa vào giao diện
        pnlSouth.add(btnRefresh);
        pnlSouth.add(new JLabel(" | Mã lớp:")); pnlSouth.add(txtSearch);
        pnlSouth.add(btnFilter);
        add(pnlSouth, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN NÚT BẤM ---

        // 1. Xử lý sự kiện THÊM MỚI (Có LAMBDA chặn trùng lịch)
        btnAdd.addActionListener(e -> {
            try {
                Long classId = Long.parseLong(txtClassId.getText().trim());
                String dateStr = txtDate.getText().trim();
                String startTimeStr = txtStartTime.getText().trim();
                String endTimeStr = txtEndTime.getText().trim();

                // DÙNG LAMBDA KIỂM TRA TRÙNG LỊCH HỌC
                boolean isDuplicate = scheduleService.getAll().stream()
                        .anyMatch(s -> s.getClassId().equals(classId) &&
                                       dateFmt.format(s.getStudyDate()).equals(dateStr) &&
                                       s.getStartTime().toString().equals(startTimeStr));

                if (isDuplicate) {
                    JOptionPane.showMessageDialog(this, 
                        "LỖI XẾP LỊCH:\nLớp ID " + classId + " đã có lịch học vào lúc " + startTimeStr + " ngày " + dateStr + " rồi!\nVui lòng chọn ngày hoặc giờ khác.", 
                        "Cảnh báo trùng lịch", 
                        JOptionPane.ERROR_MESSAGE);
                    return; // Dừng lại, không cho lưu
                }

                Schedule s = new Schedule(
                    classId,
                    dateFmt.parse(dateStr),
                    Time.valueOf(startTimeStr), 
                    Time.valueOf(endTimeStr)
                );
                scheduleService.saveOrUpdate(s);
                JOptionPane.showMessageDialog(this, "Lưu lịch học thành công!");
                loadData(scheduleService.getAll());
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Mã Lớp phải là số nguyên!", "Sai định dạng số", JOptionPane.WARNING_MESSAGE);
            } catch (java.text.ParseException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Ngày học phải đúng định dạng năm-tháng-ngày (VD: 2026-02-15)!", "Sai định dạng ngày", JOptionPane.WARNING_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Giờ học phải đúng định dạng Giờ:Phút:Giây (VD: 18:30:00)!", "Sai định dạng giờ", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 2. Xử lý sự kiện XÓA
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng click chọn một lịch học trên bảng để xóa!", "Chưa chọn dữ liệu", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Lấy ID của dòng đang chọn (Cột 0)
            Long scheduleId = Long.parseLong(model.getValueAt(row, 0).toString());
            
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa lịch học này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                scheduleService.delete(scheduleId);
                JOptionPane.showMessageDialog(this, "Xóa lịch học thành công!");
                loadData(scheduleService.getAll()); // Tải lại bảng
                
                // Xóa trắng các ô nhập liệu
                txtClassId.setText(""); txtDate.setText(""); 
                txtStartTime.setText(""); txtEndTime.setText("");
            }
        });

        // 3. Xử lý sự kiện LÀM MỚI
        btnRefresh.addActionListener(e -> {
            txtClassId.setText(""); txtDate.setText(""); 
            txtStartTime.setText(""); txtEndTime.setText("");
            txtSearch.setText("");
            loadData(scheduleService.getAll());
        });

        // 4. Xử lý lọc Lambda
        btnFilter.addActionListener(e -> {
            try {
                Long cId = Long.parseLong(txtSearch.getText().trim());
                loadData(scheduleService.filterByClass(cId));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã lớp hợp lệ (số nguyên)!");
            }
        });
    }

    private void loadData(List<Schedule> list) {
        model.setRowCount(0);
        list.forEach(s -> model.addRow(new Object[]{
            s.getScheduleId(), s.getClassId(), dateFmt.format(s.getStudyDate()), s.getStartTime(), s.getEndTime()
        }));
    }
}