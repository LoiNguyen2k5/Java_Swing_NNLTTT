package com.mis.ui;

import com.mis.dao.ScheduleDAO;
import com.mis.entity.Schedule;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.List;

public class ScheduleFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtClassId, txtDate, txtStartTime, txtEndTime, txtSearch;
    private ScheduleDAO dao = new ScheduleDAO();
    private SimpleDateFormat dateFmt = new SimpleDateFormat("yyyy-MM-dd");

    public ScheduleFrame() {
        setTitle("Quản lý Lịch học");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadData(dao.getAll());
    }

    private void initComponents() {
        // Panel nhập liệu
        JPanel pnlInput = new JPanel(new GridLayout(2, 4, 10, 10));
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin lịch học"));

        pnlInput.add(new JLabel("Mã Lớp:"));
        txtClassId = new JTextField(); pnlInput.add(txtClassId);

        pnlInput.add(new JLabel("Ngày (yyyy-MM-dd):"));
        txtDate = new JTextField(); pnlInput.add(txtDate);

        // Thay thế TimeField bị lỗi bằng JTextField tiêu chuẩn
        pnlInput.add(new JLabel("Bắt đầu (HH:mm:ss):"));
        txtStartTime = new JTextField(); pnlInput.add(txtStartTime);

        pnlInput.add(new JLabel("Kết thúc (HH:mm:ss):"));
        txtEndTime = new JTextField(); pnlInput.add(txtEndTime);

        add(pnlInput, BorderLayout.NORTH);

        // Bảng dữ liệu
        model = new DefaultTableModel(new String[]{"ID", "Lớp", "Ngày học", "Bắt đầu", "Kết thúc"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel chức năng Lambda
        JPanel pnlSouth = new JPanel(new FlowLayout());
        JButton btnAdd = new JButton("Thêm lịch học");
        txtSearch = new JTextField(10);
        JButton btnFilter = new JButton("Lọc theo lớp (Lambda)");

        pnlSouth.add(btnAdd);
        pnlSouth.add(new JLabel("Mã lớp:")); pnlSouth.add(txtSearch);
        pnlSouth.add(btnFilter);
        add(pnlSouth, BorderLayout.SOUTH);

        // Xử lý sự kiện thêm mới
        btnAdd.addActionListener(e -> {
            try {
                // Chuyển đổi từ chuỗi nhập vào sang kiểu Time và Date
                Schedule s = new Schedule(
                    Long.parseLong(txtClassId.getText()),
                    dateFmt.parse(txtDate.getText()),
                    Time.valueOf(txtStartTime.getText()), // Định dạng yêu cầu HH:mm:ss
                    Time.valueOf(txtEndTime.getText())
                );
                dao.saveOrUpdate(s);
                JOptionPane.showMessageDialog(this, "Lưu lịch học thành công!");
                loadData(dao.getAll());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Kiểm tra định dạng yyyy-MM-dd và HH:mm:ss");
            }
        });

        // Xử lý lọc Lambda
        btnFilter.addActionListener(e -> {
            try {
                Long cId = Long.parseLong(txtSearch.getText());
                loadData(dao.filterByClass(cId));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã lớp hợp lệ!");
            }
        });
    }

    private void loadData(List<Schedule> list) {
        model.setRowCount(0);
        // Duyệt danh sách bằng Lambda forEach
        list.forEach(s -> model.addRow(new Object[]{
            s.getScheduleId(), s.getClassId(), dateFmt.format(s.getStudyDate()), s.getStartTime(), s.getEndTime()
        }));
    }
}
