package com.mis.ui;

import com.mis.service.EnrollmentService;
import com.mis.entity.Enrollment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class EnrollmentFrame extends JFrame {
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;
    private JTextField txtStudentId, txtClassId, txtSearchClass, txtSearchStudent;
    private JComboBox<String> cbStatus;
    private EnrollmentService enrollmentService;

    public EnrollmentFrame() {
        enrollmentService = new EnrollmentService(); 

        setTitle("Quản lý Đăng ký Học viên - Tích hợp Lambda");
        setSize(1000, 650); // Tăng chiều cao lên 650 để chứa 2 dòng nút bấm
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadDataToTable(enrollmentService.getAllEnrollments()); 
    }

    private void initComponents() {
        // --- PANEL NHẬP LIỆU (NORTH) ---
        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBorder(BorderFactory.createTitledBorder("Thông tin đăng ký"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 1: Mã Học Viên và Mã Lớp Học
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        panelInput.add(new JLabel("Mã Học Viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        txtStudentId = new JTextField();
        panelInput.add(txtStudentId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        panelInput.add(new JLabel("Mã Lớp Học:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        txtClassId = new JTextField();
        panelInput.add(txtClassId, gbc);

        // Dòng 2: Trạng thái 
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        panelInput.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        cbStatus = new JComboBox<>(new String[]{"Enrolled", "Dropped", "Completed"});
        panelInput.add(cbStatus, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        panelInput.add(new JLabel(""), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        panelInput.add(new JLabel(""), gbc);

        add(panelInput, BorderLayout.NORTH);

        // --- BẢNG HIỂN THỊ (CENTER) ---
        String[] columns = {"ID Đăng Ký", "Mã Học Viên", "Mã Lớp", "Ngày Đăng Ký", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        enrollmentTable = new JTable(tableModel);
        enrollmentTable.setRowHeight(25);
        add(new JScrollPane(enrollmentTable), BorderLayout.CENTER);

        // --- PANEL NÚT BẤM (SOUTH) - CHIA THÀNH 2 DÒNG ---
        JPanel bottomWrapper = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Dòng 1: Các nút thao tác cơ bản
        JPanel panelActionButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnAdd = new JButton("Đăng ký mới");
        JButton btnRefresh = new JButton("Làm mới danh sách");
        panelActionButtons.add(btnAdd);
        panelActionButtons.add(btnRefresh);

        // Dòng 2: Các nút chức năng Lambda
        JPanel panelLambdaButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        txtSearchStudent = new JTextField(6);
        JButton btnFilterStudent = new JButton("Lọc theo SV (Lambda)");
        
        txtSearchClass = new JTextField(6);
        JButton btnFilterClass = new JButton("Lọc theo Lớp (Lambda)");
        
        JButton btnCount = new JButton("Thống kê hoàn thành (Lambda)");

        panelLambdaButtons.add(new JLabel("Mã SV:"));
        panelLambdaButtons.add(txtSearchStudent);
        panelLambdaButtons.add(btnFilterStudent);
        
        panelLambdaButtons.add(new JLabel("   |   Mã Lớp:"));
        panelLambdaButtons.add(txtSearchClass);
        panelLambdaButtons.add(btnFilterClass);
        
        panelLambdaButtons.add(new JLabel("   |   "));
        panelLambdaButtons.add(btnCount);

        bottomWrapper.add(panelActionButtons);
        bottomWrapper.add(panelLambdaButtons);
        add(bottomWrapper, BorderLayout.SOUTH);

        // --- SỰ KIỆN NÚT BẤM ---
        
        // Nút Thêm mới
       // Nút Thêm mới (Đã nâng cấp bắt lỗi trùng lặp bằng Lambda)
        btnAdd.addActionListener(e -> {
            try {
                // Lấy ID do người dùng nhập vào
                Long studentId = Long.parseLong(txtStudentId.getText().trim());
                Long classId = Long.parseLong(txtClassId.getText().trim());

                // --- BƯỚC 1: BẮT LỖI TRÙNG LẶP BẰNG LAMBDA ---
                // Duyệt danh sách xem đã có cặp (Mã SV, Mã Lớp) này tồn tại chưa
                boolean isDuplicate = enrollmentService.getAllEnrollments().stream()
                        .anyMatch(en -> en.getStudentId().equals(studentId) && en.getClassId().equals(classId));

                // Nếu đã tồn tại -> Báo lỗi đúng bệnh và dừng lại ngay
                if (isDuplicate) {
                    JOptionPane.showMessageDialog(this, 
                        "LỖI TỪ CHỐI GHI DANH:\nHọc viên (Mã " + studentId + ") đã được đăng ký vào Lớp (Mã " + classId + ") từ trước!\nVui lòng kiểm tra lại danh sách.", 
                        "Cảnh báo trùng lặp", 
                        JOptionPane.ERROR_MESSAGE);
                    return; // Thoát ra, không chạy tiếp đoạn code lưu DB ở dưới
                }
                // ---------------------------------------------

                // --- BƯỚC 2: NẾU KHÔNG TRÙNG THÌ MỚI LƯU ---
                Enrollment en = new Enrollment(
                    studentId,
                    classId,
                    new Date(),
                    cbStatus.getSelectedItem().toString()
                );
                enrollmentService.saveOrUpdateEnrollment(en);
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                
                // Tải lại bảng và xóa trắng ô nhập
                loadDataToTable(enrollmentService.getAllEnrollments());
                txtStudentId.setText("");
                txtClassId.setText("");

            } catch (NumberFormatException ex) {
                // Bắt đúng lỗi nếu người dùng nhập chữ cái hoặc để trống
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: Mã Học Viên và Mã Lớp bắt buộc phải là các con số!", "Lỗi định dạng", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                // Bắt các lỗi hệ thống khác
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Làm mới
        btnRefresh.addActionListener(e -> {
            txtSearchStudent.setText("");
            txtSearchClass.setText("");
            loadDataToTable(enrollmentService.getAllEnrollments());
        });

        // SỰ KIỆN LAMBDA 1: Lọc theo Mã Sinh Viên
        btnFilterStudent.addActionListener(e -> {
            try {
                Long sId = Long.parseLong(txtSearchStudent.getText().trim());
                List<Enrollment> result = enrollmentService.filterByStudent(sId);
                loadDataToTable(result);
                if (result.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Học viên này chưa đăng ký lớp nào!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Sinh Viên hợp lệ (số nguyên)!");
            }
        });

        // SỰ KIỆN LAMBDA 2: Lọc theo Mã Lớp
        btnFilterClass.addActionListener(e -> {
            try {
                Long cId = Long.parseLong(txtSearchClass.getText().trim());
                List<Enrollment> result = enrollmentService.filterByClass(cId);
                loadDataToTable(result);
                if (result.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Lớp này hiện chưa có học viên nào đăng ký!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Lớp hợp lệ (số nguyên)!");
            }
        });

        // SỰ KIỆN LAMBDA 3: Thống kê trạng thái Completed
        btnCount.addActionListener(e -> {
            long total = enrollmentService.countCompletedEnrollments();
            JOptionPane.showMessageDialog(this, "Tổng số lượt đăng ký đã hoàn thành khóa học: " + total, 
                    "Thống kê (Lambda)", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void loadDataToTable(List<Enrollment> list) {
        tableModel.setRowCount(0);
        list.forEach(e -> {
            tableModel.addRow(new Object[]{
                e.getEnrollmentId(), e.getStudentId(), e.getClassId(), e.getEnrollmentDate(), e.getStatus()
            });
        });
    }
}