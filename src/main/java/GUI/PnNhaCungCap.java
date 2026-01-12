package GUI;

import BUS.NhaCungCapBUS;
import DTO.NhaCungCapDTO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.List;

public class PnNhaCungCap extends JPanel {

    private JTextField txtMa, txtTen, txtDiaChi, txtSDT;
    private JCheckBox chkHoatDong;
    private JTable table;
    private DefaultTableModel model;
    private NhaCungCapBUS bus = new NhaCungCapBUS();

    public PnNhaCungCap() {
        setPreferredSize(new Dimension(1100, 650)); // Thiết lập kích thước ưa thích
        setLayout(new BorderLayout(8, 8)); // Vẫn giữ layout
        
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
        JLabel lblTen = new JLabel("Tên NCC:");
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        JLabel lblSDT = new JLabel("Số điện thoại:");
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblMa.setFont(lblFont); lblTen.setFont(lblFont);
        lblDiaChi.setFont(lblFont); lblSDT.setFont(lblFont); lblTrangThai.setFont(lblFont);

        txtMa = new JTextField(); txtMa.setEditable(false);
        txtTen = new JTextField();
        txtDiaChi = new JTextField();
        txtSDT = new JTextField();
        chkHoatDong = new JCheckBox("Hoạt động");
        chkHoatDong.setBackground(Color.WHITE);
        chkHoatDong.setFont(lblFont);

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

        // Nút thêm
        row++;
        g.gridx = 0; g.gridy = row; g.gridwidth = 2;
        JPanel pnlAddHolder = new JPanel();
        pnlAddHolder.setOpaque(false);
        JButton btnThem = createClassicButton("Thêm", "/icon/them.png");
        pnlAddHolder.add(btnThem);
        leftPanel.add(pnlAddHolder, g);

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
        JButton btnSua = createClassicButton("Sửa", "/icon/sua.png");
        JButton btnXoa = createClassicButton("Xóa", "/icon/xoa.png");
        JButton btnLamMoi = createClassicButton("Làm mới", "/icon/undo.png");
        pnlBottom.add(btnSua);
        pnlBottom.add(btnXoa);
        pnlBottom.add(btnLamMoi);
        rightPanel.add(pnlBottom, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        split.setDividerLocation(380);
        add(split, BorderLayout.CENTER);

        // ====== SỰ KIỆN ======
        loadData();

        // Khi click chọn dòng → hiển thị sang form
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

        // Thêm mới
        btnThem.addActionListener(e -> {
            String ten = txtTen.getText().trim();
            String diaChi = txtDiaChi.getText().trim();
            String sdt = txtSDT.getText().trim();
            boolean tt = chkHoatDong.isSelected();

            if (ten.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đủ Tên NCC và Số điện thoại");
                return;
            }

            NhaCungCapDTO ncc = new NhaCungCapDTO(0, ten, diaChi, sdt, tt);
            if (bus.themNCC(ncc)) {
                JOptionPane.showMessageDialog(this, "Thêm thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi khi thêm NCC!");
            }
        });

        // Xóa
        btnXoa.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp cần xóa!");
                return;
            }
            int ma = Integer.parseInt(model.getValueAt(r, 0).toString());
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa nhà cung cấp này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (bus.xoaNCC(ma)) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    loadData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể xóa (có thể đang được tham chiếu).");
                }
            }
        });

        // Sửa
        btnSua.addActionListener(e -> {
            if (txtMa.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Chọn một nhà cung cấp để sửa!");
                return;
            }
            try {
                int ma = Integer.parseInt(txtMa.getText());
                String ten = txtTen.getText().trim();
                String diaChi = txtDiaChi.getText().trim();
                String sdt = txtSDT.getText().trim();
                boolean tt = chkHoatDong.isSelected();

                NhaCungCapDTO ncc = new NhaCungCapDTO(ma, ten, diaChi, sdt, tt);
                if (bus.suaNCC(ncc)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể cập nhật!");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Làm mới
        btnLamMoi.addActionListener(e -> {
            clearForm();
            loadData();
        });
    }

    private void loadData() {
        model.setRowCount(0);
        List<NhaCungCapDTO> list = bus.getAll();
        for (NhaCungCapDTO n : list) {
            model.addRow(new Object[]{
                    n.getMaNCC(),
                    n.getTenNCC(),
                    n.getDiaChi(),
                    n.getSoDienThoai(),
                    n.isTrangThai() ? "Hoạt động" : "Ngừng"
            });
        }
    }

    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        chkHoatDong.setSelected(false);
    }

    // ======== TẠO NÚT PHONG CÁCH CỔ ĐIỂN ========
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PnNhaCungCap().setVisible(true));
    }
}
