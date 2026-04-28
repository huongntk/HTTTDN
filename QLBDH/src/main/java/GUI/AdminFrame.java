package GUI;

import DTO.PhanQuyen;
import DTO.TaiKhoan;
import java.awt.*;
import javax.swing.*;
import java.util.*;

public class AdminFrame extends JFrame {

    private TaiKhoan taiKhoan;
    private PhanQuyen phanQuyen; // Có thể không cần dùng chi tiết, admin có toàn quyền

    private JPanel pnlMenu;
    private JPanel pnlContent;
    private CardLayout cardLayout;
    private ArrayList<JButton> menuButtons = new ArrayList<>();

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
        pnlMenu.setBackground(new Color(0, 153, 153));
        pnlMenu.setPreferredSize(new Dimension(220, 0));
        pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));

        // Tiêu đề chào mừng
        JLabel lblWelcome = new JLabel("Xin chào Admin", SwingConstants.CENTER);
        lblWelcome.setForeground(Color.ORANGE);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        pnlMenu.add(lblWelcome);

        // Các nút chức năng
        JButton btnNhanSu = createMenuButton("Quản lý nhân sự", "/icon/multiple-users.png");
        JButton btnKho = createMenuButton("Quản lý kho", "/icon/boxes.png");
        JButton btnBanHang = createMenuButton("Quản lý bán hàng", "/icon/pay.png");
        JButton btnTaiKhoan = createMenuButton("Quản lý tài khoản", "/icon/account.png");
        JButton btnSanPham = createMenuButton("Quản lý sản phẩm", "/icon/watch.png");
        JButton btnKhachHang = createMenuButton("Quản lý khách hàng", "/icon/user.png");
        JButton btnGiamGia = createMenuButton("Quản lý khuyến mãi", "/icon/tag.png");
        JButton btnPhanQuyen = createMenuButton("Phân quyền", "/icon/settings.png");
        JButton btnThongKe = createMenuButton("Báo cáo tổng hợp", "/icon/bar-char.png");
        JButton btnDoiMatKhau = createMenuButton("Đổi mật khẩu", "/icon/undo.png");
        JButton btnDangXuat = createMenuButton("Đăng xuất", "/icon/logout.png");

        pnlMenu.add(btnNhanSu);
        pnlMenu.add(btnKho);
        pnlMenu.add(btnBanHang);
        pnlMenu.add(btnTaiKhoan);
        pnlMenu.add(btnSanPham);
        pnlMenu.add(btnKhachHang);
        pnlMenu.add(btnGiamGia);
        pnlMenu.add(btnPhanQuyen);
        pnlMenu.add(btnThongKe);
        pnlMenu.add(Box.createVerticalGlue()); // Đẩy nút đăng xuất xuống 
        pnlMenu.add(btnDoiMatKhau);
        pnlMenu.add(btnDangXuat);

        add(pnlMenu, BorderLayout.WEST);

        // ===== Panel nội dung với CardLayout =====
        cardLayout = new CardLayout();
        pnlContent = new JPanel(cardLayout);
        add(pnlContent, BorderLayout.CENTER);

        // Thêm các panel chức năng (cần đảm bảo các panel này đã tồn tại)
        pnlContent.add(new PnNhanVien(phanQuyen), "nhanSu");
        pnlContent.add(new PnNhapHang(phanQuyen), "kho");
        pnlContent.add(new PnBanHang(phanQuyen), "banHang");
        pnlContent.add(new PnTaiKhoan(phanQuyen), "taiKhoan");
         pnlContent.add(new PnSanPham(phanQuyen), "sanPham");
        pnlContent.add(new PnKhachHang(phanQuyen), "khachHang");
        pnlContent.add(new PnKhuyenMai(phanQuyen), "giamGia");
        // PnPhanQuyen cần nhận PhanQuyen, nhưng admin có thể truyền null và xử lý trong panel
        pnlContent.add(new PnPhanQuyen(phanQuyen), "phanQuyen");
        pnlContent.add(new PnThongKe(phanQuyen), "thongKe");

        // ===== Sự kiện các nút =====
        // Thêm các nút chuyển tab vào danh sách để quản lý
        menuButtons.add(btnNhanSu);
        menuButtons.add(btnKho);
        menuButtons.add(btnBanHang);
        menuButtons.add(btnTaiKhoan);
        menuButtons.add(btnSanPham);
        menuButtons.add(btnKhachHang);
        menuButtons.add(btnGiamGia);
        menuButtons.add(btnPhanQuyen);
        menuButtons.add(btnThongKe);

        // Bắt sự kiện chuyển Panel và gọi hàm làm nổi bật nút
        btnNhanSu.addActionListener(e -> {
            cardLayout.show(pnlContent, "nhanSu");
            setActiveMenuButton(btnNhanSu);
        });
        btnKho.addActionListener(e -> {
            cardLayout.show(pnlContent, "kho");
            setActiveMenuButton(btnKho);
        });
        btnBanHang.addActionListener(e -> {
            cardLayout.show(pnlContent, "banHang");
            setActiveMenuButton(btnBanHang);
        });
        btnTaiKhoan.addActionListener(e -> {
            cardLayout.show(pnlContent, "taiKhoan");
            setActiveMenuButton(btnTaiKhoan);
        });
        btnSanPham.addActionListener(e -> {
            cardLayout.show(pnlContent, "sanPham");
            setActiveMenuButton(btnSanPham);
        });
        btnKhachHang.addActionListener(e -> {
            cardLayout.show(pnlContent, "khachHang");
            setActiveMenuButton(btnKhachHang);
        });
        btnGiamGia.addActionListener(e -> {
            cardLayout.show(pnlContent, "giamGia");
            setActiveMenuButton(btnGiamGia);
        });
        btnPhanQuyen.addActionListener(e -> {
            cardLayout.show(pnlContent, "phanQuyen");
            setActiveMenuButton(btnPhanQuyen);
        });
        btnThongKe.addActionListener(e -> {
            cardLayout.show(pnlContent, "thongKe");
            setActiveMenuButton(btnThongKe);
        });

        btnDoiMatKhau.addActionListener(e -> doiMatKhau());
        btnDangXuat.addActionListener(e -> dangXuat());

        // Đặt trạng thái active mặc định cho tab "Quản lý nhân sự" khi vừa mở Frame
        setActiveMenuButton(btnNhanSu);
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 153, 153));
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
            if (url == null) {
                return null;
            }
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    private void doiMatKhau() {
        if (this.taiKhoan == null) {
            JOptionPane.showMessageDialog(this, "Không có thông tin tài khoản hiện tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Khởi tạo FrmDoiMatKhau Dialog
        // FrmDoiMatKhau cần constructor: (java.awt.Frame parent, boolean modal, TaiKhoan tk)
        GUI.FrmDoiMatKhau doiMatKhauDialog = new GUI.FrmDoiMatKhau(this, true, this.taiKhoan);

        // 3. Hiển thị Dialog
        doiMatKhauDialog.setVisible(true);
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

    private void setActiveMenuButton(JButton activeButton) {
        for (JButton btn : menuButtons) {
            if (btn == activeButton) {
                // Đổi màu nền (ví dụ: xanh dương nhạt) và đóng khung viền trắng dày 2px
                btn.setBackground(new Color(151, 255, 255));
                btn.setFont(btn.getFont().deriveFont(java.awt.Font.BOLD, 15f));
                btn.setForeground(java.awt.Color.BLACK);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.BLACK, 2),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            } else {
                // Trả về màu nền  và bỏ khung viền
                btn.setBackground(new Color(0, 153, 153));
                btn.setForeground(java.awt.Color.WHITE);
                btn.setFont(btn.getFont().deriveFont(java.awt.Font.PLAIN, 15f));
                btn.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            }
        }
    }
}
