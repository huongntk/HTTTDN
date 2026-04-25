
package GUI;

import BUS.NhaCungCapBUS;
import DTO.NhaCungCapDTO;
import DTO.PhanQuyen;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
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
    private NhaCungCapBUS bus = new NhaCungCapBUS();
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    private JButton btnCapNhat; // Thêm nút Cập nhật riêng
    
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
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        
        lblMa.setFont(lblFont); 
        lblTen.setFont(lblFont);
        lblTen.setForeground(Color.RED);
        lblDiaChi.setFont(lblFont); 
        lblSDT.setFont(lblFont);
        lblSDT.setForeground(Color.RED);
        lblTrangThai.setFont(lblFont);
        
        txtMa = new JTextField(); 
        txtMa.setEditable(false);
        txtMa.setBackground(new Color(240, 240, 240));
        
        txtTen = new JTextField();
        txtDiaChi = new JTextField();
        txtSDT = new JTextField();
        chkHoatDong = new JCheckBox("Hoạt động");
        chkHoatDong.setBackground(Color.WHITE);
        chkHoatDong.setFont(lblFont);
        chkHoatDong.setSelected(true); // Mặc định là hoạt động
        
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
        g.gridx = 0; g.gridy = row; leftPanel.add(lblTrangThai, g);
        g.gridx = 1; leftPanel.add(chkHoatDong, g);
        
        // Panel chứa các nút
        row++;
        g.gridx = 0; g.gridy = row; g.gridwidth = 2;
        JPanel pnlButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
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
        
        String[] cols = {"Mã NCC", "Tên NCC", "Địa chỉ", "Số điện thoại", "Trạng thái"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);
        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        rightPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        
        // ===== Bottom Buttons =====
        JPanel pnlBottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        btnXoa = createClassicButton("Xóa", "/icon/xoa.png");
        pnlBottom.add(btnXoa);
        rightPanel.add(pnlBottom, BorderLayout.SOUTH);
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setDividerLocation(400);
        add(split, BorderLayout.CENTER);
        
        // ====== SỰ KIỆN ======
        loadData();
        
        // Click chọn dòng → hiển thị lên form để sửa/xóa
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int r = table.getSelectedRow();
                if (r != -1) {
                    txtMa.setText(model.getValueAt(r, 0).toString());
                    txtTen.setText(model.getValueAt(r, 1).toString());
                    txtDiaChi.setText(model.getValueAt(r, 2).toString());
                    txtSDT.setText(model.getValueAt(r, 3).toString());
                    chkHoatDong.setSelected(model.getValueAt(r, 4).toString().equals("Hoạt động"));
                }
            }
        });
        
        // Thêm mới (không cần chọn dòng)
        btnThem.addActionListener(e -> themMoi());
        
        // Cập nhật (phải chọn dòng)
        btnCapNhat.addActionListener(e -> capNhat());
        
        // Xóa (phải chọn dòng)
        btnXoa.addActionListener(e -> xoa());
        
        // Làm mới form
        btnLamMoi.addActionListener(e -> {
            clearForm();
            table.clearSelection();
        });
        
        configureByPermission();
        
        // Thêm ràng buộc cho SDT (chỉ cho phép nhập số)
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
    
    // ===== PHƯƠNG THỨC THÊM MỚI =====
    private void themMoi() {
        // Kiểm tra dữ liệu nhập
        if (!validateInput()) return;
        
        String ten = txtTen.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String sdt = txtSDT.getText().trim();
        boolean trangThai = chkHoatDong.isSelected();
        
        // Kiểm tra trùng tên (nếu cần)
        if (bus.isTenExists(ten)) {
            JOptionPane.showMessageDialog(this, 
                "Tên nhà cung cấp '" + ten + "' đã tồn tại trong hệ thống!", 
                "Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
            txtTen.requestFocus();
            return;
        }
        
        // Kiểm tra trùng số điện thoại
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
        
        // Kiểm tra trùng tên (trừ chính nó)
        if (bus.isTenExists(ten, ma)) {
            JOptionPane.showMessageDialog(this, 
                "Tên nhà cung cấp '" + ten + "' đã tồn tại trong hệ thống!", 
                "Lỗi trùng lặp", JOptionPane.ERROR_MESSAGE);
            txtTen.requestFocus();
            return;
        }
        
        // Kiểm tra trùng số điện thoại (trừ chính nó)
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
        
        int ma = Integer.parseInt(model.getValueAt(r, 0).toString());
        String ten = model.getValueAt(r, 1).toString();
        
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
        // Kiểm tra tên không được trống
        String ten = txtTen.getText().trim();
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà cung cấp không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTen.requestFocus();
            return false;
        }
        
        // Kiểm tra độ dài tên (2-100 ký tự)
        if (ten.length() < 2 || ten.length() > 100) {
            JOptionPane.showMessageDialog(this, "Tên nhà cung cấp phải có độ dài từ 2 đến 100 ký tự!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtTen.requestFocus();
            return false;
        }
        
        // Kiểm tra số điện thoại
        String sdt = txtSDT.getText().trim();
        if (sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }
        
        // Kiểm tra định dạng số điện thoại (10-11 số, bắt đầu bằng 0)
        if (!Pattern.matches("^0[0-9]}$", sdt)) {
            JOptionPane.showMessageDialog(this, 
                "Số điện thoại không hợp lệ!\n"
                + "Yêu cầu: Bắt đầu bằng số 0, gồm 10 chữ số.\n",                
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }
        
        // Kiểm tra địa chỉ (nếu có thì không được quá dài)
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