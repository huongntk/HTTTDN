package GUI;

import DTO.PhanQuyen;
import DTO.TaiKhoan;
import java.awt.*;
import javax.swing.*;

public class AdminFrame extends JFrame {
    private TaiKhoan taiKhoan;
    private PhanQuyen phanQuyen; // Có thể không cần dùng chi tiết, admin có toàn quyền

    private JPanel pnlMenu;
    private JPanel pnlContent;
    private CardLayout cardLayout;

    public AdminFrame(TaiKhoan tk, PhanQuyen pq) {
        this.taiKhoan = tk;
        this.phanQuyen = pq; // Có thể null, nhưng admin đặc quyền
        initComponents();
        setTitle("Quản trị hệ thống - Admin");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // ===== Menu bên trái =====
        pnlMenu = new JPanel();
        pnlMenu.setBackground(new Color(50, 50, 50));
        pnlMenu.setPreferredSize(new Dimension(220, 0));
        pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));

        // Tiêu đề chào mừng
        JLabel lblWelcome = new JLabel("Xin chào Admin", SwingConstants.CENTER);
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        pnlMenu.add(lblWelcome);

        // Các nút chức năng
        JButton btnTaiKhoan = createMenuButton("Quản lý tài khoản", "/icon/account.png");
        JButton btnPhanQuyen = createMenuButton("Phân quyền", "/icon/role.png");
        JButton btnThongKe = createMenuButton("Báo cáo tổng hợp", "/icon/report.png");
        JButton btnDangXuat = createMenuButton("Đăng xuất", "/icon/logout.png");

        pnlMenu.add(btnTaiKhoan);
        pnlMenu.add(btnPhanQuyen);
        pnlMenu.add(btnThongKe);
        pnlMenu.add(Box.createVerticalGlue()); // Đẩy nút đăng xuất xuống dưới
        pnlMenu.add(btnDangXuat);

        add(pnlMenu, BorderLayout.WEST);

        // ===== Panel nội dung với CardLayout =====
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        add(pnlContent, BorderLayout.CENTER);

        // Thêm các panel chức năng (cần đảm bảo các panel này đã tồn tại)
        pnlContent.add(new PnTaiKhoan(phanQuyen), "taiKhoan");
        // PnPhanQuyen cần nhận PhanQuyen, nhưng admin có thể truyền null và xử lý trong panel
        pnlContent.add(new PnPhanQuyen(phanQuyen), "phanQuyen");
        pnlContent.add(new PnThongKe(phanQuyen), "thongKe");

        // ===== Sự kiện các nút =====
        btnTaiKhoan.addActionListener(e -> cardLayout.show(pnlContent, "taiKhoan"));
        btnPhanQuyen.addActionListener(e -> cardLayout.show(pnlContent, "phanQuyen"));
        btnThongKe.addActionListener(e -> cardLayout.show(pnlContent, "thongKe"));
        btnDangXuat.addActionListener(e -> dangXuat());
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(70, 70, 70));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Thêm icon nếu có
        ImageIcon icon = loadScaledIcon(iconPath, 20, 20);
        if (icon != null) {
            btn.setIcon(icon);
            btn.setHorizontalTextPosition(SwingConstants.RIGHT);
            btn.setIconTextGap(10);
        }
        return btn;
    }

    private ImageIcon loadScaledIcon(String path, int w, int h) {
        try {
            java.net.URL url = getClass().getResource(path);
            if (url == null) return null;
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    private void dangXuat() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đăng xuất?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new FrmDangNhap().setVisible(true);
        }
    }
}