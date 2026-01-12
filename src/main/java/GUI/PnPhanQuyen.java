
package GUI;

import BUS.PhanQuyenBUS;
import DTO.PhanQuyen;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;

public class PnPhanQuyen extends javax.swing.JPanel{

    PhanQuyenBUS pqBUS = new PhanQuyenBUS();
    
    public PnPhanQuyen() {
        initComponents();
        loadDanhSachQuyen();
    }
    private void loadDanhSachQuyen() {
        cboQuyen.removeAllItems();
        
        cboQuyen.addItem("-- Chọn quyền --");
        try {
            ResultSet rs = pqBUS.layDanhSachQuyen();
            while (rs.next()) {
                cboQuyen.addItem(rs.getString("Quyen"));
            }
            cboQuyen.setSelectedIndex(0);
        } catch (Exception e) {
            e.printStackTrace();
        }

}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlThanhCongCu4 = new javax.swing.JPanel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        pnlCenter = new javax.swing.JPanel();
        cboQuyen = new javax.swing.JComboBox<>();
        chkBanHang = new javax.swing.JCheckBox();
        chkSanPham = new javax.swing.JCheckBox();
        chkKhachHang = new javax.swing.JCheckBox();
        chkNhanVien = new javax.swing.JCheckBox();
        chkNhapHang = new javax.swing.JCheckBox();
        chkKhuyenMai = new javax.swing.JCheckBox();
        chkPhanQuyen = new javax.swing.JCheckBox();
        chkThongKe = new javax.swing.JCheckBox();

        setLayout(new java.awt.BorderLayout());

