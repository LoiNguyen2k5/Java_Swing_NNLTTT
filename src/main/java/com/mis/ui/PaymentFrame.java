package com.mis.ui;

import com.mis.entity.Payment;
import com.mis.service.PaymentService;

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
    
    // GIAO DIỆN CHỈ GỌI DUY NHẤT 1 FILE SERVICE NÀY
    private PaymentService paymentService = new PaymentService();
    private SimpleDateFormat dateTimeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public PaymentFrame() {
        setTitle("Quản lý Thanh Toán");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponents();
        loadData(paymentService.getAllPayments());
    }

    private void initComponents() {
        // --- PANEL NHẬP LIỆU (NORTH) ---
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBorder(BorderFactory.createTitledBorder("Thông tin giao dịch"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

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
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton btnAdd = new JButton("Ghi nhận thanh toán");
        JButton btnCheckDebt = new JButton("Kiểm tra Nợ (Service/Lambda)"); 
        txtMinAmount = new JTextField(10);
        JButton btnFilter = new JButton("Lọc GD Lớn (Lambda)");

        pnlBottom.add(btnAdd);
        pnlBottom.add(btnCheckDebt);
        pnlBottom.add(new JLabel("| Lọc từ mức:"));
        pnlBottom.add(txtMinAmount);
        pnlBottom.add(btnFilter);
        add(pnlBottom, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN ---
        btnAdd.addActionListener(e -> {
            try {
                Payment p = new Payment(
                    Long.parseLong(txtStudentId.getText().trim()),
                    new BigDecimal(txtAmount.getText().trim()),
                    cbMethod.getSelectedItem().toString(),
                    cbStatus.getSelectedItem().toString()
                );
                paymentService.addPayment(p);
                loadData(paymentService.getAllPayments());
                JOptionPane.showMessageDialog(this, "Thành công!");
                txtAmount.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: Mã Học Viên và Số Tiền bắt buộc phải là số!", "Lỗi nhập liệu", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // NÚT KIỂM TRA NỢ GỌN GÀNG NHỜ CÓ SERVICE
        btnCheckDebt.addActionListener(e -> {
            String studentIdStr = txtStudentId.getText().trim();
            if (studentIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập Mã Học Viên ở ô phía trên trước khi kiểm tra nợ!");
                return;
            }
            try {
                Long studentId = Long.parseLong(studentIdStr);
                
                // Gọi thẳng các hàm đã tính sẵn từ Service
                double totalDebt = paymentService.getTotalDebt(studentId);
                double totalPaid = paymentService.getTotalPaid(studentId);
                double remainingDebt = totalDebt - totalPaid;

                String message = String.format(
                        "THỐNG KÊ TÀI CHÍNH CỦA HỌC VIÊN ID: %d\n\n" +
                        "- Tổng tiền học phí: %,.0f VND\n" +
                        "- Đã thanh toán: %,.0f VND\n" +
                        "--------------------------------------------------\n" +
                        ">> CÒN NỢ: %,.0f VND", 
                        studentId, totalDebt, totalPaid, remainingDebt);

                if (remainingDebt > 0) {
                    JOptionPane.showMessageDialog(this, message, "Thông tin Công Nợ", JOptionPane.WARNING_MESSAGE);
                    txtAmount.setText(String.format("%.0f", remainingDebt));
                } else if (remainingDebt == 0 && totalDebt > 0) {
                    JOptionPane.showMessageDialog(this, message + "\n\n(Học viên đã hoàn thành 100% học phí!)", "Thông tin", JOptionPane.INFORMATION_MESSAGE);
                    txtAmount.setText("");
                } else if (totalDebt == 0) {
                    JOptionPane.showMessageDialog(this, "Học viên này chưa đăng ký lớp nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, message + "\n\n(Học viên nộp dư tiền)", "Thông tin", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Mã Học Viên phải là số!");
            }
        });

        btnFilter.addActionListener(e -> {
            try {
                double min = Double.parseDouble(txtMinAmount.getText().trim());
                loadData(paymentService.getPaymentsAbove(min)); // Gọi Service
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Nhập số tiền hợp lệ!");
            }
        });
    }

    private void loadData(List<Payment> list) {
        model.setRowCount(0);
        list.forEach(p -> model.addRow(new Object[]{
            p.getPaymentId(), p.getStudentId(), String.format("%,.0f", p.getAmount()),
            dateTimeFmt.format(p.getPaymentDate()), p.getPaymentMethod(), p.getStatus()
        }));
    }
}