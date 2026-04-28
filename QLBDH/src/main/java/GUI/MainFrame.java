/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import DTO.PhanQuyen;
import DTO.TaiKhoan;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import GUI.PnChamCong;

public class MainFrame extends javax.swing.JFrame {

    private TaiKhoan taiKhoan;
    private PhanQuyen phanQuyen;
    // Biến lưu nút hiện đang được chọn
    private JButton selectedButton = null;
    private ArrayList<JButton> menuButtons = new ArrayList<>();
    private JButton btnLichLamViec;
    private JButton btnCaLam;

    public MainFrame(TaiKhoan tk, PhanQuyen pq) {
        setTitle("Quản lý Bán Đồng Hồ Cơ");
        initComponents();
        initLichLamViecButton();
        menuButtons.add(btnBanHang);
        menuButtons.add(btnSanPham);
        menuButtons.add(btnNhanVien);
        menuButtons.add(btnKhachHang);
        menuButtons.add(btnNhapHang);
        menuButtons.add(btnThongKe);
        menuButtons.add(btnPhanQuyen);
        menuButtons.add(btnKhuyenMai);
        menuButtons.add(btnQuanLyLuong);
        menuButtons.add(btnLichLamViec);
        menuButtons.add(btnChamCong);
        menuButtons.add(btnDuyetNghi);

        setExtendedState(MAXIMIZED_BOTH); // Mở toàn màn hình
        setLocationRelativeTo(null);             // Căn giữa
        this.taiKhoan = tk;
        this.phanQuyen = pq;
        txtaccount.setText(tk.getTaiKhoan());
        loadMenuByRole();

        // KÍCH HOẠT CHỨC NĂNG ĐẦU TIÊN
        openFirstAccessiblePanel();

    }

