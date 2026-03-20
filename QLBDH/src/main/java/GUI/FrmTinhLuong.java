package GUI;

import BUS.LuongBUS;
import DTO.PhanQuyen;
import java.awt.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class FrmTinhLuong extends JDialog {
    private LuongBUS luongBUS;
    private JComboBox<String> cboThang, cboNam;
    private JButton btnTinh, btnDong;
    private PhanQuyen phanQuyen;

    public FrmTinhLuong(PhanQuyen phanQuyen) {
        this.phanQuyen = phanQuyen;
        luongBUS = new LuongBUS();
        initComponents();
        setTitle("Tính lương nhân viên");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setModal(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("TÍNH LƯƠNG THEO THÁNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlCenter = new JPanel(new FlowLayout());
        pnlCenter.add(new JLabel("Tháng:"));
        cboThang = new JComboBox<>(new String[]{"1","2","3","4","5","6","7","8","9","10","11","12"});
        pnlCenter.add(cboThang);

        pnlCenter.add(new JLabel("Năm:"));
        cboNam = new JComboBox<>();
        int currentYear = YearMonth.now().getYear();
        for (int y = currentYear - 2; y <= currentYear + 1; y++) {
            cboNam.addItem(String.valueOf(y));
        }
        cboNam.setSelectedItem(String.valueOf(currentYear));
        pnlCenter.add(cboNam);

        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel();
        btnTinh = new JButton("Tính lương");
        btnTinh.addActionListener(e -> tinhLuong());
        btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());
        pnlButton.add(btnTinh);
        pnlButton.add(btnDong);
        add(pnlButton, BorderLayout.SOUTH);
    }

    private void tinhLuong() {
        int thang = Integer.parseInt(cboThang.getSelectedItem().toString());
        int nam = Integer.parseInt(cboNam.getSelectedItem().toString());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn tính lương tháng " + thang + "/" + nam + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = luongBUS.tinhLuongTheoThang(thang, nam);
            if (success) {
                JOptionPane.showMessageDialog(this, "Tính lương thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Tính lương thất bại (có thể đã tính rồi)!");
            }
        }
    }
}