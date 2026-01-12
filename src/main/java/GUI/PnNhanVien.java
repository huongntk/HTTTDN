package GUI;

import BUS.NhanVienBUS;
import DTO.NhanVienDTO;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PnNhanVien extends javax.swing.JPanel {

    private NhanVienBUS nhanVienBUS;
    private DefaultTableModel modelNhanVien;
    private ArrayList<NhanVienDTO> currentNhanVienList;

    public PnNhanVien() {
        initComponents();

        nhanVienBUS = new NhanVienBUS();
        modelNhanVien = (DefaultTableModel) tblNhanVien.getModel();

        javax.swing.table.JTableHeader header = tblNhanVien.getTableHeader();
        ((javax.swing.table.DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.JLabel.CENTER);
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        loadChucVu();
        loadData();
        addEvents();
    }
    
    private void loadChucVu() {
        // Gọi BUS để lấy danh sách chức vụ từ CSDL (Giả định BUS đã gọi DAO)
        ArrayList<String> chucVuList = nhanVienBUS.layDanhSachChucVu();
        
        // Tạo DefaultComboBoxModel mới
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        // Thêm các chức vụ vào model
        for (String chucVu : chucVuList) {
            model.addElement(chucVu);
        }
        
        // Thiết lập model cho cboChucVu
        cboChucVu.setModel(model);
    }

    private void loadData() {
        currentNhanVienList = nhanVienBUS.layDanhSachNhanVien();
        showData(currentNhanVienList);
    }
    
    private void timKiemData(String tuKhoa) {
        currentNhanVienList = nhanVienBUS.timKiemNhanVien(tuKhoa);
        showData(currentNhanVienList);
    }
    
    private void showData(ArrayList<NhanVienDTO> danhSach) {
        modelNhanVien.setRowCount(0);
        for (NhanVienDTO nv : danhSach) {
            String hoTen = nv.getHo() + " " + nv.getTen();
            String gioiTinh = nv.getGioiTinh();
            String trangThai = nv.isTrangThai() ? "Đang làm" : "Nghỉ việc";

            Object[] row = new Object[]{
                nv.getMaNV(),
                hoTen,
                gioiTinh,
                nv.getSoDienThoai(),
                nv.getChucVu(),
                trangThai
            };
            modelNhanVien.addRow(row);
        }
    }

    private void lamMoiForm() {
        txtMaNV.setText("");
        txtHo.setText("");
        txtTen.setText("");
        txtSoDienThoai.setText("");
        rdoNam.setSelected(true);
        if (cboChucVu.getItemCount() > 0) {
            cboChucVu.setSelectedIndex(0); 
        }
        txtTenTaiKhoan.setText("");
        txtMatKhau.setText("");
        rdoDangLam.setSelected(true);
        tblNhanVien.clearSelection();
        txtTenTaiKhoan.setEditable(true); 
    }
    
    private void addEvents() {
        
        tblNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblNhanVien.getSelectedRow();
                if (selectedRow != -1) {
                    int maNV = (int) modelNhanVien.getValueAt(selectedRow, 0);
                    
                    NhanVienDTO nv = null;
                    for (NhanVienDTO item : currentNhanVienList) {
                        if (item.getMaNV() == maNV) {
                            nv = item;
                            break;
                        }
                    }
                    
                    if (nv != null) {
                        txtMaNV.setText(String.valueOf(nv.getMaNV()));
                        txtHo.setText(nv.getHo());
                        txtTen.setText(nv.getTen());
                        txtSoDienThoai.setText(nv.getSoDienThoai());

                        if (nv.getGioiTinh().equalsIgnoreCase("Nam")) {
                            rdoNam.setSelected(true);
                        } else {
                            rdoNu.setSelected(true);
                        }
                        
                        cboChucVu.setSelectedItem(nv.getChucVu());
                        txtTenTaiKhoan.setText(nv.getTenTaiKhoan());
                        txtMatKhau.setText(nv.getMatKhau());
                        
                        if (nv.isTrangThai()) {
                            rdoDangLam.setSelected(true);
                        } else {
                            rdoNghiViec.setSelected(true);
                        }
                        
                        txtTenTaiKhoan.setEditable(true);
                    }
                }
            }
        });

        btnThem.addActionListener(e -> {
            NhanVienDTO nv = getNhanVienFromForm();
            if (nv == null) return; 

            String result = nhanVienBUS.themNhanVien(nv);
            JOptionPane.showMessageDialog(this, result);
            
            if (result.contains("thành công")) {
                loadData();
                lamMoiForm();
            }
        });

        btnSua.addActionListener(e -> {
            if (txtMaNV.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            NhanVienDTO nv = getNhanVienFromForm();
            if (nv == null) return;

            String result = nhanVienBUS.suaNhanVien(nv);
            JOptionPane.showMessageDialog(this, result);
            
            if (result.contains("thành công")) {
                loadData();
                lamMoiForm();
            }
        });

        btnXoa.addActionListener(e -> {
            if (txtMaNV.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn vô hiệu hóa nhân viên này?\n(Hành động này sẽ khóa tài khoản của họ)",
                    "Xác nhận vô hiệu hóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                int maNV = Integer.parseInt(txtMaNV.getText());
                String result = nhanVienBUS.xoaNhanVien(maNV);
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
    
    private NhanVienDTO getNhanVienFromForm() {
        int maNV = 0;
        if (!txtMaNV.getText().isEmpty()) {
            maNV = Integer.parseInt(txtMaNV.getText());
        }
        
        String ho = txtHo.getText().trim();
        String ten = txtTen.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String gioiTinh = rdoNam.isSelected() ? "Nam" : "Nữ";
        String chucVu = cboChucVu.getSelectedItem() != null ? cboChucVu.getSelectedItem().toString() : "";
        String tenTaiKhoan = txtTenTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        boolean trangThai = rdoDangLam.isSelected();
        
        return new NhanVienDTO(maNV, ho, ten, gioiTinh, sdt, chucVu, trangThai, tenTaiKhoan, matKhau);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        btnGroupGioiTinh = new javax.swing.ButtonGroup();
        btnGroupTrangThai = new javax.swing.ButtonGroup();
        pnlThongTin = new javax.swing.JPanel();
        lblTieuDeThongTin = new javax.swing.JLabel();
        lblMaNV = new javax.swing.JLabel();
        txtMaNV = new javax.swing.JTextField();
        lblHo = new javax.swing.JLabel();
        txtHo = new javax.swing.JTextField();
        lblTen = new javax.swing.JLabel();
        txtTen = new javax.swing.JTextField();
        lblSoDienThoai = new javax.swing.JLabel();
        txtSoDienThoai = new javax.swing.JTextField();
        lblGioiTinh = new javax.swing.JLabel();
        rdoNam = new javax.swing.JRadioButton();
        rdoNu = new javax.swing.JRadioButton();
        lblChucVu = new javax.swing.JLabel();
        cboChucVu = new javax.swing.JComboBox<>();
        lblTrangThai = new javax.swing.JLabel();
        rdoDangLam = new javax.swing.JRadioButton();
        rdoNghiViec = new javax.swing.JRadioButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        lblMatKhau = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JPasswordField();
        lblTenTaiKhoan = new javax.swing.JLabel();
        txtTenTaiKhoan = new javax.swing.JTextField();
        pnlBang = new javax.swing.JPanel();
        lblTieuDeBang = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhanVien = new javax.swing.JTable();
        lblTimKiem = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();

        pnlThongTin.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));

        lblTieuDeThongTin.setFont(new java.awt.Font("Segoe UI", 1, 14));
        lblTieuDeThongTin.setText("Thông tin nhân viên");

        lblMaNV.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblMaNV.setText("Mã nhân viên:");

        txtMaNV.setEditable(false);
        txtMaNV.setFont(new java.awt.Font("Segoe UI", 0, 14));

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

        lblChucVu.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblChucVu.setText("Chức vụ:");

        cboChucVu.setFont(new java.awt.Font("Segoe UI", 0, 14));
//        cboChucVu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Quản trị viên", "Nhân viên bán hàng" }));

        lblTrangThai.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTrangThai.setText("Trạng thái:");

        btnGroupTrangThai.add(rdoDangLam);
        rdoDangLam.setFont(new java.awt.Font("Segoe UI", 0, 14));
        rdoDangLam.setSelected(true);
        rdoDangLam.setText("Đang làm");

        btnGroupTrangThai.add(rdoNghiViec);
        rdoNghiViec.setFont(new java.awt.Font("Segoe UI", 0, 14));
        rdoNghiViec.setText("Nghỉ việc");

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

        lblMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblMatKhau.setText("Mật khẩu:");

        txtMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 14));

        lblTenTaiKhoan.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblTenTaiKhoan.setText("Tên tài khoản:");

        txtTenTaiKhoan.setFont(new java.awt.Font("Segoe UI", 0, 14));

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
                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addComponent(btnThem)
                                .addGap(18, 18, 18)
                                .addComponent(btnSua)
                                .addGap(18, 18, 18)
                                .addComponent(btnXoa)
                                .addGap(18, 18, 18)
                                .addComponent(btnLamMoi))
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblMaNV)
                                    .addComponent(lblHo)
                                    .addComponent(lblTen)
                                    .addComponent(lblSoDienThoai)
                                    .addComponent(lblGioiTinh)
                                    .addComponent(lblChucVu)
                                    .addComponent(lblTrangThai)
                                    .addComponent(lblMatKhau)
                                    .addComponent(lblTenTaiKhoan))
                                .addGap(18, 18, 18)
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMaNV)
                                    .addComponent(txtHo)
                                    .addComponent(txtTen)
                                    .addComponent(txtSoDienThoai)
                                    .addComponent(cboChucVu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                                .addComponent(rdoNam)
                                                .addGap(18, 18, 18)
                                                .addComponent(rdoNu))
                                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                                .addComponent(rdoDangLam)
                                                .addGap(18, 18, 18)
                                                .addComponent(rdoNghiViec)))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtMatKhau)
                                    .addComponent(txtTenTaiKhoan))))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlThongTinLayout.setVerticalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTieuDeThongTin)
                .addGap(30, 30, 30)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaNV)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(lblChucVu)
                    .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTenTaiKhoan)
                    .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMatKhau)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTrangThai)
                    .addComponent(rdoDangLam)
                    .addComponent(rdoNghiViec))
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
        lblTieuDeBang.setText("Danh sách nhân viên");

        tblNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 14));
        tblNhanVien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã NV", "Họ và tên", "Giới tính", "Số điện thoại", "Chức vụ", "Trạng thái"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhanVien.setGridColor(new java.awt.Color(51, 51, 51));
        tblNhanVien.setRowHeight(23);
        tblNhanVien.setShowGrid(true);
        tblNhanVien.setShowHorizontalLines(true);
        tblNhanVien.setShowVerticalLines(true);
        jScrollPane1.setViewportView(tblNhanVien);

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
    private javax.swing.ButtonGroup btnGroupTrangThai;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboChucVu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblChucVu;
    private javax.swing.JLabel lblGioiTinh;
    private javax.swing.JLabel lblHo;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblMatKhau;
    private javax.swing.JLabel lblSoDienThoai;
    private javax.swing.JLabel lblTen;
    private javax.swing.JLabel lblTenTaiKhoan;
    private javax.swing.JLabel lblTieuDeBang;
    private javax.swing.JLabel lblTieuDeThongTin;
    private javax.swing.JLabel lblTimKiem;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JPanel pnlBang;
    private javax.swing.JPanel pnlThongTin;
    private javax.swing.JRadioButton rdoDangLam;
    private javax.swing.JRadioButton rdoNam;
    private javax.swing.JRadioButton rdoNghiViec;
    private javax.swing.JRadioButton rdoNu;
    private javax.swing.JTable tblNhanVien;
    private javax.swing.JTextField txtHo;
    private javax.swing.JTextField txtMaNV;
    private javax.swing.JPasswordField txtMatKhau;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JTextField txtTen;
    private javax.swing.JTextField txtTenTaiKhoan;
    private javax.swing.JTextField txtTimKiem;
}