package com.mis.ui;

import com.mis.dao.StudentDAO;
import com.mis.entity.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.List;

public class StudentFrame extends JFrame {
    // Khai báo các component
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtPhone, txtEmail, txtAddress, txtSearch;
    private JComboBox<String> cbGender, cbStatus;
    // Thêm 2 nút bấm mới cho Lambda
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnSearch, btnFilterStatus, btnCountMale;

    private StudentDAO studentDAO;

    public StudentFrame() {
        studentDAO = new StudentDAO();

        setTitle("Quản lý Học Viên");
        setSize(1000, 650); // Tăng chiều cao lên một chút để chứa thêm dòng nút bấm
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadDataToTable(studentDAO.getAllStudents());
    }

    private void initComponents() {
        // --- PANEL NHẬP LIỆU (Phía trên) ---
        JPanel panelInput = new JPanel(new GridLayout(4, 4, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Thông tin Học viên"));

        panelInput.add(new JLabel("ID (Tự sinh):"));
        txtId = new JTextField(); txtId.setEditable(false);
        panelInput.add(txtId);

        panelInput.add(new JLabel("Họ và Tên:"));
        txtName = new JTextField();
        panelInput.add(txtName);

        panelInput.add(new JLabel("Giới tính:"));
        cbGender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        panelInput.add(cbGender);

        panelInput.add(new JLabel("Điện thoại:"));
        txtPhone = new JTextField();
        panelInput.add(txtPhone);

        panelInput.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelInput.add(txtEmail);

        panelInput.add(new JLabel("Địa chỉ:"));
        txtAddress = new JTextField();
        panelInput.add(txtAddress);

        panelInput.add(new JLabel("Trạng thái:"));
        cbStatus = new JComboBox<>(new String[]{"Active", "Inactive"});
        panelInput.add(cbStatus);

        panelInput.add(new JLabel("")); 
        panelInput.add(new JLabel("")); 

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topWrapper.add(panelInput, BorderLayout.CENTER);
        add(topWrapper, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU (Ở giữa) ---
        String[] columns = {"ID", "Họ Tên", "Giới Tính", "Điện Thoại", "Email", "Địa Chỉ", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        // Sự kiện click vào bảng
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    cbGender.setSelectedItem(tableModel.getValueAt(row, 2).toString());
                    txtPhone.setText(tableModel.getValueAt(row, 3).toString());
                    Object emailObj = tableModel.getValueAt(row, 4);
                    txtEmail.setText(emailObj != null ? emailObj.toString() : "");
                    Object addressObj = tableModel.getValueAt(row, 5);
                    txtAddress.setText(addressObj != null ? addressObj.toString() : "");
                    cbStatus.setSelectedItem(tableModel.getValueAt(row, 6).toString());
                }
            }
        });

        // --- PANEL NÚT BẤM (Phía dưới) ---
        // Sử dụng GridLayout 2 dòng để chia khu vực nút thường và nút Lambda
        JPanel bottomWrapper = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Dòng 1: Các nút CRUD cơ bản
        JPanel panelActionButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAdd = new JButton("Thêm mới");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Làm mới");
        
        panelActionButtons.add(btnAdd);
        panelActionButtons.add(btnUpdate);
        panelActionButtons.add(btnDelete);
        panelActionButtons.add(btnRefresh);

        // Dòng 2: Khu vực biểu diễn Java Lambdas cho Giáo viên chấm
        JPanel panelLambdaButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtSearch = new JTextField(15);
        btnSearch = new JButton("Tìm Tên (Lambda 1)");
        btnFilterStatus = new JButton("Lọc Trạng thái (Lambda 2)");
        btnCountMale = new JButton("Đếm HV Nam (Lambda 3)");

        panelLambdaButtons.add(new JLabel("Tìm kiếm (Tên):"));
        panelLambdaButtons.add(txtSearch);
        panelLambdaButtons.add(btnSearch);
        panelLambdaButtons.add(new JLabel("   |   "));
        panelLambdaButtons.add(btnFilterStatus);
        panelLambdaButtons.add(btnCountMale);

        bottomWrapper.add(panelActionButtons);
        bottomWrapper.add(panelLambdaButtons);
        add(bottomWrapper, BorderLayout.SOUTH);

        // --- ĐĂNG KÝ SỰ KIỆN CHO CÁC NÚT ---

        btnAdd.addActionListener(e -> {
            if (txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Họ và Tên!"); return;
            }
            Student st = new Student(txtName.getText().trim(), new Date(), cbGender.getSelectedItem().toString(),
                    txtPhone.getText().trim(), txtEmail.getText().trim(), txtAddress.getText().trim(), cbStatus.getSelectedItem().toString());
            studentDAO.addStudent(st);
            JOptionPane.showMessageDialog(this, "Thêm thành công!");
            clearForm(); loadDataToTable(studentDAO.getAllStudents());
        });

        btnUpdate.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chọn học viên để sửa!"); return;
            }
            Student st = new Student();
            st.setStudentId(Long.parseLong(txtId.getText()));
            st.setFullName(txtName.getText().trim());
            st.setDateOfBirth(new Date());
            st.setGender(cbGender.getSelectedItem().toString());
            st.setPhone(txtPhone.getText().trim());
            st.setEmail(txtEmail.getText().trim());
            st.setAddress(txtAddress.getText().trim());
            st.setStatus(cbStatus.getSelectedItem().toString());

