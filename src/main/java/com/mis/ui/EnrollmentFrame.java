package com.mis.ui;

import com.mis.dao.EnrollmentDAO;
import com.mis.entity.Enrollment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.util.List;

public class EnrollmentFrame extends JFrame {
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;
    private JTextField txtStudentId, txtClassId, txtSearchClass;
    private JComboBox<String> cbStatus;
    private EnrollmentDAO enrollmentDAO;

    public EnrollmentFrame() {
        enrollmentDAO = new EnrollmentDAO(); // Khởi tạo lớp xử lý dữ liệu

        setTitle("Quản lý Đăng ký Học viên");
        setSize(1000, 600);
        // DISPOSE_ON_CLOSE để khi đóng không làm thoát Dashboard chính
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadDataToTable(enrollmentDAO.getAllEnrollments()); // Tải dữ liệu ban đầu
    }

    private void initComponents() {
        // --- PANEL NHẬP LIỆU (NORTH) ---
        // Sử dụng GridBagLayout để căn chỉnh các ô nhập liệu thẳng hàng tuyệt đối
        JPanel panelInput = new JPanel(new GridBagLayout());
        panelInput.setBorder(BorderFactory.createTitledBorder("Thông tin đăng ký"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Tạo khoảng cách đệm giữa các ô
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

        // Dòng 2: Trạng thái (được đặt thẳng hàng với cột đầu tiên)
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        panelInput.add(new JLabel("Trạng thái:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        cbStatus = new JComboBox<>(new String[]{"Enrolled", "Dropped", "Completed"});
        panelInput.add(cbStatus, gbc);

        // Các ô trống bổ sung để giữ khung hàng 2 cân bằng với hàng 1
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        panelInput.add(new JLabel(""), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        panelInput.add(new JLabel(""), gbc);

        add(panelInput, BorderLayout.NORTH);

        // --- BẢNG HIỂN THỊ (CENTER) ---
        String[] columns = {"ID", "Mã Học Viên", "Mã Lớp", "Ngày Đăng Ký", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        enrollmentTable = new JTable(tableModel);
        enrollmentTable.setRowHeight(25);
        add(new JScrollPane(enrollmentTable), BorderLayout.CENTER);

        // --- PANEL NÚT BẤM LAMBDA (SOUTH) ---
        JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAdd = new JButton("Đăng ký mới");
        txtSearchClass = new JTextField(10);
        JButton btnFilter = new JButton("Lọc theo Lớp (Lambda)");
        JButton btnCount = new JButton("Thống kê hoàn thành (Lambda)");

        panelBottom.add(btnAdd);
        panelBottom.add(new JLabel(" | Nhập mã lớp:"));
        panelBottom.add(txtSearchClass);
        panelBottom.add(btnFilter);
        panelBottom.add(btnCount);
        add(panelBottom, BorderLayout.SOUTH);

        // --- SỰ KIỆN NÚT BẤM (Sử dụng Java Lambda) ---
        btnAdd.addActionListener(e -> {
            try {
                Enrollment en = new Enrollment(
                    Long.parseLong(txtStudentId.getText()),
                    Long.parseLong(txtClassId.getText()),
                    new Date(),
                    cbStatus.getSelectedItem().toString()
                );
                enrollmentDAO.saveOrUpdateEnrollment(en);
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                loadDataToTable(enrollmentDAO.getAllEnrollments());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Vui lòng nhập ID là số!");
            }
        });

        btnFilter.addActionListener(e -> {
            try {
                Long cId = Long.parseLong(txtSearchClass.getText());
                // Gọi hàm Lambda xử lý Stream từ DAO
                List<Enrollment> result = enrollmentDAO.filterByClass(cId);
                loadDataToTable(result);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã lớp hợp lệ!");
            }
        });

        btnCount.addActionListener(e -> {
            // Gọi hàm Lambda đếm từ DAO
            long total = enrollmentDAO.countCompletedEnrollments();
            JOptionPane.showMessageDialog(this, "Tổng số đăng ký đã hoàn thành: " + total);
        });
    }

    private void loadDataToTable(List<Enrollment> list) {
        tableModel.setRowCount(0);
        // Sử dụng Lambda forEach để duyệt danh sách
        list.forEach(e -> {
            tableModel.addRow(new Object[]{
                e.getEnrollmentId(), e.getStudentId(), e.getClassId(), e.getEnrollmentDate(), e.getStatus()
            });
        });
    }
}
