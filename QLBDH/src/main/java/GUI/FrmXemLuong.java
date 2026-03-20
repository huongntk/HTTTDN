package GUI;

import BUS.LuongBUS;
import DTO.Luong;
import DTO.PhanQuyen;
import UTIL.Auth;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmXemLuong extends JDialog {
    private JTable tblLuong;
    private DefaultTableModel model;
    private LuongBUS luongBUS;
    private int maNV;
    private PhanQuyen phanQuyen;
    public FrmXemLuong(int maNV, PhanQuyen phanQuyen) {
        this.phanQuyen = phanQuyen;
        this.maNV = maNV;
        luongBUS = new LuongBUS();
        initComponents();
        loadData();
        setTitle("Xem lương nhân viên");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setModal(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("BẢNG LƯƠNG CÁ NHÂN", SwingConstants.CENTER);
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
        JButton btnIn = new JButton("In bảng lương");
        btnIn.addActionListener(e -> inBangLuong());
        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());
        pnlButton.add(btnIn);
        pnlButton.add(btnDong);
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

    private void inBangLuong() {
        // Gọi chức năng in (có thể mở FrmInBangLuong với tham số)
        new FrmInBangLuong(maNV).setVisible(true);
    }
}