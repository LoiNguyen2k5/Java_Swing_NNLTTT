package com.mis.ui;

import com.mis.dao.CourseDAO;
import com.mis.entity.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

public class CourseFrame extends JFrame {
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtName, txtDuration, txtFee, txtSearch;
    private JTextArea txtDescription;
    private JComboBox<String> cbLevel, cbStatus;
    
    // Nút CRUD
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh;
    // Nút Lambda
    private JButton btnSearch, btnFilterLevel, btnFilterFee;

    private CourseDAO courseDAO;

    public CourseFrame() {
        courseDAO = new CourseDAO();

        setTitle("Quản lý Khóa Học");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());

        initComponents(); 
        loadDataToTable(courseDAO.getAllCourses()); 
    }

    private void initComponents() {
        // --- VÙNG NHẬP LIỆU ---
        JPanel panelInput = new JPanel(new GridLayout(4, 4, 10, 10));
        panelInput.setBorder(BorderFactory.createTitledBorder("Thông tin Khóa học"));

        panelInput.add(new JLabel("ID (Tự sinh):"));
        txtId = new JTextField(); txtId.setEditable(false);
        panelInput.add(txtId);

        panelInput.add(new JLabel("Tên Khóa Học:"));
        txtName = new JTextField();
        panelInput.add(txtName);

        panelInput.add(new JLabel("Cấp độ:"));
        cbLevel = new JComboBox<>(new String[]{"Beginner", "Intermediate", "Advanced"});
        panelInput.add(cbLevel);

        panelInput.add(new JLabel("Thời lượng (Tuần):"));
        txtDuration = new JTextField();
        panelInput.add(txtDuration);

        panelInput.add(new JLabel("Học phí (VND):"));
        txtFee = new JTextField();
        panelInput.add(txtFee);

        panelInput.add(new JLabel("Trạng thái:"));
        cbStatus = new JComboBox<>(new String[]{"Active", "Inactive"});
        panelInput.add(cbStatus);

        panelInput.add(new JLabel("Mô tả khóa học:"));
        txtDescription = new JTextArea(2, 20);
        JScrollPane scrollDesc = new JScrollPane(txtDescription);
        panelInput.add(scrollDesc);
        
        panelInput.add(new JLabel(""));
        panelInput.add(new JLabel(""));

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topWrapper.add(panelInput, BorderLayout.CENTER);
        add(topWrapper, BorderLayout.NORTH);

        // --- BẢNG DỮ LIỆU ---
        String[] columns = {"ID", "Tên Khóa", "Cấp độ", "Thời lượng", "Học phí", "Mô tả", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0);
        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(courseTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(scrollPane, BorderLayout.CENTER);

        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = courseTable.getSelectedRow();
                if (row >= 0) {
                    txtId.setText(tableModel.getValueAt(row, 0).toString());
                    txtName.setText(tableModel.getValueAt(row, 1).toString());
                    
                    Object lvlObj = tableModel.getValueAt(row, 2);
                    if (lvlObj != null) cbLevel.setSelectedItem(lvlObj.toString());
                    
                    Object durObj = tableModel.getValueAt(row, 3);
                    txtDuration.setText(durObj != null ? durObj.toString() : "");
                    
                    Object feeObj = tableModel.getValueAt(row, 4);
                    txtFee.setText(feeObj != null ? feeObj.toString() : "0");
                    
                    Object descObj = tableModel.getValueAt(row, 5);
                    txtDescription.setText(descObj != null ? descObj.toString() : "");
                    
                    cbStatus.setSelectedItem(tableModel.getValueAt(row, 6).toString());
                }
            }
        });

        // --- NÚT BẤM CHIA 2 DÒNG ---
        JPanel bottomWrapper = new JPanel(new GridLayout(2, 1, 5, 5));
        bottomWrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Dòng 1: Nút cơ bản
        JPanel panelActionButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAdd = new JButton("Thêm mới");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnRefresh = new JButton("Làm mới");
        panelActionButtons.add(btnAdd); panelActionButtons.add(btnUpdate); 
        panelActionButtons.add(btnDelete); panelActionButtons.add(btnRefresh);

        // Dòng 2: Nút Lambda
        JPanel panelLambdaButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtSearch = new JTextField(15);
        btnSearch = new JButton("Tìm Tên (Lambda 1)");
        btnFilterLevel = new JButton("Lọc Cấp Độ (Lambda 2)");
        btnFilterFee = new JButton("Tìm Học Phí Rẻ Hơn (Lambda 3)");
        
        panelLambdaButtons.add(new JLabel("Tìm kiếm:"));
        panelLambdaButtons.add(txtSearch);
        panelLambdaButtons.add(btnSearch);
        panelLambdaButtons.add(new JLabel("   |   "));
        panelLambdaButtons.add(btnFilterLevel);
        panelLambdaButtons.add(btnFilterFee);

        bottomWrapper.add(panelActionButtons);
        bottomWrapper.add(panelLambdaButtons);
        add(bottomWrapper, BorderLayout.SOUTH);

        // --- SỰ KIỆN CRUD ---
        btnAdd.addActionListener(e -> {
            try {
                Course c = new Course(
                        txtName.getText().trim(), txtDescription.getText().trim(), cbLevel.getSelectedItem().toString(),
                        Integer.parseInt(txtDuration.getText().trim()), new BigDecimal(txtFee.getText().trim()), cbStatus.getSelectedItem().toString()
                );
                courseDAO.addCourse(c);
                JOptionPane.showMessageDialog(this, "Thêm khóa học thành công!");
                clearForm(); loadDataToTable(courseDAO.getAllCourses());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Nhập đúng định dạng số cho Thời lượng và Học phí!");
            }
        });

        btnUpdate.addActionListener(e -> {
            if (txtId.getText().isEmpty()) return;
            try {
                Course c = new Course();
                c.setCourseId(Long.parseLong(txtId.getText()));
                c.setCourseName(txtName.getText().trim()); c.setDescription(txtDescription.getText().trim());
                c.setLevel(cbLevel.getSelectedItem().toString()); c.setDuration(Integer.parseInt(txtDuration.getText().trim()));
                c.setFee(new BigDecimal(txtFee.getText().trim())); c.setStatus(cbStatus.getSelectedItem().toString());

                courseDAO.updateCourse(c);
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                clearForm(); loadDataToTable(courseDAO.getAllCourses());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!");
            }
        });

        btnDelete.addActionListener(e -> {
            if (!txtId.getText().isEmpty() && JOptionPane.showConfirmDialog(this, "Chắc chắn xóa?") == 0) {
                courseDAO.deleteCourse(Long.parseLong(txtId.getText()));
                clearForm(); loadDataToTable(courseDAO.getAllCourses());
            }
        });

        btnRefresh.addActionListener(e -> {
            clearForm(); loadDataToTable(courseDAO.getAllCourses());
        });

        // --- SỰ KIỆN LAMBDA ---
        
        // Lambda 1: Tìm theo tên
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            loadDataToTable(keyword.isEmpty() ? courseDAO.getAllCourses() : courseDAO.searchCoursesByName(keyword));
        });

        // Lambda 2: Lọc theo Cấp độ (Level) đang được chọn ở ComboBox
        btnFilterLevel.addActionListener(e -> {
            String selectedLevel = cbLevel.getSelectedItem().toString();
            List<Course> result = courseDAO.filterCoursesByLevel(selectedLevel);
            loadDataToTable(result);
            JOptionPane.showMessageDialog(this, "Đã lọc khóa học có cấp độ: " + selectedLevel);
        });

        // Lambda 3: Lọc khóa học rẻ hơn mức giá nhập vào
        btnFilterFee.addActionListener(e -> {
            // Hiển thị hộp thoại để người dùng nhập mức giá tối đa
            String input = JOptionPane.showInputDialog(this, "Nhập mức học phí tối đa bạn muốn tìm (VND):", "Tìm khóa học rẻ hơn", JOptionPane.QUESTION_MESSAGE);
            if (input != null && !input.trim().isEmpty()) {
                try {
                    double maxFee = Double.parseDouble(input.trim());
                    List<Course> result = courseDAO.getCoursesCheaperThan(maxFee);
                    loadDataToTable(result);
                    if (result.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy khóa học nào có giá dưới " + maxFee + " VND");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập một số tiền hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void loadDataToTable(List<Course> courses) {
        tableModel.setRowCount(0);
        courses.forEach(c -> {
            tableModel.addRow(new Object[]{
                    c.getCourseId(), c.getCourseName(), c.getLevel(),
                    c.getDuration(), c.getFee(), c.getDescription(), c.getStatus()
            });
        });
    }

    private void clearForm() {
        txtId.setText(""); txtName.setText(""); txtDuration.setText("");
        txtFee.setText(""); txtDescription.setText(""); txtSearch.setText("");
        cbLevel.setSelectedIndex(0); cbStatus.setSelectedIndex(0);
        courseTable.clearSelection();
    }
}