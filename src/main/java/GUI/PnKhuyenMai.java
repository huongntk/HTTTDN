package GUI;

import BUS.KhuyenMaiBUS;
import DTO.KhuyenMai;

import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.ButtonGroup;

public class PnKhuyenMai extends javax.swing.JPanel {

    private KhuyenMaiBUS khuyenMaiBUS;
    private DefaultTableModel modelKhuyenMai;
    
    public PnKhuyenMai() {
        initComponents();
        
        javax.swing.table.JTableHeader header = tblDSMaGiam.getTableHeader() ;
        ((javax.swing.table.DefaultTableCellRenderer) header.getDefaultRenderer())
            .setHorizontalAlignment(javax.swing.JLabel.CENTER);
            header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
            
        ButtonGroup bgTinhTrang = new ButtonGroup();
        bgTinhTrang.add(rdoHoatDong);
        bgTinhTrang.add(rdoKhongHoatDong);
        
        khuyenMaiBUS = new KhuyenMaiBUS();
        modelKhuyenMai = (DefaultTableModel) tblDSMaGiam.getModel();
        
        loadDataKhuyenMai();
        
        addEvents();
    }
    
    private void loadDataKhuyenMai() {
        modelKhuyenMai.setRowCount(0); // Xóa dữ liệu cũ

        ArrayList<KhuyenMai> danhSach = khuyenMaiBUS.layDanhSachKhuyenMai();

        for (KhuyenMai km : danhSach) {
            String tinhTrang = km.isTinhTrang() ? "Hoạt động" : "Không hoạt động";
            
            Object[] row = new Object[]{
                km.getMaGG(),
                km.getTenGG(),
                km.getPhanTramGiam() + "%",    // Hiển thị % giảm
                km.getDieuKien(),
                km.getNgayBD(),                // Hiển thị trực tiếp java.util.Date
                km.getNgayKT(),
                tinhTrang
            };

            modelKhuyenMai.addRow(row);
        }
    }
    
    private void timKiemData(String tuKhoa) {
        modelKhuyenMai.setRowCount(0); 
        ArrayList<KhuyenMai> danhSach = khuyenMaiBUS.timKiemKhuyenMai(tuKhoa);

        for (KhuyenMai km : danhSach) {
            String tinhTrang = km.isTinhTrang() ? "Hoạt động" : "Không hoạt động";

            Object[] row = new Object[]{
                km.getMaGG(),
                km.getTenGG(),
                km.getPhanTramGiam() + "%",    // Hiển thị % giảm
                km.getDieuKien(),
                km.getNgayBD(),                // Hiển thị trực tiếp java.util.Date
                km.getNgayKT(),
                tinhTrang
            };
            modelKhuyenMai.addRow(row);
        }
    }

    private void lamMoiFormKhuyenMai() {
        txtMaGiam.setText("");
        txtTenGiam.setText("");
        txtPhanTramGiam.setText("");
        txtDieuKienGiam.setText("");

        dcNgayBD.setDate(null); // Xóa ngày bắt đầu
        dcNgayKT.setDate(null); // Xóa ngày kết thúc
        
        rdoHoatDong.setSelected(true);

        tblDSMaGiam.clearSelection();       // Xóa chọn trên JTable
    }
    
