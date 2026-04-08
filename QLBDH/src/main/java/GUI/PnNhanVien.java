package GUI;

import BUS.DonNghiBUS;
import BUS.NhanVienBUS;
import DTO.DonNghiDTO;
import DTO.NhanVienDTO;
import DTO.PhanQuyen;
import UTIL.Auth;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class PnNhanVien extends javax.swing.JPanel {
    private PhanQuyen phanQuyen;
    private NhanVienBUS nhanVienBUS;
    private DefaultTableModel modelNhanVien;
    private DefaultTableModel modelDonNghi;
    private ArrayList<NhanVienDTO> currentNhanVienList;
    private javax.swing.JButton btnTaoDonNghi;
    private javax.swing.JButton btnXemDonCuaToi;
    private javax.swing.JComboBox<String> cblLoaiNghi;
   
    public PnNhanVien(PhanQuyen pq) {
        
        initComponents();
        this.phanQuyen = pq;
        nhanVienBUS = new NhanVienBUS();
        modelNhanVien = (DefaultTableModel) tblNhanVien.getModel();
        modelDonNghi = (DefaultTableModel) tblDonNghi.getModel();
        javax.swing.table.JTableHeader header = tblNhanVien.getTableHeader();
        ((javax.swing.table.DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.JLabel.CENTER);
        header.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        
        // Format header cho bảng đơn nghỉ
        javax.swing.table.JTableHeader headerDN = tblDonNghi.getTableHeader();
        ((javax.swing.table.DefaultTableCellRenderer) headerDN.getDefaultRenderer())
                .setHorizontalAlignment(javax.swing.JLabel.CENTER);
        headerDN.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        loadChucVu();
        loadLoaiNghi();
        loadData();
        addEvents();
        configureByPermission();
    }
    private void loadLoaiNghi() {
        cboLoaiNghi.removeAllItems();
        cboLoaiNghi.addItem("Nghỉ phép");
        cboLoaiNghi.addItem("Nghỉ ốm đau");
        cboLoaiNghi.addItem("Nghỉ thai sản");
        cboLoaiNghi.addItem("Nghỉ việc");
    }
    private void configureByPermission(){
        btnThem.setVisible(phanQuyen.isNsThem());
        btnSua.setVisible(phanQuyen.isNsSua());
        btnXoa.setVisible(phanQuyen.isNsXoa());
        // Chỉ hiển thị nút Duyệt nghỉ bên trong PnNhanVien nếu là ADMIN
        boolean isAdmin = Auth.getUser().getMaQuyen().equals("ADMIN");
        btnDuyetNghi.setVisible(phanQuyen.isNsDuyetNghi() && isAdmin);
//        btnThayDoiChucVu.setVisible(phanQuyen.isNsThayDoiChucVu());
        btnTinhLuong.setVisible(phanQuyen.isNsTinhLuong());
        btnTaoDonNghi.setVisible(phanQuyen.isNsTaoDonNghi());
        btnXemDonCuaToi.setVisible(phanQuyen.isNsXemDonCuaMinh());


    
    // Quyền xem danh sách nhân viên (nếu không có, ẩn bảng và chỉ cho xem thông tin cá nhân)
        boolean coQuyenXemDanhSach = phanQuyen.isNsXemDanhSach();
        
        CardLayout cl = (CardLayout) pnlRight.getLayout();
        
        pnlBang.setVisible(coQuyenXemDanhSach);
        lblTieuDeBang.setVisible(coQuyenXemDanhSach);
        jScrollPane1.setVisible(coQuyenXemDanhSach);
        lblTimKiem.setVisible(coQuyenXemDanhSach);
        txtTimKiem.setVisible(coQuyenXemDanhSach);
        if (coQuyenXemDanhSach) {
            // Màn hình Quản lý
            cl.show(pnlRight, "DanhSachNhanVien");
        } else {
            // Màn hình Nhân viên thường
            cl.show(pnlRight, "DonNghiCaNhan");
            loadDanhSachDonNghiCaNhan(); // Load data đơn nghỉ của riêng NV đó
            
            // Lấy thông tin cá nhân fill vào form bên trái
            int maNV = Auth.getUser().getMaNV();
            NhanVienDTO nv = nhanVienBUS.layNhanVienTheoMa(maNV);
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
                cboChucVu.setSelectedItem(nv.getMaCV());
                txtTenTaiKhoan.setText(nv.getTenTaiKhoan());
                txtMatKhau.setText(nv.getMatKhau());
                if (nv.isTrangThai()) {
                    rdoDangLam.setSelected(true);
                } else {
                    rdoNghiViec.setSelected(true);
                }
                
                // Khóa các trường không cho phép nhân viên tự sửa
                txtHo.setEditable(phanQuyen.isNsSua());
                txtTen.setEditable(phanQuyen.isNsSua());
                txtSoDienThoai.setEditable(phanQuyen.isNsSua());
                rdoNam.setEnabled(phanQuyen.isNsSua());
                rdoNu.setEnabled(phanQuyen.isNsSua());
                cboChucVu.setEnabled(false);
                txtTenTaiKhoan.setEditable(false);
                txtMatKhau.setEditable(false);
                rdoDangLam.setEnabled(false);
                rdoNghiViec.setEnabled(false);
            }
        }
        
        // Quyền xem lương cá nhân và in lương (cho cả nhân viên và quản lý)
        btnXemLuong.setVisible(phanQuyen.isNsXemLuongCaNhan());
        btnInLuong.setVisible(phanQuyen.isNsInBangLuong());

        // Nếu không có quyền gì liên quan đến nhân sự, ẩn toàn bộ panel (trường hợp hiếm)
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
    
    private Date parseDate(String dateStr) {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.parse(dateStr);
    } catch (ParseException e) {
        return null;
    }
}

    private void loadData() {
        currentNhanVienList = nhanVienBUS.layDanhSachNhanVien();
        showData(currentNhanVienList);
    }
    
    private void timKiemData(String tuKhoa) {
        currentNhanVienList = nhanVienBUS.timKiemNhanVien(tuKhoa);
        showData(currentNhanVienList);
    }
    private void loadDanhSachDonNghiCaNhan() {
        modelDonNghi.setRowCount(0); // Xóa trắng dữ liệu cũ trên bảng

        DonNghiBUS donNghiBUS = new DonNghiBUS();
        int maNV = Auth.getUser().getMaNV();

        // Gọi BUS lấy danh sách đơn của nhân viên đang đăng nhập
        ArrayList<DonNghiDTO> list = donNghiBUS.layDonTheoNhanVien(maNV);

        // Format ngày để hiển thị đẹp mắt
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (DonNghiDTO dn : list) {
            String strNgayBatDau = dn.getNgayBatDau() != null ? sdf.format(dn.getNgayBatDau()) : "";
            String strNgayKetThuc = dn.getNgayKetThuc() != null ? sdf.format(dn.getNgayKetThuc()) : "";

            Object[] row = new Object[]{
                dn.getMaDon(),
                strNgayBatDau,
                strNgayKetThuc,
                dn.getLyDo(),
                dn.getTrangThai()
            };
            modelDonNghi.addRow(row);
        }
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
                nv.getMaCV(),
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
                        
                        cboChucVu.setSelectedItem(nv.getMaCV());
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
        
        btnNopDonNghi.addActionListener(e -> {
            String loaiNghi = (String) cboLoaiNghi.getSelectedItem();
            String tuNgay = txtTuNgayNghi.getText().trim();
            String denNgay = txtDenNgayNghi.getText().trim();
            String lyDo = txtLyDoNghi.getText().trim();

            if (tuNgay.isEmpty() || denNgay.isEmpty() || lyDo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thời gian và lý do nghỉ!");
                return;
            }

            // 1. Tạo đối tượng và Gán dữ liệu TRƯỚC khi xóa form
            DonNghiBUS donNghiBUS = new DonNghiBUS();
            DonNghiDTO don = new DonNghiDTO();
            don.setMaNV(Auth.getUser().getMaNV());                  
            don.setLoaiDon(loaiNghi); // Lấy linh hoạt từ Combobox thay vì gán cứng "Nghỉ phép"
            don.setNgayBatDau(parseDate(tuNgay)); 
            don.setNgayKetThuc(parseDate(denNgay));
            don.setLyDo(lyDo);             
            don.setTrangThai("Chờ duyệt");                         
            don.setNgayGui(new Date());                            

            // 2. Gọi BUS thêm đơn vào CSDL
            String result = donNghiBUS.themDonNghi(don);
            JOptionPane.showMessageDialog(this, result); // Hiện thông báo thật từ BUS trả về

            // 3. NẾU thêm thành công thì mới xóa trắng form và load lại bảng
            if (result.contains("Đã gửi đơn nghỉ")) {
                txtTuNgayNghi.setText("");
                txtDenNgayNghi.setText("");
                txtLyDoNghi.setText("");
                loadDanhSachDonNghiCaNhan(); // Cập nhật lại JTable
            }
        });
        
        btnTaoDonNghi.addActionListener(e -> {
        if (!phanQuyen.isNsTaoDonNghi()) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền tạo đơn nghỉ!");
            return;
        }
           
        
        });

    btnXemDonCuaToi.addActionListener(e -> {
        if (!phanQuyen.isNsXemDonCuaMinh()) {
            JOptionPane.showMessageDialog(this, "Bạn không có quyền xem đơn của mình!");
            return;
        }
        JOptionPane.showMessageDialog(this, "chức năng phát triển");
        // Mở dialog xem danh sách đơn nghỉ của nhân viên hiện tại
//        new DlgXemDonCuaToi((Frame) SwingUtilities.getWindowAncestor(this), true, Auth.getUser().getMaNV()).setVisible(true);
        
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
        
                btnXemLuong.addActionListener(e -> {
            int maNV;
            // Nếu người dùng không có quyền xem danh sách (chỉ xem được của mình)
            if (!phanQuyen.isNsXemDanhSach()) {
                maNV = Auth.getUser().getMaNV();
            } else {
                // Nếu có quyền xem danh sách, ưu tiên lấy mã NV từ form (nếu đang chọn)
                if (txtMaNV.getText().isEmpty()) {
                    maNV = Auth.getUser().getMaNV();
                } else {
                    maNV = Integer.parseInt(txtMaNV.getText());
                }
            }
            new FrmXemLuong(maNV, phanQuyen).setVisible(true);
        });

        btnInLuong.addActionListener(e -> {
            int maNV;
            if (!phanQuyen.isNsXemDanhSach()) {
                maNV = Auth.getUser().getMaNV();
            } else {
                if (txtMaNV.getText().isEmpty()) {
                    maNV = Auth.getUser().getMaNV();
                } else {
                    maNV = Integer.parseInt(txtMaNV.getText());
                }
            }
            new FrmInBangLuong(maNV).setVisible(true);
        });
        btnDuyetNghi.addActionListener(e -> {
            java.awt.Window window = SwingUtilities.getWindowAncestor(this);
            JDialog dialog = new JDialog((Frame) window, "Duyệt đơn nghỉ", true);
            dialog.add(new FrmDuyetNghi(phanQuyen));
            dialog.pack();
            dialog.setLocationRelativeTo(window);
            dialog.setVisible(true);
        });
//        pnContent.removeAll();
//        FrmDuyetNghi pn = new FrmDuyetNghi(phanQuyen);
//        
//        pnContent.add(pn, java.awt.BorderLayout.CENTER);
//        pnContent.revalidate();
//        pnContent.repaint();
        // Xử lý nút thay đổi chức vụ (quản lý)
//        btnThayDoiChucVu.addActionListener(e -> {
//           
//            if (txtMaNV.getText().isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần thay đổi chức vụ");
//                return;
//            }
//            int maNV = Integer.parseInt(txtMaNV.getText());
//            new FrmThayDoiChucVu(maNV,phanQuyen).setVisible(true);
//        });
//        btnTinhLuong.addActionListener(e -> {
//           
//            // Mở form tính lương hàng loạt hoặc theo tháng
//            new FrmTinhLuong(phanQuyen).setVisible(true);
//        });
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
        String tenChucVu = cboChucVu.getSelectedItem() != null ? cboChucVu.getSelectedItem().toString().trim() : "";
        String maCV = "";
        String maQuyen = "";
        switch (tenChucVu) {
            case "Giám đốc":
                maCV = "CV01"; 
                maQuyen = "ADMIN"; 
                break;
            case "Quản lý nhân sự":
                maCV = "CV02"; 
                maQuyen = "QL_NHANSU";
                break;
            case "Quản lý kho":
                maCV = "CV03"; 
                maQuyen = "QL_KHO"; 
                break;
            case "Quản lý bán hàng":
                maCV = "CV04"; 
                maQuyen = "QL_KINHDOANH"; 
                break;
            case "Nhân viên bán hàng":
                maCV = "CV05"; 
                maQuyen = "NHANVIEN"; 
                break;
            case "Nhân viên nhập hàng":
                maCV = "CV06"; 
                maQuyen = "NHANVIEN"; 
                break;
            default:
                maCV = "CV05";
                maQuyen = "NHANVIEN"; // Dự phòng
                break;
        }
       
        String tenTaiKhoan = txtTenTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        boolean trangThai = rdoDangLam.isSelected();
        
        return new NhanVienDTO(maNV, ho, ten, gioiTinh, sdt, maCV, maQuyen, trangThai, tenTaiKhoan, matKhau);
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
        
        btnXemLuong = new javax.swing.JButton();
        btnInLuong = new javax.swing.JButton();
        btnDuyetNghi = new javax.swing.JButton();
//        btnThayDoiChucVu = new javax.swing.JButton();
        btnTinhLuong = new javax.swing.JButton();
        btnTaoDonNghi = new javax.swing.JButton();
        btnXemDonCuaToi = new javax.swing.JButton();

        pnlRight = new javax.swing.JPanel();
        pnlRight.setLayout(new CardLayout());
        pnlBang = new javax.swing.JPanel();
//        btnThayDoiChucVu = new javax.swing.JButton();
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

        btnXemLuong.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnXemLuong.setText("Xem lương");

        btnInLuong.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnInLuong.setText("In lương");

        btnDuyetNghi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnDuyetNghi.setText("Duyệt nghỉ");

//        btnThayDoiChucVu.setFont(new java.awt.Font("Segoe UI", 0, 14));
//        btnThayDoiChucVu.setText("Đổi chức vụ");

        btnTinhLuong.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnTinhLuong.setText("Tính lương");

        btnTaoDonNghi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnTaoDonNghi.setText("Tạo đơn nghỉ");

        btnXemDonCuaToi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnXemDonCuaToi.setText("Xem đơn của tôi");
        javax.swing.GroupLayout pnlThongTinLayout = new javax.swing.GroupLayout(pnlThongTin);
        pnlThongTin.setLayout(pnlThongTinLayout);
        pnlThongTinLayout.setHorizontalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTieuDeThongTin, javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblMaNV)
                            .addComponent(lblHo)
                            .addComponent(lblTen)
                            .addComponent(lblSoDienThoai)
                            .addComponent(lblGioiTinh)
                            .addComponent(lblChucVu)
                            .addComponent(lblTenTaiKhoan)
                            .addComponent(lblMatKhau)
                            .addComponent(lblTrangThai))
                        .addGap(18, 18, 18)
                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaNV)
                            .addComponent(txtHo)
                            .addComponent(txtTen)
                            .addComponent(txtSoDienThoai)
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addComponent(rdoNam)
                                .addGap(18)
                                .addComponent(rdoNu))
                            .addComponent(cboChucVu, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtTenTaiKhoan)
                            .addComponent(txtMatKhau)
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addComponent(rdoDangLam)
                                .addGap(18)
                                .addComponent(rdoNghiViec))))
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addGap(20)
                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addComponent(btnThem)
                                .addGap(18)
                                .addComponent(btnSua)
                                .addGap(18)
                                .addComponent(btnXoa)
                                .addGap(18)
                                .addComponent(btnLamMoi)
                                .addGap(18)
                                .addComponent(btnTaoDonNghi)
                                .addGap(18)
                                .addComponent(btnXemDonCuaToi))
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addComponent(btnXemLuong)
                                .addGap(18)
                                .addComponent(btnInLuong)
                                .addGap(18)
                                .addComponent(btnTinhLuong))
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addComponent(btnDuyetNghi)))))
//                                .addGap(18)
//                                .addComponent(btnThayDoiChucVu)))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        pnlThongTinLayout.setVerticalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTieuDeThongTin)
                .addGap(18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaNV)
                    .addComponent(txtMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblHo)
                    .addComponent(txtHo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTen)
                    .addComponent(txtTen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSoDienThoai)
                    .addComponent(txtSoDienThoai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblGioiTinh)
                    .addComponent(rdoNam)
                    .addComponent(rdoNu))
                .addGap(18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblChucVu)
                    .addComponent(cboChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTenTaiKhoan)
                    .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMatKhau)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTrangThai)
                    .addComponent(rdoDangLam)
                    .addComponent(rdoNghiViec))
                .addGap(30)
                // Hàng nút thứ nhất
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnLamMoi)
                    .addComponent(btnTaoDonNghi)
                    .addComponent(btnXemDonCuaToi))
                .addGap(18)
                // Hàng nút thứ hai
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnXemLuong)
                    .addComponent(btnInLuong)
                    .addComponent(btnTinhLuong))
                .addGap(18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDuyetNghi))
