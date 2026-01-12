/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import DTO.PhanQuyen;
import DTO.TaiKhoan;
import java.awt.*;
import javax.swing.JOptionPane;
import javax.swing.*;

public class MainFrame extends javax.swing.JFrame {
    
    private TaiKhoan taiKhoan;
    private PhanQuyen phanQuyen;
    // Biến lưu nút hiện đang được chọn
    private JButton selectedButton = null;
   

    public MainFrame( TaiKhoan tk, PhanQuyen pq) {
        setTitle("Quản lý Bán Đồng Hồ Cơ");
        initComponents();
        
        setExtendedState(MAXIMIZED_BOTH); // Mở toàn màn hình
        setLocationRelativeTo(null);             // Căn giữa
        this.taiKhoan = tk;
        this.phanQuyen = pq;
        txtaccount.setText(tk.getTaiKhoan());

        loadMenuByRole(tk.getQuyen());
        
        // KÍCH HOẠT CHỨC NĂNG ĐẦU TIÊN
        openFirstAccessiblePanel();

    }
    
    public void loadMenuByRole(String role) {
        if(phanQuyen == null) return;
        btnBanHang.setVisible(phanQuyen.isQLBanHang());
        btnSanPham.setVisible(phanQuyen.isQLSanPham());
        btnKhachHang.setVisible(phanQuyen.isQLKhachHang());
        btnNhanVien.setVisible(phanQuyen.isQLNhanVien());
        btnNhapHang.setVisible(phanQuyen.isQLNhapHang());
        btnPhanQuyen.setVisible(phanQuyen.isQLPhanQuyen());
        btnKhuyenMai.setVisible(phanQuyen.isQLKhuyenMai());
        btnThongKe.setVisible(phanQuyen.isQLThongKe());
        
        
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
        btnSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/smartphone.png"))); // NOI18N
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
        if (phanQuyen == null) return;

        // Tùy chọn 1: Ưu tiên mở chức năng Bán Hàng
        if (phanQuyen.isQLBanHang()) {
            btnBanHang.doClick();
            return;
        }

        // Tùy chọn 2: Nếu không có Bán Hàng, kiểm tra các quyền khác theo thứ tự ưu tiên
        if (phanQuyen.isQLSanPham()) {
            btnSanPham.doClick();
            return;
        }

        if (phanQuyen.isQLNhapHang()) {
            btnNhapHang.doClick();
            return;
        }

        if (phanQuyen.isQLNhanVien()) {
            btnNhanVien.doClick();
            return;
        }

        if (phanQuyen.isQLKhachHang()) {
            btnKhachHang.doClick();
            return;
        }

        // ... và tiếp tục với các quyền còn lại (QLKhuyenMai, QLPhanQuyen, QLThongKe)

        if (phanQuyen.isQLKhuyenMai()) {
            btnKhuyenMai.doClick();
            return;
        }

        if (phanQuyen.isQLPhanQuyen()) {
            btnPhanQuyen.doClick();
            return;
        }

        if (phanQuyen.isQLThongKe()) {
            btnThongKe.doClick();
            return;
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
        if (confirm == JOptionPane.YES_OPTION){
            this.dispose();
            new FrmDangNhap().setVisible(true);
        }
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void btnThongKeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongKeActionPerformed
//        // TODO add your handling code here:
        pnContent.removeAll();
        pnContent.add(QLThongKe);
        pnContent.revalidate();
        pnContent.repaint();
    }//GEN-LAST:event_btnThongKeActionPerformed

    private void btnKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhuyenMaiActionPerformed
        pnContent.removeAll();
        pnContent.add(QLKhuyenMai);
        pnContent.revalidate();
        pnContent.repaint();
    }//GEN-LAST:event_btnKhuyenMaiActionPerformed

    private void btnPhanQuyenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPhanQuyenActionPerformed
        pnContent.removeAll();
        pnContent.add(QLPhanQuyen);
        pnContent.revalidate();
        pnContent.repaint();
    }//GEN-LAST:event_btnPhanQuyenActionPerformed

    private void btnNhapHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhapHangActionPerformed
        pnContent.removeAll();
        pnContent.add(QLNhapHang);
        pnContent.revalidate();
        pnContent.repaint();
    }//GEN-LAST:event_btnNhapHangActionPerformed

    private void btnNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNhanVienActionPerformed
        pnContent.removeAll();
        pnContent.add(QLNhanVien);
        pnContent.revalidate();
        pnContent.repaint();
    }//GEN-LAST:event_btnNhanVienActionPerformed

    private void btnKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhachHangActionPerformed
        pnContent.removeAll();
        pnContent.add(QLKhachHang);
        pnContent.revalidate();
        pnContent.repaint();
    }//GEN-LAST:event_btnKhachHangActionPerformed

    private void btnSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSanPhamActionPerformed
        pnContent.removeAll();
        pnContent.add(QLSanPham);
        pnContent.revalidate();
        pnContent.repaint();
    }//GEN-LAST:event_btnSanPhamActionPerformed

    private void btnBanHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBanHangActionPerformed
        pnContent.removeAll();
        pnContent.add(QLBanHang);
        pnContent.revalidate();
        pnContent.repaint();
    }//GEN-LAST:event_btnBanHangActionPerformed

    private void txtaccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtaccountActionPerformed
        
    }//GEN-LAST:event_txtaccountActionPerformed

    JPanel QLBanHang = new PnBanHang();
    JPanel QLSanPham = new PnSanPham();
    JPanel QLKhachHang = new PnKhachHang();
    JPanel QLKhuyenMai = new PnKhuyenMai();
    JPanel QLNhanVien = new PnNhanVien();
    JPanel QLNhapHang = new PnNhapHang();
    JPanel QLPhanQuyen = new PnPhanQuyen();
    JPanel QLThongKe = new PnThongKe();
    
    public static void main(String[] args) {
    // Khởi chạy MainFrame mà không cần đăng nhập
    TaiKhoan tk = new TaiKhoan(1, "admin", "123", "Quản trị viên", 1);
    BUS.PhanQuyenBUS pqBUS = new BUS.PhanQuyenBUS();
    DTO.PhanQuyen pq = pqBUS.getPhanQuyen(tk.getQuyen());
    if (pq == null){
        JOptionPane.showConfirmDialog(null, "Không tìm thấy quyền 'admin' trong csdl", "Lỗi khởi tạo", JOptionPane.ERROR_MESSAGE);
        return;
    }
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            MainFrame main = new MainFrame(tk,pq);
            main.setVisible(true);
        }
    });
    
}

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBanHang;
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnDoiMatKhau;
    private javax.swing.JButton btnKhachHang;
    private javax.swing.JButton btnKhuyenMai;
    private javax.swing.JButton btnNhanVien;
    private javax.swing.JButton btnNhapHang;
    private javax.swing.JButton btnPhanQuyen;
    private javax.swing.JButton btnSanPham;
    private javax.swing.JButton btnThongKe;
    private javax.swing.JPanel pnBottomMenu;
    private javax.swing.JPanel pnContent;
    private javax.swing.JPanel pnMenu;
    private javax.swing.JPanel pnSidebar;
    private javax.swing.JTextField txtaccount;
    // End of variables declaration//GEN-END:variables
}