    private void initLichLamViecButton() {
        btnLichLamViec = new javax.swing.JButton();

        btnLichLamViec.setBackground(new java.awt.Color(0, 204, 204));
        btnLichLamViec.setFont(new java.awt.Font("Segoe UI", 1, 14));

        // Dùng tạm icon clock vì project đã có icon này.
        // Nếu sau này có icon calendar.png thì đổi lại đường dẫn icon.
        btnLichLamViec.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/calendar.png")));

        btnLichLamViec.setText("Lịch làm việc");
        btnLichLamViec.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnLichLamViec.setBorderPainted(false);
        btnLichLamViec.setContentAreaFilled(false);
        btnLichLamViec.setHideActionText(true);
        btnLichLamViec.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnLichLamViec.setMaximumSize(new java.awt.Dimension(200, 45));
        btnLichLamViec.setPreferredSize(new java.awt.Dimension(200, 30));
        btnLichLamViec.setSelected(true);

        btnLichLamViec.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLichLamViecActionPerformed(evt);
            }
        });
        btnCaLam = new JButton();
        btnCaLam.setBackground(new java.awt.Color(0, 204, 204));
        btnCaLam.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnCaLam.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/working.png")));
        btnCaLam.setText("Ca l\u00e0m");
        btnCaLam.setBorderPainted(false);
        btnCaLam.setContentAreaFilled(false);
        btnCaLam.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnCaLam.setMaximumSize(new java.awt.Dimension(200, 45));
        btnCaLam.setPreferredSize(new java.awt.Dimension(200, 30));
        btnCaLam.addActionListener(e -> {
            pnContent.removeAll();
            PnCaLam pn = new PnCaLam(taiKhoan);
            pnContent.add(pn);
            pnContent.revalidate();
            pnContent.repaint();
            setButtonActive(btnCaLam);
        });
        menuButtons.add(btnCaLam);
        pnMenu.add(btnCaLam);
        ;

        // Chèn nút Lịch làm việc trước nút Chấm công
        pnMenu.remove(btnChamCong);
        pnMenu.add(btnLichLamViec);
        pnMenu.add(btnChamCong);

        pnMenu.revalidate();
        pnMenu.repaint();
    }

    private void btnLichLamViecActionPerformed(java.awt.event.ActionEvent evt) {
        pnContent.removeAll();

        PnLichLamViec pn = new PnLichLamViec();

        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();

        setButtonActive(btnLichLamViec);
    }

    public void loadMenuByRole() {
        // 1. Kiểm tra nếu đối tượng phanQuyen chưa được load từ DB
        if (this.phanQuyen == null) {
            JOptionPane.showMessageDialog(this, "Không thể tải thông tin phân quyền!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Phân quyền cho nhóm Nhân sự (Sử dụng các thuộc tính mới trong PhanQuyen.java)
        btnNhanVien.setVisible(phanQuyen.isNsXemDanhSach() || phanQuyen.isNsXemLuongCaNhan() || phanQuyen.isNsTaoDonNghi());

        // . Phân quyền cho nhóm Bán hàng
        // Nếu có quyền lập phiếu xuất thì hiện nút Bán Hàng
        btnBanHang.setVisible(phanQuyen.isBhLapPhieuXuat() || phanQuyen.isBhThongKeDoanhThu() || phanQuyen.isBhThongKeLoiNhuan());

        // 4. Phân quyền cho nhóm Kho
        // Cho phép hiện nút Nhập hàng nếu có quyền Nhập hàng hoặc Quản lý NCC
        boolean isKhoAccess = phanQuyen.isKhoNhapHang() || phanQuyen.isKhoQuanLyNCC() && phanQuyen.isKhoXemSanPham();
        btnNhapHang.setVisible(isKhoAccess);
        btnSanPham.setVisible(phanQuyen.isKhoXemSanPham());

        // 5. Phân quyền cho nhóm Khách hàng & Khuyến mãi
        // (Nếu bạn chưa tách chi tiết các quyền này, có thể dùng các thuộc tính cũ hoặc mặc định)

        btnKhachHang.setVisible(phanQuyen.isQLKhachHang());
        btnKhuyenMai.setVisible(phanQuyen.isQLKhuyenMai());
        // 6. Phân quyền hệ thống (Admin)
        btnPhanQuyen.setVisible(phanQuyen.isPqQuanLyPhanQuyen());
        btnThongKe.setVisible(phanQuyen.isAdminBaoCaoTongHop() || phanQuyen.isBhThongKeDoanhThu() || phanQuyen.isBhThongKeLoiNhuan() || phanQuyen.isKhoBaoCaoTonKho());
        // Quản lý lương: hiển thị nếu có bất kỳ quyền nào về lương
        btnQuanLyLuong.setVisible(phanQuyen.isNsXemLuongCaNhan() && phanQuyen.isNsTinhLuong() && phanQuyen.isNsInBangLuong());
        btnCaLam.setVisible(phanQuyen.isNsXemLuongCaNhan());
        btnLichLamViec.setVisible(phanQuyen.isNsXemLuongCaNhan() && phanQuyen.isNsTinhLuong() && phanQuyen.isNsInBangLuong());
        boolean isAdmin = taiKhoan.getMaQuyen().equals("ADMIN");
        btnDuyetNghi.setVisible(phanQuyen.isNsDuyetNghi() && !isAdmin);
        btnChamCong.setVisible(phanQuyen.isNsChamCong());

        // 7. Refresh lại giao diện thanh menu sau khi ẩn/hiện các nút
        pnMenu.revalidate();
        pnMenu.repaint();
    }

    public TaiKhoan getTaiKhoan() {
        return this.taiKhoan;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnContent = new javax.swing.JPanel();
        pnSidebar = new javax.swing.JPanel();
        pnMenu = new javax.swing.JPanel();
        txtaccount = new javax.swing.JTextField();
        btnBanHang = new javax.swing.JButton();
        btnSanPham = new javax.swing.JButton();
        btnKhachHang = new javax.swing.JButton();
        btnNhapHang = new javax.swing.JButton();
        btnNhanVien = new javax.swing.JButton();
        btnPhanQuyen = new javax.swing.JButton();
        btnKhuyenMai = new javax.swing.JButton();
        btnThongKe = new javax.swing.JButton();
        btnQuanLyLuong = new javax.swing.JButton();
        btnDuyetNghi = new javax.swing.JButton();
        btnChamCong = new javax.swing.JButton();
        pnBottomMenu = new javax.swing.JPanel();
        btnDangXuat = new javax.swing.JButton();
        btnDoiMatKhau = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Quản lý bán điện thoại\n");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        pnContent.setLayout(new java.awt.CardLayout());
        getContentPane().add(pnContent, java.awt.BorderLayout.CENTER);

        pnSidebar.setForeground(new java.awt.Color(102, 204, 255));
        pnSidebar.setLayout(new java.awt.BorderLayout());

        pnMenu.setBackground(new java.awt.Color(170, 222, 215));
        pnMenu.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 10, 20, 10));
        pnMenu.setForeground(new java.awt.Color(204, 255, 204));
        pnMenu.setPreferredSize(new java.awt.Dimension(220, 280));
        pnMenu.setLayout(new javax.swing.BoxLayout(pnMenu, javax.swing.BoxLayout.Y_AXIS));

        txtaccount.setEditable(false);
        txtaccount.setBackground(new java.awt.Color(204, 255, 255));
        txtaccount.setFont(new java.awt.Font("Script MT Bold", 1, 24)); // NOI18N
        txtaccount.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtaccount.setFocusable(false);
        txtaccount.setMaximumSize(new java.awt.Dimension(2290, 55));
        txtaccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtaccountActionPerformed(evt);
            }
        });
        pnMenu.add(txtaccount);

        btnBanHang.setBackground(new java.awt.Color(0, 204, 204));
        btnBanHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBanHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/pay.png"))); // NOI18N
        btnBanHang.setText("Quản lý bán hàng");
        btnBanHang.setToolTipText("");
        btnBanHang.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        btnBanHang.setBorderPainted(false);
        btnBanHang.setContentAreaFilled(false);
        btnBanHang.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnBanHang.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnBanHang.setMaximumSize(new java.awt.Dimension(200, 45));
        btnBanHang.setPreferredSize(new java.awt.Dimension(200, 30));
        btnBanHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBanHangActionPerformed(evt);
            }
        });
        pnMenu.add(btnBanHang);

        btnSanPham.setBackground(new java.awt.Color(0, 204, 204));
        btnSanPham.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/watch.png"))); // NOI18N
        btnSanPham.setText("Quản lý sản phẩm");
        btnSanPham.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnSanPham.setBorderPainted(false);
        btnSanPham.setContentAreaFilled(false);
        btnSanPham.setHideActionText(true);
        btnSanPham.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSanPham.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnSanPham.setIconTextGap(10);
        btnSanPham.setMaximumSize(new java.awt.Dimension(200, 45));
        btnSanPham.setMinimumSize(new java.awt.Dimension(124, 23));
        btnSanPham.setPreferredSize(new java.awt.Dimension(200, 30));
        btnSanPham.setSelected(true);
        btnSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSanPhamActionPerformed(evt);
            }
        });
        pnMenu.add(btnSanPham);

        btnKhachHang.setBackground(new java.awt.Color(0, 204, 204));
        btnKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnKhachHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/multiple-users.png"))); // NOI18N
        btnKhachHang.setText("Quản lý khách hàng");
        btnKhachHang.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnKhachHang.setBorderPainted(false);
        btnKhachHang.setContentAreaFilled(false);
        btnKhachHang.setHideActionText(true);
        btnKhachHang.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnKhachHang.setMaximumSize(new java.awt.Dimension(200, 45));
        btnKhachHang.setMinimumSize(new java.awt.Dimension(124, 23));
        btnKhachHang.setPreferredSize(new java.awt.Dimension(200, 30));
        btnKhachHang.setSelected(true);
        btnKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhachHangActionPerformed(evt);
            }
        });
        pnMenu.add(btnKhachHang);

        btnNhapHang.setBackground(new java.awt.Color(0, 204, 204));
        btnNhapHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNhapHang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/boxes.png"))); // NOI18N
        btnNhapHang.setText("Quản lý nhập hàng");
        btnNhapHang.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnNhapHang.setBorderPainted(false);
        btnNhapHang.setContentAreaFilled(false);
        btnNhapHang.setHideActionText(true);
        btnNhapHang.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnNhapHang.setMaximumSize(new java.awt.Dimension(200, 45));
        btnNhapHang.setPreferredSize(new java.awt.Dimension(200, 30));
        btnNhapHang.setSelected(true);
        btnNhapHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhapHangActionPerformed(evt);
            }
        });
        pnMenu.add(btnNhapHang);

        btnNhanVien.setBackground(new java.awt.Color(0, 204, 204));
        btnNhanVien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/user.png"))); // NOI18N
        btnNhanVien.setText("Quản lý nhân viên");
        btnNhanVien.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnNhanVien.setBorderPainted(false);
        btnNhanVien.setContentAreaFilled(false);
        btnNhanVien.setHideActionText(true);
        btnNhanVien.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnNhanVien.setMaximumSize(new java.awt.Dimension(200, 45));
        btnNhanVien.setPreferredSize(new java.awt.Dimension(200, 30));
        btnNhanVien.setSelected(true);
        btnNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNhanVienActionPerformed(evt);
            }
        });
        pnMenu.add(btnNhanVien);

        btnPhanQuyen.setBackground(new java.awt.Color(0, 204, 204));
        btnPhanQuyen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPhanQuyen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/settings.png"))); // NOI18N
        btnPhanQuyen.setText("Quản lý phân quyền");
        btnPhanQuyen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnPhanQuyen.setBorderPainted(false);
        btnPhanQuyen.setContentAreaFilled(false);
        btnPhanQuyen.setHideActionText(true);
        btnPhanQuyen.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnPhanQuyen.setMaximumSize(new java.awt.Dimension(200, 45));
        btnPhanQuyen.setPreferredSize(new java.awt.Dimension(200, 30));
        btnPhanQuyen.setSelected(true);
        btnPhanQuyen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPhanQuyenActionPerformed(evt);
            }
        });
        pnMenu.add(btnPhanQuyen);

        btnKhuyenMai.setBackground(new java.awt.Color(0, 204, 204));
        btnKhuyenMai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnKhuyenMai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/tag.png"))); // NOI18N
        btnKhuyenMai.setText("Quản lý khuyến mãi");
        btnKhuyenMai.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnKhuyenMai.setBorderPainted(false);
        btnKhuyenMai.setContentAreaFilled(false);
        btnKhuyenMai.setHideActionText(true);
        btnKhuyenMai.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnKhuyenMai.setMaximumSize(new java.awt.Dimension(200, 45));
        btnKhuyenMai.setPreferredSize(new java.awt.Dimension(200, 30));
        btnKhuyenMai.setSelected(true);
        btnKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhuyenMaiActionPerformed(evt);
            }
        });
        pnMenu.add(btnKhuyenMai);

        btnThongKe.setBackground(new java.awt.Color(0, 204, 204));
        btnThongKe.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/bar-char.png"))); // NOI18N
        btnThongKe.setText("Quản lý thống kê");
        btnThongKe.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnThongKe.setBorderPainted(false);
        btnThongKe.setContentAreaFilled(false);
        btnThongKe.setHideActionText(true);
        btnThongKe.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnThongKe.setMaximumSize(new java.awt.Dimension(200, 45));
        btnThongKe.setPreferredSize(new java.awt.Dimension(200, 30));
        btnThongKe.setSelected(true);
        btnThongKe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongKeActionPerformed(evt);
            }
        });
        pnMenu.add(btnThongKe);

        btnQuanLyLuong.setBackground(new java.awt.Color(0, 204, 204));
        btnQuanLyLuong.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuanLyLuong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/money.png"))); // NOI18N
        btnQuanLyLuong.setText("Quản lý lương");
        btnQuanLyLuong.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnQuanLyLuong.setBorderPainted(false);
        btnQuanLyLuong.setContentAreaFilled(false);
        btnQuanLyLuong.setHideActionText(true);
        btnQuanLyLuong.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnQuanLyLuong.setMaximumSize(new java.awt.Dimension(200, 45));
        btnQuanLyLuong.setPreferredSize(new java.awt.Dimension(200, 30));
        btnQuanLyLuong.setSelected(true);
        btnQuanLyLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuanLyLuongActionPerformed(evt);
            }
        });
        pnMenu.add(btnQuanLyLuong);

        btnDuyetNghi.setBackground(new java.awt.Color(0, 204, 204));
        btnDuyetNghi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDuyetNghi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/xacnhan.png"))); // NOI18N
        btnDuyetNghi.setText("Duyệt nghỉ");
        btnDuyetNghi.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnDuyetNghi.setBorderPainted(false);
        btnDuyetNghi.setContentAreaFilled(false);
        btnDuyetNghi.setHideActionText(true);
        btnDuyetNghi.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnDuyetNghi.setMaximumSize(new java.awt.Dimension(200, 45));
        btnDuyetNghi.setPreferredSize(new java.awt.Dimension(200, 30));
        btnDuyetNghi.setSelected(true);
        btnDuyetNghi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDuyetNghiActionPerformed(evt);
            }
        });
        pnMenu.add(btnDuyetNghi);

        btnChamCong.setBackground(new java.awt.Color(0, 204, 204));
        btnChamCong.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnChamCong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clock.png"))); // NOI18N
        btnChamCong.setText("Chấm công");
        btnChamCong.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btnChamCong.setBorderPainted(false);
        btnChamCong.setContentAreaFilled(false);
        btnChamCong.setHideActionText(true);
        btnChamCong.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnChamCong.setMaximumSize(new java.awt.Dimension(200, 45));
        btnChamCong.setPreferredSize(new java.awt.Dimension(200, 30));
        btnChamCong.setSelected(true);
        btnChamCong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChamCongActionPerformed(evt);
            }
        });
        pnMenu.add(btnChamCong);

        pnSidebar.add(pnMenu, java.awt.BorderLayout.CENTER);

        pnBottomMenu.setLayout(new java.awt.GridLayout(2, 1, 10, 10));

        btnDangXuat.setBackground(new java.awt.Color(0, 204, 204));
        btnDangXuat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDangXuat.setForeground(new java.awt.Color(255, 255, 255));
        btnDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/logout_2.png"))); // NOI18N
        btnDangXuat.setText("Đăng xuất");
        btnDangXuat.setBorderPainted(false);
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.setMaximumSize(new java.awt.Dimension(70, 27));
        btnDangXuat.setMinimumSize(new java.awt.Dimension(70, 27));
        btnDangXuat.setPreferredSize(new java.awt.Dimension(70, 30));
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });
        pnBottomMenu.add(btnDangXuat);

        btnDoiMatKhau.setBackground(new java.awt.Color(0, 204, 204));
        btnDoiMatKhau.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDoiMatKhau.setForeground(new java.awt.Color(255, 255, 255));
        btnDoiMatKhau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/undo.png"))); // NOI18N
        btnDoiMatKhau.setText("Đổi mật khẩu");
        btnDoiMatKhau.setBorderPainted(false);
        btnDoiMatKhau.setFocusPainted(false);
        btnDoiMatKhau.setPreferredSize(new java.awt.Dimension(110, 30));
        btnDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiMatKhauActionPerformed(evt);
            }
        });
        pnBottomMenu.add(btnDoiMatKhau);

        pnSidebar.add(pnBottomMenu, java.awt.BorderLayout.SOUTH);

        getContentPane().add(pnSidebar, java.awt.BorderLayout.WEST);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void openFirstAccessiblePanel() {
        if (phanQuyen == null) {
            return;
        }

        // Tùy chọn 1: Ưu tiên mở chức năng Bán Hàng
        if (phanQuyen.isBhLapPhieuXuat()) {
            btnBanHang.doClick();
        } else if (phanQuyen.isKhoXemSanPham()) {
            btnSanPham.doClick();
        } else if (phanQuyen.isKhoNhapHang() || phanQuyen.isKhoQuanLyNCC()) {
            btnNhapHang.doClick();
        } else if (phanQuyen.isNsXemDanhSach()) {
            btnNhanVien.doClick();
        } else if (phanQuyen.isQLKhachHang()) {
            btnKhachHang.doClick();
        } else if (phanQuyen.isQLKhuyenMai()) {
            btnKhuyenMai.doClick();
        } else if (phanQuyen.isPqQuanLyPhanQuyen()) {
            btnPhanQuyen.doClick();
        } else if (phanQuyen.isAdminBaoCaoTongHop()) {
            btnThongKe.doClick();
        }

        // Nếu không có quyền nào (rất hiếm):
        // JOptionPane.showMessageDialog(this, "Tài khoản không có quyền truy cập chức năng nào.", "Thông báo", JOptionPane.WARNING_MESSAGE);
    }
    private void btnDoiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiMatKhauActionPerformed
        // 1. Kiểm tra đối tượng tài khoản hiện tại
        if (this.taiKhoan == null) {
            JOptionPane.showMessageDialog(this, "Không có thông tin tài khoản hiện tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Khởi tạo FrmDoiMatKhau Dialog
        // FrmDoiMatKhau cần constructor: (java.awt.Frame parent, boolean modal, TaiKhoan tk)
        GUI.FrmDoiMatKhau doiMatKhauDialog = new GUI.FrmDoiMatKhau(this, true, this.taiKhoan);

        // 3. Hiển thị Dialog
        doiMatKhauDialog.setVisible(true);
    }//GEN-LAST:event_btnDoiMatKhauActionPerformed

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đăng xuất không?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new FrmDangNhap().setVisible(true);
        }
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
//        // TODO add your handling code here:
        pnContent.removeAll();
        PnThongKe pn = new PnThongKe(phanQuyen);
        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnThongKe);
    }//GEN-LAST:event_btnThongKeActionPerformed

    private void btnKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhuyenMaiActionPerformed
        pnContent.removeAll();
        PnKhuyenMai pn = new PnKhuyenMai(phanQuyen);
        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnKhuyenMai);
    }//GEN-LAST:event_btnKhuyenMaiActionPerformed

    private void btnPhanQuyenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhanQuyenActionPerformed
        pnContent.removeAll();
        PnPhanQuyen pn = new PnPhanQuyen(phanQuyen);
        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnPhanQuyen);
    }//GEN-LAST:event_btnPhanQuyenActionPerformed

    private void btnNhapHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhapHangActionPerformed
        pnContent.removeAll();
        PnNhapHang pn = new PnNhapHang(phanQuyen);
        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnNhapHang);
    }//GEN-LAST:event_btnNhapHangActionPerformed

    private void btnNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhanVienActionPerformed
        pnContent.removeAll();
        PnNhanVien pn = new PnNhanVien(phanQuyen);
        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnNhanVien);
    }//GEN-LAST:event_btnNhanVienActionPerformed

    private void btnKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhachHangActionPerformed
        pnContent.removeAll();
        PnKhachHang pn = new PnKhachHang(phanQuyen);
        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnKhachHang);
    }//GEN-LAST:event_btnKhachHangActionPerformed

    private void btnSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSanPhamActionPerformed
        pnContent.removeAll();
        PnSanPham pn = new PnSanPham(phanQuyen);
        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnSanPham);
    }//GEN-LAST:event_btnSanPhamActionPerformed

    private void btnBanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBanHangActionPerformed
        pnContent.removeAll();
        PnBanHang pn = new PnBanHang(phanQuyen);
        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnBanHang);
    }//GEN-LAST:event_btnBanHangActionPerformed

    private void txtaccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtaccountActionPerformed

    }//GEN-LAST:event_txtaccountActionPerformed

    private void btnQuanLyLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuanLyLuongActionPerformed
        pnContent.removeAll();
        PnLuong pn = new PnLuong(phanQuyen);
        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnQuanLyLuong);

    }//GEN-LAST:event_btnQuanLyLuongActionPerformed

    private void btnDuyetNghiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDuyetNghiActionPerformed
        pnContent.removeAll();
        FrmDuyetNghi pn = new FrmDuyetNghi(phanQuyen);

        pnContent.add(pn, java.awt.BorderLayout.CENTER);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnDuyetNghi);
    }//GEN-LAST:event_btnDuyetNghiActionPerformed

    private void btnChamCongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChamCongActionPerformed
        pnContent.removeAll();
