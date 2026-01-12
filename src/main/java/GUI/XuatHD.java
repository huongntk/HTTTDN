/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI;

import BUS.HoaDonBUS;
import DTO.CTHoaDon;
import DTO.HoaDon;
import java.awt.Component;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.awt.Dimension;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


public class XuatHD extends javax.swing.JDialog {

    private DefaultTableModel modelChiTiet;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,### đ"); 
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private int currentMaHD;
    
    public XuatHD(Frame parent, boolean modal, HoaDon hd, ArrayList<CTHoaDon> listCT) {
        super(parent, modal);
        initComponents();
        
        // Khởi tạo model cho bảng
        modelChiTiet = new DefaultTableModel();
        tblChiTietPhieu.setModel(modelChiTiet);
        // Thiết lập header 
        modelChiTiet.setColumnIdentifiers(new Object[]{"Sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"});
        // Đẩy dữ liệu lên form
        if (hd != null && listCT != null) {
            this.currentMaHD = hd.getMaHD();
            setHoaDonData(hd, listCT);
        }

        // [OPTIONAL] Điều chỉnh chiều cao bảng (theo yêu cầu trước)
        adjustTableHeight(tblChiTietPhieu); 

        pack();
        setLocationRelativeTo(parent);
        
    }
    
    private boolean doCapture(JComponent component, File file) {
        try {
            BufferedImage image = new BufferedImage(
                component.getWidth(), 
                component.getHeight(), 
                BufferedImage.TYPE_INT_ARGB
            );

            // Vẽ component lên BufferedImage
            component.paint(image.getGraphics());

            // Lưu ảnh ra file PNG
            ImageIO.write(image, "png", file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void capturePanel(Component component, String fileName) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Phiếu Nhập dưới dạng ảnh");
        // Thiết lập thư mục mặc định là Desktop (tùy chọn)
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        // Đặt tên file mặc định
        String defaultFileName = "HD_" + currentMaHD + "_" + 
                                 new SimpleDateFormat("yyyyMMdd").format(new java.util.Date()) + ".png";
        fileChooser.setSelectedFile(new File(defaultFileName));

        // Thêm bộ lọc file (chỉ cho phép .png)
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            // Đảm bảo tên file kết thúc bằng .png
            if (!fileToSave.getName().toLowerCase().endsWith(".png")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".png");
            }

            // Thực hiện chụp và lưu
            if (doCapture((JComponent) component, fileToSave)) {
                JOptionPane.showMessageDialog(this, 
                        "Phiếu nhập đã được lưu thành công tại:\n" + fileToSave.getAbsolutePath(), 
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Lưu file thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setHoaDonData(HoaDon hd, ArrayList<CTHoaDon> listCT) {
        String tenNV = hoaDonBUS.getTenNhanVien(hd.getMaNV());
        String tenKH = hoaDonBUS.getTenKhachHang(hd.getMaKH());
        lblMaHD.setText(String.valueOf(hd.getMaHD())); 
        lblNgayLap.setText(dateFormat.format(hd.getNgayLap())); 
        lblNhanVien.setText(String.valueOf(tenNV));
         
        lblKhachHang.setText(String.valueOf(tenKH));
        // Nạp dữ liệu vào bảng chi tiết
        loadTableData(listCT);
//        lblGiamGia.setText(hd.getG());
        lblTongTien.setText(currencyFormat.format(hd.getTongTien()));
    }

    private void loadTableData(ArrayList<CTHoaDon> listCT) {
        modelChiTiet.setRowCount(0); 

        HoaDonBUS hoaDonBUS = new HoaDonBUS(); 

        for (CTHoaDon cthd : listCT) {
            // Lấy Tên sản phẩm từ ID
            String tenSP = hoaDonBUS.getTenSanPham(cthd.getID()); 

            String sanPhamHienThi = tenSP + "\n(ID: " + cthd.getID() + ")"; 

            modelChiTiet.addRow(new Object[]{
                sanPhamHienThi,
                cthd.getSoLuong(),
                currencyFormat.format(cthd.getDonGia()),
                currencyFormat.format(cthd.getThanhTien())
            });
        }
    }

  
    public void adjustTableHeight(JTable table) {
        if (table.getModel().getRowCount() == 0) {
            // Nếu không có dòng nào, chiều cao tối thiểu cho tiêu đề
            table.setPreferredSize(new Dimension(table.getWidth(), table.getRowHeight()));
            return;
        }

        // Chiều cao tiêu đề (header)
        int headerHeight = table.getTableHeader().getPreferredSize().height;

        // Chiều cao tổng các hàng
        int rowHeight = table.getRowHeight();
        int rowCount = table.getModel().getRowCount();
        int totalRowsHeight = rowCount * rowHeight;

        // Chiều cao tổng: Header + Chiều cao tất cả các hàng
        int totalHeight = headerHeight + totalRowsHeight;

        // Đặt lại kích thước ưu tiên cho bảng
        table.setPreferredSize(new Dimension(table.getWidth(), totalHeight));

        // Đảm bảo JScrollPane không xuất hiện thanh cuộn
        // Bỏ thanh cuộn khỏi JScrollPane để bảng tự mở rộng
        table.setPreferredScrollableViewportSize(new Dimension(table.getWidth(), totalRowsHeight));
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPhieuThanhToan = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblMaHD = new javax.swing.JLabel();
        lblNgayLap = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblNhanVien = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblKhachHang = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblChiTietPhieu = new javax.swing.JTable();
        lblGiamGia = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnXuatHD = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("PHIẾU THANH TOÁN");

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("Số CT:");
        jPanel1.add(jLabel2);

        lblMaHD.setMaximumSize(new java.awt.Dimension(90, 20));
        lblMaHD.setPreferredSize(new java.awt.Dimension(90, 20));
        jPanel1.add(lblMaHD);

        lblNgayLap.setMaximumSize(new java.awt.Dimension(190, 20));
        lblNgayLap.setPreferredSize(new java.awt.Dimension(190, 20));
        jPanel1.add(lblNgayLap);

        jLabel3.setText("NV:");
        jPanel1.add(jLabel3);

        lblNhanVien.setMaximumSize(new java.awt.Dimension(90, 20));
        lblNhanVien.setPreferredSize(new java.awt.Dimension(90, 20));
        jPanel1.add(lblNhanVien);

        jLabel4.setText("KH:");
        jPanel1.add(jLabel4);
        jPanel1.add(lblKhachHang);

        tblChiTietPhieu.setEnabled(false);
        jScrollPane1.setViewportView(tblChiTietPhieu);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Giảm giá:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Phải thanh toán:");

        javax.swing.GroupLayout pnlPhieuThanhToanLayout = new javax.swing.GroupLayout(pnlPhieuThanhToan);
        pnlPhieuThanhToan.setLayout(pnlPhieuThanhToanLayout);
        pnlPhieuThanhToanLayout.setHorizontalGroup(
            pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPhieuThanhToanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlPhieuThanhToanLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlPhieuThanhToanLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 298, Short.MAX_VALUE)
                        .addGroup(pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTongTien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPhieuThanhToanLayout.createSequentialGroup()
                                .addComponent(lblGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(15, 15, 15))))))
            .addGroup(pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPhieuThanhToanLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPhieuThanhToanLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPhieuThanhToanLayout.createSequentialGroup()
                    .addGap(16, 16, 16)
                    .addComponent(jLabel6)
                    .addContainerGap(418, Short.MAX_VALUE)))
        );
        pnlPhieuThanhToanLayout.setVerticalGroup(
            pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPhieuThanhToanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 333, Short.MAX_VALUE)
                .addGroup(pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPhieuThanhToanLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(49, 49, 49))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPhieuThanhToanLayout.createSequentialGroup()
                        .addComponent(lblGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
            .addGroup(pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPhieuThanhToanLayout.createSequentialGroup()
                    .addGap(32, 32, 32)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(376, Short.MAX_VALUE)))
            .addGroup(pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlPhieuThanhToanLayout.createSequentialGroup()
                    .addGap(64, 64, 64)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(91, Short.MAX_VALUE)))
            .addGroup(pnlPhieuThanhToanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPhieuThanhToanLayout.createSequentialGroup()
                    .addContainerGap(401, Short.MAX_VALUE)
                    .addComponent(jLabel6)
                    .addGap(7, 7, 7)))
        );

        getContentPane().add(pnlPhieuThanhToan, java.awt.BorderLayout.CENTER);

        btnXuatHD.setBackground(new java.awt.Color(204, 255, 255));
        btnXuatHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnXuatHD.setText("Xuất hóa đơn");
        btnXuatHD.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnXuatHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(205, 205, 205)
                .addComponent(btnXuatHD)
                .addContainerGap(245, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(btnXuatHD, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_END);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnXuatHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHDActionPerformed
        int maHD = this.currentMaHD;
        String fileName = "HoaDon_" + maHD; 

        // 2. Chụp Panel và lưu thành ảnh
        capturePanel(pnlPhieuThanhToan, fileName); 

        // 3. Cập nhật trạng thái hóa đơn thành "Đã Thanh Toán" hoặc "Đã Xuất"
        boolean success = hoaDonBUS.updateTrangThai(maHD, "Đã Thanh Toán"); 

        if (success) {
            // Thông báo cập nhật trạng thái thành công (thường tích hợp vào thông báo chụp ảnh)
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật trạng thái Hóa đơn.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        

        this.dispose();
    }//GEN-LAST:event_btnXuatHDActionPerformed

    /**
     * @param args the command line arguments
     */
    // GUI/XuatHD.java (trong phương thức main)

//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        // ... (Giữ nguyên phần code Look and Feel)
//
//        // THÊM ĐOẠN CODE KHAI BÁO VÀ KHỞI TẠO DỮ LIỆU GIẢ/NULL Ở ĐÂY
//
//        // Khai báo DTO.HoaDon và DTO.CTHoaDon (bạn cần import hoặc dùng tên đầy đủ)
//        DTO.HoaDon hd = null; // Hoặc new DTO.HoaDon() với dữ liệu test
//        java.util.ArrayList<DTO.CTHoaDon> listCT = null; // Hoặc new ArrayList<DTO.CTHoaDon>() với dữ liệu test
//
//        //-------------------------------------------------------------
//
//        /* Create and display the dialog */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                // Bây giờ, các biến hd và listCT đã được khai báo ở phạm vi ngoài Runnable
//                XuatHD dialog = new XuatHD(new javax.swing.JFrame(), true, hd, listCT); 
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    @Override
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnXuatHD;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGiamGia;
    private javax.swing.JLabel lblKhachHang;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblNgayLap;
    private javax.swing.JLabel lblNhanVien;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JPanel pnlPhieuThanhToan;
    private javax.swing.JTable tblChiTietPhieu;
    // End of variables declaration//GEN-END:variables
}
