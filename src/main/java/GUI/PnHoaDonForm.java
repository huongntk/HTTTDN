package GUI;

import BUS.KhuyenMaiBUS;
import BUS.HoaDonBUS; 
import BUS.KhachHangBUS;
import DTO.CTHoaDon;  
import DTO.HoaDon;
import DTO.KhachHangDTO;
import DTO.KhuyenMai;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class PnHoaDonForm extends javax.swing.JPanel {
    private HoaDonBUS hoaDonBUS = new HoaDonBUS();
    // Trong lớp PnBanHangForm hoặc lớp quản lý tab Hóa đơn
    private DefaultTableModel modelHoaDon; 
    private DefaultTableModel modelCTHoaDon;
    private final DecimalFormat currencyFormat = new DecimalFormat("#,### đ"); 
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private DefaultComboBoxModel<String> modelKhachHang; 
    private DefaultComboBoxModel<String> modelMaGiamGia;
    private KhachHangBUS khachHangBUS = new KhachHangBUS();
    private KhuyenMaiBUS khuyenmaiBUS = new KhuyenMaiBUS();
    
    public PnHoaDonForm() {
        initComponents();
        
        modelKhachHang = new DefaultComboBoxModel<>();
        cboMaKH.setModel(modelKhachHang);
        
        modelMaGiamGia = new DefaultComboBoxModel<>();
        cboGiamGia.setModel(modelMaGiamGia);

        loadDataToCombobox();
        javax.swing.table.JTableHeader header = tblChiTietHD.getTableHeader() ;
        ((javax.swing.table.DefaultTableCellRenderer) header.getDefaultRenderer())
            .setHorizontalAlignment(javax.swing.JLabel.CENTER);
            header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        
        header = tblHoaDon.getTableHeader();        
        ((javax.swing.table.DefaultTableCellRenderer) header.getDefaultRenderer())
            .setHorizontalAlignment(javax.swing.JLabel.CENTER);
            header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));

        modelHoaDon = (DefaultTableModel) tblHoaDon.getModel(); 

        modelCTHoaDon = (DefaultTableModel) tblChiTietHD.getModel(); 

        tblHoaDon.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    tblHoaDonSelectionChanged();
                }
            }
        });
              
        loadDanhSachHoaDon();

    }

    private void loadDataToCombobox() {

        modelKhachHang.removeAllElements();

        modelKhachHang.addElement("0 - Khách vãng lai"); 

        ArrayList<KhachHangDTO> listKH = khachHangBUS.layDanhSachKhachHang(); 
        for (KhachHangDTO kh : listKH) {
            modelKhachHang.addElement(kh.getMaKH() + " - " + kh.getTen());
        }

        modelMaGiamGia.removeAllElements();

        modelMaGiamGia.addElement("0 - Không áp dụng");

        KhuyenMaiBUS kmBUS = new KhuyenMaiBUS(); 
        ArrayList<KhuyenMai> listGG = kmBUS.layDanhSachKhuyenMai();
        
        for (KhuyenMai gg : listGG) {
            modelMaGiamGia.addElement(gg.getMaGG() + " - " + gg.getTenGG());
        }
    }
    

   public void hienThiHoaDonMoi(int maHD) {
       
       loadDanhSachHoaDon();
       
       for (int i = 0; i < modelHoaDon.getRowCount(); i++) {
           int currentMaHD = (int) modelHoaDon.getValueAt(i, 0); 
           if (currentMaHD == maHD) {

               tblHoaDon.setRowSelectionInterval(i, i);

               tblHoaDon.scrollRectToVisible(tblHoaDon.getCellRect(i, 0, true));

               break;
           }
       }
   }
    // Hàm hỗ trợ định dạng tiền tệ
    private String formatCurrency(double amount) {
        // Cần đảm bảo DecimalFormat đã được import
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(amount) + " đ";
    }

    private void loadDanhSachHoaDon() {
        modelHoaDon.setRowCount(0);

        // Giả sử HoaDonBUS có phương thức getAllHoaDon()
        ArrayList<DTO.HoaDon> listHD = hoaDonBUS.getAllHoaDon(); 

        // Đổ dữ liệu vào bảng Hóa đơn (tblHoaDon)
        for (DTO.HoaDon hd : listHD) {
            Object[] row = new Object[]{
                hd.getMaHD(),
                hd.getMaKH(),
                // Cần đảm bảo có cột Ngày lập (Giả sử bạn đã thêm)
                hd.getNgayLap(), 
                // Cần hàm formatCurrency để hiển thị Tổng tiền
                formatCurrency(hd.getTongTien()), 
//                hd.getTrangThai() // Nếu có cột trạng thái
            };
            modelHoaDon.addRow(row);
        }
    }
    
    private void tblHoaDonSelectionChanged() {
        int selectedRow = tblHoaDon.getSelectedRow();

        if (selectedRow >= 0) {
            try {
                // Lấy Mã HD từ cột đầu tiên (index 0)
                int maHD = (int) modelHoaDon.getValueAt(selectedRow, 0);

                // Gọi hàm load Chi tiết Hóa đơn
                loadChiTietHoaDon(maHD);

                // Cập nhật các JTextField thông tin Hóa đơn (Mã HD, Mã KH, Tổng tiền...)
                loadFormHoaDon(maHD);

            } catch (Exception ex) {
                System.err.println("Lỗi khi chọn Hóa đơn: " + ex.getMessage());
                modelCTHoaDon.setRowCount(0); // Xóa bảng chi tiết nếu có lỗi
            }
        } else {
            modelCTHoaDon.setRowCount(0); // Không chọn dòng nào thì xóa bảng chi tiết
        }
    }
    public void loadFormHoaDon(int maHD) {
        // 1. Lấy thông tin Hóa đơn (MaHD, MaKH, MaNV, MaGG, TongTien)
        HoaDon hd = hoaDonBUS.getHoaDonByMaHD(maHD); 

        if (hd != null) {
            // 2. Lấy tên tương ứng
            String tenKH = hoaDonBUS.getTenKhachHang(hd.getMaKH());
            String tenNV = hoaDonBUS.getTenNhanVien(hd.getMaNV());
            String tenGG = hoaDonBUS.getTenMaGiamGia(hd.getMaGG()); // Giả định DTO.HoaDon có getMaGG()
            String khItemToSelect = hd.getMaKH() + " - " + tenKH;
            String nvTextToDisplay = hd.getMaNV()+ " - " + tenNV;
            String ggItemToSelect = hd.getMaGG()+ " - " + tenGG;
            
            // 3. Hiển thị lên giao diện
            // Hiển thị MaHD:
            txtMaHD.setText(String.valueOf(hd.getMaHD())); 

            // Hiển thị MaKH kèm Tên
            modelKhachHang.setSelectedItem(khItemToSelect);

            // Hiển thị MaNV kèm Tên
            txtMaNV.setText(hd.getTenNV()); 

            modelMaGiamGia.setSelectedItem(ggItemToSelect);

            // Hiển thị các thông tin khác (Ngày lập, Tổng tiền...)
            txtNgayLap.setText(dateFormat.format(hd.getNgayLap()));
            txtTongTien.setText(currencyFormat.format(hd.getTongTien()));
            txtTrangThai.setText(hd.getTrangThai());
            txtGhiChu.setText(hd.getGhiChu());

            // 4. Load chi tiết hóa đơn vào tblChiTietHD
            // loadCTHoaDon(maHD); // Gọi hàm load chi tiết (tôi giả định hàm này đã tồn tại)

        } else {
            // Xử lý không tìm thấy hóa đơn
            clearHoaDonFields(); 
        }
    }
    
    public void clearHoaDonFields() {
        // 1. Xóa các trường JTextField/JLabel chứa thông tin Hóa đơn
        txtMaHD.setText("");
        txtMaNV.setText(""); // Mã Nhân viên

        // Nếu bạn có trường hiển thị Ngày lập và Tổng tiền là JLabel
        txtNgayLap.setText("..."); 
        txtTongTien.setText("0 đ"); 
        txtTrangThai.setText("");
        txtGhiChu.setText("");

        // 2. Reset JComboBox Khách hàng và Mã Giảm giá
        // Trở về mục mặc định "Khách vãng lai" (MaKH=0) và "Không áp dụng" (MaGG=0)
        if (modelKhachHang.getIndexOf("0 - Khách vãng lai") != -1) {
            cboMaKH.setSelectedIndex(0);
        }

        if (modelMaGiamGia.getIndexOf("0 - Không áp dụng") != -1) {
            cboGiamGia.setSelectedIndex(0);
        }

        // 3. Xóa dữ liệu trong bảng Chi tiết Hóa đơn
        if (modelCTHoaDon != null) {
            modelCTHoaDon.setRowCount(0);
        }

        // 4. Xóa việc chọn dòng trên bảng Hóa đơn chính (nếu cần)
        if (tblHoaDon.getSelectedRow() != -1) {
            tblHoaDon.clearSelection();
        }
    }
    private void loadChiTietHoaDon(int maHD) {
        modelCTHoaDon.setRowCount(0);

        // Giả sử HoaDonBUS có phương thức getCTHoaDonByMaHD(int maHD)
        ArrayList<CTHoaDon> listCTHD = hoaDonBUS.getCTHoaDonByMaHD(maHD); 

        // Đổ dữ liệu vào bảng Chi tiết hóa đơn (tblChiTietHoaDon)
        for (CTHoaDon cthd : listCTHD) {
            // Cấu trúc cột Chi tiết hóa đơn: Mã HD, Mã SP, Số lượng, Đơn giá, Thành tiền
            double donGia = cthd.getDonGia(); // Đơn giá gốc
            double thanhTien = donGia * cthd.getSoLuong();

            Object[] row = new Object[]{
                cthd.getMaHD(),
                cthd.getID(),
                cthd.getSoLuong(),
                formatCurrency(donGia),
                formatCurrency(thanhTien)
            };
            modelCTHoaDon.addRow(row);
        }
    }
    private int getSelectedIdFromCombobox(javax.swing.JComboBox<String> cbo) {
        String selectedItem = (String) cbo.getSelectedItem();

        if (selectedItem != null && selectedItem.contains(" - ")) {
            try {
                // Lấy phần tử trước dấu " - " (là ID)
                return Integer.parseInt(selectedItem.split(" - ")[0].trim());
            } catch (NumberFormatException e) {
                System.err.println("Lỗi parse ID từ Combobox: " + e.getMessage());
                return -1;
            }
        }
        return 0; // Trả về 0 cho Mã KH (Khách vãng lai) hoặc Mã GG (Không áp dụng) nếu chuỗi không hợp lệ
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        lblMaNV = new javax.swing.JLabel();
        lblNgayLap = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        lblMaHD = new javax.swing.JLabel();
        txtMaHD = new javax.swing.JTextField();
        lblMaKH = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        txtNgayLap = new javax.swing.JTextField();
        txtTongTien = new javax.swing.JTextField();
        lblTrangThai = new javax.swing.JLabel();
        lblTongTien1 = new javax.swing.JLabel();
        cboGiamGia = new javax.swing.JComboBox<>();
        cboMaKH = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtGhiChu = new javax.swing.JTextArea();
        btnCapNhat = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        txtTrangThai = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblTimKiemHD = new javax.swing.JLabel();
        lblGia = new javax.swing.JLabel();
        lblNgay = new javax.swing.JLabel();
        lblGiaDen = new javax.swing.JLabel();
        lblNgayDen = new javax.swing.JLabel();
        txtGiaTu = new javax.swing.JTextField();
        txtGiaDen = new javax.swing.JTextField();
        scrTimKiemHD = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        btnTim = new javax.swing.JButton();
        dcNgayTu = new com.toedter.calendar.JDateChooser();
        dcNgayDen = new com.toedter.calendar.JDateChooser();
        jPanel3 = new javax.swing.JPanel();
        lblChiTietHD = new javax.swing.JLabel();
        scrChiTietHD = new javax.swing.JScrollPane();
        tblChiTietHD = new javax.swing.JTable();
        btnXuatHD = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(510, 600));
        jPanel1.setPreferredSize(new java.awt.Dimension(510, 600));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));
        jPanel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        lblMaNV.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaNV.setText("Mã nhân viên");

        lblNgayLap.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNgayLap.setText("Ngày lập");

        lblTongTien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTongTien.setText("Tổng tiền");

        lblMaHD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaHD.setText("Mã hóa đơn");

        txtMaHD.setEditable(false);
        txtMaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHDActionPerformed(evt);
            }
        });

        lblMaKH.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblMaKH.setText("Mã khách hàng");

        txtMaNV.setEditable(false);
        txtMaNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaNVActionPerformed(evt);
            }
        });

        txtNgayLap.setEditable(false);
        txtNgayLap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNgayLapActionPerformed(evt);
            }
        });

        txtTongTien.setEditable(false);
        txtTongTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTongTienActionPerformed(evt);
            }
        });

        lblTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTrangThai.setText("Ghi chú");

        lblTongTien1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTongTien1.setText("Mã giảm giá");

        cboGiamGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboGiamGiaActionPerformed(evt);
            }
        });

        txtGhiChu.setColumns(20);
        txtGhiChu.setRows(5);
        jScrollPane1.setViewportView(txtGhiChu);

        btnCapNhat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnCapNhat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/cap_nhat.png"))); // NOI18N
        btnCapNhat.setText("Cập nhật");
        btnCapNhat.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCapNhat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapNhatActionPerformed(evt);
            }
        });

        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        txtTrangThai.setEditable(false);
        txtTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTrangThaiActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel1.setText("Trạng Thái");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMaKH)
                            .addComponent(lblMaHD)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                                    .addGap(52, 52, 52)
                                    .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel4Layout.createSequentialGroup()
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTongTien1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(21, 21, 21)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtTongTien)
                                        .addComponent(cboGiamGia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
                                        .addComponent(txtMaHD)
                                        .addComponent(txtNgayLap)
                                        .addComponent(txtMaNV)
                                        .addComponent(cboMaKH, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(117, 117, 117)
                        .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaHD))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaKH))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaNV)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNgayLap)
                    .addComponent(txtNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTongTien)
                    .addComponent(txtTongTien))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboGiamGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTongTien1))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTrangThai)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCapNhat, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        jPanel1.add(jPanel4);

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(255, 255, 255), new java.awt.Color(204, 204, 204)));
        jPanel5.setMaximumSize(new java.awt.Dimension(500, 600));
        jPanel5.setPreferredSize(new java.awt.Dimension(500, 600));

        lblTimKiemHD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblTimKiemHD.setText("Tìm kiếm:");

        lblGia.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGia.setText("Giá");

        lblNgay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNgay.setText("Ngày");

        lblGiaDen.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblGiaDen.setText("Đến");

        lblNgayDen.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lblNgayDen.setText("Đến");

        txtGiaTu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiaTuActionPerformed(evt);
            }
        });

        txtGiaDen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiaDenActionPerformed(evt);
            }
        });

        tblHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Mã HĐ", "Mã KH", "Ngày lập", "Tổng tiền"
            }
        ));
        tblHoaDon.setGridColor(new java.awt.Color(51, 51, 51));
        tblHoaDon.setMaximumSize(new java.awt.Dimension(2147483647, 300));
        tblHoaDon.setPreferredSize(new java.awt.Dimension(300, 300));
        tblHoaDon.setRowHeight(23);
        tblHoaDon.setShowGrid(true);
        scrTimKiemHD.setViewportView(tblHoaDon);

        btnTim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/timkiem.png"))); // NOI18N
        btnTim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(lblTimKiemHD)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGia, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGiaTu, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcNgayTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblGiaDen)
                            .addComponent(lblNgayDen))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGiaDen, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dcNgayDen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(btnTim))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(scrTimKiemHD, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTimKiemHD, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(lblGia)
                        .addGap(11, 11, 11)
                        .addComponent(lblNgay))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(txtGiaTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(dcNgayTu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lblGiaDen)
                        .addGap(10, 10, 10)
                        .addComponent(lblNgayDen))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(txtGiaDen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(dcNgayDen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(btnTim)))
                .addGap(18, 18, 18)
                .addComponent(scrTimKiemHD, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(jPanel5);

        add(jPanel1);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));
        jPanel3.setPreferredSize(new java.awt.Dimension(400, 550));

        lblChiTietHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblChiTietHD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblChiTietHD.setText("Chi tiết hóa đơn");

        tblChiTietHD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tblChiTietHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã HĐ", "Mã SP", "Số lượng", "Đơn giá", "Thành tiền"
            }
        ));
        tblChiTietHD.setGridColor(new java.awt.Color(51, 51, 51));
        tblChiTietHD.setRowHeight(23);
        tblChiTietHD.setShowGrid(true);
        scrChiTietHD.setViewportView(tblChiTietHD);

        btnXuatHD.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnXuatHD.setText("Xuất Hóa đơn");
        btnXuatHD.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnXuatHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrChiTietHD, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lblChiTietHD, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnXuatHD, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChiTietHD)
                    .addComponent(btnXuatHD, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(scrChiTietHD)
                .addContainerGap())
        );

        add(jPanel3);
    }// </editor-fold>//GEN-END:initComponents

    private void txtMaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHDActionPerformed

    private void txtMaNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaNVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaNVActionPerformed

    private void txtNgayLapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNgayLapActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNgayLapActionPerformed

    private void txtTongTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTongTienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTongTienActionPerformed

    private void txtGiaTuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGiaTuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGiaTuActionPerformed

    private void txtGiaDenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGiaDenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGiaDenActionPerformed

    private void btnCapNhatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapNhatActionPerformed
        int selectedRow = tblHoaDon.getSelectedRow();
    
        // KIỂM TRA ĐÃ CHỌN DÒNG CHƯA
        if (selectedRow == -1) {
            // Thông báo nếu chưa chọn dòng nào trên bảng
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần cập nhật trên bảng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

    int maHD = (int) tblHoaDon.getValueAt(selectedRow, 0);

        HoaDon hd = hoaDonBUS.getHoaDonByMaHD(maHD); 

        if (hd != null && "Đã Thanh Toán".equals(hd.getTrangThai())) {
            JOptionPane.showMessageDialog(this, "Hóa đơn đã được xuất và thanh toán, không thể cập nhật.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return; // NGĂN CHẶN THAO TÁC CẬP NHẬT
        }

        // 1. Lấy dữ liệu mới
        int newMaKH = getSelectedIdFromCombobox(cboMaKH);
        int newMaGG = getSelectedIdFromCombobox(cboGiamGia);
        String ghiChu = txtGhiChu.getText();
        double newTongTien = hoaDonBUS.tinhLaiTongTien(maHD, newMaGG);
        boolean success = hoaDonBUS.updateKhachHangVaGiamGia(maHD, newMaKH, newMaGG, ghiChu, newTongTien); 

        if (success) {
            JOptionPane.showMessageDialog(this, "Cập nhật hóa đơn thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);

            loadFormHoaDon(maHD); 
            hienThiHoaDonMoi(maHD); 
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật hóa đơn thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnCapNhatActionPerformed

    private void cboGiamGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboGiamGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboGiamGiaActionPerformed

    private void btnXuatHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHDActionPerformed
        int selectedRow = tblHoaDon.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một Hóa đơn để xuất.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy Mã Hóa Đơn từ dòng được chọn
        int maHD =0;
        try {
            maHD = Integer.parseInt(tblHoaDon.getValueAt(selectedRow, 0).toString());
        } catch (NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần cập nhật.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        // Lấy đối tượng HoaDon từ BUS
        HoaDon hd = hoaDonBUS.getHoaDonByMaHD(maHD); 

        // Lấy danh sách CTHoaDon từ BUS
        ArrayList<CTHoaDon> listCT = hoaDonBUS.getCTHoaDonByMaHD(maHD);

        if (hd != null && listCT != null) {
            // Hiển thị dialog thanh toán
            // Sử dụng this.getParent() để lấy Frame cha và truyền vào PaymentDialog
            java.awt.Frame parentFrame = (java.awt.Frame) javax.swing.SwingUtilities.getWindowAncestor(this);
            // Giả sử PnHoaDonForm được đặt trong một JFrame
            XuatHD dialog = new XuatHD((JFrame) SwingUtilities.getWindowAncestor(this), true, hd, listCT);
            dialog.setVisible(true);

            // Sau khi dialog đóng, refresh lại bảng nếu cần
             loadDanhSachHoaDon(); 
             clearHoaDonFields();
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu chi tiết cho Hóa đơn này.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnXuatHDActionPerformed

    private void txtTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTrangThaiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTrangThaiActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        loadDanhSachHoaDon();
        clearHoaDonFields();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnTimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimActionPerformed
        String giaTuStr = txtGiaTu.getText().trim();
        String giaDenStr = txtGiaDen.getText().trim();
        
        Date ngayTu = dcNgayTu.getDate();
        Date ngayDen = dcNgayDen.getDate();

        ArrayList<HoaDon> result = new ArrayList<>();

        try {
            Double giaTu = null, giaDen = null;
            // Xử lý khoảng giá nếu có
            if (!giaTuStr.isEmpty() && !giaDenStr.isEmpty()) {
                giaTuStr = giaTuStr.replaceAll("[^\\d.]", "");
                giaDenStr = giaDenStr.replaceAll("[^\\d.]", "");
                giaTu = Double.parseDouble(giaTuStr);
                giaDen = Double.parseDouble(giaDenStr);
                if (giaTu > giaDen) {
                    double tmp = giaTu; giaTu = giaDen; giaDen = tmp;
                }
            }
            
            // Hoán đổi ngày nếu nhập ngược
            if (ngayTu != null && ngayDen != null && ngayTu.after(ngayDen)) {
                Date tmp = ngayTu; ngayTu = ngayDen; ngayDen = tmp;
            }

            // Duyệt tất cả hóa đơn
            for (HoaDon hd : hoaDonBUS.getAllHoaDon()) {
                double tongTien = hd.getTongTien();
                Date ngayLap = hd.getNgayLap();
                
                boolean matchGia = (giaTu != null && giaDen != null) ? (tongTien >= giaTu && tongTien <= giaDen) : true;
                boolean matchNgay = (ngayTu != null && ngayDen != null) ? (!ngayLap.before(ngayTu) && !ngayLap.after(ngayDen)) : true;
                
                if (matchGia && matchNgay) {
                    result.add(hd);
                }
            }

            // Xóa bảng cũ
            modelHoaDon.setRowCount(0);
            
            // Kiểm tra kết quả
            if (result.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy hóa đơn phù hợp!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Hiển thị kết quả
            for (HoaDon hd : result) {
                Object[] row = new Object[]{
                    hd.getMaHD(),
                    hd.getMaKH(),
                    dateFormat.format(hd.getNgayLap()),
                    formatCurrency(hd.getTongTien()),
                    hd.getTrangThai()
                };
                modelHoaDon.addRow(row);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Hãy nhập khoảng giá hợp lệ!",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnTimActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCapNhat;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnTim;
    private javax.swing.JButton btnXuatHD;
    private javax.swing.JComboBox<String> cboGiamGia;
    private javax.swing.JComboBox<String> cboMaKH;
    private com.toedter.calendar.JDateChooser dcNgayDen;
    private com.toedter.calendar.JDateChooser dcNgayTu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblChiTietHD;
    private javax.swing.JLabel lblGia;
    private javax.swing.JLabel lblGiaDen;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblMaKH;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblNgay;
    private javax.swing.JLabel lblNgayDen;
    private javax.swing.JLabel lblNgayLap;
    private javax.swing.JLabel lblTimKiemHD;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JLabel lblTongTien1;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JScrollPane scrChiTietHD;
    private javax.swing.JScrollPane scrTimKiemHD;
    private javax.swing.JTable tblChiTietHD;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextArea txtGhiChu;
    private javax.swing.JTextField txtGiaDen;
    private javax.swing.JTextField txtGiaTu;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JTextField txtNgayLap;
    private javax.swing.JTextField txtTongTien;
    private javax.swing.JTextField txtTrangThai;
    // End of variables declaration//GEN-END:variables
}
