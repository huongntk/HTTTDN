package GUI;

import BUS.KhachHangBUS;
import DTO.KhachHangDTO;
import java.awt.event.KeyAdapter; 
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PnKhachHang extends javax.swing.JPanel {

    private KhachHangBUS khachHangBUS;
    private DefaultTableModel modelKhachHang;

    public PnKhachHang() {
        initComponents();
        
        khachHangBUS = new KhachHangBUS();
        modelKhachHang = (DefaultTableModel) tblKhachHang.getModel();

        javax.swing.table.JTableHeader header = tblKhachHang.getTableHeader();
        ((javax.swing.table.DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.JLabel.CENTER);
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        
        loadData();
        
        addEvents();
    }
    
    private void loadData() {
        modelKhachHang.setRowCount(0); 
        ArrayList<KhachHangDTO> danhSach = khachHangBUS.layDanhSachKhachHang();

        for (KhachHangDTO kh : danhSach) {
            String hoTen = kh.getHo() + " " + kh.getTen();
            String gioiTinh = kh.getGioiTinh();
            String tinhTrang = kh.isTinhTrang() ? "Hoạt động" : "Không hoạt động";

            Object[] row = new Object[]{
                kh.getMaKH(),
                hoTen,
                gioiTinh,
                kh.getSoDienThoai(),
                String.format("%,d", kh.getTongChiTieu()), 
                tinhTrang
            };
            modelKhachHang.addRow(row);
        }
    }
    
    private void timKiemData(String tuKhoa) {
        modelKhachHang.setRowCount(0); 
        ArrayList<KhachHangDTO> danhSach = khachHangBUS.timKiemKhachHang(tuKhoa);

        for (KhachHangDTO kh : danhSach) {
            String hoTen = kh.getHo() + " " + kh.getTen();
            String gioiTinh = kh.getGioiTinh();
            String tinhTrang = kh.isTinhTrang() ? "Hoạt động" : "Không hoạt động";

            Object[] row = new Object[]{
                kh.getMaKH(),
                hoTen,
                gioiTinh,
                kh.getSoDienThoai(),
                String.format("%,d", kh.getTongChiTieu()), 
                tinhTrang
            };
            modelKhachHang.addRow(row);
        }
    }

    private void lamMoiForm() {
        txtMaKH.setText("");
        txtHo.setText("");
        txtTen.setText("");
        txtSoDienThoai.setText("");
        rdoNam.setSelected(true);
        rdoHoatDong.setSelected(true);
        tblKhachHang.clearSelection(); 
    }
    
    private void addEvents() {
        
        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblKhachHang.getSelectedRow();
                if (selectedRow != -1) {
                    String maKH = modelKhachHang.getValueAt(selectedRow, 0).toString();
                    
                    ArrayList<KhachHangDTO> danhSach = khachHangBUS.layDanhSachKhachHang();
                    KhachHangDTO kh = null;
                    for (KhachHangDTO item : danhSach) {
                        if (item.getMaKH() == Integer.parseInt(maKH)) {
                            kh = item;
                            break;
                        }
                    }
                    
                    if (kh != null) {
                        txtMaKH.setText(String.valueOf(kh.getMaKH()));
                        txtHo.setText(kh.getHo());
                        txtTen.setText(kh.getTen());
                        txtSoDienThoai.setText(kh.getSoDienThoai());

                        if (kh.getGioiTinh().equalsIgnoreCase("Nam")) {
                            rdoNam.setSelected(true);
                        } else {
                            rdoNu.setSelected(true);
                        }

                        if (kh.isTinhTrang()) {
                            rdoHoatDong.setSelected(true);
                        } else {
                            rdoKhongHoatDong.setSelected(true);
                        }
                    }
                }
            }
        });

        btnThem.addActionListener(e -> {
            String ho = txtHo.getText().trim();
            String ten = txtTen.getText().trim();
            String sdt = txtSoDienThoai.getText().trim();
            String gioiTinh = rdoNam.isSelected() ? "Nam" : "Nữ";
            boolean tinhTrang = rdoHoatDong.isSelected();

            KhachHangDTO kh = new KhachHangDTO(0, ho, ten, gioiTinh, sdt, 0, tinhTrang);

            String result = khachHangBUS.themKhachHang(kh);
            JOptionPane.showMessageDialog(this, result);
            
            if (result.contains("thành công")) {
                loadData();
                lamMoiForm();
            }
        });

        btnSua.addActionListener(e -> {
            if (txtMaKH.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int maKH = Integer.parseInt(txtMaKH.getText());
            String ho = txtHo.getText().trim();
            String ten = txtTen.getText().trim();
            String sdt = txtSoDienThoai.getText().trim();
            String gioiTinh = rdoNam.isSelected() ? "Nam" : "Nữ";
            boolean tinhTrang = rdoHoatDong.isSelected();

            KhachHangDTO kh = new KhachHangDTO(maKH, ho, ten, gioiTinh, sdt, 0, tinhTrang);

            String result = khachHangBUS.suaKhachHang(kh);
            JOptionPane.showMessageDialog(this, result);
            
            if (result.contains("thành công")) {
                loadData();
                lamMoiForm();
            }
        });

        btnXoa.addActionListener(e -> {
            if (txtMaKH.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn vô hiệu hóa khách hàng này?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                int maKH = Integer.parseInt(txtMaKH.getText());
                String result = khachHangBUS.xoaKhachHang(maKH);
                JOptionPane.showMessageDialog(this, result);
                
                if (result.contains("thành công")) {
                    loadData();
                    lamMoiForm();
                }
            }
        });

        btnLamMoi.addActionListener(e -> {
            lamMoiForm();
            loadData(); 
            txtTimKiem.setText("");
        });
        
        txtTimKiem.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String tuKhoa = txtTimKiem.getText().trim();
                timKiemData(tuKhoa); 
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        btnGroupGioiTinh = new javax.swing.ButtonGroup();
        btnGroupTinhTrang = new javax.swing.ButtonGroup();
        pnlThongTin = new javax.swing.JPanel();
        lblTieuDeThongTin = new javax.swing.JLabel();
        lblMaKH = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        lblHo = new javax.swing.JLabel();
        txtHo = new javax.swing.JTextField();
        lblTen = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        lblSoDienThoai = new javax.swing.JLabel();
        txtSoDienThoai = new javax.swing.JTextField();
        lblGioiTinh = new javax.swing.JLabel();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        lblTinhTrang = new javax.swing.JLabel();
        rdoHoatDong = new javax.swing.JRadioButton();
        rdoKhongHoatDong = new javax.swing.JRadioButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        pnlBang = new javax.swing.JPanel();
        lblTieuDeBang = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblKhachHang = new javax.swing.JTable();
        lblTimKiem = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();

        pnlThongTin.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));

        lblTieuDeThongTin.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        lblTieuDeThongTin.setText("Thông tin khách hàng");

        lblMaKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        lblMaKH.setText("Mã khách hàng:");

        txtMaKH.setEditable(false);
        txtMaKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); 

        lblHo.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        lblHo.setText("Họ:");

        txtHo.setFont(new java.awt.Font("Segoe UI", 0, 14)); 

        lblTen.setFont(new java.awt.Font("Segoe UI", 0, 14));  
        lblTen.setText("Tên:");

        txtTen.setFont(new java.awt.Font("Segoe UI", 0, 14)); 

        lblSoDienThoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        lblSoDienThoai.setText("Số điện thoại:");

        txtSoDienThoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); 

        lblGioiTinh.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        lblGioiTinh.setText("Giới tính:");

        btnGroupGioiTinh.add(rdoNam);
        rdoNam.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        rdoNam.setSelected(true);
        rdoNam.setText("Nam");

        btnGroupGioiTinh.add(rdoNu);
        rdoNu.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        rdoNu.setText("Nữ");

        lblTinhTrang.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        lblTinhTrang.setText("Tình trạng:");

        btnGroupTinhTrang.add(rdoHoatDong);
        rdoHoatDong.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        rdoHoatDong.setSelected(true);
        rdoHoatDong.setText("Hoạt động");

        btnGroupTinhTrang.add(rdoKhongHoatDong);
        rdoKhongHoatDong.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        rdoKhongHoatDong.setText("Không hoạt động");

        btnThem.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/them.png"))); 
        btnThem.setText("Thêm");

        btnSua.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/sua.png"))); 
        btnSua.setText("Sửa");

        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/xoa.png"))); 
        btnXoa.setText("Xóa");

        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); 
        btnLamMoi.setText("Làm mới");

        javax.swing.GroupLayout pnlThongTinLayout = new javax.swing.GroupLayout(pnlThongTin);
        pnlThongTin.setLayout(pnlThongTinLayout);
        pnlThongTinLayout.setHorizontalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(lblTieuDeThongTin))
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMaKH)
                                    .addComponent(lblHo)
                                    .addComponent(lblTen)
                                    .addComponent(lblSoDienThoai)
                                    .addComponent(lblGioiTinh)
                                    .addComponent(lblTinhTrang))
                                .addGap(18, 18, 18)
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtSoDienThoai)
                                    .addComponent(txtTen)
                                    .addComponent(txtHo)
                                    .addComponent(txtMaKH)
                                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                                        .addComponent(rdoNam)
                                        .addGap(18, 18, 18)
                                        .addComponent(rdoNu))
                                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                                        .addComponent(rdoHoatDong)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                                        .addComponent(rdoKhongHoatDong))))
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addComponent(btnThem)
                                .addGap(18, 18, 18)
                                .addComponent(btnSua)
                                .addGap(18, 18, 18)
                                .addComponent(btnXoa)
                                .addGap(18, 18, 18)
                                .addComponent(btnLamMoi)))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlThongTinLayout.setVerticalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTieuDeThongTin)
                .addGap(30, 30, 30)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaKH)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHo)
                    .addComponent(txtHo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTen)
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSoDienThoai)
                    .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGioiTinh)
                    .addComponent(rdoNam)
                    .addComponent(rdoNu))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTinhTrang)
                    .addComponent(rdoHoatDong)
                    .addComponent(rdoKhongHoatDong))
                .addGap(50, 50, 50)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnLamMoi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlBang.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));

        lblTieuDeBang.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        lblTieuDeBang.setText("Danh sách khách hàng");

        tblKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        tblKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã KH", "Họ và tên", "Giới tính", "Số điện thoại", "Tổng chi tiêu", "Tình trạng"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblKhachHang.setGridColor(new java.awt.Color(51, 51, 51));
        tblKhachHang.setRowHeight(23);
        tblKhachHang.setShowGrid(true);
        tblKhachHang.setShowHorizontalLines(true);
        tblKhachHang.setShowVerticalLines(true);
        jScrollPane1.setViewportView(tblKhachHang);

        lblTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); 
        lblTimKiem.setText("Tìm kiếm:");

        txtTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); 

        javax.swing.GroupLayout pnlBangLayout = new javax.swing.GroupLayout(pnlBang);
        pnlBang.setLayout(pnlBangLayout);
        pnlBangLayout.setHorizontalGroup(
            pnlBangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBangLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTieuDeBang)
                .addGap(181, 181, 181))
            .addGroup(pnlBangLayout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addComponent(lblTimKiem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBangLayout.setVerticalGroup(
            pnlBangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBangLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTieuDeBang)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlBangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTimKiem)
                    .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE) 
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlBang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlBang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }


    private javax.swing.ButtonGroup btnGroupGioiTinh;
    private javax.swing.ButtonGroup btnGroupTinhTrang;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblGioiTinh;
    private javax.swing.JLabel lblHo;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblSoDienThoai;
    private javax.swing.JLabel lblTen;
    private javax.swing.JLabel lblTieuDeBang;
    private javax.swing.JLabel lblTieuDeThongTin;
    private javax.swing.JLabel lblTimKiem;
    private javax.swing.JLabel lblTinhTrang;
    private javax.swing.JPanel pnlBang;
    private javax.swing.JPanel pnlThongTin;
    private javax.swing.JRadioButton rdoHoatDong;
    private javax.swing.JRadioButton rdoKhongHoatDong;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tblKhachHang;
    private javax.swing.JTextField txtHo;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTimKiem;
}