//                    .addComponent(btnThayDoiChucVu))
                   
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
        
        // --- 2. Tạo Panel Đơn Nghỉ Của Tôi (dành cho Nhân viên) ---
        pnlDonNghiCaNhan = new javax.swing.JPanel(new BorderLayout(10, 10));
        pnlDonNghiCaNhan.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, new java.awt.Color(204, 204, 204)));
        
        javax.swing.JLabel lblTieuDeDonNghi = new javax.swing.JLabel("Danh sách đơn từ của tôi", javax.swing.SwingConstants.CENTER);
        lblTieuDeDonNghi.setFont(new java.awt.Font("Segoe UI", 1, 14));
        pnlDonNghiCaNhan.add(lblTieuDeDonNghi, BorderLayout.NORTH);

        tblDonNghi = new javax.swing.JTable();
        tblDonNghi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        tblDonNghi.setRowHeight(23);
        tblDonNghi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {"Mã đơn", "Từ ngày", "Đến ngày", "Lý do", "Trạng thái"}
        ) {
            boolean[] canEdit = new boolean [] {false, false, false, false, false};
            public boolean isCellEditable(int rowIndex, int columnIndex) { return canEdit [columnIndex]; }
        });
        scrollDonNghi = new javax.swing.JScrollPane(tblDonNghi);
        pnlDonNghiCaNhan.add(scrollDonNghi, BorderLayout.CENTER);

        // Form nhập đơn phía dưới
        javax.swing.JPanel pnlFormDon = new javax.swing.JPanel(new GridLayout(5, 2, 10, 10));
        pnlFormDon.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Nhập đơn nghỉ mới", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12)));
        cboLoaiNghi = new javax.swing.JComboBox<>();
        txtTuNgayNghi = new javax.swing.JTextField();
        txtDenNgayNghi = new javax.swing.JTextField();
        txtLyDoNghi = new javax.swing.JTextField();
        btnNopDonNghi = new javax.swing.JButton("Nộp đơn nghỉ");
        btnNopDonNghi.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnNopDonNghi.setBackground(new java.awt.Color(0, 204, 153));
        btnNopDonNghi.setForeground(java.awt.Color.WHITE);
        
        pnlFormDon.add(new javax.swing.JLabel("Loại nghỉ:"));
        pnlFormDon.add(cboLoaiNghi);
        pnlFormDon.add(new javax.swing.JLabel("Từ ngày (dd/MM/yyyy):"));
        pnlFormDon.add(txtTuNgayNghi);
        pnlFormDon.add(new javax.swing.JLabel("Đến ngày (dd/MM/yyyy):"));
        pnlFormDon.add(txtDenNgayNghi);
        pnlFormDon.add(new javax.swing.JLabel("Lý do nghỉ:"));
        pnlFormDon.add(txtLyDoNghi);
        pnlFormDon.add(new javax.swing.JLabel("")); // Ô trống
        pnlFormDon.add(btnNopDonNghi);

        pnlDonNghiCaNhan.add(pnlFormDon, BorderLayout.SOUTH);

        // --- Add các panel vào CardLayout ---
        pnlRight.add(pnlBang, "DanhSachNhanVien");
        pnlRight.add(pnlDonNghiCaNhan, "DonNghiCaNhan");

//        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
//        this.setLayout(layout);
//        layout.setHorizontalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(layout.createSequentialGroup()
//                .addContainerGap()
//                .addComponent(pnlThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
//                .addComponent(pnlBang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                .addContainerGap())
//        );
//        layout.setVerticalGroup(
//            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
//                .addContainerGap()
//                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
//                    .addComponent(pnlBang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                    .addComponent(pnlThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//                .addContainerGap())
//        );
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlRight, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
    private javax.swing.JButton btnXemLuong;
    private javax.swing.JButton btnInLuong;
    private javax.swing.JButton btnDuyetNghi;
    private javax.swing.JButton btnThayDoiChucVu;
    private javax.swing.JButton btnTinhLuong;
    private javax.swing.JPanel pnlRight;
    private javax.swing.JPanel pnlDonNghiCaNhan;
    private javax.swing.JScrollPane scrollDonNghi;
    private javax.swing.JTable tblDonNghi;
    private javax.swing.JTextField txtTuNgayNghi, txtDenNgayNghi, txtLyDoNghi;
    private javax.swing.JButton btnNopDonNghi;
    private javax.swing.JComboBox<String> cboLoaiNghi;
}