        pnlThanhCongCu4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), new java.awt.Color(204, 204, 204)));
        pnlThanhCongCu4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnThem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/them.png"))); // NOI18N
        btnThem.setText("THÊM");
        btnThem.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnThem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        pnlThanhCongCu4.add(btnThem);

        btnSua.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sua.png"))); // NOI18N
        btnSua.setText("SỬA");
        btnSua.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSua.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        pnlThanhCongCu4.add(btnSua);

        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/xoa.png"))); // NOI18N
        btnXoa.setText("XÓA");
        btnXoa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnXoa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        pnlThanhCongCu4.add(btnXoa);

        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        btnLamMoi.setText("LÀM MỚI");
        btnLamMoi.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLamMoi.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });
        pnlThanhCongCu4.add(btnLamMoi);

        add(pnlThanhCongCu4, java.awt.BorderLayout.NORTH);

        pnlCenter.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        pnlCenter.setMaximumSize(new java.awt.Dimension(146, 368));
        pnlCenter.setPreferredSize(new java.awt.Dimension(278, 582));
        pnlCenter.setLayout(new javax.swing.BoxLayout(pnlCenter, javax.swing.BoxLayout.Y_AXIS));

        cboQuyen.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboQuyen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Chọn quyền --" }));
        cboQuyen.setToolTipText("Chọn quyền");
        cboQuyen.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cboQuyen.setMaximumSize(new java.awt.Dimension(200, 30));
        cboQuyen.setName(""); // NOI18N
        cboQuyen.setPreferredSize(new java.awt.Dimension(200, 26));
        cboQuyen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboQuyenActionPerformed(evt);
            }
        });
        pnlCenter.add(cboQuyen);

        chkBanHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkBanHang.setText("Quản lý bán hàng");
        chkBanHang.setMargin(new java.awt.Insets(8, 8, 8, 8));
        chkBanHang.setRolloverEnabled(false);
        pnlCenter.add(chkBanHang);

        chkSanPham.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkSanPham.setText("Quản lý sản phẩm");
        chkSanPham.setMargin(new java.awt.Insets(8, 8, 8, 8));
        pnlCenter.add(chkSanPham);

        chkKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkKhachHang.setText("Quản lý khách hàng");
        chkKhachHang.setMargin(new java.awt.Insets(8, 8, 8, 8));
        chkKhachHang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkKhachHangActionPerformed(evt);
            }
        });
        pnlCenter.add(chkKhachHang);

        chkNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkNhanVien.setText("Quản lý nhân viên");
        chkNhanVien.setMargin(new java.awt.Insets(8, 8, 8, 8));
        pnlCenter.add(chkNhanVien);

        chkNhapHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkNhapHang.setText("Quản lý nhập hàng");
        chkNhapHang.setMargin(new java.awt.Insets(8, 8, 8, 8));
        pnlCenter.add(chkNhapHang);

        chkKhuyenMai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkKhuyenMai.setText("Quản lý khuyến mãi");
        chkKhuyenMai.setMargin(new java.awt.Insets(8, 8, 8, 8));
        chkKhuyenMai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkKhuyenMaiActionPerformed(evt);
            }
        });
        pnlCenter.add(chkKhuyenMai);

        chkPhanQuyen.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkPhanQuyen.setText("Quản lý phân quyền");
        chkPhanQuyen.setMargin(new java.awt.Insets(8, 8, 8, 8));
        chkPhanQuyen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkPhanQuyenActionPerformed(evt);
            }
        });
        pnlCenter.add(chkPhanQuyen);

        chkThongKe.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        chkThongKe.setText("Quản lý thống kê");
        chkThongKe.setMargin(new java.awt.Insets(8, 8, 8, 8));
        pnlCenter.add(chkThongKe);

        add(pnlCenter, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    
    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // 1. Tìm Parent Frame (cửa sổ chính)
    // PnPhanQuyen là JPanel, nó cần tìm JFrame chứa nó (thường là MainFrame)
    Frame parentFrame = JOptionPane.getFrameForComponent(this);

    if (parentFrame != null) {
        // 2. Khởi tạo DlgThemQuyen
        // Giả sử DlgThemQuyen có constructor là (Frame parent, boolean modal)
        DlgThemQuyen dlg = new DlgThemQuyen((java.awt.Frame) parentFrame, true);
        
        // 3. Hiển thị Dialog
        dlg.setVisible(true);

        // 4. Cập nhật lại danh sách quyền sau khi Dialog đóng
        // (Nếu có quyền mới được thêm thành công)
        // viết lại hàm loadDanhSachQuyen để nó xóa dữ liệu cũ trước khi load lại
        cboQuyen.removeAllItems(); // Xóa các mục cũ
        loadDanhSachQuyen();       // Tải lại các mục mới từ CSDL
    } else {
        JOptionPane.showMessageDialog(this, "Không tìm thấy cửa sổ chính để hiển thị Dialog!", "Lỗi", JOptionPane.ERROR_MESSAGE);
    }    
    }//GEN-LAST:event_btnThemActionPerformed

    private void chkKhachHangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkKhachHangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkKhachHangActionPerformed

    private void chkKhuyenMaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkKhuyenMaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkKhuyenMaiActionPerformed

     // OPTIONAL: Tạo thêm hàm resetCheckboxes() cho rõ ràng
    private void resetCheckboxes() {
        chkBanHang.setSelected(false);
        chkSanPham.setSelected(false);
        chkKhachHang.setSelected(false);
        chkNhanVien.setSelected(false);
        chkNhapHang.setSelected(false);
        chkKhuyenMai.setSelected(false);
        chkPhanQuyen.setSelected(false);
        chkThongKe.setSelected(false);
    }
    private void cboQuyenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboQuyenActionPerformed
        String quyenDuocChon = (String) cboQuyen.getSelectedItem();
        if (quyenDuocChon==null  || quyenDuocChon.equals("") || quyenDuocChon.equals("-- Chọn quyền --")) {
            // OPTIONAL: reset các checkbox về FALSE nếu chọn "--Chọn quyền--"
            resetCheckboxes(); 
            return;
        }
        
        PhanQuyen pq = pqBUS.getPhanQuyen(quyenDuocChon);
        if (pq != null ) {
//            boolean[] dsQuyen = pqBUS.layQuyen(quyenDuocChon);

            chkBanHang.setSelected(pq.isQLBanHang());
            chkSanPham.setSelected(pq.isQLSanPham());
            chkKhachHang.setSelected(pq.isQLKhachHang());
            chkNhanVien.setSelected(pq.isQLNhanVien());
            chkNhapHang.setSelected(pq.isQLNhapHang());
            chkKhuyenMai.setSelected(pq.isQLKhuyenMai());
            chkPhanQuyen.setSelected(pq.isQLPhanQuyen());
            chkThongKe.setSelected(pq.isQLThongKe());
        }
    }//GEN-LAST:event_cboQuyenActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        String Quyen = (String) cboQuyen.getSelectedItem();
        if (Quyen == null || Quyen.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn quyền!");
            return;
        }

        boolean qlBanHang = chkBanHang.isSelected();
        boolean qlNhapHang = chkNhapHang.isSelected();
        boolean qlNhanVien = chkNhanVien.isSelected();
        boolean qlSanPham = chkSanPham.isSelected();
        boolean qlKhachHang = chkKhachHang.isSelected();
        boolean qlKhuyenMai = chkKhuyenMai.isSelected();
        boolean qlPhanQuyen = chkPhanQuyen.isSelected();
        boolean qlThongKe = chkThongKe.isSelected();

        boolean kq = pqBUS.updateQuyen(Quyen, qlBanHang, qlNhapHang, qlNhanVien, qlSanPham, qlKhachHang, qlKhuyenMai, qlPhanQuyen, qlThongKe);

        if (kq) {
            JOptionPane.showMessageDialog(this, "Lưu quyền thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Lưu quyền thất bại!");
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        String Quyen = (String) cboQuyen.getSelectedItem();
        if (Quyen == null || Quyen.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn quyền cần xóa!");
            return;
        }
        
        //Ngăn chặn xóa quyền Admin
        if (Quyen.equalsIgnoreCase("admin")){
            JOptionPane.showConfirmDialog(this, "Không thể xóa quyền admin","Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        

        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa quyền \"" + Quyen + "\"?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            boolean kq = pqBUS.xoaQuyen(Quyen);

        if (kq) {
            JOptionPane.showMessageDialog(this, "Vô hiệu hóa quyền [" + Quyen + "] thành công!");
            
            // Tải lại giao diện để hiển thị các checkbox đều là FALSE
            cboQuyenActionPerformed(null); 
            
        } else {
            JOptionPane.showMessageDialog(this, "Vô hiệu hóa quyền thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void chkPhanQuyenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkPhanQuyenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkPhanQuyenActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        // Gọi lại hàm xử lý sự kiện chọn ComboBox
    // Hàm này sẽ:
    // 1. Lấy tên Quyền đang được chọn từ cboQuyen
    // 2. Gọi pqBUS.getPhanQuyen(Quyen) để lấy dữ liệu từ CSDL
    // 3. Set trạng thái (setSelected) cho tất cả các checkbox
    
        cboQuyenActionPerformed(evt);
    }//GEN-LAST:event_btnLamMoiActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboQuyen;
    private javax.swing.JCheckBox chkBanHang;
    private javax.swing.JCheckBox chkKhachHang;
    private javax.swing.JCheckBox chkKhuyenMai;
    private javax.swing.JCheckBox chkNhanVien;
    private javax.swing.JCheckBox chkNhapHang;
    private javax.swing.JCheckBox chkPhanQuyen;
    private javax.swing.JCheckBox chkSanPham;
    private javax.swing.JCheckBox chkThongKe;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlThanhCongCu4;
    // End of variables declaration//GEN-END:variables
}
