package GUI;

import BUS.BangLuongBUS;
import BUS.NhanVienBUS;
import DTO.BangLuongDTO;
import DTO.NhanVienDTO;
import DTO.PhanQuyen;
import UTIL.Auth;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnLuong extends JPanel {
    private PhanQuyen phanQuyen;
    private NhanVienBUS nhanVienBUS;
    private BangLuongBUS bangLuongBUS;
    private DefaultTableModel modelNhanVien;
    private DefaultTableModel modelLuong;
    private ArrayList<NhanVienDTO> currentNhanVienList;

    // Components
    private JTable tblNhanVien;
    private JTable tblLuongChiTiet;
    private JComboBox<Integer> cboThang;
    private JComboBox<Integer> cboNam;
    private JTextField txtThuong;
    private JTextField txtPhat;
    private JButton btnTinhLuong;
    private JButton btnXemLuong;
    private JButton btnInLuong;
    private JButton btnLamMoi;
    private JTextField txtTimKiem;
    private JLabel lblHoTen;
    private JLabel lblChucVu;
    private JLabel lblLuongCoBan;
    private JLabel lblPhuCap;
    private JLabel lblTongLuong;

    public PnLuong(PhanQuyen pq) {
        this.phanQuyen = pq;
        nhanVienBUS = new NhanVienBUS();
        bangLuongBUS = new BangLuongBUS();
        initComponents();
        loadData();
        addEvents();
        configureByPermission();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ========== Panel trên: Bộ lọc và nút chức năng ==========
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        pnlTop.add(new JLabel("Tháng:"));
        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cboThang.addItem(i);
        cboThang.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH) + 1);
        pnlTop.add(cboThang);

        pnlTop.add(new JLabel("Năm:"));
        cboNam = new JComboBox<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = year - 5; i <= year + 5; i++) cboNam.addItem(i);
        cboNam.setSelectedItem(year);
        pnlTop.add(cboNam);

        btnTinhLuong = new JButton("Tính lương tháng");
        btnXemLuong = new JButton("Xem lương");
        btnInLuong = new JButton("In lương");
        btnLamMoi = new JButton("Làm mới");

        pnlTop.add(btnTinhLuong);
        pnlTop.add(btnXemLuong);
        pnlTop.add(btnInLuong);
        pnlTop.add(btnLamMoi);

        pnlTop.add(new JLabel("Tìm kiếm:"));
        txtTimKiem = new JTextField(15);
        pnlTop.add(txtTimKiem);

        add(pnlTop, BorderLayout.NORTH);

        // ========== Panel trung tâm: Bảng nhân viên (trái) và Chi tiết lương (phải) ==========
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.5);

        // Bảng danh sách nhân viên
        String[] colNhanVien = {"Mã NV", "Họ tên", "Chức vụ", "Lương cơ bản", "Phụ cấp"};
        modelNhanVien = new DefaultTableModel(colNhanVien, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblNhanVien = new JTable(modelNhanVien);
        tblNhanVien.setRowHeight(25);
        JScrollPane scrollNV = new JScrollPane(tblNhanVien);
        scrollNV.setBorder(BorderFactory.createTitledBorder("Danh sách nhân viên"));

        // Panel chi tiết lương (bên phải)
        JPanel pnlChiTiet = new JPanel(new BorderLayout(5, 5));
        pnlChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết lương tháng"));

        // Form thông tin
        JPanel pnlForm = new JPanel(new GridLayout(6, 2, 10, 10));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pnlForm.add(new JLabel("Họ tên:"));
        lblHoTen = new JLabel("---");
        pnlForm.add(lblHoTen);
        pnlForm.add(new JLabel("Chức vụ:"));
        lblChucVu = new JLabel("---");
        pnlForm.add(lblChucVu);
        pnlForm.add(new JLabel("Lương cơ bản:"));
        lblLuongCoBan = new JLabel("0");
        pnlForm.add(lblLuongCoBan);
        pnlForm.add(new JLabel("Phụ cấp:"));
        lblPhuCap = new JLabel("0");
        pnlForm.add(lblPhuCap);
        pnlForm.add(new JLabel("Thưởng (+):"));
        txtThuong = new JTextField("0");
        pnlForm.add(txtThuong);
        pnlForm.add(new JLabel("Phạt/khấu trừ (-):"));
        txtPhat = new JTextField("0");
        pnlForm.add(txtPhat);
        pnlForm.add(new JLabel("Tổng lương thực lĩnh:"));
        lblTongLuong = new JLabel("0");
        pnlForm.add(lblTongLuong);

        pnlChiTiet.add(pnlForm, BorderLayout.NORTH);

        // Bảng lương chi tiết (lịch sử)
        String[] colLuong = {"Tháng/Năm", "Lương CB", "Phụ cấp", "Thưởng", "Phạt", "Thực lĩnh", "Trạng thái"};
        modelLuong = new DefaultTableModel(colLuong, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblLuongChiTiet = new JTable(modelLuong);
        JScrollPane scrollLuong = new JScrollPane(tblLuongChiTiet);
        scrollLuong.setBorder(BorderFactory.createTitledBorder("Lịch sử lương"));
        pnlChiTiet.add(scrollLuong, BorderLayout.CENTER);

        splitPane.setLeftComponent(scrollNV);
        splitPane.setRightComponent(pnlChiTiet);
        add(splitPane, BorderLayout.CENTER);
    }

    private void loadData() {
        currentNhanVienList = nhanVienBUS.layDanhSachNhanVien();
        modelNhanVien.setRowCount(0);
        for (NhanVienDTO nv : currentNhanVienList) {
            // Lấy lương cơ bản và phụ cấp từ cấu hình (cần method trong NhanVienBUS)
            double luongCB = nhanVienBUS.getLuongCoBanByChucVu(nv.getMaCV());
            double phuCap = nhanVienBUS.getPhuCapByChucVu(nv.getMaCV());
            modelNhanVien.addRow(new Object[]{
                nv.getMaNV(),
                nv.getHo() + " " + nv.getTen(),
                nv.getMaCV(),
                luongCB,
                phuCap
            });
        }
    }

    private void timKiem(String tuKhoa) {
        ArrayList<NhanVienDTO> filtered = new ArrayList<>();
        for (NhanVienDTO nv : currentNhanVienList) {
            String fullName = nv.getHo() + " " + nv.getTen();
            if (fullName.toLowerCase().contains(tuKhoa.toLowerCase()) ||
                String.valueOf(nv.getMaNV()).contains(tuKhoa)) {
                filtered.add(nv);
            }
        }
        modelNhanVien.setRowCount(0);
        for (NhanVienDTO nv : filtered) {
            double luongCB = nhanVienBUS.getLuongCoBanByChucVu(nv.getMaCV());
            double phuCap = nhanVienBUS.getPhuCapByChucVu(nv.getMaCV());
            modelNhanVien.addRow(new Object[]{
                nv.getMaNV(),
                nv.getHo() + " " + nv.getTen(),
                nv.getMaCV(),
                luongCB,
                phuCap
            });
        }
    }

    private void hienThiThongTinNhanVien(int maNV) {
        NhanVienDTO nv = nhanVienBUS.layNhanVienTheoMa(maNV);
        if (nv != null) {
            lblHoTen.setText(nv.getHo() + " " + nv.getTen());
            lblChucVu.setText(nv.getMaCV());
            double luongCB = nhanVienBUS.getLuongCoBanByChucVu(nv.getMaCV());
            double phuCap = nhanVienBUS.getPhuCapByChucVu(nv.getMaCV());
            lblLuongCoBan.setText(String.format("%,.0f", luongCB));
            lblPhuCap.setText(String.format("%,.0f", phuCap));
            // Tính lại tổng lương khi thay đổi thưởng/phạt
            tinhTongLuongTamThoi();
        }
    }

    private void tinhTongLuongTamThoi() {
        try {
            double luongCB = Double.parseDouble(lblLuongCoBan.getText().replace(",", ""));
            double phuCap = Double.parseDouble(lblPhuCap.getText().replace(",", ""));
            double thuong = Double.parseDouble(txtThuong.getText().trim());
            double phat = Double.parseDouble(txtPhat.getText().trim());
            double tong = luongCB + phuCap + thuong - phat;
            lblTongLuong.setText(String.format("%,.0f", tong));
        } catch (NumberFormatException ex) {
            lblTongLuong.setText("0");
        }
    }

    private void tinhLuongChoNhanVien() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần tính lương", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maNV = (int) modelNhanVien.getValueAt(selectedRow, 0);
        int thang = (int) cboThang.getSelectedItem();
        int nam = (int) cboNam.getSelectedItem();

        // Kiểm tra đã tính lương tháng này chưa
        if (bangLuongBUS.kiemTraTonTai(maNV, thang, nam)) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Lương tháng " + thang + "/" + nam + " đã được tính. Bạn có muốn tính lại?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
        }

        double thuong = Double.parseDouble(txtThuong.getText().trim());
        double phat = Double.parseDouble(txtPhat.getText().trim());
        BangLuongDTO bl = bangLuongBUS.tinhLuongChoNhanVien(maNV, thang, nam, thuong, phat);
        if (bl == null) {
            JOptionPane.showMessageDialog(this, "Không thể tính lương. Kiểm tra lại dữ liệu nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String result = bangLuongBUS.luuBangLuong(bl);
        JOptionPane.showMessageDialog(this, result);
        if (result.contains("thành công")) {
            // Cập nhật lại bảng lương chi tiết nếu đang xem nhân viên này
            if (maNV == (tblNhanVien.getSelectedRow() != -1 ? (int) modelNhanVien.getValueAt(tblNhanVien.getSelectedRow(), 0) : -1)) {
                xemLuongNhanVien();
            }
        }
    }

    private void xemLuongNhanVien() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để xem lương", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maNV = (int) modelNhanVien.getValueAt(selectedRow, 0);
        modelLuong.setRowCount(0);
        ArrayList<BangLuongDTO> list = bangLuongBUS.getListByMaNV(maNV);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        for (BangLuongDTO bl : list) {
            modelLuong.addRow(new Object[]{
                bl.getThang() + "/" + bl.getNam(),
                String.format("%,.0f", bl.getLuongCoBan()),
                String.format("%,.0f", bl.getPhuCap()),
                String.format("%,.0f", bl.getThuong()),
                String.format("%,.0f", bl.getPhat()),
                String.format("%,.0f", bl.getTongLuong()),
                bl.getTrangThai() == 1 ? "Đã duyệt" : "Tạm tính"
            });
        }
    }

    private void inLuong() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để in lương", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maNV = (int) modelNhanVien.getValueAt(selectedRow, 0);
        int thang = (int) cboThang.getSelectedItem();
        int nam = (int) cboNam.getSelectedItem();
        BangLuongDTO bl = bangLuongBUS.getByMaNVAndMonth(maNV, thang, nam);
        if (bl == null) {
            JOptionPane.showMessageDialog(this, "Chưa có bảng lương cho tháng này. Vui lòng tính lương trước.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        // Gọi form in lương (có thể dùng FrmInBangLuong tương tự như trong PnNhanVien)
        new FrmInBangLuong(maNV).setVisible(true);
    }

    private void addEvents() {
        tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblNhanVien.getSelectedRow();
                if (row != -1) {
                    int maNV = (int) modelNhanVien.getValueAt(row, 0);
                    hienThiThongTinNhanVien(maNV);
                    xemLuongNhanVien();
                }
            }
        });

        txtThuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tinhTongLuongTamThoi();
            }
        });
        txtPhat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tinhTongLuongTamThoi();
            }
        });

        btnTinhLuong.addActionListener(e -> tinhLuongChoNhanVien());
        btnXemLuong.addActionListener(e -> xemLuongNhanVien());
        btnInLuong.addActionListener(e -> inLuong());
        btnLamMoi.addActionListener(e -> {
            loadData();
            txtTimKiem.setText("");
            modelLuong.setRowCount(0);
            lblHoTen.setText("---");
            lblChucVu.setText("---");
            lblLuongCoBan.setText("0");
            lblPhuCap.setText("0");
            txtThuong.setText("0");
            txtPhat.setText("0");
            lblTongLuong.setText("0");
        });
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                timKiem(txtTimKiem.getText().trim());
            }
        });
    }

    private void configureByPermission() {
        // Ẩn nút tính lương nếu không có quyền
        btnTinhLuong.setVisible(phanQuyen.isNsTinhLuong());
        btnInLuong.setVisible(phanQuyen.isNsInBangLuong());
        // Nếu không có quyền xem danh sách nhân viên, chỉ cho xem lương của chính mình
        if (!phanQuyen.isNsXemDanhSach()) {
            int maNV = Auth.getUser().getMaNV();
            // Lọc chỉ hiển thị nhân viên hiện tại
            ArrayList<NhanVienDTO> self = new ArrayList<>();
            NhanVienDTO nv = nhanVienBUS.layNhanVienTheoMa(maNV);
            if (nv != null) self.add(nv);
            currentNhanVienList = self;
            modelNhanVien.setRowCount(0);
            double luongCB = nhanVienBUS.getLuongCoBanByChucVu(nv.getMaCV());
            double phuCap = nhanVienBUS.getPhuCapByChucVu(nv.getMaCV());
            modelNhanVien.addRow(new Object[]{nv.getMaNV(), nv.getHo() + " " + nv.getTen(), nv.getMaCV(), luongCB, phuCap});
            tblNhanVien.setEnabled(false); // không cho chọn, nhưng vẫn hiển thị
            txtThuong.setEditable(false);
            txtPhat.setEditable(false);
            btnTinhLuong.setEnabled(false);
        }
    }
}