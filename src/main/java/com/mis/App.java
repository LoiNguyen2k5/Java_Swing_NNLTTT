package com.mis;

import com.mis.ui.StudentFrame;
import com.mis.ui.TeacherFrame;
import com.mis.ui.ClassFrame;
import com.mis.ui.CourseFrame;
import javax.swing.SwingUtilities;
public final class App {
 public static void main(String[] args) {
        // Khởi chạy giao diện Java Swing an toàn trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Khởi tạo cửa sổ quản lý Học viên
            //StudentFrame frame = new StudentFrame();
           TeacherFrame frame = new TeacherFrame();
           // ClassFrame frame = new ClassFrame();
            //CourseFrame frame = new CourseFrame();
            // Hiển thị cửa sổ
            frame.setVisible(true);
        });
    }
}