            studentDAO.updateStudent(st);
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            clearForm(); loadDataToTable(studentDAO.getAllStudents());
        });

        btnDelete.addActionListener(e -> {
            if (txtId.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chọn học viên để xóa!"); return;
            }
            if (JOptionPane.showConfirmDialog(this, "Chắc chắn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                studentDAO.deleteStudent(Long.parseLong(txtId.getText()));
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                clearForm(); loadDataToTable(studentDAO.getAllStudents());
            }
        });

        btnRefresh.addActionListener(e -> {
            clearForm(); loadDataToTable(studentDAO.getAllStudents());
        });

        // --- SỰ KIỆN LAMBDA YÊU CẦU CỦA GIÁO VIÊN ---

        // Lambda 1: Tìm theo tên
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (keyword.isEmpty()) {
                loadDataToTable(studentDAO.getAllStudents());
            } else {
                List<Student> result = studentDAO.searchStudentsByName(keyword);
                loadDataToTable(result);
            }
        });

        // Lambda 2: Lọc theo trạng thái đang được chọn ở ô ComboBox "Trạng thái"
        btnFilterStatus.addActionListener(e -> {
            String selectedStatus = cbStatus.getSelectedItem().toString();
            List<Student> result = studentDAO.getStudentsByStatus(selectedStatus);
            loadDataToTable(result);
            JOptionPane.showMessageDialog(this, "Đã lọc danh sách các học viên có trạng thái: " + selectedStatus);
        });

        // Lambda 3: Đếm tổng số học viên Nam
        btnCountMale.addActionListener(e -> {
            long count = studentDAO.countMaleStudents();
            JOptionPane.showMessageDialog(this, 
                    "Tổng số học viên Nam (Male) hiện tại trong hệ thống là: " + count + " học viên.", 
                    "Kết quả đếm (Java Lambda)", 
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void loadDataToTable(List<Student> students) {
        tableModel.setRowCount(0);
        students.forEach(s -> {
            tableModel.addRow(new Object[]{
                    s.getStudentId(), s.getFullName(), s.getGender(),
                    s.getPhone(), s.getEmail(), s.getAddress(), s.getStatus()
            });
        });
    }

    private void clearForm() {
        txtId.setText(""); txtName.setText(""); txtPhone.setText("");
        txtEmail.setText(""); txtAddress.setText(""); txtSearch.setText("");
        cbGender.setSelectedIndex(0); cbStatus.setSelectedIndex(0);
        studentTable.clearSelection();
    }
}