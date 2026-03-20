package GUI;

import BUS.LuongBUS;
import DTO.Luong;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmInBangLuong extends JDialog {
    private JTable tblLuong;
    private DefaultTableModel model;
    private LuongBUS luongBUS;
    private int maNV;

    public FrmInBangLuong(int maNV) {
        this.maNV = maNV;
        luongBUS = new LuongBUS();
        initComponents();
        loadData();
        setTitle("In bảng lương");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setModal(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("BẢNG LƯƠNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new String[]{"Tháng", "Năm", "Lương cơ bản", "Phụ cấp", "Thưởng", "Khấu trừ", "Thực lĩnh"}, 0
        );
        tblLuong = new JTable(model);
        tblLuong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblLuong.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tblLuong);
        add(scroll, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel();
        JButton btnPrint = new JButton("In");
        btnPrint.addActionListener(this::printTable);
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        pnlButton.add(btnPrint);
        pnlButton.add(btnClose);
        add(pnlButton, BorderLayout.SOUTH);
    }

    private void loadData() {
        ArrayList<Luong> list = luongBUS.layLuongTheoNV(maNV);
        model.setRowCount(0);
        for (Luong l : list) {
            model.addRow(new Object[]{
                l.getThang(), l.getNam(), l.getLuongCoBan(), l.getPhuCap(),
                l.getThuong(), l.getKhauTru(), l.getThucLinh()
            });
        }
    }

    private void printTable(ActionEvent e) {
        try {
            boolean complete = tblLuong.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, "In thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "In bị hủy!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi in: " + ex.getMessage());
        }
    }
}