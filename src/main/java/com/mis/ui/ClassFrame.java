package com.mis.ui;

import com.mis.dao.ClassDAO;
import com.mis.entity.SchoolClass;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ClassFrame extends JFrame {
    private JTable classTable;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtCourseId, txtTeacherId, txtStartDate, txtEndDate, txtMaxStudent, txtSearch;
    private JComboBox<String> cbStatus;
    
    // Nút cơ bản
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;
    // Nút Lambda
    private JButton btnSearch, btnFilterStatus;

    private ClassDAO classDAO;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public ClassFrame() {
        classDAO = new ClassDAO();

        setTitle("Quản lý Lớp Học");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadDataToTable(classDAO.getAllClasses());
    }

    private void initComponents() {
        // --- NHẬP LIỆU ---
        JPanel panelInput = new JPanel(new GridLayout(4, 4, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Thông tin Lớp học"));

        panelInput.add(new JLabel("ID Lớp:"));
        txtId = new JTextField(); txtId.setEditable(false);
        panelInput.add(txtId);

        panelInput.add(new JLabel("Tên Lớp:"));
        txtName = new JTextField();
        panelInput.add(txtName);

        panelInput.add(new JLabel("ID Khóa Học (Bắt buộc):"));
        txtCourseId = new JTextField();
        panelInput.add(txtCourseId);

        panelInput.add(new JLabel("ID Giáo Viên:"));
        txtTeacherId = new JTextField();
        panelInput.add(txtTeacherId);

        panelInput.add(new JLabel("Ngày bắt đầu (yyyy-MM-dd):"));
        txtStartDate = new JTextField();
        panelInput.add(txtStartDate);

        panelInput.add(new JLabel("Ngày kết thúc (yyyy-MM-dd):"));
        txtEndDate = new JTextField();
        panelInput.add(txtEndDate);

        panelInput.add(new JLabel("Sỉ số tối đa:"));
        txtMaxStudent = new JTextField("30"); 
        panelInput.add(txtMaxStudent);

        panelInput.add(new JLabel("Trạng thái:"));
        cbStatus = new JComboBox<>(new String[]{"Planned", "Open", "Ongoing", "Completed", "Cancelled"});
        panelInput.add(cbStatus);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topWrapper.add(panelInput, BorderLayout.CENTER);
        add(topWrapper, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU ---
        String[] columns = {"ID", "Tên Lớp", "ID Khóa", "ID Giáo Viên", "Ngày BĐ", "Ngày KT", "Sỉ số", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0);
        classTable = new JTable(tableModel);
        classTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(classTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        classTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = classTable.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    txtCourseId.setText(tableModel.getValueAt(row, 2).toString());
                    
                    Object tId = tableModel.getValueAt(row, 3);
                    txtTeacherId.setText(tId != null ? tId.toString() : "");
                    
                    Object sDate = tableModel.getValueAt(row, 4);
                    txtStartDate.setText(sDate != null ? sdf.format((Date)sDate) : "");
                    
                    Object eDate = tableModel.getValueAt(row, 5);
                    txtEndDate.setText(eDate != null ? sdf.format((Date)eDate) : "");
                    
                    txtMaxStudent.setText(tableModel.getValueAt(row, 6).toString());
                    cbStatus.setSelectedItem(tableModel.getValueAt(row, 7).toString());
                }
            }
        });

        // --- NÚT BẤM CHIA 2 DÒNG ---
        JPanel bottomWrapper = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel panelActionButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAdd = new JButton("Thêm mới");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Làm mới");
        panelActionButtons.add(btnAdd); panelActionButtons.add(btnUpdate); 
        panelActionButtons.add(btnDelete); panelActionButtons.add(btnRefresh);

        JPanel panelLambdaButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtSearch = new JTextField(15);
        btnSearch = new JButton("Tìm Tên (Lambda 1)");
        btnFilterStatus = new JButton("Lọc Trạng Thái (Lambda 2)");
        
        panelLambdaButtons.add(new JLabel("Tìm kiếm:"));
        panelLambdaButtons.add(txtSearch);
        panelLambdaButtons.add(btnSearch);
        panelLambdaButtons.add(new JLabel("   |   "));
        panelLambdaButtons.add(btnFilterStatus);

        bottomWrapper.add(panelActionButtons);
        bottomWrapper.add(panelLambdaButtons);
        add(bottomWrapper, BorderLayout.SOUTH);

        // --- SỰ KIỆN CRUD ---
        btnAdd.addActionListener(e -> {
            try {
                SchoolClass sc = new SchoolClass(
                        txtName.getText().trim(), Long.parseLong(txtCourseId.getText().trim()),
                        txtTeacherId.getText().isEmpty() ? null : Long.parseLong(txtTeacherId.getText().trim()),
                        sdf.parse(txtStartDate.getText().trim()),
                        txtEndDate.getText().isEmpty() ? null : sdf.parse(txtEndDate.getText().trim()),
                        Integer.parseInt(txtMaxStudent.getText().trim()), cbStatus.getSelectedItem().toString()
                );
                classDAO.addClass(sc);
                JOptionPane.showMessageDialog(this, "Thêm lớp học thành công!");
                clearForm(); loadDataToTable(classDAO.getAllClasses());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi! Ngày tháng (yyyy-MM-dd) và ID phải là số.");
            }
        });

        btnUpdate.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            try {
                SchoolClass sc = new SchoolClass();
                sc.setClassId(Long.parseLong(txtId.getText()));
                sc.setClassName(txtName.getText().trim()); sc.setCourseId(Long.parseLong(txtCourseId.getText().trim()));
                sc.setTeacherId(txtTeacherId.getText().isEmpty() ? null : Long.parseLong(txtTeacherId.getText().trim()));
                sc.setStartDate(sdf.parse(txtStartDate.getText().trim()));
                sc.setEndDate(txtEndDate.getText().isEmpty() ? null : sdf.parse(txtEndDate.getText().trim()));
                sc.setMaxStudent(Integer.parseInt(txtMaxStudent.getText().trim())); sc.setStatus(cbStatus.getSelectedItem().toString());

                classDAO.updateClass(sc);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                clearForm(); loadDataToTable(classDAO.getAllClasses());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi định dạng dữ liệu!");
            }
        });

        btnDelete.addActionListener(e -> {
            if (!txtId.getText().isEmpty() && JOptionPane.showConfirmDialog(this, "Chắc chắn xóa?") == 0) {
                classDAO.deleteClass(Long.parseLong(txtId.getText()));
                clearForm(); loadDataToTable(classDAO.getAllClasses());
            }
        });

        btnRefresh.addActionListener(e -> { clearForm(); loadDataToTable(classDAO.getAllClasses()); });

        // --- SỰ KIỆN LAMBDA ---
        
        // Lambda 1: Tìm kiếm theo tên
        btnSearch.addActionListener(e -> {
            String kw = txtSearch.getText().trim();
            loadDataToTable(kw.isEmpty() ? classDAO.getAllClasses() : classDAO.searchClassByName(kw));
        });

        // Lambda 2: Lọc theo Trạng thái đang chọn ở ComboBox
        btnFilterStatus.addActionListener(e -> {
            String selectedStatus = cbStatus.getSelectedItem().toString();
            List<SchoolClass> result = classDAO.getClassesByStatus(selectedStatus);
            loadDataToTable(result);
            JOptionPane.showMessageDialog(this, "Đã lọc danh sách Lớp học có trạng thái: " + selectedStatus);
        });
    }

    private void loadDataToTable(List<SchoolClass> classes) {
        tableModel.setRowCount(0);
        classes.forEach(c -> {
            tableModel.addRow(new Object[]{
                    c.getClassId(), c.getClassName(), c.getCourseId(), c.getTeacherId(),
                    c.getStartDate() != null ? sdf.format(c.getStartDate()) : null,
                    c.getEndDate() != null ? sdf.format(c.getEndDate()) : null,
                    c.getMaxStudent(), c.getStatus()
            });
        });
    }

    private void clearForm() {
        txtId.setText(""); txtName.setText(""); txtCourseId.setText(""); txtTeacherId.setText("");
        txtStartDate.setText(""); txtEndDate.setText(""); txtMaxStudent.setText("30"); txtSearch.setText("");
        cbStatus.setSelectedIndex(0); classTable.clearSelection();
    }
}