    private void addEvents() {
        
        tblDSMaGiam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblDSMaGiam.getSelectedRow();
                if (selectedRow != -1) {
                    String maGG = modelKhuyenMai.getValueAt(selectedRow, 0).toString();
                    
                    ArrayList<KhuyenMai> danhSach = khuyenMaiBUS.layDanhSachKhuyenMai();
                    KhuyenMai km = null;
                    for (KhuyenMai item : danhSach) {
                        if (item.getMaGG() == Integer.parseInt(maGG)) {
                            km = item;
                            break;
                        }
                    }
                    
                    if (km != null) {
                        txtMaGiam.setText(String.valueOf(km.getMaGG()));
                        txtTenGiam.setText(km.getTenGG());
                        txtPhanTramGiam.setText(String.valueOf(km.getPhanTramGiam()));
                        txtDieuKienGiam.setText(String.valueOf(km.getDieuKien()));

                        dcNgayBD.setDate(km.getNgayBD());
                        dcNgayKT.setDate(km.getNgayKT());

                        if (km.isTinhTrang()) {
                            rdoHoatDong.setSelected(true);
                        } else {
                            rdoKhongHoatDong.setSelected(true);
                        }
                    }
                }
            }
        });
        
        btnThem.addActionListener(e -> {
            String ten = txtTenGiam.getText().trim();
            int phanTram = Integer.parseInt(txtPhanTramGiam.getText().trim());
            int dieuKien = Integer.parseInt(txtDieuKienGiam.getText().trim());
            
            Date ngayBD = dcNgayBD.getDate();
            Date ngayKT = dcNgayKT.getDate();
            boolean tinhTrang = rdoHoatDong.isSelected();
            
            KhuyenMai km = new KhuyenMai(0, ten, phanTram, dieuKien, ngayBD, ngayKT, tinhTrang);

            String result = khuyenMaiBUS.themKhuyenMai(km);
            JOptionPane.showMessageDialog(this, result);

            if (result.contains("thành công")) {
                loadDataKhuyenMai();
                lamMoiFormKhuyenMai();
            }
        });

        btnSua.addActionListener(e -> {
            if (txtMaGiam.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần sửa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String ten = txtTenGiam.getText().trim();
            int phanTram = Integer.parseInt(txtPhanTramGiam.getText().trim());
            int dieuKien = Integer.parseInt(txtDieuKienGiam.getText().trim());
            Date ngayBD = dcNgayBD.getDate();
            Date ngayKT = dcNgayKT.getDate();
            boolean tinhTrang = rdoHoatDong.isSelected();
            
            int maGG = Integer.parseInt(txtMaGiam.getText());
            KhuyenMai km = new KhuyenMai(maGG, ten, phanTram, dieuKien, ngayBD, ngayKT, tinhTrang);
            
            String result = khuyenMaiBUS.suaKhuyenMai(km);
            JOptionPane.showMessageDialog(this, result);

            if (result.contains("thành công")) {
                loadDataKhuyenMai();
                lamMoiFormKhuyenMai();
            }
        });

        btnXoa.addActionListener(e -> {
            if (txtMaGiam.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xóa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn vô hiệu hóa khuyến mãi này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                int maGG = Integer.parseInt(txtMaGiam.getText());
                String result = khuyenMaiBUS.xoaKhuyenMai(maGG);
                JOptionPane.showMessageDialog(this, result);
                
                if (result.contains("thành công")) {
                    loadDataKhuyenMai();
                    lamMoiFormKhuyenMai();
                }
            }
        });

        btnLamMoi.addActionListener(e -> {
            loadDataKhuyenMai();
            lamMoiFormKhuyenMai(); 
            txtTuKhoa.setText("");
        });
        
        txtTuKhoa.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String tuKhoa = txtTuKhoa.getText().trim();
                timKiemData(tuKhoa); 
            }
        });
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        pnlNhapTTKM = new javax.swing.JPanel();
        lblMaGiam = new javax.swing.JLabel();
        txtMaGiam = new javax.swing.JTextField();
        txtPhanTramGiam = new javax.swing.JTextField();
        lblNgayBatDau = new javax.swing.JLabel();
        lblPhanTramGiam = new javax.swing.JLabel();
        lblTenMaGiam = new javax.swing.JLabel();
        lblDieuKienGiam = new javax.swing.JLabel();
        lblNgayKetThuc = new javax.swing.JLabel();
        txtDieuKienGiam = new javax.swing.JTextField();
        txtTenGiam = new javax.swing.JTextField();
        lblThongTinMG = new javax.swing.JLabel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        dcNgayBD = new com.toedter.calendar.JDateChooser();
        dcNgayKT = new com.toedter.calendar.JDateChooser();
        rdoHoatDong = new javax.swing.JRadioButton();
        rdoKhongHoatDong = new javax.swing.JRadioButton();
        lblTinhTrang = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtTuKhoa = new javax.swing.JTextField();
        scrDSMaGiam = new javax.swing.JScrollPane();
        tblDSMaGiam = new javax.swing.JTable();
        lblTimKiem = new javax.swing.JLabel();
        lblDSMaGiam = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        pnlNhapTTKM.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), new java.awt.Color(204, 204, 204)));
        pnlNhapTTKM.setMaximumSize(new java.awt.Dimension(400, 573));
        pnlNhapTTKM.setMinimumSize(new java.awt.Dimension(390, 500));
        pnlNhapTTKM.setPreferredSize(new java.awt.Dimension(390, 537));
        pnlNhapTTKM.setLayout(new java.awt.GridBagLayout());

        lblMaGiam.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaGiam.setText("Mã giảm giá");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 37, 0, 0);
        pnlNhapTTKM.add(lblMaGiam, gridBagConstraints);

        txtMaGiam.setEditable(false);
        txtMaGiam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaGiamActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 152;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 12, 0, 0);
        pnlNhapTTKM.add(txtMaGiam, gridBagConstraints);

        txtPhanTramGiam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhanTramGiamActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 152;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 12, 0, 0);
        pnlNhapTTKM.add(txtPhanTramGiam, gridBagConstraints);

        lblNgayBatDau.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNgayBatDau.setText("Ngày bắt đầu");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 34, 0, 0);
        pnlNhapTTKM.add(lblNgayBatDau, gridBagConstraints);

        lblPhanTramGiam.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblPhanTramGiam.setText("Phần trăm giảm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 34, 0, 0);
        pnlNhapTTKM.add(lblPhanTramGiam, gridBagConstraints);

        lblTenMaGiam.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTenMaGiam.setText("Tên giảm giá");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 34, 0, 0);
        pnlNhapTTKM.add(lblTenMaGiam, gridBagConstraints);

        lblDieuKienGiam.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblDieuKienGiam.setText("Điều kiện giảm");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 34, 0, 0);
        pnlNhapTTKM.add(lblDieuKienGiam, gridBagConstraints);

        lblNgayKetThuc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNgayKetThuc.setText("Ngày kết thúc");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 34, 0, 0);
        pnlNhapTTKM.add(lblNgayKetThuc, gridBagConstraints);

        txtDieuKienGiam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDieuKienGiamActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 153;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 11, 0, 0);
        pnlNhapTTKM.add(txtDieuKienGiam, gridBagConstraints);

        txtTenGiam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenGiamActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 152;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 12, 0, 0);
        pnlNhapTTKM.add(txtTenGiam, gridBagConstraints);

        lblThongTinMG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblThongTinMG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblThongTinMG.setText("Nhập thông tin khuyến mãi");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 15;
        gridBagConstraints.ipadx = 217;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 2, 0, 0);
        pnlNhapTTKM.add(lblThongTinMG, gridBagConstraints);

        btnThem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/them.png"))); // NOI18N
        btnThem.setAlignmentY(0.0F);
        btnThem.setIconTextGap(3);
        btnThem.setLabel("Thêm");
        btnThem.setMargin(new java.awt.Insets(3, 10, 3, 10));
        btnThem.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(40, 46, 0, 0);
        pnlNhapTTKM.add(btnThem, gridBagConstraints);

        btnSua.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sua.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setAlignmentY(0.0F);
        btnSua.setIconTextGap(3);
        btnSua.setMargin(new java.awt.Insets(3, 10, 3, 10));
        btnSua.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(40, 38, 0, 0);
        pnlNhapTTKM.add(btnSua, gridBagConstraints);

        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/xoa.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setAlignmentY(0.0F);
        btnXoa.setIconTextGap(3);
        btnXoa.setMargin(new java.awt.Insets(3, 10, 3, 10));
        btnXoa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 15;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(40, 31, 0, 0);
        pnlNhapTTKM.add(btnXoa, gridBagConstraints);

        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setAlignmentY(0.0F);
        btnLamMoi.setIconTextGap(3);
        btnLamMoi.setMargin(new java.awt.Insets(3, 10, 3, 10));
        btnLamMoi.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 16;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(29, 23, 24, 0);
        pnlNhapTTKM.add(btnLamMoi, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 134;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 12, 0, 0);
        pnlNhapTTKM.add(dcNgayBD, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 134;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(26, 12, 0, 0);
        pnlNhapTTKM.add(dcNgayKT, gridBagConstraints);

        rdoHoatDong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rdoHoatDong.setText("Hoạt động");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 12, 0, 0);
        pnlNhapTTKM.add(rdoHoatDong, gridBagConstraints);

        rdoKhongHoatDong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        rdoKhongHoatDong.setText("Không hoạt động");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 8, 0, 0);
        pnlNhapTTKM.add(rdoKhongHoatDong, gridBagConstraints);

        lblTinhTrang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTinhTrang.setText("Tình trạng");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(35, 34, 0, 0);
        pnlNhapTTKM.add(lblTinhTrang, gridBagConstraints);

        jPanel1.add(pnlNhapTTKM);

        jPanel2.setMaximumSize(new java.awt.Dimension(594, 573));
        jPanel2.setMinimumSize(new java.awt.Dimension(400, 500));
        jPanel2.setPreferredSize(new java.awt.Dimension(554, 537));

        txtTuKhoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTuKhoaActionPerformed(evt);
            }
        });

        scrDSMaGiam.setPreferredSize(new java.awt.Dimension(450, 400));

        tblDSMaGiam.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã giảm giá", "Tên giảm giá", "Phần trăm", "Điều kiện", "Ngày bắt đầu", "Ngày kết thúc", "Tình trạng"
            }
        ));
        tblDSMaGiam.setRowHeight(30);
        tblDSMaGiam.setShowGrid(true);
        scrDSMaGiam.setViewportView(tblDSMaGiam);

        lblTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTimKiem.setText("Tìm kiếm");

        lblDSMaGiam.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDSMaGiam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDSMaGiam.setText("Danh sách mã giảm giá");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblDSMaGiam, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(scrDSMaGiam, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblTimKiem)
                        .addGap(12, 12, 12)
                        .addComponent(txtTuKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 403, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTuKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblDSMaGiam, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(scrDSMaGiam, javax.swing.GroupLayout.DEFAULT_SIZE, 449, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTuKhoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTuKhoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTuKhoaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnThemActionPerformed

    private void txtTenGiamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenGiamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenGiamActionPerformed

    private void txtDieuKienGiamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDieuKienGiamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDieuKienGiamActionPerformed

    private void txtPhanTramGiamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhanTramGiamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhanTramGiamActionPerformed

    private void txtMaGiamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaGiamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaGiamActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private com.toedter.calendar.JDateChooser dcNgayBD;
    private com.toedter.calendar.JDateChooser dcNgayKT;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblDSMaGiam;
    private javax.swing.JLabel lblDieuKienGiam;
    private javax.swing.JLabel lblMaGiam;
    private javax.swing.JLabel lblNgayBatDau;
    private javax.swing.JLabel lblNgayKetThuc;
    private javax.swing.JLabel lblPhanTramGiam;
    private javax.swing.JLabel lblTenMaGiam;
    private javax.swing.JLabel lblThongTinMG;
    private javax.swing.JLabel lblTimKiem;
    private javax.swing.JLabel lblTinhTrang;
    private javax.swing.JPanel pnlNhapTTKM;
    private javax.swing.JRadioButton rdoHoatDong;
    private javax.swing.JRadioButton rdoKhongHoatDong;
    private javax.swing.JScrollPane scrDSMaGiam;
    private javax.swing.JTable tblDSMaGiam;
    private javax.swing.JTextField txtDieuKienGiam;
    private javax.swing.JTextField txtMaGiam;
    private javax.swing.JTextField txtPhanTramGiam;
    private javax.swing.JTextField txtTenGiam;
    private javax.swing.JTextField txtTuKhoa;
    // End of variables declaration//GEN-END:variables
}
