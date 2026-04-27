package GUI;

import BUS.NhaCungCapBUS;
import DTO.NhaCungCapDTO;
import DTO.PhanQuyen;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.List;
import java.util.regex.Pattern;

public class PnNhaCungCap extends JPanel {
    private PhanQuyen phanQuyen;
    private JTextField txtMa, txtTen, txtDiaChi, txtSDT;
    private JCheckBox chkHoatDong;
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private NhaCungCapBUS bus = new NhaCungCapBUS();
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JButton btnCapNhat;
    private JTextField txtTimKiem;
    private JComboBox<String> cbTimKiem;
    
    public PnNhaCungCap(PhanQuyen pq) {
        this.phanQuyen = pq;
        
        setPreferredSize(new Dimension(1100, 650));
        setLayout(new BorderLayout(8, 8));
        
        Font lblFont = new Font("Segoe UI", Font.PLAIN, 14);
        
        // ===== LEFT PANEL =====
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "THÔNG TIN NHÀ CUNG CẤP",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));
        
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 12, 8, 12);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        
        JLabel lblMa = new JLabel("Mã NCC:");
        JLabel lblTen = new JLabel("Tên NCC:*");
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        JLabel lblSDT = new JLabel("Số điện thoại:*");
        
        lblMa.setFont(lblFont); 
        lblTen.setFont(lblFont);
        lblTen.setForeground(Color.RED);
        lblDiaChi.setFont(lblFont); 
        lblSDT.setFont(lblFont);
        lblSDT.setForeground(Color.RED);
        
        txtMa = new JTextField(); 
        txtMa.setEditable(false);
        txtMa.setBackground(new Color(240, 240, 240));
        
        txtTen = new JTextField(20);
        txtDiaChi = new JTextField(20);
        txtSDT = new JTextField(20);
        chkHoatDong = new JCheckBox("Hoạt động");
        chkHoatDong.setBackground(Color.WHITE);
        chkHoatDong.setFont(lblFont);
        chkHoatDong.setSelected(true);
        
        int row = 0;
        g.gridx = 0; g.gridy = row; leftPanel.add(lblMa, g);
        g.gridx = 1; leftPanel.add(txtMa, g);
        row++;
        g.gridx = 0; g.gridy = row; leftPanel.add(lblTen, g);
        g.gridx = 1; leftPanel.add(txtTen, g);
        row++;
        g.gridx = 0; g.gridy = row; leftPanel.add(lblDiaChi, g);
        g.gridx = 1; leftPanel.add(txtDiaChi, g);
        row++;
        g.gridx = 0; g.gridy = row; leftPanel.add(lblSDT, g);
        g.gridx = 1; leftPanel.add(txtSDT, g);
        row++;
        
        
        row++;
        g.gridx = 0; g.gridy = row; g.gridwidth = 2;
        g.insets = new Insets(8, 14, 8, 14); 
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10)); 
        pnlButtons.setOpaque(false);
        
        btnThem = createClassicButton("Thêm mới", "/icon/them.png");
        btnCapNhat = createClassicButton("Cập nhật", "/icon/sua.png");
        btnLamMoi = createClassicButton("Làm mới", "/icon/undo.png");
        
        pnlButtons.add(btnThem);
        pnlButtons.add(btnCapNhat);
        pnlButtons.add(btnLamMoi);
        leftPanel.add(pnlButtons, g);
        
        // ===== RIGHT PANEL =====
        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "DANH SÁCH NHÀ CUNG CẤP",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14)));
        
        // ===== PANEL TÌM KIẾM =====
        JPanel pnlTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pnlTimKiem.setBackground(Color.WHITE);
        
        JLabel lblTimKiem = new JLabel("Tìm kiếm:");
        lblTimKiem.setFont(lblFont);
        
        cbTimKiem = new JComboBox<>(new String[]{"Tên NCC", "Số điện thoại", "Địa chỉ"});
        cbTimKiem.setFont(lblFont);
        cbTimKiem.setPreferredSize(new Dimension(120, 30));
        
        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(lblFont);
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        
        JButton btnTimKiem = createClassicButton("Tìm", "/icon/tim.png");
        btnTimKiem.setPreferredSize(new Dimension(80, 32));
        
        JButton btnHuyTim = createClassicButton("Hiển thị tất cả", "/icon/refresh.png");
        btnHuyTim.setPreferredSize(new Dimension(130, 32));
        
        pnlTimKiem.add(lblTimKiem);
        pnlTimKiem.add(cbTimKiem);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnTimKiem);
        pnlTimKiem.add(btnHuyTim);
        
        String[] cols = {"Mã NCC", "Tên NCC", "Địa chỉ", "Số điện thoại"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Setup sorter cho tìm kiếm
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        
        rightPanel.add(pnlTimKiem, BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // ===== Bottom Buttons =====
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        btnXoa = createClassicButton("Xóa", "/icon/xoa.png");
        pnlBottom.add(btnXoa);
        rightPanel.add(pnlBottom, BorderLayout.SOUTH);
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setDividerLocation(420); // Tăng lên 420 để hiển thị tốt hơn
        split.setResizeWeight(0.35);
        add(split, BorderLayout.CENTER);
        
        // ====== SỰ KIỆN ======
        loadData();
        
        // Tìm kiếm theo điều kiện
        btnTimKiem.addActionListener(e -> timKiem());
        
        // Hủy tìm kiếm, hiển thị lại toàn bộ
        btnHuyTim.addActionListener(e -> {
            txtTimKiem.setText("");
            sorter.setRowFilter(null);
            loadData();
        });
        
        // Tìm kiếm khi nhấn Enter
        txtTimKiem.addActionListener(e -> timKiem());
        
        // Click chọn dòng → hiển thị lên form để sửa/xóa
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r != -1) {
                    // Lấy chỉ số thực tế trong model (vì có thể đang filtered)
                    int modelRow = table.convertRowIndexToModel(r);
                    txtMa.setText(model.getValueAt(modelRow, 0).toString());
                    txtTen.setText(model.getValueAt(modelRow, 1).toString());
                    txtDiaChi.setText(model.getValueAt(modelRow, 2).toString());
                    txtSDT.setText(model.getValueAt(modelRow, 3).toString());
                    chkHoatDong.setSelected(model.getValueAt(modelRow, 4).toString().equals("Hoạt động"));
                }
            }
        });
        
        // Thêm mới
        btnThem.addActionListener(e -> themMoi());
        
        // Cập nhật
        btnCapNhat.addActionListener(e -> capNhat());
        
        // Xóa
        btnXoa.addActionListener(e -> xoa());
        
        // Làm mới form
        btnLamMoi.addActionListener(e -> {
            clearForm();
            table.clearSelection();
        });
        
        configureByPermission();
        
        // Ràng buộc cho SDT (chỉ cho phép nhập số)
        txtSDT.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    e.consume();
                    JOptionPane.showMessageDialog(PnNhaCungCap.this, 
                        "Số điện thoại chỉ được nhập số!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    
    // ===== PHƯƠNG THỨC TÌM KIẾM =====
    private void timKiem() {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        
        int luaChon = cbTimKiem.getSelectedIndex();
        final int cotTim;
        switch (luaChon) {
            case 0: cotTim = 1; break; // Tên NCC
            case 1: cotTim = 3; break; // Số điện thoại
            case 2: cotTim = 2; break; // Địa chỉ
            default: cotTim = 1;
        }
        
        sorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String value = entry.getStringValue(cotTim);
                if (value == null) return false;
                return value.toLowerCase().contains(tuKhoa.toLowerCase());
            }
        });
        
        if (sorter.getViewRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy nhà cung cấp nào với từ khóa '" + tuKhoa + "'!", 
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // ===== PHƯƠNG THỨC THÊM MỚI =====
    private void themMoi() {
        if (!validateInput()) return;
        
        String ten = txtTen.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String sdt = txtSDT.getText().trim();
        boolean trangThai = chkHoatDong.isSelected();
        
        if (bus.isTenExists(ten)) {
            JOptionPane.showMessageDialog(this, 
                "Tên nhà cung cấp '" + ten + "' đã tồn tại trong hệ thống!", 
                "Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
            txtTen.requestFocus();
            return;
        }
        
        if (bus.isSDTExists(sdt)) {
            JOptionPane.showMessageDialog(this, 
                "Số điện thoại '" + sdt + "' đã được đăng ký bởi nhà cung cấp khác!", 
                "Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return;
        }
        
        NhaCungCapDTO ncc = new NhaCungCapDTO(0, ten, diaChi, sdt, trangThai);
        if (bus.themNCC(ncc)) {
            JOptionPane.showMessageDialog(this, "Thêm nhà cung cấp thành công!");
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ===== PHƯƠNG THỨC CẬP NHẬT =====
    private void capNhat() {
        if (txtMa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) return;
        
        int ma = Integer.parseInt(txtMa.getText());
        String ten = txtTen.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String sdt = txtSDT.getText().trim();
        boolean trangThai = chkHoatDong.isSelected();
        
        if (bus.isTenExists(ten, ma)) {
            JOptionPane.showMessageDialog(this, 
                "Tên nhà cung cấp '" + ten + "' đã tồn tại trong hệ thống!", 
                "Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
            txtTen.requestFocus();
            return;
        }
        
        if (bus.isSDTExists(sdt, ma)) {
            JOptionPane.showMessageDialog(this, 
                "Số điện thoại '" + sdt + "' đã được đăng ký bởi nhà cung cấp khác!", 
                "Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn cập nhật thông tin nhà cung cấp '" + ten + "'?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            NhaCungCapDTO ncc = new NhaCungCapDTO(ma, ten, diaChi, sdt, trangThai);
            if (bus.suaNCC(ncc)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ===== PHƯƠNG THỨC XÓA =====
    private void xoa() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int modelRow = table.convertRowIndexToModel(r);
        int ma = Integer.parseInt(model.getValueAt(modelRow, 0).toString());
        String ten = model.getValueAt(modelRow, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa nhà cung cấp '" + ten + "'?\nLưu ý: Sẽ không thể khôi phục!", 
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (bus.xoaNCC(ma)) {
                JOptionPane.showMessageDialog(this, "Xóa nhà cung cấp thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Không thể xóa nhà cung cấp này vì đang có tham chiếu đến phiếu nhập hàng!", 
                    "Lỗi ràng buộc", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // ===== KIỂM TRA DỮ LIỆU NHẬP =====
    private boolean validateInput() {
        String ten = txtTen.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà cung cấp không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTen.requestFocus();
            return false;
        }
        
        if (ten.length() < 2 || ten.length() > 100) {
            JOptionPane.showMessageDialog(this, "Tên nhà cung cấp phải có độ dài từ 2 đến 100 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTen.requestFocus();
            return false;
        }
        
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }
        
        if (!Pattern.matches("^0\\d{9}$", sdt) && !Pattern.matches("^0\\d{10}$", sdt)) {
            JOptionPane.showMessageDialog(this, 
                "Số điện thoại không hợp lệ!\n"
                + "Yêu cầu: Bắt đầu bằng số 0, gồm 10 hoặc 11 chữ số.\n"
                + "Ví dụ: 0912345678 hoặc 09123456789",                
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }
        
        String diaChi = txtDiaChi.getText().trim();
        if (diaChi.length() > 200) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được vượt quá 200 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void configureByPermission() {
        boolean coQuyen = phanQuyen.isKhoQuanLyNCC();
        btnThem.setVisible(coQuyen);
        btnCapNhat.setVisible(coQuyen);
        btnXoa.setVisible(coQuyen);
    }
    
    private void loadData() {
        model.setRowCount(0);
        List<NhaCungCapDTO> list = bus.getAll();
        for (NhaCungCapDTO n : list) {
            model.addRow(new Object[]{
                    n.getMaNCC(),
                    n.getTenNCC(),
                    n.getDiaChi() != null && !n.getDiaChi().isEmpty() ? n.getDiaChi() : "---",
                    n.getSoDienThoai(),
                    n.isTrangThai() ? "Hoạt động" : "Ngừng hoạt động"
            });
        }
        // Reset sorter sau khi load
        if (sorter != null) {
            sorter.setRowFilter(null);
        }
    }
    
    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        chkHoatDong.setSelected(true);
        txtTen.setBackground(Color.WHITE);
        txtSDT.setBackground(Color.WHITE);
    }
    
    private JButton createClassicButton(String text, String iconPath) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(110, 36));
        btn.setBackground(UIManager.getColor("Button.background"));
        btn.setBorder(BorderFactory.createLineBorder(new Color(160, 180, 200)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ImageIcon icon = loadScaledIcon(iconPath, 18, 18);
        if (icon != null) btn.setIcon(icon);
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(8);
        return btn;
    }
    
    private ImageIcon loadScaledIcon(String path, int w, int h) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) return null;
            Image img = new ImageIcon(url).getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception ex) {
            return null;
        }
    }
}