//        PnChamCong pn = new PnChamCong(phanQuyen);
        PnChamCong pn = new PnChamCong(phanQuyen, taiKhoan);
        pnContent.add(pn);
        pnContent.revalidate();
        pnContent.repaint();
        setButtonActive(btnChamCong);
    }//GEN-LAST:event_btnChamCongActionPerformed

    private void setButtonActive(javax.swing.JButton active) {
        for (javax.swing.JButton btn : menuButtons) {
            if (btn == active) {
                // Tạo hiệu ứng gradient
                btn.setBackground(new java.awt.Color(0, 120, 215)); // Xanh sáng hơn
                btn.setForeground(java.awt.Color.WHITE);
                btn.setFont(btn.getFont().deriveFont(java.awt.Font.BOLD, 15f));

                // Thêm icon mũi tên chỉ bên trái để nổi bật
                btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                btn.setIconTextGap(15);

                btn.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                        javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(255, 100, 0)), // Viền cam bên trái
                        javax.swing.BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));

                // Thêm hiệu ứng shadow nhẹ
                btn.setOpaque(true);
            } else {
                btn.setBackground(new java.awt.Color(170, 222, 215)); // Màu nền gốc của pnMenu
                btn.setForeground(new java.awt.Color(50, 50, 50)); // Xám đậm
                btn.setFont(btn.getFont().deriveFont(java.awt.Font.PLAIN, 14f));
                btn.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 15, 8, 15));
                btn.setIconTextGap(10);
            }
        }
    }

    public javax.swing.JPanel getPnContent() {
        return pnContent;
    }

