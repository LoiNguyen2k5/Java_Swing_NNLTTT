package com.mis.ui;

import com.mis.dao.PaymentDAO;
import com.mis.entity.Payment;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class PaymentFrame extends JFrame {
    private JTable paymentTable;
    private DefaultTableModel model;
    private JTextField txtStudentId, txtAmount, txtMinAmount;
    private JComboBox<String> cbMethod, cbStatus;
    private PaymentDAO paymentDAO = new PaymentDAO();
    private SimpleDateFormat dateTimeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PaymentFrame() {
        setTitle("Quản lý Thanh Toán");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadData(paymentDAO.getAllPayments());
    }

    private void initComponents() {
        // --- PANEL NHẬP LIỆU (NORTH) - SỬ DỤNG GRIDBAGLAYOUT ĐỂ THẲNG HÀNG ---
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin giao dịch"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20); // Khoảng cách giữa các ô
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hàng 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlInput.add(new JLabel("Mã Học Viên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        txtStudentId = new JTextField();
        pnlInput.add(txtStudentId, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.1;
        pnlInput.add(new JLabel("Số Tiền (VND):"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.4;
        txtAmount = new JTextField();
        pnlInput.add(txtAmount, gbc);

        // Hàng 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlInput.add(new JLabel("Phương Thức:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.4;
        cbMethod = new JComboBox<>(new String[]{"Cash", "Bank", "Momo", "Card"});
        pnlInput.add(cbMethod, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.1;
        pnlInput.add(new JLabel("Trạng Thái:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.4;
        cbStatus = new JComboBox<>(new String[]{"Completed", "Pending", "Failed"});
        pnlInput.add(cbStatus, gbc);

        add(pnlInput, BorderLayout.NORTH);

        // --- BẢNG (CENTER) ---
        model = new DefaultTableModel(new String[]{"ID", "Mã SV", "Số Tiền", "Ngày GD", "Phương Thức", "Trạng Thái"}, 0);
        paymentTable = new JTable(model);
        paymentTable.setRowHeight(25);
        add(new JScrollPane(paymentTable), BorderLayout.CENTER);

        // --- NÚT BẤM (SOUTH) ---
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnAdd = new JButton("Ghi nhận thanh toán");
        txtMinAmount = new JTextField(10);
        JButton btnFilter = new JButton("Lọc GD Lớn (Lambda)");

        pnlBottom.add(btnAdd);
        pnlBottom.add(new JLabel("| Lọc từ mức:"));
        pnlBottom.add(txtMinAmount);
        pnlBottom.add(btnFilter);
        add(pnlBottom, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN ---
        btnAdd.addActionListener(e -> {
            try {
                Payment p = new Payment(
                    Long.parseLong(txtStudentId.getText()),
                    new BigDecimal(txtAmount.getText()),
                    cbMethod.getSelectedItem().toString(),
                    cbStatus.getSelectedItem().toString()
                );
                paymentDAO.addPayment(p);
                loadData(paymentDAO.getAllPayments());
                JOptionPane.showMessageDialog(this, "Thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi nhập liệu!");
            }
        });

        btnFilter.addActionListener(e -> {
            try {
                double min = Double.parseDouble(txtMinAmount.getText());
                loadData(paymentDAO.getPaymentsAbove(min));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Nhập số tiền hợp lệ!");
            }
        });
    }

    private void loadData(List<Payment> list) {
        model.setRowCount(0);
        list.forEach(p -> model.addRow(new Object[]{
            p.getPaymentId(),
            p.getStudentId(),
            String.format("%,.0f", p.getAmount()),
            dateTimeFmt.format(p.getPaymentDate()), // SỬ DỤNG GETTER ĐÃ BỔ SUNG
            p.getPaymentMethod(),
            p.getStatus()
        }));
    }
}
