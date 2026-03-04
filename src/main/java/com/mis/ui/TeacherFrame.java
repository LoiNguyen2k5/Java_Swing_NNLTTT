package com.mis.ui;

import com.mis.dao.TeacherDAO;
import com.mis.entity.Teacher;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

public class TeacherFrame extends JFrame {
    private JTable teacherTable;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtPhone, txtEmail, txtSearch;
    private JComboBox<String> cbSpecialty, cbStatus;
    
    // Khai báo thêm các nút bấm cho Lambda
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;
    private JButton btnSearch, btnFilterSpecialty, btnCountActive;

    private TeacherDAO teacherDAO;

    public TeacherFrame() {
        teacherDAO = new TeacherDAO();

        setTitle("Quản lý Giáo Viên");
        setSize(1000, 650); // Tăng chiều cao để chứa 2 dòng nút bấm
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadDataToTable(teacherDAO.getAllTeachers());
    }

    private void initComponents() {
        // --- PANEL NHẬP LIỆU (GridLayout 4x4) ---
        JPanel panelInput = new JPanel(new GridLayout(4, 4, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Thông tin Giáo Viên"));

        // Dòng 1
        panelInput.add(new JLabel("ID (Tự sinh):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelInput.add(txtId);

        panelInput.add(new JLabel("Họ và Tên:"));
        txtName = new JTextField();
        panelInput.add(txtName);

        // Dòng 2
        panelInput.add(new JLabel("Điện thoại:"));
        txtPhone = new JTextField();
        panelInput.add(txtPhone);

        panelInput.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelInput.add(txtEmail);

        // Dòng 3
        panelInput.add(new JLabel("Chuyên môn:"));
        cbSpecialty = new JComboBox<>(new String[]{"IELTS", "TOEIC", "GiaoTiep", "Khác"}); 
        panelInput.add(cbSpecialty);

        panelInput.add(new JLabel("Trạng thái:"));
        cbStatus = new JComboBox<>(new String[]{"Active", "Inactive"});
        panelInput.add(cbStatus);

        // Dòng 4 (Lấp đầy khoảng trống để form thẳng hàng)
        panelInput.add(new JLabel(""));
        panelInput.add(new JLabel(""));
        panelInput.add(new JLabel(""));
        panelInput.add(new JLabel(""));

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topWrapper.add(panelInput, BorderLayout.CENTER);
        add(topWrapper, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU ---
        String[] columns = {"ID", "Họ Tên", "Điện Thoại", "Email", "Chuyên Môn", "Ngày Tuyển", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        teacherTable = new JTable(tableModel);
        teacherTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Click vào bảng để lấy dữ liệu ngược lên
        teacherTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = teacherTable.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    
                    Object phoneObj = tableModel.getValueAt(row, 2);
                    txtPhone.setText(phoneObj != null ? phoneObj.toString() : "");
                    
                    Object emailObj = tableModel.getValueAt(row, 3);
                    txtEmail.setText(emailObj != null ? emailObj.toString() : "");
                    
                    Object specObj = tableModel.getValueAt(row, 4);
                    if (specObj != null) cbSpecialty.setSelectedItem(specObj.toString());
                    
                    cbStatus.setSelectedItem(tableModel.getValueAt(row, 6).toString());
                }
            }
        });

        // --- NÚT BẤM (Phía dưới) ---
        // Chia làm 2 dòng: Dòng 1 CRUD, Dòng 2 Lambda
        JPanel bottomWrapper = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Dòng 1: Các nút CRUD
        JPanel panelActionButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAdd = new JButton("Thêm mới");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Làm mới");
        
        panelActionButtons.add(btnAdd);
        panelActionButtons.add(btnUpdate);
        panelActionButtons.add(btnDelete);
        panelActionButtons.add(btnRefresh);

        // Dòng 2: Các nút Lambda
        JPanel panelLambdaButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtSearch = new JTextField(15);
        btnSearch = new JButton("Tìm Tên (Lambda 4)");
        btnFilterSpecialty = new JButton("Lọc Chuyên Môn (Lambda 5)");
        btnCountActive = new JButton("Đếm GV Active (Lambda 6)");

        panelLambdaButtons.add(new JLabel("Tìm kiếm (Tên):"));
        panelLambdaButtons.add(txtSearch);
        panelLambdaButtons.add(btnSearch);
        panelLambdaButtons.add(new JLabel("   |   "));
        panelLambdaButtons.add(btnFilterSpecialty);
        panelLambdaButtons.add(btnCountActive);

        bottomWrapper.add(panelActionButtons);
        bottomWrapper.add(panelLambdaButtons);
        add(bottomWrapper, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN CRUD ---

        btnAdd.addActionListener(e -> {
            if (txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Họ Tên!"); return;
            }
            Teacher t = new Teacher(
                    txtName.getText().trim(), txtPhone.getText().trim(), txtEmail.getText().trim(),
                    cbSpecialty.getSelectedItem().toString(), new Date(), cbStatus.getSelectedItem().toString()
            );
            teacherDAO.addTeacher(t);
            JOptionPane.showMessageDialog(this, "Thêm giáo viên thành công!");
            clearForm(); loadDataToTable(teacherDAO.getAllTeachers());
        });

        btnUpdate.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chọn giáo viên cần sửa!"); return;
            }
            Teacher t = new Teacher();
            t.setTeacherId(Long.parseLong(txtId.getText()));
            t.setFullName(txtName.getText().trim());
            t.setPhone(txtPhone.getText().trim());
            t.setEmail(txtEmail.getText().trim());
            t.setSpecialty(cbSpecialty.getSelectedItem().toString());
            t.setHireDate(new Date()); 
            t.setStatus(cbStatus.getSelectedItem().toString());

            teacherDAO.updateTeacher(t);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            clearForm(); loadDataToTable(teacherDAO.getAllTeachers());
        });

        btnDelete.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chọn giáo viên cần xóa!"); return;
            }
            if (JOptionPane.showConfirmDialog(this, "Chắc chắn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                teacherDAO.deleteTeacher(Long.parseLong(txtId.getText()));
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                clearForm(); loadDataToTable(teacherDAO.getAllTeachers());
            }
        });

        btnRefresh.addActionListener(e -> {
            clearForm(); loadDataToTable(teacherDAO.getAllTeachers());
        });

        // --- XỬ LÝ SỰ KIỆN LAMBDA ---

        // Lambda 4: Tìm tên
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                loadDataToTable(teacherDAO.getAllTeachers());
            } else {
                List<Teacher> result = teacherDAO.searchTeachersByName(keyword);
                loadDataToTable(result);
            }
        });

        // Lambda 5: Lọc chuyên môn dựa trên ComboBox
        btnFilterSpecialty.addActionListener(e -> {
            String selectedSpecialty = cbSpecialty.getSelectedItem().toString();
            List<Teacher> result = teacherDAO.getTeachersBySpecialty(selectedSpecialty);
            loadDataToTable(result);
            JOptionPane.showMessageDialog(this, "Đã lọc danh sách giáo viên chuyên môn: " + selectedSpecialty);
        });

        // Lambda 6: Đếm giáo viên Active
        btnCountActive.addActionListener(e -> {
            long count = teacherDAO.countActiveTeachers();
            JOptionPane.showMessageDialog(this, 
                    "Tổng số Giáo viên đang hoạt động (Active) là: " + count, 
                    "Kết quả đếm (Java Lambda)", 
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void loadDataToTable(List<Teacher> teachers) {
        tableModel.setRowCount(0);
        teachers.forEach(t -> {
            tableModel.addRow(new Object[]{
                    t.getTeacherId(), t.getFullName(), t.getPhone(), t.getEmail(),
                    t.getSpecialty(), t.getHireDate(), t.getStatus()
            });
        });
    }

    private void clearForm() {
        txtId.setText(""); txtName.setText(""); txtPhone.setText("");
        txtEmail.setText(""); txtSearch.setText("");
        cbSpecialty.setSelectedIndex(0); cbStatus.setSelectedIndex(0);
        teacherTable.clearSelection();
    }
}