package com.mis.ui;

import com.mis.service.AttendanceService;
import com.mis.entity.Attendance;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AttendanceFrame extends JFrame {
    private JTable attendanceTable;
    private DefaultTableModel model;
    private JTextField txtStudentId, txtClassId, txtDate, txtNote, txtSearchStudent;
    private JComboBox<String> cbStatus;
    private AttendanceService attendanceService;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public AttendanceFrame() {
        attendanceService = new AttendanceService();
        setTitle("Quản lý Điểm danh");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Đóng cửa sổ riêng lẻ
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadData(attendanceService.getAllAttendances()); // Tải dữ liệu ban đầu
    }

    private void initComponents() {
        // --- PANEL NHẬP LIỆU (NORTH) ---
        JPanel pnlInput = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Chi tiết điểm danh"));

        pnlInput.add(new JLabel("Mã Học Viên:"));
        txtStudentId = new JTextField(); pnlInput.add(txtStudentId);

        pnlInput.add(new JLabel("Mã Lớp:"));
        txtClassId = new JTextField(); pnlInput.add(txtClassId);

        pnlInput.add(new JLabel("Ngày (yyyy-MM-dd):"));
        txtDate = new JTextField(sdf.format(new Date())); pnlInput.add(txtDate);

        pnlInput.add(new JLabel("Trạng Thái:"));
        cbStatus = new JComboBox<>(new String[]{"Present", "Absent", "Late"});
        pnlInput.add(cbStatus);

        pnlInput.add(new JLabel("Ghi chú:"));
        txtNote = new JTextField(); pnlInput.add(txtNote);

        add(pnlInput, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU (CENTER) ---
        model = new DefaultTableModel(new String[]{"ID", "Mã SV", "Mã Lớp", "Ngày", "Trạng Thái", "Ghi Chú"}, 0);
        attendanceTable = new JTable(model);
        add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        // --- PANEL NÚT BẤM LAMBDA (SOUTH) ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnSave = new JButton("Ghi nhận điểm danh");
        txtSearchStudent = new JTextField(10);
        JButton btnCountAbsence = new JButton("Đếm số buổi vắng (Lambda)");
        JButton btnRefresh = new JButton("Làm mới");

        pnlBottom.add(btnSave);
        pnlBottom.add(new JLabel(" | Nhập mã SV:")); pnlBottom.add(txtSearchStudent);
        pnlBottom.add(btnCountAbsence);
        pnlBottom.add(btnRefresh);
        add(pnlBottom, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN ---

        // Lưu điểm danh mới
      // Lưu điểm danh mới
        btnSave.addActionListener(e -> {
            try {
                Attendance a = new Attendance(
                    Long.parseLong(txtStudentId.getText()),
                    Long.parseLong(txtClassId.getText()),
                    sdf.parse(txtDate.getText()),
                    cbStatus.getSelectedItem().toString(),
                    txtNote.getText()
                );
                attendanceService.saveOrUpdate(a);
                JOptionPane.showMessageDialog(this, "Ghi nhận thành công!");
                loadData(attendanceService.getAllAttendances());
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Mã Học Viên và Mã Lớp phải là số nguyên!", "Sai định dạng số", JOptionPane.WARNING_MESSAGE);
            } catch (java.text.ParseException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Ngày điểm danh phải đúng định dạng yyyy-MM-dd!", "Sai định dạng ngày", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Lambda: Thống kê số buổi vắng mặt
        btnCountAbsence.addActionListener(e -> {
            try {
                Long sId = Long.parseLong(txtSearchStudent.getText());
                long count = attendanceService.countAbsencesByStudent(sId); // Gọi hàm Lambda đếm
                JOptionPane.showMessageDialog(this, "Học viên " + sId + " đã vắng mặt: " + count + " buổi.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã học viên hợp lệ!");
            }
        });

        btnRefresh.addActionListener(e -> loadData(attendanceService.getAllAttendances()));
    }

    private void loadData(List<Attendance> list) {
        model.setRowCount(0); // Xóa dữ liệu cũ
        list.forEach(a -> model.addRow(new Object[]{ // Duyệt danh sách bằng Lambda forEach
            a.getAttendanceId(), a.getStudentId(), a.getClassId(),
            sdf.format(a.getAttendDate()), a.getStatus(), a.getNote()
        }));
    }
}
