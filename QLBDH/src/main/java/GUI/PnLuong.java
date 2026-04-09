package GUI;

import BUS.BangLuongBUS;
import BUS.LichSuChucVuBUS;
import BUS.NhanVienBUS;
import DAO.LuongCoBanTheoChucVuDAO;
import DTO.BangLuongDTO;
import DTO.LichSuChucVu;
import DTO.NhanVienDTO;
import DTO.PhanQuyen;
import UTIL.Auth;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnLuong extends JPanel {
    private PhanQuyen phanQuyen;
    private NhanVienBUS nhanVienBUS;
    private BangLuongBUS bangLuongBUS;
    private LichSuChucVuBUS lichSuCVBUS;
    private DefaultTableModel modelNhanVien;
    private DefaultTableModel modelLuong;
    private DefaultTableModel modelLichSuCV;
    private ArrayList<NhanVienDTO> currentNhanVienList;

    // Components
    private JTable tblNhanVien;
    private JTable tblLuongChiTiet;
    private JTable tblLichSuCV;
    private JComboBox<Integer> cboThang;
    private JComboBox<Integer> cboNam;
    private JTextField txtThuong;
    private JTextField txtPhat;
    private JButton btnTinhLuong;
    private JButton btnXemLuong;
    private JButton btnInLuong;
    private JButton btnLamMoi;
    private JButton btnCauHinhLuong;
    private JButton btnLuuThuongPhat;
    private JTextField txtTimKiem;
    private JLabel lblHoTen;
    private JLabel lblChucVu;
    private JLabel lblLuongCoBan;
    private JLabel lblPhuCap;
    private JLabel lblTongLuong;
    private ArrayList<Object[]> rawNhanVienData;

    public PnLuong(PhanQuyen pq) {
        this.phanQuyen = pq;
        nhanVienBUS = new NhanVienBUS();
        bangLuongBUS = new BangLuongBUS();
        lichSuCVBUS = new LichSuChucVuBUS();
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
//        btnXemLuong = new JButton("Xem lương");
        btnInLuong = new JButton("In lương");
        btnLamMoi = new JButton("Làm mới");
        btnCauHinhLuong = new JButton ("Cấu hình lương");
        btnLuuThuongPhat = new JButton ("Sửa thưởng/phạt");

        pnlTop.add(btnTinhLuong);
//        pnlTop.add(btnXemLuong);
        pnlTop.add(btnInLuong);
        pnlTop.add(btnLamMoi);
        pnlTop.add(btnCauHinhLuong);
        pnlTop.add(btnLuuThuongPhat);

   

        add(pnlTop, BorderLayout.NORTH);

        // ========== Panel trung tâm: Bảng nhân viên (trái) và Chi tiết lương (phải) ==========
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(500);
        splitPane.setResizeWeight(0.5);
        
        JPanel pnlLeftContent = new JPanel(new BorderLayout());
        
        JPanel pnlHeaderLeft = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Căn trái
       
        txtTimKiem = new JTextField(15);
        pnlHeaderLeft.add(new JLabel("Tìm kiếm: "));
        pnlHeaderLeft.add(txtTimKiem);

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
        
        pnlLeftContent.add(pnlHeaderLeft, BorderLayout.NORTH);
        pnlLeftContent.add(scrollNV, BorderLayout.CENTER);

        // Panel chi tiết lương (bên phải)
        JPanel pnlChiTiet = new JPanel(new BorderLayout(5, 5));
        pnlChiTiet.setBorder(BorderFactory.createTitledBorder("Chi tiết lương tháng"));

        // Form thông tin
        JPanel pnlForm = new JPanel(new GridLayout(7, 3, 10, 10));
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

        // Tabbed pane: Lịch sử chức vụ + Lịch sử lương
        JTabbedPane tabHistory = new JTabbedPane();

        // --- Lịch sử chức vụ ---
        String[] colLichSuCV = {"Ngày thay đổi", "Chức vụ cũ", "Chức vụ mới", "Lý do"};
        modelLichSuCV = new DefaultTableModel(colLichSuCV, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblLichSuCV = new JTable(modelLichSuCV);
        JScrollPane scrollLSCV = new JScrollPane(tblLichSuCV);
        tabHistory.addTab("Lịch sử chức vụ", scrollLSCV);

        // --- Lịch sử lương ---
        String[] colLuong = {"Tháng/Năm", "Lương CB", "Phụ cấp", "Thưởng", "Phạt", "Thực lĩnh", "Trạng thái"};
        modelLuong = new DefaultTableModel(colLuong, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblLuongChiTiet = new JTable(modelLuong);
        JScrollPane scrollLuong = new JScrollPane(tblLuongChiTiet);
        tabHistory.addTab("Lịch sử lương", scrollLuong);

        pnlChiTiet.add(tabHistory, BorderLayout.CENTER);

        splitPane.setLeftComponent(pnlLeftContent);
        splitPane.setRightComponent(pnlChiTiet);
        add(splitPane, BorderLayout.CENTER);
    }

    private void loadData() {
        BangLuongBUS bus = new BangLuongBUS();
        rawNhanVienData = bus.selectAllNhanVienWithLuong();
        modelNhanVien.setRowCount(0);

        for (Object[] row : rawNhanVienData) {
            modelNhanVien.addRow(formatRowForDisplay(row));
        }
    }

    private void timKiem(String tuKhoa) {
        if (rawNhanVienData == null) {
            return;
        }

        modelNhanVien.setRowCount(0); // Xóa dữ liệu cũ trên bảng

        if (tuKhoa.trim().isEmpty()) {
            for (Object[] row : rawNhanVienData) {
                modelNhanVien.addRow(formatRowForDisplay(row));
            }
            return;
        }

        String keyword = tuKhoa.toLowerCase();

        for (Object[] row : rawNhanVienData) {
            
            String maNV = row[0].toString();
            String hoTen = row[1].toString().toLowerCase();

            if (maNV.contains(keyword) || hoTen.contains(keyword)) {
                modelNhanVien.addRow(formatRowForDisplay(row));
            }
        }
    }

    private Object[] formatRowForDisplay(Object[] row) {
        Object[] formatted = new Object[row.length];
        formatted[0] = row[0]; 
        formatted[1] = row[1]; 
        formatted[2] = row[2]; 

        if (row[3] != null) {
            double luongCB = ((Number) row[3]).doubleValue();
            formatted[3] = String.format("%,.0f", luongCB);
        } else {
            formatted[3] = "0";
        }

        if (row[4] != null) {
            double phuCap = ((Number) row[4]).doubleValue();
            formatted[4] = String.format("%,.0f", phuCap);
        } else {
            formatted[4] = "0";
        }

        for (int i = 5; i < row.length; i++) {
            formatted[i] = row[i];
        }

        return formatted;
    }
    
    private void loadThuongPhatForCurrentMonth(int maNV) {
        int thang = (int) cboThang.getSelectedItem();
        int nam = (int) cboNam.getSelectedItem();
        BangLuongDTO bl = bangLuongBUS.getByMaNVAndMonth(maNV, thang, nam);
        if (bl != null) {
            txtThuong.setText(String.format("%,.0f", bl.getThuong()));
            txtPhat.setText(String.format("%,.0f", bl.getPhat()));
        } else {
            txtThuong.setText("0");
            txtPhat.setText("0");
        }
        tinhTongLuongTamThoi(); // cập nhật tổng lương hiển thị
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
            tinhTongLuongTamThoi();
            loadThuongPhatForCurrentMonth(maNV);
        }
    }

    private void xemLichSuChucVu(int maNV) {
        modelLichSuCV.setRowCount(0);
        ArrayList<LichSuChucVu> list = lichSuCVBUS.layLichSuTheoNV(maNV);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (LichSuChucVu ls : list) {
            modelLichSuCV.addRow(new Object[]{
                sdf.format(ls.getNgayThayDoi()),
                ls.getMaCVu(),
                ls.getMaCMoi(),
                ls.getGhiChu()
            });
        }
    }

    private void xemLuongNhanVien() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) return;
        int maNV = (int) modelNhanVien.getValueAt(selectedRow, 0);
        modelLuong.setRowCount(0);
        ArrayList<BangLuongDTO> list = bangLuongBUS.getListByMaNV(maNV);
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
            xemLuongNhanVien();
        }
    }

    private void inLuong() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên để in lương", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maNV = (int) modelNhanVien.getValueAt(selectedRow, 0);
        new FrmInBangLuong(maNV).setVisible(true);
    }
    private void moDialogCauHinhLuong() {
    FrmSuaLuongCoBan dialog = new FrmSuaLuongCoBan((JFrame) SwingUtilities.getWindowAncestor(this));
    dialog.setVisible(true);
    // Sau khi đóng dialog, tải lại danh sách nhân viên để cập nhật lương mới
    loadData();
    // Nếu đang chọn một nhân viên, refresh thông tin hiển thị
    int row = tblNhanVien.getSelectedRow();
    if (row != -1) {
        int maNV = (int) modelNhanVien.getValueAt(row, 0);
        hienThiThongTinNhanVien(maNV);
        xemLuongNhanVien(); // có thể cập nhật lại lịch sử lương nếu cần
    }
}

    private void addEvents() {
        tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblNhanVien.getSelectedRow();
                if (row != -1) {
                    int maNV = (int) modelNhanVien.getValueAt(row, 0);
                    hienThiThongTinNhanVien(maNV);
                    xemLichSuChucVu(maNV);
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
//        btnXemLuong.addActionListener(e -> xemLuongNhanVien());
        btnInLuong.addActionListener(e -> inLuong());
        btnLamMoi.addActionListener(e -> {
            loadData();
            txtTimKiem.setText("");
            modelLuong.setRowCount(0);
            modelLichSuCV.setRowCount(0);
            lblHoTen.setText("---");
            lblChucVu.setText("---");
            lblLuongCoBan.setText("0");
            lblPhuCap.setText("0");
            txtThuong.setText("0");
            txtPhat.setText("0");
            lblTongLuong.setText("0");
        });
        btnCauHinhLuong.addActionListener(e -> moDialogCauHinhLuong());
        btnLuuThuongPhat.addActionListener(e -> luuThuongPhat());
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                timKiem(txtTimKiem.getText().trim());
            }
        });
        cboThang.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int row = tblNhanVien.getSelectedRow();
                if (row != -1) {
                    int maNV = (int) modelNhanVien.getValueAt(row, 0);
                    loadThuongPhatForCurrentMonth(maNV);
                }
            }
        });

        cboNam.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                int row = tblNhanVien.getSelectedRow();
                if (row != -1) {
                    int maNV = (int) modelNhanVien.getValueAt(row, 0);
                    loadThuongPhatForCurrentMonth(maNV);
                }
            }
        });
    }
    
    private void luuThuongPhat() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa thưởng/phạt", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy giá trị hiện tại
        double thuongHienTai = 0;
        double phatHienTai = 0;
        try {
            thuongHienTai = Double.parseDouble(txtThuong.getText().trim());
            phatHienTai = Double.parseDouble(txtPhat.getText().trim());
        } catch (NumberFormatException ignored) {}

        // Tạo dialog nhập liệu
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa thưởng/phạt", true);
        dialog.setLayout(new GridLayout(3, 2, 10, 10));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);

        JTextField txtThuongMoi = new JTextField(String.format("%.0f", thuongHienTai));
        JTextField txtPhatMoi = new JTextField(String.format("%.0f", phatHienTai));

        dialog.add(new JLabel("Thưởng (+):"));
        dialog.add(txtThuongMoi);
        dialog.add(new JLabel("Phạt (-):"));
        dialog.add(txtPhatMoi);

        JButton btnOk = new JButton("OK");
        JButton btnCancel = new JButton("Hủy");
        dialog.add(btnOk);
        dialog.add(btnCancel);

        btnOk.addActionListener(ev -> {
            try {
                double thuong = Double.parseDouble(txtThuongMoi.getText().trim());
                double phat = Double.parseDouble(txtPhatMoi.getText().trim());
                if (thuong < 0 || phat < 0) {
                    JOptionPane.showMessageDialog(dialog, "Giá trị thưởng và phạt không được âm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                txtThuong.setText(String.format("%.0f", thuong));
                txtPhat.setText(String.format("%.0f", phat));
                tinhTongLuongTamThoi(); 
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập số hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(ev -> dialog.dispose());

        dialog.setVisible(true);
    }

    private void configureByPermission() {
        btnTinhLuong.setVisible(phanQuyen.isNsTinhLuong());
        btnInLuong.setVisible(phanQuyen.isNsInBangLuong());
        btnLuuThuongPhat.setVisible(phanQuyen.isNsTinhLuong());
        if (!phanQuyen.isNsXemDanhSach()) {
            int maNV = Auth.getUser().getMaNV();
            NhanVienDTO nv = nhanVienBUS.layNhanVienTheoMa(maNV);
            if (nv != null) {
                ArrayList<NhanVienDTO> self = new ArrayList<>();
                self.add(nv);
                currentNhanVienList = self;
                modelNhanVien.setRowCount(0);
                double luongCB = nhanVienBUS.getLuongCoBanByChucVu(nv.getMaCV());
                double phuCap = nhanVienBUS.getPhuCapByChucVu(nv.getMaCV());
                modelNhanVien.addRow(new Object[]{nv.getMaNV(), nv.getHo() + " " + nv.getTen(), nv.getMaCV(), luongCB, phuCap});
                tblNhanVien.setEnabled(false);
                txtThuong.setEditable(false);
                txtPhat.setEditable(false);
                btnTinhLuong.setEnabled(false);
            }
        }
    }
}