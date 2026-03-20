package GUI;

import BUS.DonNghiBUS;
import DTO.DonNghi;
import DTO.PhanQuyen;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FrmDuyetNghi extends JDialog {
    private JTable tblDon;
    private DefaultTableModel model;
    private DonNghiBUS donNghiBUS;
    private PhanQuyen phanQuyen;

    public FrmDuyetNghi(PhanQuyen phanQuyen) {
        this.phanQuyen = phanQuyen;
        donNghiBUS = new DonNghiBUS();
        initComponents();
        loadData();
        setTitle("Duyệt đơn nghỉ phép");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setModal(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("DANH SÁCH ĐƠN XIN NGHỈ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(lblTitle, BorderLayout.NORTH);

        model = new DefaultTableModel(
            new String[]{"Mã đơn", "Mã NV", "Họ tên", "Ngày bắt đầu", "Ngày kết thúc", "Lý do", "Trạng thái"}, 0
        );
        tblDon = new JTable(model);
        tblDon.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblDon.setRowHeight(25);
        JScrollPane scroll = new JScrollPane(tblDon);
        add(scroll, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel();
        JButton btnDuyet = new JButton("Duyệt");
        btnDuyet.addActionListener(e -> duyetDon());
        JButton btnTuChoi = new JButton("Từ chối");
        btnTuChoi.addActionListener(e -> tuChoiDon());
        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());
        pnlButton.add(btnDuyet);
        pnlButton.add(btnTuChoi);
        pnlButton.add(btnDong);
        add(pnlButton, BorderLayout.SOUTH);
    }

    private void loadData() {
        ArrayList<DonNghi> list = donNghiBUS.layDonChoDuyet();
        model.setRowCount(0);
        for (DonNghi d : list) {
            model.addRow(new Object[]{
                d.getMaDon(), d.getMaNV(), d.getHoTen(), d.getNgayBatDau(),
                d.getNgayKetThuc(), d.getLyDo(), d.getTrangThai()
            });
        }
    }

    private void duyetDon() {
        int row = tblDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đơn cần duyệt!");
            return;
        }
        int maDon = (int) model.getValueAt(row, 0);
        boolean success = donNghiBUS.duyetDon(maDon, "Đã duyệt");
        if (success) {
            JOptionPane.showMessageDialog(this, "Duyệt thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Duyệt thất bại!");
        }
    }

    private void tuChoiDon() {
        int row = tblDon.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Chọn đơn cần từ chối!");
            return;
        }
        int maDon = (int) model.getValueAt(row, 0);
        boolean success = donNghiBUS.duyetDon(maDon, "Từ chối");
        if (success) {
            JOptionPane.showMessageDialog(this, "Từ chối thành công!");
            loadData();
        } else {
            JOptionPane.showMessageDialog(this, "Từ chối thất bại!");
        }
    }
}