//    JPanel QLBanHang = new PnBanHang(phanQuyen);
//    JPanel QLSanPham = new PnSanPham(phanQuyen);
//    JPanel QLKhachHang = new PnKhachHang(phanQuyen);
//    JPanel QLKhuyenMai = new PnKhuyenMai(phanQuyen);
//    JPanel QLNhanVien = new PnNhanVien(phanQuyen);
//    JPanel QLNhapHang = new PnNhapHang(phanQuyen);
//    JPanel QLPhanQuyen = new PnPhanQuyen(phanQuyen);
//    JPanel QLThongKe = new PnThongKe(phanQuyen);
    public static void main(String[] args) {
        // Khởi chạy MainFrame mà không cần đăng nhập
        TaiKhoan tk = new TaiKhoan(1, "admin", "admin", "ADMIN", 1);
        BUS.PhanQuyenBUS pqBUS = new BUS.PhanQuyenBUS();
        DTO.PhanQuyen pq = pqBUS.getPhanQuyenByTen(tk.getMaQuyen());
        if (pq == null) {
            JOptionPane.showConfirmDialog(null, "Không tìm thấy quyền 'admin' trong csdl", "Lỗi khởi tạo", JOptionPane.ERROR_MESSAGE);
            return;
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainFrame main = new MainFrame(tk, pq);
                main.setVisible(true);
            }
        });
    }
    
    


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBanHang;
    private javax.swing.JButton btnChamCong;
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnDoiMatKhau;
    private javax.swing.JButton btnDuyetNghi;
    private javax.swing.JButton btnKhachHang;
    private javax.swing.JButton btnKhuyenMai;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnNhapHang;
    private javax.swing.JButton btnPhanQuyen;
    private javax.swing.JButton btnQuanLyLuong;
    private javax.swing.JButton btnSanPham;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JPanel pnBottomMenu;
    private javax.swing.JPanel pnContent;
    private javax.swing.JPanel pnMenu;
    private javax.swing.JPanel pnSidebar;
    private javax.swing.JTextField txtaccount;
    // End of variables declaration//GEN-END:variables
}
