/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

/**
 *
 * @author PC
 */
public class PnNhapHang extends javax.swing.JPanel {

    private PnPhieuNhap pnPhieuNhap;
    private PnNhaCungCap pnNCC;
    
    public PnNhapHang() {
        initComponents();
        this.pnPhieuNhap = new PnPhieuNhap();
        this.pnNCC = new PnNhaCungCap();
        
        // Xóa tab mặc định (nếu NetBeans tạo sẵn)
        tabQLNhapHang.removeAll();

        // Thêm tab mới với form riêng
        tabQLNhapHang.addTab("Nhập hàng", pnPhieuNhap);
        tabQLNhapHang.addTab("Nhà cung cấp", pnNCC);
    }

     public void switchToTab(int index) {
    // tabQLNhapHang là tên biến JTabbedPane của bạn
    if (index >= 0 && index < tabQLNhapHang.getTabCount()) {
         tabQLNhapHang.setSelectedIndex(index);
    }
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabQLNhapHang = new javax.swing.JTabbedPane();
        tabNhapHang = new javax.swing.JTabbedPane();
        tabQLNhaCungCap = new javax.swing.JTabbedPane();

        tabQLNhapHang.addTab("Nhập hàng", tabNhapHang);
        tabQLNhapHang.addTab("Nhà cung cấp", tabQLNhaCungCap);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabQLNhapHang, javax.swing.GroupLayout.DEFAULT_SIZE, 875, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabQLNhapHang, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabNhapHang;
    private javax.swing.JTabbedPane tabQLNhaCungCap;
    private javax.swing.JTabbedPane tabQLNhapHang;
    // End of variables declaration//GEN-END:variables
}
