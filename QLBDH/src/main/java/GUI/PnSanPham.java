package GUI;

import BUS.CTSanPhamBUS;
import BUS.SanPhamBUS;
import DAO.LoaiDAO;
import DAO.NhaCungCapDAO;
import DTO.Loai;
import DTO.PhanQuyen;
import DTO.SanPhamDTO;
import DTO.ProductDetail;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class PnSanPham extends javax.swing.JPanel {

    private ArrayList<SanPhamDTO> list;
    SanPhamBUS bus = new SanPhamBUS();
    CTSanPhamBUS ctbus = new CTSanPhamBUS();
    private java.util.Map<Integer, String> loaiMap = new java.util.HashMap<>();
    private java.util.Map<Integer, String> nccMap = new java.util.HashMap<>();
    private PhanQuyen phanQuyen;

    public PnSanPham(PhanQuyen pq) {
        initComponents();
        this.phanQuyen = pq;
        loadData();
        configureByPermission();
    }

    private void configureByPermission() {
        btnThem.setVisible(phanQuyen.isKhoThemSanPham());
        btnSua.setVisible(phanQuyen.isKhoSuaSanPham());
        btnXoa.setVisible(phanQuyen.isKhoXoaSanPham());
        btnCapNhat.setVisible(phanQuyen.isKhoSuaSanPham());
        btnLuu.setVisible(phanQuyen.isKhoSuaSanPham());
        btnXuatExcel.setVisible(phanQuyen.isKhoXemSanPham());
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnXuatExcel = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField12 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setPreferredSize(new java.awt.Dimension(1280, 720));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sua.png")));
        btnSua.setText("Ch\u1ec9nh s\u1eeda");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/xoa.png")));
        btnXoa.setText("Xo\u00e1 s\u1ea3n ph\u1ea9m");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnXuatExcel.setText("Xu\u1ea5t Excel");
        btnXuatExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatExcelActionPerformed(evt);
            }
        });

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/them.png")));
        btnThem.setText("Th\u00eam s\u1ea3n ph\u1ea9m");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png")));
        btnLamMoi.setText("L\u00e0m m\u1edbi");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btnThem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSua)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnXoa)
                .addGap(18, 18, 18)
                .addComponent(btnLamMoi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 547, Short.MAX_VALUE)
                .addComponent(btnXuatExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btnXuatExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Chi ti\u1ebft s\u1ea3n ph\u1ea9m");

        jLabel3.setText("\u0110\u01b0\u1eddng k\u00ednh");
        jLabel4.setText("\u0110\u1ed9 d\u00e0y");
        jLabel5.setText("M\u00e0u s\u1eafc");
        jLabel6.setText("V\u1ecf");
        jLabel7.setText("D\u00e2y");
        jLabel8.setText("K\u00ednh");
        jLabel9.setText("Ki\u1ec3u d\u00e1ng");
        jLabel10.setText("B\u1ed9 m\u00e1y");
        jLabel11.setText("N\u0103ng l\u01b0\u1ee3ng c\u01a1");
        jLabel12.setText("Th\u1eddi gian");
        jLabel13.setText("Ch\u1ed1ng n\u01b0\u1edbc");
        jLabel14.setText("Tr\u1ecdng l\u01b0\u1ee3ng");
        jLabel15.setText("Ch\u1ee9c n\u0103ng kh\u00e1c");
        jLabel16.setText("B\u1ea3o h\u00e0nh");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(jTextField8, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField7))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextField6))))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField4)
                                    .addComponent(jTextField2)))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField5))))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField9)
                            .addComponent(jTextField10)
                            .addComponent(jTextField11)
                            .addComponent(jTextField13)
                            .addComponent(jTextField12)
                            .addComponent(jTextField14)
                            .addComponent(jTextField15))))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel3))
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel7))
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 204));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"ID", "T\u00ean s\u1ea3n ph\u1ea9m", "Th\u01b0\u01a1ng hi\u1ec7u", "Xu\u1ea5t x\u1ee9", "Lo\u1ea1i", "Gi\u1edbi t\u00ednh", "Gi\u00e1 b\u00e1n", "S\u1ed1 l\u01b0\u1ee3ng", "H\u00ecnh \u1ea3nh", "M\u00f4 t\u1ea3", "Nh\u00e0 cung c\u1ea5p"}
        ));
        jTable1.setRowHeight(40);
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabel1.setText("Danh s\u00e1ch s\u1ea3n ph\u1ea9m");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton5.setText("T\u00ecm ki\u1ebfm");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        btnCapNhat.setText("C\u1eadp nh\u1eadt");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnLuu.setText("L\u01b0u");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 906, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnCapNhat)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLuu)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        openAddDialog();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        loadData();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui l\u00f2ng ch\u1ecdn m\u1ed9t s\u1ea3n ph\u1ea9m \u0111\u1ec3 ch\u1ec9nh s\u1eeda!");
            return;
        }
        int id = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
        ArrayList<SanPhamDTO> products = bus.getSanPhamById(id);
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Kh\u00f4ng t\u00ecm th\u1ea5y s\u1ea3n ph\u1ea9m v\u1edbi ID: " + id);
            return;
        }
        SanPhamDTO selectedProduct = products.get(0);
        openEditDialog(selectedProduct);
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXuatExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatExcelActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ch\u1ecdn n\u01a1i l\u01b0u file Excel");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".xlsx")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
            }
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Danh s\u00e1ch s\u1ea3n ph\u1ea9m");
                TableModel model = jTable1.getModel();
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < model.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(model.getColumnName(i));
                }
                for (int i = 0; i < model.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        Object value = model.getValueAt(i, j);
                        if (value != null) {
                            row.createCell(j).setCellValue(value.toString());
                        }
                    }
                }
                try (FileOutputStream out = new FileOutputStream(fileToSave)) {
                    workbook.write(out);
                }
                JOptionPane.showMessageDialog(this, "Xu\u1ea5t Excel th\u00e0nh c\u00f4ng!");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "L\u1ed7i khi xu\u1ea5t Excel: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_btnXuatExcelActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        int row = jTable1.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui l\u00f2ng ch\u1ecdn m\u1ed9t s\u1ea3n ph\u1ea9m \u0111\u1ec3 x\u00f3a!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(null,
                "B\u1ea1n c\u00f3 ch\u1eafc ch\u1eafn mu\u1ed1n x\u00f3a s\u1ea3n ph\u1ea9m n\u00e0y kh\u00f4ng?",
                "X\u00e1c nh\u1eadn x\u00f3a", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            int selectedID = Integer.parseInt(jTable1.getValueAt(row, 0).toString());
            bus.deleteSanPham(selectedID);
            loadDataToTable();
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        String searchText = jTextField1.getText();
        if (searchText.trim().isEmpty()) {
            loadDataToTable();
            return;
        }
        ArrayList<SanPhamDTO> listBySearch = bus.getSanPhamByName(searchText);
        String[] columnNames = {"ID", "T\u00ean s\u1ea3n ph\u1ea9m", "Th\u01b0\u01a1ng hi\u1ec7u", "Xu\u1ea5t x\u1ee9", "Lo\u1ea1i", "Gi\u1edbi t\u00ednh", "Gi\u00e1 b\u00e1n", "S\u1ed1 l\u01b0\u1ee3ng", "H\u00ecnh \u1ea3nh", "M\u00f4 t\u1ea3", "Nh\u00e0 cung c\u1ea5p"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 8) {
                    return ImageIcon.class;
                }
                return Object.class;
            }
        };
        for (SanPhamDTO p : listBySearch) {
            model.addRow(buildRow(p));
        }
        jTable1.setModel(model);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui l\u00f2ng ch\u1ecdn m\u1ed9t s\u1ea3n ph\u1ea9m \u0111\u1ec3 c\u1eadp nh\u1eadt chi ti\u1ebft!");
            return;
        }
        setEditable(true);
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui l\u00f2ng ch\u1ecdn m\u1ed9t s\u1ea3n ph\u1ea9m \u0111\u1ec3 l\u01b0u chi ti\u1ebft!");
            return;
        }
        int id = Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString());
        ProductDetail detail = new ProductDetail();
        detail.setID(id);
        detail.setDuongKinhMat(jTextField2.getText());
        detail.setDoDayMat(jTextField3.getText());
        detail.setMauMatSo(jTextField4.getText());
        detail.setChatLieuVo(jTextField5.getText());
        detail.setChatLieuDay(jTextField6.getText());
        detail.setKinh(jTextField7.getText());
        detail.setBoMay(jTextField8.getText());
        detail.setNangLuongCo(jTextField9.getText());
        detail.setThoiGianTruCoc(jTextField10.getText());
        detail.setDoChiuNuoc(jTextField11.getText());
        detail.setKieuMat(jTextField12.getText());
        String trongLuongText = jTextField13.getText();
        if (trongLuongText == null || trongLuongText.trim().isEmpty() || trongLuongText.equals("Ch\u01b0a c\u1eadp nh\u1eadp")) {
            detail.setTrongLuong(0.0f);
        } else {
            detail.setTrongLuong(Float.valueOf(trongLuongText));
        }
        detail.setChucNangKhac(jTextField14.getText());
        detail.setBaoHanh(jTextField15.getText());
        boolean result = ctbus.updateCTSanPham(detail);
        if (result) {
            JOptionPane.showMessageDialog(this, "C\u1eadp nh\u1eadt chi ti\u1ebft s\u1ea3n ph\u1ea9m th\u00e0nh c\u00f4ng!");
        } else {
            JOptionPane.showMessageDialog(this, "C\u1eadp nh\u1eadt th\u1ea5t b\u1ea1i!");
        }
        setEditable(false);
    }//GEN-LAST:event_btnLuuActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXuatExcel;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables

    private Object[] buildRow(SanPhamDTO p) {
        String resourcePath = p.getHinhAnh();
        URL imgURL = getClass().getResource(resourcePath);
        ImageIcon resizedIcon = null;
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            if (icon.getImage() != null) {
                Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                resizedIcon = new ImageIcon(img);
            }
        }
        return new Object[]{
            p.getID(), p.getTenSP(), p.getThuongHieu(), p.getXuatXu(),
            loaiMap.getOrDefault(p.getMaLoai(), "Lo\u1ea1i " + p.getMaLoai()),
            p.getGioiTinh(), p.getGiaBan(), p.getSoLuong(), resizedIcon,
            p.getMoTa(),
            nccMap.getOrDefault(p.getMaNCC(), "NCC " + p.getMaNCC())
        };
    }

    private void loadDataToTable() {
        list = bus.getSanPham();
        String[] columnNames = {"ID", "T\u00ean s\u1ea3n ph\u1ea9m", "Th\u01b0\u01a1ng hi\u1ec7u", "Xu\u1ea5t x\u1ee9", "Lo\u1ea1i", "Gi\u1edbi t\u00ednh", "Gi\u00e1 b\u00e1n", "S\u1ed1 l\u01b0\u1ee3ng", "H\u00ecnh \u1ea3nh", "M\u00f4 t\u1ea3", "Nh\u00e0 cung c\u1ea5p"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 8) {
                    return ImageIcon.class;
                }
                return Object.class;
            }
        };
        for (SanPhamDTO p : list) {
            model.addRow(buildRow(p));
        }
        jTable1.setModel(model);
    }

    private void loadDataToDetailForm() {
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selected = jTable1.getSelectedRow();
                if (selected < 0) {
                    clearDetailForm();
                    return;
                }
                int selectedID = Integer.parseInt(jTable1.getValueAt(selected, 0).toString());
                ProductDetail detail = ctbus.getDetail(selectedID);
                if (detail == null) {
                    setDetailForm("Ch\u01b0a c\u1eadp nh\u1eadp");
                } else {
                    jTextField2.setText(detail.getDuongKinhMat());
                    jTextField3.setText(detail.getDoDayMat());
                    jTextField4.setText(detail.getMauMatSo());
                    jTextField5.setText(detail.getChatLieuVo());
                    jTextField6.setText(detail.getChatLieuDay());
                    jTextField7.setText(detail.getKinh());
                    jTextField8.setText(detail.getBoMay());
                    jTextField9.setText(detail.getNangLuongCo());
                    jTextField10.setText(detail.getThoiGianTruCoc());
                    jTextField11.setText(detail.getDoChiuNuoc());
                    jTextField12.setText(detail.getKieuMat());
                    jTextField13.setText(String.valueOf(detail.getTrongLuong()));
                    jTextField14.setText(detail.getChucNangKhac());
                    jTextField15.setText(detail.getBaoHanh());
                }
            }
        });
    }

    private void clearDetailForm() {
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
        jTextField5.setText("");
        jTextField6.setText("");
        jTextField7.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
        jTextField10.setText("");
        jTextField11.setText("");
        jTextField12.setText("");
        jTextField13.setText("");
        jTextField14.setText("");
        jTextField15.setText("");
    }

    private void setDetailForm(String value) {
        jTextField2.setText(value);
        jTextField3.setText(value);
        jTextField4.setText(value);
        jTextField5.setText(value);
        jTextField6.setText(value);
        jTextField7.setText(value);
        jTextField8.setText(value);
        jTextField9.setText(value);
        jTextField10.setText(value);
        jTextField11.setText(value);
        jTextField12.setText(value);
        jTextField13.setText(value);
        jTextField14.setText(value);
        jTextField15.setText(value);
    }

    private void setEditable(boolean editable) {
        jTextField2.setEditable(editable);
        jTextField3.setEditable(editable);
        jTextField4.setEditable(editable);
        jTextField5.setEditable(editable);
        jTextField6.setEditable(editable);
        jTextField7.setEditable(editable);
        jTextField8.setEditable(editable);
        jTextField9.setEditable(editable);
        jTextField10.setEditable(editable);
        jTextField11.setEditable(editable);
        jTextField12.setEditable(editable);
        jTextField13.setEditable(editable);
        jTextField14.setEditable(editable);
        jTextField15.setEditable(editable);
    }

    private void loadData() {
        loaiMap.clear();
        for (Loai l : new LoaiDAO().getAll()) {
            loaiMap.put(l.getMaLoai(), l.getTenLoai());
        }
        nccMap.clear();
        for (DTO.NhaCungCapDTO n : new NhaCungCapDAO().getAll()) {
            nccMap.put(n.getMaNCC(), n.getTenNCC());
        }
        loadDataToTable();
        loadDataToDetailForm();
        setEditable(false);
    }

    private void openEditDialog(SanPhamDTO p) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "S\u1eeda s\u1ea3n ph\u1ea9m", true);
        dialog.setSize(400, 500);
        dialog.setLayout(new GridLayout(11, 2, 5, 5));

        JTextField txtTenSP = new JTextField(p.getTenSP());
        JTextField txtThuongHieu = new JTextField(p.getThuongHieu());
        JTextField txtXuatXu = new JTextField(p.getXuatXu());

        ArrayList<Loai> dsLoai = new LoaiDAO().getAll();
        JComboBox<Loai> cbLoai = new JComboBox<>();
        for (Loai l : dsLoai) {
            cbLoai.addItem(l);
        }
        for (int i = 0; i < dsLoai.size(); i++) {
            if (dsLoai.get(i).getMaLoai() == p.getMaLoai()) {
                cbLoai.setSelectedIndex(i);
                break;
            }
        }

        JTextField txtGioiTinh = new JTextField(p.getGioiTinh());
        JTextField txtGiaBan = new JTextField(String.valueOf(p.getGiaBan()));
        JTextField txtSoLuong = new JTextField(String.valueOf(p.getSoLuong()));
        JTextField txtHinhAnh = new JTextField(p.getHinhAnh());
        JTextField txtMoTa = new JTextField(p.getMoTa());

        ArrayList<DTO.NhaCungCapDTO> dsNCC = new NhaCungCapDAO().getAll();
        JComboBox<DTO.NhaCungCapDTO> cbNCC = new JComboBox<>();
        for (DTO.NhaCungCapDTO n : dsNCC) {
            cbNCC.addItem(n);
        }
        for (int i = 0; i < dsNCC.size(); i++) {
            if (dsNCC.get(i).getMaNCC() == p.getMaNCC()) {
                cbNCC.setSelectedIndex(i);
                break;
            }
        }

        dialog.add(new JLabel("T\u00ean s\u1ea3n ph\u1ea9m:"));
        dialog.add(txtTenSP);
        dialog.add(new JLabel("Th\u01b0\u01a1ng hi\u1ec7u:"));
        dialog.add(txtThuongHieu);
        dialog.add(new JLabel("Xu\u1ea5t x\u1ee9:"));
        dialog.add(txtXuatXu);
        dialog.add(new JLabel("Lo\u1ea1i:"));
        dialog.add(cbLoai);
        dialog.add(new JLabel("Gi\u1edbi t\u00ednh:"));
        dialog.add(txtGioiTinh);
        dialog.add(new JLabel("Gi\u00e1 b\u00e1n:"));
        dialog.add(txtGiaBan);
        dialog.add(new JLabel("S\u1ed1 l\u01b0\u1ee3ng:"));
        dialog.add(txtSoLuong);
        dialog.add(new JLabel("H\u00ecnh \u1ea3nh:"));
        dialog.add(txtHinhAnh);
        dialog.add(new JLabel("M\u00f4 t\u1ea3:"));
        dialog.add(txtMoTa);
        dialog.add(new JLabel("Nh\u00e0 cung c\u1ea5p:"));
        dialog.add(cbNCC);

        JButton btnSave = new JButton("L\u01b0u");
        JButton btnCancel = new JButton("H\u1ee7y");
        dialog.add(btnSave);
        dialog.add(btnCancel);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnSave.addActionListener(e -> {
            try {
                p.setTenSP(txtTenSP.getText());
                p.setThuongHieu(txtThuongHieu.getText());
                p.setXuatXu(txtXuatXu.getText());
                p.setMaLoai(((Loai) cbLoai.getSelectedItem()).getMaLoai());
                p.setGioiTinh(txtGioiTinh.getText());
                String giaBanStr = txtGiaBan.getText().trim().replace(".", "");
                p.setGiaBan(new BigDecimal(giaBanStr));
                p.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
                p.setHinhAnh(txtHinhAnh.getText());
                p.setMoTa(txtMoTa.getText());
                p.setMaNCC(((DTO.NhaCungCapDTO) cbNCC.getSelectedItem()).getMaNCC());
                if (bus.updateSanPham(p)) {
                    JOptionPane.showMessageDialog(dialog, "C\u1eadp nh\u1eadt th\u00e0nh c\u00f4ng!");
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(dialog, "C\u1eadp nh\u1eadt th\u1ea5t b\u1ea1i!");
                }
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "L\u1ed7i: Gi\u00e1 b\u00e1n v\u00e0 s\u1ed1 l\u01b0\u1ee3ng ph\u1ea3i l\u00e0 s\u1ed1!", "L\u1ed7i", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void openAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Th\u00eam s\u1ea3n ph\u1ea9m m\u1edbi", true);
        dialog.setSize(400, 500);
        dialog.setLayout(new GridLayout(11, 2, 5, 5));

        JTextField txtTenSP = new JTextField();
        JTextField txtThuongHieu = new JTextField();
        JTextField txtXuatXu = new JTextField();

        ArrayList<Loai> dsLoai = new LoaiDAO().getAll();
        JComboBox<Loai> cbLoai = new JComboBox<>();
        for (Loai l : dsLoai) {
            cbLoai.addItem(l);
        }

        JTextField txtGioiTinh = new JTextField();
        JTextField txtGiaBan = new JTextField();
        JTextField txtSoLuong = new JTextField();
        JTextField txtHinhAnh = new JTextField("/images/");
        JTextField txtMoTa = new JTextField();

        ArrayList<DTO.NhaCungCapDTO> dsNCC = new NhaCungCapDAO().getAll();
        JComboBox<DTO.NhaCungCapDTO> cbNCC = new JComboBox<>();
        for (DTO.NhaCungCapDTO n : dsNCC) {
            cbNCC.addItem(n);
        }

        dialog.add(new JLabel("T\u00ean s\u1ea3n ph\u1ea9m:"));
        dialog.add(txtTenSP);
        dialog.add(new JLabel("Th\u01b0\u01a1ng hi\u1ec7u:"));
        dialog.add(txtThuongHieu);
        dialog.add(new JLabel("Xu\u1ea5t x\u1ee9:"));
        dialog.add(txtXuatXu);
        dialog.add(new JLabel("Lo\u1ea1i:"));
        dialog.add(cbLoai);
        dialog.add(new JLabel("Gi\u1edbi t\u00ednh:"));
        dialog.add(txtGioiTinh);
        dialog.add(new JLabel("Gi\u00e1 b\u00e1n:"));
        dialog.add(txtGiaBan);
        dialog.add(new JLabel("S\u1ed1 l\u01b0\u1ee3ng:"));
        dialog.add(txtSoLuong);
        dialog.add(new JLabel("H\u00ecnh \u1ea3nh:"));
        dialog.add(txtHinhAnh);
        dialog.add(new JLabel("M\u00f4 t\u1ea3:"));
        dialog.add(txtMoTa);
        dialog.add(new JLabel("Nh\u00e0 cung c\u1ea5p:"));
        dialog.add(cbNCC);

        JButton btnAdd = new JButton("Th\u00eam m\u1edbi");
        JButton btnCancel = new JButton("H\u1ee7y");
        dialog.add(btnAdd);
        dialog.add(btnCancel);

        btnCancel.addActionListener(e -> dialog.dispose());
        btnAdd.addActionListener(e -> {
            try {
                if (txtTenSP.getText().isEmpty() || txtGiaBan.getText().isEmpty() || txtSoLuong.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Vui l\u00f2ng nh\u1eadp \u0111\u1ea7y \u0111\u1ee7 T\u00ean, Gi\u00e1 v\u00e0 S\u1ed1 l\u01b0\u1ee3ng!");
                    return;
                }
                SanPhamDTO p = new SanPhamDTO();
                p.setTenSP(txtTenSP.getText());
                p.setThuongHieu(txtThuongHieu.getText());
                p.setXuatXu(txtXuatXu.getText());
                p.setMaLoai(((Loai) cbLoai.getSelectedItem()).getMaLoai());
                p.setGioiTinh(txtGioiTinh.getText());
                String giaBanStr = txtGiaBan.getText().trim().replace(".", "");
                p.setGiaBan(new BigDecimal(giaBanStr));
                p.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
                p.setHinhAnh(txtHinhAnh.getText());
                p.setMoTa(txtMoTa.getText());
                p.setMaNCC(((DTO.NhaCungCapDTO) cbNCC.getSelectedItem()).getMaNCC());

                int newID = bus.insertSanPham(p);
                ProductDetail newCT = new ProductDetail(newID);
                boolean result = ctbus.insertCTSanPham(newCT);
                if (newID > 0 && result) {
                    JOptionPane.showMessageDialog(dialog, "Th\u00eam s\u1ea3n ph\u1ea9m th\u00e0nh c\u00f4ng!");
                    loadDataToTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Th\u00eam th\u1ea5t b\u1ea1i!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "L\u1ed7i: Gi\u00e1 b\u00e1n v\u00e0 s\u1ed1 l\u01b0\u1ee3ng ph\u1ea3i l\u00e0 s\u1ed1!", "L\u1ed7i", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "L\u1ed7i h\u1ec7 th\u1ed1ng: " + ex.getMessage());
            }
        });

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
