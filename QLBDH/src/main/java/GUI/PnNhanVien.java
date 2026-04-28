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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
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
    private javax.swing.JButton btnNopDonNghi;     // Thêm
    private javax.swing.JComboBox<String> cboLoaiNghi; // Thêm
    private javax.swing.JTextField txtLyDoNghi;
    private com.toedter.calendar.JDateChooser txtTuNgayNghi;
    private com.toedter.calendar.JDateChooser txtDenNgayNghi;
    private javax.swing.JScrollPane scrollDonNghi; // Thêm
    private javax.swing.JTable tblDonNghi;         // Thêm
    private javax.swing.JPanel pnlDonNghiCaNhan;   // Thêm
   
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
       
        boolean isAdmin = Auth.getUser().getMaQuyen().equals("ADMIN");
     
        btnTaoDonNghi.setVisible(phanQuyen.isNsTaoDonNghi());
        btnXemDonCuaToi.setVisible(phanQuyen.isNsXemDonCuaMinh());
        
        boolean isQLNS = phanQuyen.getMaQuyen().trim().equalsIgnoreCase("QL_NHANSU");

        btnThayDoiChucVu.setVisible(isQLNS || isAdmin );
        btnXemLuong.setVisible(phanQuyen.isNsXemLuongCaNhan());
    
        boolean coQuyenXemDanhSach = phanQuyen.isNsXemDanhSach();
        
               
        pnlBang.setVisible(coQuyenXemDanhSach);
        lblTieuDeBang.setVisible(coQuyenXemDanhSach);
        jScrollPane1.setVisible(coQuyenXemDanhSach);
        lblTimKiem.setVisible(coQuyenXemDanhSach);
        txtTimKiem.setVisible(coQuyenXemDanhSach);
        
        if (coQuyenXemDanhSach) {
            // Màn hình Quản lý
            CardLayout cl = (CardLayout) pnlRight.getLayout();
            cl.show(pnlRight, "DanhSachNhanVien");
            refreshThongTinCaNhan();
        } else {
            // Màn hình Nhân viên thường
            CardLayout cl = (CardLayout) pnlRight.getLayout();
            cl.show(pnlRight, "DonNghiCaNhan");
            loadDanhSachDonNghiCaNhan(); // Load data đơn nghỉ của riêng NV đó
            
            refreshThongTinCaNhan();        
        }      

    }    

    private String layTenChucVuTuMa(String maCV) {
        switch (maCV) {
            case "CV01": return "Giám đốc";
            case "CV02": return "Quản lý nhân sự";
            case "CV03": return "Quản lý kho";
            case "CV04": return "Quản lý bán hàng";
            case "CV05": return "Nhân viên bán hàng";
            case "CV06": return "Nhân viên nhập hàng";
            default: return "Nhân viên";
        }
    }
    
    private void loadChucVu() {
        ArrayList<String> chucVuList = nhanVienBUS.layDanhSachChucVu();
        
     
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        for (String chucVu : chucVuList) {
            model.addElement(chucVu);
        }
        
        cboChucVu.setModel(model);
    }
    
    private Date parseDate(String dateStr) {
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); 
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
        // Lấy Frame cha để JDialog modal hoạt động đúng
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        java.awt.Frame parentFrame = (parentWindow instanceof java.awt.Frame) ? (java.awt.Frame) parentWindow : null;

        // Tạo JDialog modal
        JDialog dialog = new JDialog(parentFrame, "Thêm nhân viên mới", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new java.awt.BorderLayout());

        // Panel chứa form nhập liệu
        JPanel formPanel = new JPanel(new java.awt.GridLayout(8, 2, 10, 10));
        formPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Các trường nhập liệu
        JTextField txtDialogHo = new JTextField();
        JTextField txtDialogTen = new JTextField();
        JTextField txtDialogSDT = new JTextField();
        JRadioButton rdoDialogNam = new JRadioButton("Nam", true);
        JRadioButton rdoDialogNu = new JRadioButton("Nữ");
        javax.swing.ButtonGroup bgGioiTinh = new javax.swing.ButtonGroup();
        bgGioiTinh.add(rdoDialogNam);
        bgGioiTinh.add(rdoDialogNu);
        JComboBox<String> cboDialogChucVu = new JComboBox<>();
        // Load danh sách chức vụ
        ArrayList<String> chucVuList = nhanVienBUS.layDanhSachChucVu();
        for (String cv : chucVuList) cboDialogChucVu.addItem(cv);
        JTextField txtDialogTaiKhoan = new JTextField();
        JPasswordField txtDialogMatKhau = new JPasswordField();
        JRadioButton rdoDialogDangLam = new JRadioButton("Đang làm", true);
        JRadioButton rdoDialogNghi = new JRadioButton("Nghỉ việc");
        javax.swing.ButtonGroup bgTrangThai = new javax.swing.ButtonGroup();
        bgTrangThai.add(rdoDialogDangLam);
        bgTrangThai.add(rdoDialogNghi);

        // Thêm label + field vào form
        formPanel.add(new JLabel("Họ:"));
        formPanel.add(txtDialogHo);
        formPanel.add(new JLabel("Tên:"));
        formPanel.add(txtDialogTen);
        formPanel.add(new JLabel("Số điện thoại:"));
        formPanel.add(txtDialogSDT);
        formPanel.add(new JLabel("Giới tính:"));
        JPanel pnlGioiTinh = new JPanel();
        pnlGioiTinh.add(rdoDialogNam);
        pnlGioiTinh.add(rdoDialogNu);
        formPanel.add(pnlGioiTinh);
        formPanel.add(new JLabel("Chức vụ:"));
        formPanel.add(cboDialogChucVu);
        formPanel.add(new JLabel("Tên tài khoản:"));
        formPanel.add(txtDialogTaiKhoan);
        formPanel.add(new JLabel("Mật khẩu:"));
        formPanel.add(txtDialogMatKhau);
        formPanel.add(new JLabel("Trạng thái:"));
        JPanel pnlTrangThai = new JPanel();
        pnlTrangThai.add(rdoDialogDangLam);
        pnlTrangThai.add(rdoDialogNghi);
        formPanel.add(pnlTrangThai);

        dialog.add(formPanel, BorderLayout.CENTER);

        // Panel nút Lưu / Hủy
        JPanel btnPanel = new JPanel();
        JButton btnLuu = new JButton("Lưu");
        btnLuu.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/diskette.png")));
        JButton btnHuy = new JButton("Hủy");
        btnHuy.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnHuy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/close.png")));
        btnPanel.add(btnLuu);
        btnPanel.add(btnHuy);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        // Xử lý nút Lưu
        btnLuu.addActionListener(ev -> {
            // Lấy dữ liệu và validate
            String ho = txtDialogHo.getText().trim();
            String ten = txtDialogTen.getText().trim();
            String sdt = txtDialogSDT.getText().trim();

            if (ho.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Họ, tên, số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!sdt.matches("^0[0-9]{9,10}$")) {
                JOptionPane.showMessageDialog(dialog, "Số điện thoại không hợp lệ! (10-11 số, bắt đầu 0)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String gioiTinh = rdoDialogNam.isSelected() ? "Nam" : "Nữ";
            String tenChucVu = (String) cboDialogChucVu.getSelectedItem();
            // Ánh xạ chức vụ -> maCV, maQuyen (giống logic cũ)
            String maCV, maQuyen;
            switch (tenChucVu) {
                case "Giám đốc": maCV = "CV01"; maQuyen = "ADMIN"; break;
                case "Quản lý nhân sự": maCV = "CV02"; maQuyen = "QL_NHANSU"; break;
                case "Quản lý kho": maCV = "CV03"; maQuyen = "QL_KHO"; break;
                case "Quản lý bán hàng": maCV = "CV04"; maQuyen = "QL_KINHDOANH"; break;
                case "Nhân viên bán hàng": maCV = "CV05"; maQuyen = "NVBH"; break;
                case "Nhân viên nhập hàng": maCV = "CV06"; maQuyen = "NVNH"; break;
                default: maCV = "CV05"; maQuyen = "NVBH";
            }

            String tenTaiKhoan = txtDialogTaiKhoan.getText().trim();
            String matKhau = new String(txtDialogMatKhau.getPassword()).trim();
            if (tenTaiKhoan.isEmpty() || matKhau.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tài khoản & mật khẩu không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean trangThai = rdoDialogDangLam.isSelected();

            NhanVienDTO nvMoi = new NhanVienDTO(0, ho, ten, gioiTinh, sdt, maCV, maQuyen, trangThai, tenTaiKhoan, matKhau);
            String ketQua = nhanVienBUS.themNhanVien(nvMoi); // Phải có BUS hỗ trợ thêm
            JOptionPane.showMessageDialog(dialog, ketQua);

            if (ketQua.contains("thành công")) {
                dialog.dispose();      // Đóng dialog
                loadData();            // Cập nhật bảng chính
                lamMoiForm();          // Xóa form bên trái
            }
        });

        btnHuy.addActionListener(ev -> dialog.dispose());

        dialog.setVisible(true);
    });
        
        btnNopDonNghi.addActionListener(e -> {
            String loaiNghi = (String) cboLoaiNghi.getSelectedItem();
    
            // Lấy ngày từ JDateChooser
            Date ngayBatDau = txtTuNgayNghi.getDate();
            Date ngayKetThuc = txtDenNgayNghi.getDate();
            String lyDo = txtLyDoNghi.getText().trim();

            // Kiểm tra không để trống
            if (ngayBatDau == null || ngayKetThuc == null || lyDo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ thời gian và nhập lý do nghỉ!");
                return;
            }
            
            // Kiểm tra ngày bắt đầu <= ngày kết thúc
            if (ngayBatDau.after(ngayKetThuc)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc!");
                return;
            }

           // Kiểm tra ngày bắt đầu không được nhỏ hơn ngày hiện tại
            Date today = new Date();
            // So sánh ngày (bỏ giờ, phút, giây)
            java.util.Calendar cal1 = java.util.Calendar.getInstance();
            java.util.Calendar cal2 = java.util.Calendar.getInstance();
            cal1.setTime(ngayBatDau);
            cal2.setTime(today);

            if (cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) < cal2.get(java.util.Calendar.DAY_OF_YEAR)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu nghỉ không được nhỏ hơn ngày hiện tại!");
                return;
            }

            if (ngayBatDau.before(today)) {
                JOptionPane.showMessageDialog(this, "Ngày bắt đầu nghỉ không được nhỏ hơn ngày hiện tại!");
                return;
            }

            // 1. Tạo đối tượng và gửi
            DonNghiBUS donNghiBUS = new DonNghiBUS();
            DonNghiDTO don = new DonNghiDTO();
            don.setMaNV(Auth.getUser().getMaNV());                  
            don.setLoaiDon(loaiNghi);
            don.setNgayBatDau(ngayBatDau); 
            don.setNgayKetThuc(ngayKetThuc);
            don.setLyDo(lyDo);             
            don.setTrangThai("Chờ duyệt");                         
            don.setNgayGui(new Date());                                

            // 2. Gọi BUS thêm đơn vào CSDL
            String result = donNghiBUS.themDonNghi(don);
            JOptionPane.showMessageDialog(this, result); // Hiện thông báo thật từ BUS trả về

            // 3. NẾU thêm thành công thì mới xóa trắng form và load lại bảng
            if (result.contains("Đã gửi đơn nghỉ")) {
                txtTuNgayNghi.setDate(null);
                txtDenNgayNghi.setDate(null);
                txtLyDoNghi.setText("");
                loadDanhSachDonNghiCaNhan();
            }
        });
        
        btnTaoDonNghi.addActionListener(e -> {
            if (!phanQuyen.isNsTaoDonNghi()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền tạo đơn nghỉ!");
                return;
            }
            // Chuyển sang panel đơn nghỉ cá nhân
            CardLayout cl = (CardLayout) pnlRight.getLayout();
            cl.show(pnlRight, "DonNghiCaNhan");
            loadDanhSachDonNghiCaNhan();
        });

        btnXemDonCuaToi.addActionListener(e -> {
            if (!phanQuyen.isNsXemDonCuaMinh()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền xem đơn của mình!");
                return;
            }
            // Chuyển sang panel đơn nghỉ cá nhân
            CardLayout cl = (CardLayout) pnlRight.getLayout();
            cl.show(pnlRight, "DonNghiCaNhan");
            loadDanhSachDonNghiCaNhan();
        });

        btnSua.addActionListener(e -> {
            if (txtMaNV.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            NhanVienDTO nv = getNhanVienFromForm();
            if (nv == null) return;

            // Lấy mã nhân viên và chức vụ mới từ form
            int maNV = nv.getMaNV();
            String chucVuMoi = nv.getMaCV();

            // Lấy chức vụ cũ từ database (gọi BUS)
            NhanVienDTO nvCu = nhanVienBUS.layNhanVienTheoMa(maNV);
            String chucVuCu = nvCu != null ? nvCu.getMaCV() : "";

            String lyDo = "";
            if (!chucVuMoi.equals(chucVuCu)) {
                // Hiển thị dialog nhập lý do
                lyDo = JOptionPane.showInputDialog(this,
                        "Nhập lý do thay đổi chức vụ từ '" + chucVuCu + "' sang '" + chucVuMoi + "':",
                        "Lý do thay đổi",
                        JOptionPane.QUESTION_MESSAGE);
                if (lyDo == null) {
                    return; // Người dùng hủy nhập
                }
                if (lyDo.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Lý do không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Gọi BUS sửa nhân viên, truyền lý do (nếu có)
            String result = nhanVienBUS.suaNhanVien(nv, lyDo);
            JOptionPane.showMessageDialog(this, result);

            if (result.contains("thành công")) {
                loadData();
                lamMoiForm();
            }
            
            // Cập nhật lại thông tin hiển thị 
            // Nếu đang ở chế độ nhân viên (không có quyền xem danh sách)
            if (!phanQuyen.isNsXemDanhSach()) {
                // Load lại thông tin nhân viên hiện tại vào form
                int currentMaNV = Auth.getUser().getMaNV();
                NhanVienDTO nvUpdated = nhanVienBUS.layNhanVienTheoMa(currentMaNV);
                if (nvUpdated != null) {
                    // Cập nhật form bên trái
                    txtMaNV.setText(String.valueOf(nvUpdated.getMaNV()));
                    txtHo.setText(nvUpdated.getHo());
                    txtTen.setText(nvUpdated.getTen());
                    txtSoDienThoai.setText(nvUpdated.getSoDienThoai());
                    if (nvUpdated.getGioiTinh().equalsIgnoreCase("Nam")) {
                        rdoNam.setSelected(true);
                    } else {
                        rdoNu.setSelected(true);
                    }
                    String tenChucVu = layTenChucVuTuMa(nvUpdated.getMaCV());
                    cboChucVu.setSelectedItem(tenChucVu);
                    txtTenTaiKhoan.setText(nvUpdated.getTenTaiKhoan());
                    txtMatKhau.setText(nvUpdated.getMatKhau());
                    if (nvUpdated.isTrangThai()) {
                        rdoDangLam.setSelected(true);
                    } else {
                        rdoNghiViec.setSelected(true);
                    }
                }

                // Load lại danh sách đơn nghỉ
                loadDanhSachDonNghiCaNhan();
            } else {
                // Nếu có quyền xem danh sách, chỉ cần load lại danh sách và làm mới form
                refreshThongTinCaNhan();
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
            int maNV = Auth.getUser().getMaNV(); // luôn lấy của chính mình
            new FrmXemLuong(maNV, phanQuyen).setVisible(true);
        });
        
        btnThayDoiChucVu.addActionListener(e -> {
           
            if (txtMaNV.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần thay đổi chức vụ");
                return;
            }
            int maNV = Integer.parseInt(txtMaNV.getText());
            new FrmThayDoiChucVu(maNV,phanQuyen).setVisible(true);
        });

    }

    private NhanVienDTO getNhanVienFromForm() {
        // Validate dữ liệu
        String ho = txtHo.getText().trim();
        String ten = txtTen.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();

        if (ho.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ và tên không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Validate số điện thoại (10-11 số)
        if (!sdt.matches("^0[0-9]{9,10}$")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ! Phải bắt đầu bằng số 0 và có 10-11 số.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        int maNV = 0;
        if (!txtMaNV.getText().isEmpty()) {
            try {
                maNV = Integer.parseInt(txtMaNV.getText());
            } catch (NumberFormatException e) {
                // Bỏ qua
            }
        }

        String gioiTinh = rdoNam.isSelected() ? "Nam" : "Nữ";
        String tenChucVu = cboChucVu.getSelectedItem() != null ? cboChucVu.getSelectedItem().toString().trim() : "";

        String maCV = "";
        String maQuyen = "";
        switch (tenChucVu) {
            case "Giám đốc":
                maCV = "CV01"; maQuyen = "ADMIN"; break;
            case "Quản lý nhân sự":
                maCV = "CV02"; maQuyen = "QL_NHANSU"; break;
            case "Quản lý kho":
                maCV = "CV03"; maQuyen = "QL_KHO"; break;
            case "Quản lý bán hàng":
                maCV = "CV04"; maQuyen = "QL_KINHDOANH"; break;
            case "Nhân viên bán hàng":
                maCV = "CV05"; maQuyen = "NVBH"; break;
            case "Nhân viên nhập hàng":
                maCV = "CV06"; maQuyen = "NVNH"; break;
            default:
                maCV = "CV05"; maQuyen = "NVBH"; break;
        }

        String tenTaiKhoan = txtTenTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();

        // Validate tài khoản khi thêm mới
        if (maNV == 0 && tenTaiKhoan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên tài khoản không được để trống khi thêm mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }

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
        btnThayDoiChucVu = new javax.swing.JButton();
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
        btnXemLuong.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/search.png")));
        btnXemLuong.setText("Xem lương");

        btnThayDoiChucVu.setFont(new java.awt.Font("Segoe UI", 0, 14));
        btnThayDoiChucVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/multiple-users.png")));
        btnThayDoiChucVu.setText("Thay đổi chức vụ");
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
                                .addComponent(btnThayDoiChucVu)))))
                            
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
                    .addComponent(btnThayDoiChucVu))
                                 
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
        pnlFormDon.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createTitledBorder(null, "Nhập đơn nghỉ mới", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12)),
            javax.swing.BorderFactory.createEmptyBorder(0, 0, 50, 0) 
        ));
        cboLoaiNghi = new javax.swing.JComboBox<>();
        txtTuNgayNghi = new com.toedter.calendar.JDateChooser();
        txtTuNgayNghi.setDateFormatString("dd/MM/yyyy");
        txtTuNgayNghi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtTuNgayNghi.getJCalendar().setPreferredSize(new java.awt.Dimension(280, 160)); 

        txtDenNgayNghi = new com.toedter.calendar.JDateChooser();
        txtDenNgayNghi.setDateFormatString("dd/MM/yyyy");
        txtDenNgayNghi.setFont(new java.awt.Font("Segoe UI", 0, 14));
        txtDenNgayNghi.getJCalendar().setPreferredSize(new java.awt.Dimension(280, 160));
        
        txtLyDoNghi = new javax.swing.JTextField();
        btnNopDonNghi = new javax.swing.JButton("Nộp đơn nghỉ");
        btnNopDonNghi.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnNopDonNghi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/send.png")));
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

    private void refreshThongTinCaNhan() {
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
            String tenChucVu = layTenChucVuTuMa(nv.getMaCV());
            cboChucVu.setSelectedItem(tenChucVu);

            txtTenTaiKhoan.setText(nv.getTenTaiKhoan());
            txtMatKhau.setText(nv.getMatKhau());
            if (nv.isTrangThai()) {
                rdoDangLam.setSelected(true);
            } else {
                rdoNghiViec.setSelected(true);
            }

            // Khóa các trường nếu không có quyền sửa
            boolean coQuyenSua = phanQuyen.isNsSua();
            txtHo.setEditable(coQuyenSua);
            txtTen.setEditable(coQuyenSua);
            txtSoDienThoai.setEditable(coQuyenSua);
            rdoNam.setEnabled(coQuyenSua);
            rdoNu.setEnabled(coQuyenSua);
            cboChucVu.setEnabled(false);
            txtTenTaiKhoan.setEditable(false);
            txtMatKhau.setEditable(false);
            rdoDangLam.setEnabled(false);
            rdoNghiViec.setEnabled(false);
        }
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
    private javax.swing.JButton btnThayDoiChucVu;
    private javax.swing.JPanel pnlRight;
}