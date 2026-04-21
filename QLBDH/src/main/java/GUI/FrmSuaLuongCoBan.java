package GUI;

import BUS.LuongCoBanTheoChucVuBUS;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FrmSuaLuongCoBan extends JDialog {
    private JComboBox<String> cboChucVu;
    private JTextField txtLuongCoBan;
    private JTextField txtPhuCap;
    private LuongCoBanTheoChucVuBUS luongBUS;

    public FrmSuaLuongCoBan(Frame parent) {
        super(parent, "Cấu hình lương cơ bản và phụ cấp theo chức vụ", true);
        luongBUS = new LuongCoBanTheoChucVuBUS();
        initComponents();
        loadChucVu();
        setSize(400, 220);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel pnlForm = new JPanel(new GridLayout(3, 2, 10, 10));
        pnlForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        pnlForm.add(new JLabel("Chức vụ:"));
        cboChucVu = new JComboBox<>();
        pnlForm.add(cboChucVu);

        pnlForm.add(new JLabel("Lương cơ bản:"));
        txtLuongCoBan = new JTextField();
        pnlForm.add(txtLuongCoBan);

        pnlForm.add(new JLabel("Phụ cấp chức vụ:"));
        txtPhuCap = new JTextField();
        pnlForm.add(txtPhuCap);

        add(pnlForm, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel(new FlowLayout());
        JButton btnLuu = new JButton("Lưu");
        JButton btnHuy = new JButton("Hủy");
        pnlButton.add(btnLuu);
        pnlButton.add(btnHuy);
        add(pnlButton, BorderLayout.SOUTH);

        btnLuu.addActionListener(e -> luu());
        btnHuy.addActionListener(e -> dispose());

        cboChucVu.addActionListener(e -> hienThiThongTin());
    }

    private void loadChucVu() {
        List<String> dsChucVu = luongBUS.getAllChucVu();
        cboChucVu.removeAllItems();
        for (String cv : dsChucVu) {
            cboChucVu.addItem(cv);
        }
        if (cboChucVu.getItemCount() > 0) {
            cboChucVu.setSelectedIndex(0);
        }
    }

    private void hienThiThongTin() {
        String maCV = (String) cboChucVu.getSelectedItem();
        if (maCV != null) {
            double luong = luongBUS.getLuongCoBan(maCV);
            double phuCap = luongBUS.getPhuCap(maCV);
            txtLuongCoBan.setText(String.format("%.0f", luong));
            txtPhuCap.setText(String.format("%.0f", phuCap));
        }
    }

    private void luu() {
        String maCV = (String) cboChucVu.getSelectedItem();
        if (maCV == null) return;
        try {
            double luongCoBan = Double.parseDouble(txtLuongCoBan.getText().trim());
            double phuCap = Double.parseDouble(txtPhuCap.getText().trim());
            boolean success = luongBUS.update(maCV, luongCoBan, phuCap);
            if (success) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ!");
        }
    }
}