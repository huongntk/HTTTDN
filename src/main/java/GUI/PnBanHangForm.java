/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI;

import BUS.HoaDonBUS;
import BUS.SanPhamBUS;
import DTO.CTHoaDon;
import DTO.Product;
import DTO.TaiKhoan;
import UTIL.Auth;
import java.util.ArrayList;
import java.awt.Image;
import java.text.DecimalFormat;
import javax.swing.*;
import java.sql.*;
import UTIL.DBConnect;
import java.net.URL;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author HP
 */
public class PnBanHangForm extends javax.swing.JPanel {


    private SanPhamBUS sanPhamBUS = new SanPhamBUS();
    private DefaultTableModel modelDSSP; 
    private DefaultTableModel modelGioHang;
    private int soLuongTonHienTai = 0;
    private HoaDonBUS hoaDonBUS = new HoaDonBUS();
    private PnBanHang parentPanel;
    public PnBanHangForm(PnBanHang parentPanel) {
        this.parentPanel = parentPanel;
        initComponents();
        modelDSSP = (DefaultTableModel) tblDanhSachSP.getModel();
        modelGioHang = (DefaultTableModel) tblGioHang.getModel();

        loadDanhSachSanPham();
        
        try {
            TaiKhoan loggedInUser = UTIL.Auth.getUser();
            
            if (loggedInUser != null) {
                txtMaNV.setText(String.valueOf(loggedInUser.getMaNV())); 
            } else {
                 txtMaNV.setText("0"); 
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy Mã nhân viên: " + e.getMessage());
            txtMaNV.setText("0");
        }
        txtMaNV.setEditable(false);
        
        tblDanhSachSP.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
            
                if (!e.getValueIsAdjusting()) {
                    tblDanhSachSPSelectionChanged();
                }
            }
        });
    }
    
    private void loadDanhSachSanPham() {
        modelDSSP.setRowCount(0);
        
        ArrayList<Product> listSP = sanPhamBUS.getSanPham(); 
        
        for (Product p : listSP) {
            
            Object[] row = new Object[]{
                p.getID(),
                p.getTenSP(),
                p.getTenLoai(),
                p.getGiaBanFormatted(), 
                
                p.getMoTa(),
                p.getSoLuong()
            };
            modelDSSP.addRow(row);
        }
    }
    private void clearProductFields() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("0"); 
        txtLoai.setText("");
        txtMoTa.setText("");

    }
    
    private void tblDanhSachSPSelectionChanged() {
        int selectedRow = tblDanhSachSP.getSelectedRow();

        lblimage.setIcon(null);
//        lblimage.setText("Ảnh"); 
       

        if (selectedRow >= 0) {
            try {
                int productID = (int) modelDSSP.getValueAt(selectedRow, 0);
                ArrayList<Product> list = sanPhamBUS.getSanPhamById(productID);

                if (!list.isEmpty()) {
                    Product p = list.get(0);

                    soLuongTonHienTai = p.getSoLuong();

                    txtMaSP.setText(String.valueOf(p.getID()));
                    txtTenSP.setText(p.getTenSP());

                    txtDonGia.setText(p.getGiaBanVND()); 

                    txtSoLuong.setText("1"); 

                    txtLoai.setText(p.getTenLoai() ); 

                    txtMoTa.setText(p.getMoTa());

                    String hinhAnhPath = p.getHinhAnh();

                    if (hinhAnhPath != null && !hinhAnhPath.isEmpty()) {
                        java.net.URL imgURL = getClass().getResource(hinhAnhPath); 

                        if (imgURL != null) {
                            ImageIcon icon = new ImageIcon(imgURL);
                            java.awt.Image img = icon.getImage().getScaledInstance(105, 105, java.awt.Image.SCALE_SMOOTH); 
                            lblimage.setIcon(new ImageIcon(img));
                        } else {
                            lblimage.setText("Ảnh lỗi/Không tìm thấy");
                            lblimage.setIcon(null);
                        }
                    }

                    txtMaSP.setEditable(false);
                    txtTenSP.setEditable(false);
                    txtDonGia.setEditable(false);
                    txtLoai.setEditable(false);
                    txtMoTa.setEditable(false);
                    txtSoLuong.setEditable(true); 

                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    clearProductFields();
                }

            } catch (Exception ex) {
                System.err.println("Lỗi khi chọn sản phẩm: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi hiển thị thông tin sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                clearProductFields();
            }
        } else {
            clearProductFields();
        }
        
    }
    private String formatCurrency(double amount) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(amount) + " đ";
    }
    
    private int getSoLuongTonGoc(int maSP) {
        ArrayList<Product> productList = sanPhamBUS.getSanPhamById(maSP); 
        if (!productList.isEmpty()) {
            return productList.get(0).getSoLuong();
        }
        return -1; 
    }

    private double calculateTongTienValue() {
        double tongTien = 0;
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            String thanhTienStr = modelGioHang.getValueAt(i, 5).toString();
            // Lọc chuỗi chỉ giữ lại các ký tự số
            String cleanThanhTienStr = thanhTienStr.replaceAll("[^\\d]", ""); 
            try {
                if (!cleanThanhTienStr.isEmpty()) {
                    tongTien += Double.parseDouble(cleanThanhTienStr);
                }
            } catch (NumberFormatException e) {
                 System.err.println("Lỗi parse Thành tiền tại dòng " + i);
            }
        }
        
        return tongTien;
    }

    private void updateTongTien() {
        double tongTien = 0;
        
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            String thanhTienStr = modelGioHang.getValueAt(i, 5).toString();
        
        // Loại bỏ TẤT CẢ các ký tự không phải là chữ số khỏi chuỗi.
        // Chỉ giữ lại các chữ số (0-9)
        String cleanThanhTienStr = thanhTienStr.replaceAll("[^\\d]", "");
            try {
                if (!cleanThanhTienStr.isEmpty()) {
                // Parse chuỗi đã làm sạch
                double thanhTien = Double.parseDouble(cleanThanhTienStr);
                tongTien += thanhTien;
                }
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse Thành tiền tại dòng " + i);
            }
        }
        
         lblTongTien.setText(formatCurrency(tongTien));
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        pnlDanhSachSP = new javax.swing.JPanel();
        lblDanhSachSP = new javax.swing.JLabel();
        lblTimKiem = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        btnTim = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        scrDanhSachSP = new javax.swing.JScrollPane();
        tblDanhSachSP = new javax.swing.JTable();
        pnlGioHang = new javax.swing.JPanel();
        lblGioHang = new javax.swing.JLabel();
        scrGioHang = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();
        lblTongTien = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        pnlThongTinSP = new javax.swing.JPanel();
        lblThongTinSP = new javax.swing.JLabel();
        lblMaSP = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        lblTenSP = new javax.swing.JLabel();
        txtTenSP = new javax.swing.JTextField();
        lblDonGia = new javax.swing.JLabel();
        txtDonGia = new javax.swing.JTextField();
        lblSoLuong = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        lblLoai = new javax.swing.JLabel();
        txtLoai = new javax.swing.JTextField();
        lblMaNV = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        lblAnh = new javax.swing.JLabel();
        lblMoTa = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnXacNhan = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtMoTa = new javax.swing.JTextField();
        lblimage = new javax.swing.JLabel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(620, 589));
        jPanel1.setPreferredSize(new java.awt.Dimension(620, 589));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        pnlDanhSachSP.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));
        pnlDanhSachSP.setMaximumSize(new java.awt.Dimension(620, 290));
        pnlDanhSachSP.setMinimumSize(new java.awt.Dimension(540, 250));
        pnlDanhSachSP.setPreferredSize(new java.awt.Dimension(620, 290));

        lblDanhSachSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDanhSachSP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDanhSachSP.setText("Danh sách sản phẩm");

        lblTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTimKiem.setText("Tìm kiếm");

        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });

        btnTim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/timkiem.png"))); // NOI18N
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        scrDanhSachSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tblDanhSachSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblDanhSachSP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã SP", "Tên SP", "Loại", "Đơn giá", "Mô tả", "Số lượng"
            }
        ));
        tblDanhSachSP.setGridColor(new java.awt.Color(51, 51, 51));
        tblDanhSachSP.setRowHeight(23);
        tblDanhSachSP.setShowGrid(true);
        scrDanhSachSP.setViewportView(tblDanhSachSP);

        javax.swing.GroupLayout pnlDanhSachSPLayout = new javax.swing.GroupLayout(pnlDanhSachSP);
        pnlDanhSachSP.setLayout(pnlDanhSachSPLayout);
        pnlDanhSachSPLayout.setHorizontalGroup(
            pnlDanhSachSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachSPLayout.createSequentialGroup()
                .addGroup(pnlDanhSachSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDanhSachSPLayout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(lblDanhSachSP, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(scrDanhSachSP, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlDanhSachSPLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(lblTimKiem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnTim, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 102, Short.MAX_VALUE)
                        .addComponent(btnLamMoi)))
                .addContainerGap())
        );
        pnlDanhSachSPLayout.setVerticalGroup(
            pnlDanhSachSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDanhSachSPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDanhSachSP, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(pnlDanhSachSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTimKiem)
                    .addComponent(btnTim, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(19, 19, 19)
                .addComponent(scrDanhSachSP, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jPanel1.add(pnlDanhSachSP);

        pnlGioHang.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));
        pnlGioHang.setMaximumSize(new java.awt.Dimension(620, 299));
        pnlGioHang.setMinimumSize(new java.awt.Dimension(540, 250));
        pnlGioHang.setPreferredSize(new java.awt.Dimension(620, 299));

        lblGioHang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblGioHang.setText("Giỏ hàng");

        tblGioHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SP", "Tên SP", "Loại", "Đơn giá", "Số lượng", "Thành tiền"
            }
        ));
        tblGioHang.setGridColor(new java.awt.Color(51, 51, 51));
        tblGioHang.setRowHeight(23);
        tblGioHang.setShowGrid(true);
        scrGioHang.setViewportView(tblGioHang);

        lblTongTien.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Tổng tiền:");

        javax.swing.GroupLayout pnlGioHangLayout = new javax.swing.GroupLayout(pnlGioHang);
        pnlGioHang.setLayout(pnlGioHangLayout);
        pnlGioHangLayout.setHorizontalGroup(
            pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGioHangLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addComponent(lblGioHang)
                .addGap(207, 207, 207)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
            .addGroup(pnlGioHangLayout.createSequentialGroup()
                .addComponent(scrGioHang)
                .addContainerGap())
        );
        pnlGioHangLayout.setVerticalGroup(
            pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGioHangLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlGioHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblGioHang)
                        .addComponent(jLabel1))
                    .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrGioHang, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        jPanel1.add(pnlGioHang);

        add(jPanel1);

        pnlThongTinSP.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));
        pnlThongTinSP.setMaximumSize(new java.awt.Dimension(410, 585));
        pnlThongTinSP.setMinimumSize(new java.awt.Dimension(339, 570));
        pnlThongTinSP.setPreferredSize(new java.awt.Dimension(410, 585));

        lblThongTinSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblThongTinSP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThongTinSP.setText("Thông tin sản phẩm");

        lblMaSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaSP.setText("Mã sản phẩm");

        txtMaSP.setEditable(false);
        txtMaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaSPActionPerformed(evt);
            }
        });

        lblTenSP.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenSP.setText("Tên sản phẩm");

        txtTenSP.setEditable(false);
        txtTenSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenSPActionPerformed(evt);
            }
        });

        lblDonGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDonGia.setText("Đơn giá");

        txtDonGia.setEditable(false);
        txtDonGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDonGiaActionPerformed(evt);
            }
        });

        lblSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblSoLuong.setText("Số lượng");

        txtSoLuong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoLuongActionPerformed(evt);
            }
        });

        lblLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblLoai.setText("Loại");

        txtLoai.setEditable(false);
        txtLoai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLoaiActionPerformed(evt);
            }
        });

        lblMaNV.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaNV.setText("Mã nhân viên");

        txtMaNV.setEditable(false);
        txtMaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNVActionPerformed(evt);
            }
        });

        lblAnh.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblAnh.setText("Ảnh");

        lblMoTa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMoTa.setText("Mô tả");

        btnThem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/them.png"))); // NOI18N
        btnThem.setText("Thêm vào giỏ");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/xoa.png"))); // NOI18N
        btnXoa.setText("Xóa khỏi giỏ");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnXacNhan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnXacNhan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/xacnhan.png"))); // NOI18N
        btnXacNhan.setText("Xác nhận");
        btnXacNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXacNhanActionPerformed(evt);
            }
        });

        txtMoTa.setEditable(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblimage, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(txtMoTa)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblimage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMoTa, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlThongTinSPLayout = new javax.swing.GroupLayout(pnlThongTinSP);
        pnlThongTinSP.setLayout(pnlThongTinSPLayout);
        pnlThongTinSPLayout.setHorizontalGroup(
            pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinSPLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinSPLayout.createSequentialGroup()
                        .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThongTinSPLayout.createSequentialGroup()
                                .addComponent(lblAnh)
                                .addGap(107, 107, 107)
                                .addComponent(lblMoTa)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(pnlThongTinSPLayout.createSequentialGroup()
                                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblLoai)
                                    .addComponent(lblSoLuong)
                                    .addComponent(lblTenSP)
                                    .addComponent(lblDonGia)
                                    .addComponent(lblMaNV)
                                    .addComponent(lblMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaSP)
                                    .addComponent(txtDonGia)
                                    .addComponent(txtSoLuong)
                                    .addComponent(txtLoai)
                                    .addComponent(txtMaNV)
                                    .addComponent(txtTenSP))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinSPLayout.createSequentialGroup()
                        .addComponent(btnXacNhan)
                        .addGap(110, 110, 110))))
            .addGroup(pnlThongTinSPLayout.createSequentialGroup()
                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThongTinSPLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(btnThem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                        .addComponent(btnXoa))
                    .addGroup(pnlThongTinSPLayout.createSequentialGroup()
                        .addGap(126, 126, 126)
                        .addComponent(lblThongTinSP)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinSPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlThongTinSPLayout.setVerticalGroup(
            pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinSPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblThongTinSP, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19)
                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaSP)
                    .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTenSP)
                    .addComponent(txtTenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDonGia)
                    .addComponent(txtDonGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSoLuong)
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLoai)
                    .addComponent(txtLoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaNV)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAnh)
                    .addComponent(lblMoTa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnXoa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXacNhan)
                .addGap(44, 44, 44))
        );

        add(pnlThongTinSP);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void txtMaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaSPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaSPActionPerformed

    private void txtTenSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenSPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenSPActionPerformed

    private void txtDonGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDonGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDonGiaActionPerformed

    private void txtSoLuongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoLuongActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoLuongActionPerformed

    private void txtLoaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLoaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLoaiActionPerformed

    private void txtMaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNVActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        loadDanhSachSanPham();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        String searchName = txtTimKiem.getText().trim();
        
        if (searchName.isEmpty()) {
            loadDanhSachSanPham(); // Nếu ô tìm kiếm trống, load lại tất cả
            return;
        }

        modelDSSP.setRowCount(0); // Xóa dữ liệu cũ
        ArrayList<Product> listSP = sanPhamBUS.getSanPhamByName(searchName); // Lấy sản phẩm theo tên
        
        // Đổ dữ liệu tìm kiếm vào bảng
        for (Product p : listSP) {
            Object[] row = new Object[]{
                p.getID(),
                p.getTenSP(),
                p.getMaLoai(), 
                p.getGiaBanFormatted(),
                p.getMoTa()
            };
            modelDSSP.addRow(row);
        }
        
        if (listSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm nào khớp với tên: " + searchName, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnTimActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // 1. Lấy dữ liệu và kiểm tra hợp lệ
    String maSPStr = txtMaSP.getText().trim();
    String soLuongStr = txtSoLuong.getText().trim();

    if (maSPStr.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm trước khi thêm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    int maSP;
    int soLuongMua;
    double donGia;
    String loai = txtLoai.getText();
    String tenSP = txtTenSP.getText();
    String ghiChu;
    
    try {
        // Lấy Mã SP và Số lượng mua
        maSP = Integer.parseInt(maSPStr);
        soLuongMua = Integer.parseInt(soLuongStr);
        
        String donGiaStr = txtDonGia.getText().replace(" đ", "").replace(",", "").replace(".", ""); 
        donGia = Double.parseDouble(donGiaStr);
        
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Đơn giá không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (soLuongMua <= 0) {
        JOptionPane.showMessageDialog(this, "Số lượng mua phải lớn hơn 0.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    int tonGoc = getSoLuongTonGoc(maSP);
    if (tonGoc == -1) {
        JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin sản phẩm gốc.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // 2. Xử lý Thêm/Cập nhật vào Giỏ hàng
    int existingRow = -1;
    for (int i = 0; i < modelGioHang.getRowCount(); i++) {
        // Kiểm tra xem Mã SP đã có trong giỏ hàng chưa (cột 0)
        if ((int) modelGioHang.getValueAt(i, 0) == maSP) {
            existingRow = i;
            break;
        }
    }

    if (existingRow != -1) {
        // SẢN PHẨM ĐÃ TỒN TẠI TRONG GIỎ HÀNG (Cập nhật số lượng)
        int currentSoLuongTrongGio = (int) modelGioHang.getValueAt(existingRow, 4);
        int newSoLuong = currentSoLuongTrongGio + soLuongMua;
        
        // 3. KIỂM TRA TỒN KHO GỐC (Lần 2)
        if (newSoLuong > tonGoc) {
             JOptionPane.showMessageDialog(this, 
                 "Tổng số lượng mua vượt quá số lượng tồn kho gốc (" + tonGoc + ").\n" +
                 "Hiện đang có " + currentSoLuongTrongGio + " sản phẩm trong giỏ.", 
                 "Lỗi Số Lượng", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        // Cập nhật Số lượng và Thành tiền
        double newThanhTien = newSoLuong * donGia;
        
        modelGioHang.setValueAt(newSoLuong, existingRow, 4);
        modelGioHang.setValueAt(formatCurrency(newThanhTien), existingRow, 5);

    } else {

        if (soLuongMua > tonGoc) {
            JOptionPane.showMessageDialog(this, 
                "Số lượng mua vượt quá số lượng tồn kho (" + tonGoc + ").", 
                "Lỗi Số Lượng", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double thanhTien = soLuongMua * donGia;
        Object[] row = {
            maSP,
            tenSP,
            loai,
            formatCurrency(donGia),
            soLuongMua,
            formatCurrency(thanhTien)
        };
        modelGioHang.addRow(row);
    }
    
    updateTongTien();
    clearProductFields();


    }//GEN-LAST:event_btnThemActionPerformed

    private void btnXacNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXacNhanActionPerformed
        // 1. KIỂM TRA GIỎ HÀNG
        if (modelGioHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống. Vui lòng thêm sản phẩm.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double tongTien = calculateTongTienValue();

        int maNV = UTIL.Auth.getUser().getMaNV();

        int maKH = 2; 

        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Xác nhận tạo hóa đơn với Tổng tiền: " + formatCurrency(tongTien) + " ?", 
            "Xác nhận thanh toán", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            ArrayList<CTHoaDon> listCTHD = new ArrayList<>();

            for (int i = 0; i < modelGioHang.getRowCount(); i++) {
                int maSP = (int) modelGioHang.getValueAt(i, 0);
                int soLuong = (int) modelGioHang.getValueAt(i, 4);

                // Lấy Đơn giá gốc (cần parse ngược từ chuỗi định dạng cột 2)
                String donGiaStr = modelGioHang.getValueAt(i, 3).toString().replaceAll("[^\\d]", "");
                double donGia = Double.parseDouble(donGiaStr); 
                String thanhTienStr = modelGioHang.getValueAt(i, 4).toString().replaceAll("[^\\d]", "");
                double thanhTien = Double.parseDouble(donGiaStr); 

                CTHoaDon cthd = new CTHoaDon(0, soLuong, donGia, thanhTien, maSP); 
                listCTHD.add(cthd);
            }

            int maHDMoi = hoaDonBUS.thucHienGiaoDich(maNV, maKH, tongTien, listCTHD);

            if (maHDMoi > 0) {
                // 6. THÀNH CÔNG: DỌN DẸP FORM
                modelGioHang.setRowCount(0); // Xóa hết sản phẩm khỏi giỏ
                updateTongTien();
                 boolean success = hoaDonBUS.updateTrangThai(maHDMoi, "Chưa Thanh Toán"); 
                String message = "Lập hóa đơn thành công. Mã hóa đơn: " + maHDMoi + ". Bạn có muốn chuyển sang tab Hóa đơn ngay bây giờ không?";
                int option = JOptionPane.showConfirmDialog(this, message, "Thành công", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    if (parentPanel != null) {
                        parentPanel.switchToHoaDonTabAndShowDetail(maHDMoi);
                    } else {
                        JOptionPane.showMessageDialog(this, "Không thể chuyển tab. Vui lòng chuyển thủ công.", "Lỗi hệ thống", JOptionPane.WARNING_MESSAGE);
                    }
                }
//                resetFormBanHang();

            } else {
                // 7. THẤT BẠI
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại! Đã rollback giao dịch. Vui lòng kiểm tra log hệ thống.", "Lỗi Giao Dịch", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnXacNhanActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed

        int selectedRowIndex = tblGioHang.getSelectedRow();

        if (selectedRowIndex == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa khỏi giỏ hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
          
            return; // Dừng thực thi phương thức
        }

        // 2. Hỏi xác nhận từ người dùng trước khi xóa
        int confirm = JOptionPane.showConfirmDialog(this,
                                                    "Bạn có chắc chắn muốn xóa sản phẩm này khỏi giỏ hàng không?",
                                                    "Xác nhận xóa",
                                                    JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // LẤY THÀNH TIỀN CỦA DÒNG ĐANG CHỌN
                // SỬA LỖI: Cột Thành tiền là cột 5 (Index 5)
                String thanhTienStr = modelGioHang.getValueAt(selectedRowIndex, 5).toString(); 

                // Làm sạch chuỗi tiền tệ (loại bỏ " đ" và dấu chấm phân cách hàng ngàn)
                String cleanThanhTienStr = thanhTienStr.replaceAll("[^\\d]", "");

                // Chuyển sang giá trị số double
                double thanhTienCu = Double.parseDouble(cleanThanhTienStr);

                // 3. Xóa hàng khỏi mô hình bảng
                modelGioHang.removeRow(selectedRowIndex);

                // 4. Cập nhật lại Tổng tiền (Sử dụng hàm updateTongTien() đã có)
                updateTongTien(); 

//                JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm khỏi giỏ hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Lỗi định dạng số khi cập nhật tổng tiền. Vui lòng kiểm tra dữ liệu cột Thành tiền.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                 System.err.println("Lỗi NumberFormatException: " + e.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xóa sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                 System.err.println("Lỗi Xóa: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnXoaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnXacNhan;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JLabel lblDanhSachSP;
    private javax.swing.JLabel lblDonGia;
    private javax.swing.JLabel lblGioHang;
    private javax.swing.JLabel lblLoai;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblMaSP;
    private javax.swing.JLabel lblMoTa;
    private javax.swing.JLabel lblSoLuong;
    private javax.swing.JLabel lblTenSP;
    private javax.swing.JLabel lblThongTinSP;
    private javax.swing.JLabel lblTimKiem;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JLabel lblimage;
    private javax.swing.JPanel pnlDanhSachSP;
    private javax.swing.JPanel pnlGioHang;
    private javax.swing.JPanel pnlThongTinSP;
    private javax.swing.JScrollPane scrDanhSachSP;
    private javax.swing.JScrollPane scrGioHang;
    private javax.swing.JTable tblDanhSachSP;
    private javax.swing.JTable tblGioHang;
    private javax.swing.JTextField txtDonGia;
    private javax.swing.JTextField txtLoai;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtMoTa;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTenSP;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
