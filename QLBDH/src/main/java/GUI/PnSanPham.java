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
import java.awt.BorderLayout;
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
    private boolean isSortAscending = true;

    public PnSanPham(PhanQuyen pq) {
        initComponents();
        this.phanQuyen = pq;
        loadData();
        loadDataToCboLoc();
        configureByPermission();
    }

    private void configureByPermission() {
        boolean isKho = phanQuyen.isKhoSuaSanPham();
        boolean isKinhDoanh = phanQuyen.isBhLapPhieuXuat()
                || phanQuyen.isBhThongKeDoanhThu()
                || phanQuyen.isBhThongKeLoiNhuan();

        // Kho: full quyền
        if (isKho) {
            btnThem.setVisible(true);
            btnXoa.setVisible(true);
        } // Kinh doanh: không thêm, không xóa
        else if (isKinhDoanh) {
            btnThem.setVisible(false);
            btnXoa.setVisible(false);
        }

        // chung
        btnSua.setVisible(true);
        btnLamMoi.setVisible(true);
        btnSortAZ.setVisible(true);
        btnXuatExcel.setVisible(true);

        btnXuatExcel.setVisible(phanQuyen.isKhoXemSanPham());
//        btnThem.setVisible(phanQuyen.isKhoThemSanPham());
//        btnSua.setVisible(phanQuyen.isKhoSuaSanPham());
//        btnXoa.setVisible(phanQuyen.isKhoXoaSanPham());
//        btnCapNhat.setVisible(phanQuyen.isKhoSuaSanPham());
//        btnLuu.setVisible(phanQuyen.isKhoSuaSanPham());
//        btnXuatExcel.setVisible(phanQuyen.isKhoXemSanPham());
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
        btnSortAZ = new javax.swing.JButton();
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
        tblSanPham = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        btnCapNhat = new javax.swing.JButton();
        btnLuu = new javax.swing.JButton();
        cboLoc = new javax.swing.JComboBox<>();

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

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sua.png"))); // NOI18N
        btnSua.setText("Chỉnh sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/xoa.png"))); // NOI18N
        btnXoa.setText("Xoá sản phẩm");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnXuatExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/export.png"))); // NOI18N
        btnXuatExcel.setText("Xuất Excel");
        btnXuatExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatExcelActionPerformed(evt);
            }
        });

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/them.png"))); // NOI18N
        btnThem.setText("Thêm sản phẩm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
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

        btnSortAZ.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSortAZ.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sort.png"))); // NOI18N
        btnSortAZ.setText("Sắp xếp");
        btnSortAZ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSortAZActionPerformed(evt);
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
                .addGap(18, 18, 18)
                .addComponent(btnSortAZ)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 424, Short.MAX_VALUE)
                .addComponent(btnXuatExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSortAZ, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(btnXuatExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.PAGE_START);

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Chi tiết sản phẩm");

        jLabel3.setText("Đường kính");

        jLabel4.setText("Độ dày");

        jLabel5.setText("Màu sắc");

        jLabel6.setText("Vỏ");

        jLabel7.setText("Dây");

        jLabel8.setText("Kính");

        jLabel9.setText("Kiểu dáng");

        jLabel10.setText("Bộ máy");

        jLabel11.setText("Năng lượng cơ");

        jLabel12.setText("Thời gian");

        jLabel13.setText("Chống nước");

        jLabel14.setText("Trọng lượng");

        jLabel15.setText("Chức năng khác");

        jLabel16.setText("Bảo hành");

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

        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblSanPham.setRowHeight(40);
        jScrollPane1.setViewportView(tblSanPham);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Danh sách sản phẩm");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/timkiem.png"))); // NOI18N
        jButton5.setText("Tìm kiếm");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cap_nhat.png"))); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/diskette.png"))); // NOI18N
        btnLuu.setText("Lưu");
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        cboLoc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cboLoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLocActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cboLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGap(18, 30, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cboLoc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(92, 92, 92))
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
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui l\u00f2ng ch\u1ecdn m\u1ed9t s\u1ea3n ph\u1ea9m \u0111\u1ec3 ch\u1ec9nh s\u1eeda!");
            return;
        }
        int id = Integer.parseInt(tblSanPham.getValueAt(selectedRow, 0).toString());
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
                TableModel model = tblSanPham.getModel();
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
        int row = tblSanPham.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui l\u00f2ng ch\u1ecdn m\u1ed9t s\u1ea3n ph\u1ea9m \u0111\u1ec3 x\u00f3a!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(null,
                "B\u1ea1n c\u00f3 ch\u1eafc ch\u1eafn mu\u1ed1n x\u00f3a s\u1ea3n ph\u1ea9m n\u00e0y kh\u00f4ng?",
                "X\u00e1c nh\u1eadn x\u00f3a", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            int selectedID = Integer.parseInt(tblSanPham.getValueAt(row, 0).toString());
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
        tblSanPham.setModel(model);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui l\u00f2ng ch\u1ecdn m\u1ed9t s\u1ea3n ph\u1ea9m \u0111\u1ec3 c\u1eadp nh\u1eadt chi ti\u1ebft!");
            return;
        }
        setEditable(true);
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui l\u00f2ng ch\u1ecdn m\u1ed9t s\u1ea3n ph\u1ea9m \u0111\u1ec3 l\u01b0u chi ti\u1ebft!");
            return;
        }
        int id = Integer.parseInt(tblSanPham.getValueAt(selectedRow, 0).toString());
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

    private void btnSortAZActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSortAZActionPerformed
        // 1. Kiểm tra danh sách có dữ liệu hay không
        if (list == null || list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Danh sách sản phẩm đang trống, không thể sắp xếp!");
            return;
        }

        // 2. Thực hiện sắp xếp dựa trên trạng thái của biến isSortAscending
        if (isSortAscending) {
            // Sắp xếp A -> Z
            list.sort((sp1, sp2) -> sp1.getTenSP().compareToIgnoreCase(sp2.getTenSP()));
            // JOptionPane.showMessageDialog(this, "Đã sắp xếp theo tên: A -> Z");
        } else {
            // Sắp xếp Z -> A
            list.sort((sp1, sp2) -> sp2.getTenSP().compareToIgnoreCase(sp1.getTenSP()));
            // JOptionPane.showMessageDialog(this, "Đã sắp xếp theo tên: Z -> A");
        }

        // 3. Đảo ngược trạng thái cờ cho lần nhấn sau
        isSortAscending = !isSortAscending;

        // 4. Cập nhật lại giao diện bảng
        refreshTableFromList();
    }//GEN-LAST:event_btnSortAZActionPerformed

    private void cboLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLocActionPerformed
        String selectedLoai = (String) cboLoc.getSelectedItem();

        if (selectedLoai == null || selectedLoai.equals("Tất cả các loại")) {
            loadDataToTable(); // Nếu chọn "Tất cả", gọi hàm load mặc định
        } else {
            filterSanPhamByLoai(selectedLoai);
        }
    }//GEN-LAST:event_cboLocActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnSortAZ;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXuatExcel;
    private javax.swing.JComboBox<String> cboLoc;
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
    private javax.swing.JTable tblSanPham;
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

    private void refreshTableFromList() {
        String[] columnNames = {"ID", "Tên sản phẩm", "Thương hiệu", "Xuất xứ", "Loại",
            "Giới tính", "Giá bán", "Số lượng", "Hình ảnh", "Mô tả", "Nhà cung cấp"};
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
        tblSanPham.setModel(model);
    }

    private void filterSanPhamByLoai(String tenLoai) {
        // Cập nhật lại list từ BUS để đảm bảo dữ liệu mới nhất
        list = bus.getSanPham();

        DefaultTableModel model = (DefaultTableModel) tblSanPham.getModel();
        model.setRowCount(0); // Xóa bảng cũ

        for (SanPhamDTO p : list) {
            // Kiểm tra nếu tên loại của sản phẩm trùng với loại đang lọc
            if (p.getTenLoai().equalsIgnoreCase(tenLoai)) {
                // Thêm dòng vào table (Copy lại các cột giống trong hàm loadDataToTable của bạn)
                model.addRow(buildRow(p));
            }
        }
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
        tblSanPham.setModel(model);
    }

    private void loadDataToDetailForm() {
        tblSanPham.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selected = tblSanPham.getSelectedRow();
                if (selected < 0) {
                    clearDetailForm();
                    return;
                }
                int selectedID = Integer.parseInt(tblSanPham.getValueAt(selected, 0).toString());
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

    private void loadDataToCboLoc() {
        LoaiDAO loaiDao = new LoaiDAO();
        ArrayList<Loai> dsLoai = loaiDao.getAll(); // Giả sử LoaiDAO đã có hàm getALL()

        cboLoc.removeAllItems();
        cboLoc.addItem("Tất cả các loại"); // Lựa chọn mặc định để hiển thị hết

        for (Loai l : dsLoai) {
            cboLoc.addItem(l.getTenLoai());
        }
    }

    private void openEditDialog(SanPhamDTO p) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "S\u1eeda s\u1ea3n ph\u1ea9m", true);
        dialog.setSize(500, 500);
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

        // Phân quyền form sửa
        boolean isKho = phanQuyen.isKhoSuaSanPham();
        boolean isKinhDoanh = phanQuyen.isBhLapPhieuXuat()
                || phanQuyen.isBhThongKeDoanhThu()
                || phanQuyen.isBhThongKeLoiNhuan();

        if (isKho) {
            // Kho: sửa tất cả TRỪ giá
            txtGiaBan.setEditable(false);
        } else if (isKinhDoanh) {
            // Kinh doanh: chỉ sửa giá
            txtTenSP.setEditable(false);
            txtThuongHieu.setEditable(false);
            txtXuatXu.setEditable(false);
            cbLoai.setEnabled(false);
            txtGioiTinh.setEditable(false);
            txtSoLuong.setEditable(false);
            txtHinhAnh.setEditable(false);
            txtMoTa.setEditable(false);
            cbNCC.setEnabled(false);

            txtGiaBan.setEditable(true);
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
        dialog.add(new JLabel("Hình ảnh:"));

        txtHinhAnh.setEditable(false);

        JPanel panelAnh = new JPanel(new BorderLayout());
        panelAnh.add(txtHinhAnh, BorderLayout.CENTER);

        JButton btnChonAnh = new JButton("Chọn ảnh");
        panelAnh.add(btnChonAnh, BorderLayout.EAST);

        dialog.add(panelAnh);
        dialog.add(new JLabel("M\u00f4 t\u1ea3:"));
        dialog.add(txtMoTa);
        dialog.add(new JLabel("Nh\u00e0 cung c\u1ea5p:"));
        dialog.add(cbNCC);

        JButton btnSave = new JButton("L\u01b0u");
        btnSave.setIcon(new ImageIcon("src/main/resources/icon/xacnhan.png"));
        JButton btnCancel = new JButton("H\u1ee7y");
        btnCancel.setIcon(new ImageIcon("src/main/resources/icon/close.png"));

        dialog.add(btnSave);
        dialog.add(btnCancel);

        btnChonAnh.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn hình ảnh");

            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image files", "jpg", "png", "jpeg"));

            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String fileName = selectedFile.getName();

                File dest = new File("src/main/resources/images/" + fileName);
                try {
                    java.nio.file.Files.copy(
                            selectedFile.toPath(),
                            dest.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );

                    txtHinhAnh.setText("/images/" + fileName);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi copy ảnh: " + ex.getMessage());
                }
            }
        });

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
        dialog.setSize(500, 500);
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
        txtGiaBan.setEditable(false);
        JTextField txtSoLuong = new JTextField();
        JTextField txtHinhAnh = new JTextField();
        txtHinhAnh.setEditable(false);

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
        dialog.add(new JLabel("Hình ảnh:"));

        JPanel panelAnh = new JPanel(new BorderLayout());
        panelAnh.add(txtHinhAnh, BorderLayout.CENTER);

        JButton btnChonAnh = new JButton("Chọn ảnh");
        panelAnh.add(btnChonAnh, BorderLayout.EAST);

        dialog.add(panelAnh);

        dialog.add(new JLabel("M\u00f4 t\u1ea3:"));
        dialog.add(txtMoTa);
        dialog.add(new JLabel("Nh\u00e0 cung c\u1ea5p:"));
        dialog.add(cbNCC);

        JButton btnAdd = new JButton("Lưu");
        btnAdd.setIcon(new ImageIcon("src/main/resources/icon/xacnhan.png"));
        JButton btnCancel = new JButton("H\u1ee7y");
        btnCancel.setIcon(new ImageIcon("src/main/resources/icon/close.png"));

        dialog.add(btnAdd);
        dialog.add(btnCancel);

        btnChonAnh.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn hình ảnh");

            // Chỉ cho chọn file ảnh
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                    "Image files", "jpg", "png", "jpeg"));

            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();

                // Lấy tên file
                String fileName = selectedFile.getName();

                // Copy file vào thư mục resources/images
                File dest = new File("src/main/resources/images/" + fileName);
                try {
                    java.nio.file.Files.copy(
                            selectedFile.toPath(),
                            dest.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING
                    );

                    // Lưu path dạng resource
                    txtHinhAnh.setText("/images/" + fileName);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi copy ảnh: " + ex.getMessage());
                }
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
        btnAdd.addActionListener(e -> {
            try {
                if (txtTenSP.getText().isEmpty() || txtSoLuong.getText().isEmpty()) {
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
                p.setGiaBan(BigDecimal.ZERO);
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
