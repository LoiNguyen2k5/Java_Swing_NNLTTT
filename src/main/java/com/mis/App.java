package com.mis;

import com.mis.ui.*; // Nhập tất cả giao diện từ package ui
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public final class App extends JFrame {

    public App() {
        // Cấu hình cửa sổ Dashboard chính
        setTitle("Dashboard - Hệ Thống Quản Lý Trung Tâm Ngoại Ngữ");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Thoát app khi đóng menu chính
        setLocationRelativeTo(null); // Hiển thị giữa màn hình
        setLayout(new BorderLayout(15, 15));

        // --- TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ ĐÀO TẠO MIS", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 51, 153)); // Màu xanh chuyên nghiệp
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- PANEL CHỨC NĂNG CHÍNH ---
        JPanel pnlMain = new JPanel(new GridLayout(1, 2, 20, 20));
        pnlMain.setBorder(BorderFactory.createEmptyBorder(10, 30, 30, 30));

        // Nhóm 1: Quản lý đối tượng (Module của Lợi)
        pnlMain.add(createModuleGroup("QUẢN LÝ DANH MỤC", new String[]{
            "Học Viên", "Giáo Viên", "Khóa Học", "Lớp Học"
        }, new ActionListener[]{
            e -> new StudentFrame().setVisible(true),
            e -> new TeacherFrame().setVisible(true),
            e -> new CourseFrame().setVisible(true),
            e -> new ClassFrame().setVisible(true)
        }));

        // Nhóm 2: Quản lý vận hành (Module của Quang)
        pnlMain.add(createModuleGroup("QUẢN LÝ VẬN HÀNH", new String[]{
            "Đăng Ký Học", "Lịch Học", "Thanh Toán", "Điểm Danh"
        }, new ActionListener[]{
            e -> new EnrollmentFrame().setVisible(true),
            e -> new ScheduleFrame().setVisible(true),
            e -> new PaymentFrame().setVisible(true),
            e -> new AttendanceFrame().setVisible(true)
        }));

        add(pnlMain, BorderLayout.CENTER);

        // --- FOOTER ---
        JLabel lblFooter = new JLabel("Phát triển bởi: Lợi & Quang - HCMUTE 2026", JLabel.RIGHT);
        lblFooter.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 20));
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 11));
        add(lblFooter, BorderLayout.SOUTH);
    }

    /**
     * Hàm tạo nhóm module trực quan
     */
    private JPanel createModuleGroup(String title, String[] btnTexts, ActionListener[] actions) {
        JPanel pnl = new JPanel(new GridLayout(4, 1, 10, 10));
        pnl.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), title, 0, 0, new Font("Arial", Font.BOLD, 14)));

        for (int i = 0; i < btnTexts.length; i++) {
            JButton btn = new JButton(btnTexts[i]);
            btn.setFont(new Font("Arial", Font.PLAIN, 14));
            btn.setFocusPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.addActionListener(actions[i]); // Gán sự kiện mở Frame
            pnl.add(btn);
        }
        return pnl;
    }

    public static void main(String[] args) {
        // Chạy giao diện an toàn trên luồng Swing
        SwingUtilities.invokeLater(() -> {
            try {
                // Sử dụng giao diện hệ thống cho đẹp mắt
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new App().setVisible(true); // Hiển thị Dashboard
        });
    }
}
