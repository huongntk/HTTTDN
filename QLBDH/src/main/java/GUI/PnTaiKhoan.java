package GUI;

import BUS.NhanVienBUS;
import BUS.PhanQuyenBUS;
import BUS.TaiKhoanBUS;
import DTO.NhanVienDTO;
import DTO.PhanQuyen;
import DTO.TaiKhoan;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PnTaiKhoan extends JPanel {
    private TaiKhoanBUS taiKhoanBUS = new TaiKhoanBUS();
    private NhanVienBUS nhanVienBUS = new NhanVienBUS();
    private PhanQuyenBUS phanQuyenBUS = new PhanQuyenBUS();

    // Components
    private JTextField txtMaNV;
    private JTextField txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboMaQuyen;
    private JComboBox<String> cboMaNV;
    private JComboBox<String> cboTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JTable tblTaiKhoan;
    private DefaultTableModel modelTaiKhoan;

    // Maps để lưu thông tin hiển thị
    private Map<String, String> quyenMap; // key: "maQuyen - tenQuyen", value: maQuyen
    private Map<String, Integer> nhanVienMap; // key: "maNV - hoTen", value: maNV
    private PhanQuyen phanQuyen;
    public PnTaiKhoan(PhanQuyen phanQuyen) {
        initComponents();
        loadDataToCombobox();
        loadDataToTable();
        addEvents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== LEFT PANEL (Form) =====
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Thông tin tài khoản"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        Font lblFont = new Font("Segoe UI", Font.PLAIN, 14);

        // Row 0: Mã NV
        gbc.gridx = 0; gbc.gridy = 0;
        leftPanel.add(new JLabel("Mã NV:"), gbc);
        txtMaNV = new JTextField();
        txtMaNV.setEditable(false);
        gbc.gridx = 1;
        leftPanel.add(txtMaNV, gbc);

        // Row 1: Tên đăng nhập
        gbc.gridx = 0; gbc.gridy = 1;
        leftPanel.add(new JLabel("Tên đăng nhập:"), gbc);
        txtTenDangNhap = new JTextField();
        gbc.gridx = 1;
        leftPanel.add(txtTenDangNhap, gbc);

        // Row 2: Mật khẩu
        gbc.gridx = 0; gbc.gridy = 2;
        leftPanel.add(new JLabel("Mật khẩu:"), gbc);
        txtMatKhau = new JPasswordField();
        gbc.gridx = 1;
        leftPanel.add(txtMatKhau, gbc);

        // Row 3: Quyền
        gbc.gridx = 0; gbc.gridy = 3;
        leftPanel.add(new JLabel("Quyền:"), gbc);
        cboMaQuyen = new JComboBox<>();
        gbc.gridx = 1;
        leftPanel.add(cboMaQuyen, gbc);

        // Row 4: Nhân viên
        gbc.gridx = 0; gbc.gridy = 4;
        leftPanel.add(new JLabel("Nhân viên:"), gbc);
        cboMaNV = new JComboBox<>();
        gbc.gridx = 1;
        leftPanel.add(cboMaNV, gbc);

        // Row 5: Trạng thái
        gbc.gridx = 0; gbc.gridy = 5;
        leftPanel.add(new JLabel("Trạng thái:"), gbc);
        cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Khóa"});
        gbc.gridx = 1;
        leftPanel.add(cboTrangThai, gbc);

        // Row 6: Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnThem = createButton("Thêm", "/icon/them.png");
        btnSua = createButton("Sửa", "/icon/sua.png");
        btnXoa = createButton("Xóa", "/icon/xoa.png");
        btnLamMoi = createButton("Làm mới", "/icon/refresh.png");
        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnLamMoi);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        leftPanel.add(buttonPanel, gbc);

        // ===== RIGHT PANEL (Table) =====
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Danh sách tài khoản"));

        String[] columns = {"Mã NV", "Tên đăng nhập", "Quyền", "Nhân viên", "Trạng thái"};
        modelTaiKhoan = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblTaiKhoan = new JTable(modelTaiKhoan);
        tblTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblTaiKhoan.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblTaiKhoan.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tblTaiKhoan);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.4);
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);
    }

    private JButton createButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(90, 35));
        ImageIcon icon = loadScaledIcon(iconPath, 18, 18);
        if (icon != null) btn.setIcon(icon);
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(8);
        return btn;
    }

    private ImageIcon loadScaledIcon(String path, int w, int h) {
        try {
            java.net.URL url = getClass().getResource(path);
            if (url == null) return null;
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            return null;
        }
    }

    private void loadDataToCombobox() {
        // Load quyền
        quyenMap = new HashMap<>();
        ArrayList<PhanQuyen> listQuyen = phanQuyenBUS.layDanhSachQuyen();
        cboMaQuyen.removeAllItems();
        for (PhanQuyen pq : listQuyen) {
            String display = pq.getMaQuyen() + " - " + pq.getTenQuyen();
            quyenMap.put(display, pq.getMaQuyen());
            cboMaQuyen.addItem(display);
        }

        // Load nhân viên
        nhanVienMap = new HashMap<>();
        ArrayList<NhanVienDTO> listNV = nhanVienBUS.layDanhSachNhanVien();
        cboMaNV.removeAllItems();
        for (NhanVienDTO nv : listNV) {
            String display = nv.getMaNV() + " - " + nv.getHo() + " " + nv.getTen();
            nhanVienMap.put(display, nv.getMaNV());
            cboMaNV.addItem(display);
        }
    }

    private void loadDataToTable() {
        modelTaiKhoan.setRowCount(0);
        ArrayList<TaiKhoan> listTK = taiKhoanBUS.layDanhSachTaiKhoan();
        for (TaiKhoan tk : listTK) {
            // Lấy tên quyền (xử lý null)
            String tenQuyen = "";
            try {
                PhanQuyen pq = phanQuyenBUS.getPhanQuyenByTen(tk.getMaQuyen());
                tenQuyen = (pq != null) ? pq.getTenQuyen() : tk.getMaQuyen();
            } catch (Exception e) {
                tenQuyen = tk.getMaQuyen();
            }
            // Lấy tên nhân viên (xử lý ngoại lệ)
            String tenNV = "N/A";
            try {
                NhanVienDTO nv = nhanVienBUS.layNhanVienTheoMa(tk.getMaNV());
                if (nv != null) {
                    tenNV = nv.getHo() + " " + nv.getTen();
                }
            } catch (Exception e) {
                // Log lỗi nếu cần
                tenNV = "Lỗi lấy thông tin";
            }
            
            String trangThai = tk.getTrangThai() == 1 ? "Hoạt động" : "Khóa";
            Object[] row = {
                tk.getMaNV(),
                tk.getTaiKhoan(),
                tenQuyen,
                tenNV,
                trangThai
            };
            modelTaiKhoan.addRow(row);
        }
    }

    private void addEvents() {
        // Khi chọn dòng trên bảng
        tblTaiKhoan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = tblTaiKhoan.getSelectedRow();
                if (selectedRow != -1) {
                    int maNV = (int) modelTaiKhoan.getValueAt(selectedRow, 0);
                    TaiKhoan tk = taiKhoanBUS.layTaiKhoanTheoMa(maNV);
                    if (tk != null) {
                        txtMaNV.setText(String.valueOf(tk.getMaNV()));
                        txtTenDangNhap.setText(tk.getTaiKhoan());
                        txtMatKhau.setText(""); // Không hiển thị mật khẩu cũ
                        
                        // Chọn quyền
                        PhanQuyen pq = phanQuyenBUS.getPhanQuyenByTen(tk.getMaQuyen());
                        String quyenDisplay = tk.getMaQuyen() + " - " + (pq != null ? pq.getTenQuyen() : "?");
                        cboMaQuyen.setSelectedItem(quyenDisplay);
                        
                        // Chọn nhân viên
                        NhanVienDTO nv = nhanVienBUS.layNhanVienTheoMa(tk.getMaNV());
                        String nvDisplay = nv.getMaNV() + " - " + nv.getHo() + " " + nv.getTen();
                        cboMaNV.setSelectedItem(nvDisplay);
                        
                        cboTrangThai.setSelectedIndex(tk.getTrangThai() == 1 ? 0 : 1);
                    }
                }
            }
        });

        btnThem.addActionListener(e -> themTaiKhoan());
        btnSua.addActionListener(e -> suaTaiKhoan());
        btnXoa.addActionListener(e -> xoaTaiKhoan());
        btnLamMoi.addActionListener(e -> lamMoi());
    }

    private void themTaiKhoan() {
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        String selectedQuyen = (String) cboMaQuyen.getSelectedItem();
        String selectedNV = (String) cboMaNV.getSelectedItem();
        int trangThai = cboTrangThai.getSelectedIndex() == 0 ? 1 : 0;

        if (tenDangNhap.isEmpty() || matKhau.isEmpty() || selectedQuyen == null || selectedNV == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String maQuyen = quyenMap.get(selectedQuyen);
        int maNV = nhanVienMap.get(selectedNV);

        TaiKhoan tk = new TaiKhoan(maNV, tenDangNhap, matKhau, maQuyen, trangThai);
        boolean success = taiKhoanBUS.themTaiKhoan(tk);
        if (success) {
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công!");
            loadDataToTable();
            lamMoiForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại! Tên đăng nhập có thể đã tồn tại hoặc nhân viên đã có tài khoản.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaTaiKhoan() {
        if (txtMaNV.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int maNV = Integer.parseInt(txtMaNV.getText());
        String tenDangNhap = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        String selectedQuyen = (String) cboMaQuyen.getSelectedItem();
        // Không lấy maNV từ combobox vì không thay đổi được
        int trangThai = cboTrangThai.getSelectedIndex() == 0 ? 1 : 0;

        if (tenDangNhap.isEmpty() || selectedQuyen == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String maQuyen = quyenMap.get(selectedQuyen);

        TaiKhoan tk = new TaiKhoan(maNV, tenDangNhap, matKhau, maQuyen, trangThai);
        boolean success = taiKhoanBUS.suaTaiKhoan(tk, !matKhau.isEmpty()); // cập nhật mật khẩu nếu có nhập mới
        if (success) {
            JOptionPane.showMessageDialog(this, "Sửa tài khoản thành công!");
            loadDataToTable();
            lamMoiForm();
        } else {
            JOptionPane.showMessageDialog(this, "Sửa thất bại! Có thể tên đăng nhập đã tồn tại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaTaiKhoan() {
        if (txtMaNV.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa (vô hiệu hóa) tài khoản này?",
                "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            int maNV = Integer.parseInt(txtMaNV.getText());
            boolean success = taiKhoanBUS.xoaTaiKhoan(maNV);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
                loadDataToTable();
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void lamMoi() {
        lamMoiForm();
        loadDataToTable();
    }

    private void lamMoiForm() {
        txtMaNV.setText("");
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        if (cboMaQuyen.getItemCount() > 0) cboMaQuyen.setSelectedIndex(0);
        if (cboMaNV.getItemCount() > 0) cboMaNV.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        tblTaiKhoan.clearSelection();
    }
}