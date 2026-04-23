package GUI;

import BUS.BangLuongBUS;
import BUS.NhanVienBUS;
import DTO.BangLuongDTO;
import DTO.NhanVienDTO;
import DTO.PhanQuyen;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FrmTinhLuong extends JFrame {
    private PhanQuyen phanQuyen;
    private BangLuongBUS bangLuongBUS;
    private NhanVienBUS nhanVienBUS;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<Integer> cbThang, cbNam;
    private JButton btnTinh, btnLuu, btnClose;
    private ArrayList<NhanVienDTO> dsNhanVien;

    public FrmTinhLuong(PhanQuyen phanQuyen) {
        this.phanQuyen = phanQuyen;
        bangLuongBUS = new BangLuongBUS();
        nhanVienBUS = new NhanVienBUS();
        initComponents();
        loadDanhSachNhanVien();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Tính lương nhân viên");
        setSize(1000, 600);
        setLayout(new BorderLayout());

        JPanel pnlTop = new JPanel();
        pnlTop.add(new JLabel("Tháng:"));
        cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);
        pnlTop.add(cbThang);
        pnlTop.add(new JLabel("Năm:"));
        cbNam = new JComboBox<>();
        for (int i = 2023; i <= 2030; i++) cbNam.addItem(i);
        pnlTop.add(cbNam);
        btnTinh = new JButton("Tính lương");
        btnTinh.addActionListener(e -> tinhLuongChoThang());
        pnlTop.add(btnTinh);
        btnLuu = new JButton("Lưu tất cả");
        btnLuu.addActionListener(e -> luuTatCa());
        pnlTop.add(btnLuu);
        btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        pnlTop.add(btnClose);
        add(pnlTop, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Mã NV", "Họ tên", "Chức vụ", "Lương cơ bản", "Phụ cấp", "Thưởng", "Phạt", "Tổng lương"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadDanhSachNhanVien() {
        dsNhanVien = nhanVienBUS.layDanhSachNhanVien();
    }

    private void tinhLuongChoThang() {
        int thang = (int) cbThang.getSelectedItem();
        int nam = (int) cbNam.getSelectedItem();
        model.setRowCount(0);
        for (NhanVienDTO nv : dsNhanVien) {
            // Tính lương tạm thời với thưởng/phạt = 0
            BangLuongDTO bl = bangLuongBUS.tinhLuongChoNhanVien(nv.getMaNV(), thang, nam, 0, 0);
            if (bl != null) {
                model.addRow(new Object[]{
                    nv.getMaNV(), nv.getHo() + " " + nv.getTen(), nv.getMaCV(),
                    bl.getLuongCoBan(), bl.getPhuCap(), 0, 0, bl.getTongLuong()
                });
            }
        }
        // Cho phép sửa cột thưởng (index 5) và phạt (index 6)
        table.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(new JTextField()));
        table.getColumnModel().getColumn(6).setCellEditor(new DefaultCellEditor(new JTextField()));
    }

    private void luuTatCa() {
        int thang = (int) cbThang.getSelectedItem();
        int nam = (int) cbNam.getSelectedItem();
        for (int i = 0; i < model.getRowCount(); i++) {
            int maNV = (int) model.getValueAt(i, 0);
            double thuong = 0, phat = 0;
            try {
                thuong = Double.parseDouble(model.getValueAt(i, 5).toString());
                phat = Double.parseDouble(model.getValueAt(i, 6).toString());
            } catch (NumberFormatException e) {}
            // Tính lại lương với thưởng/phạt đã nhập
            BangLuongDTO bl = bangLuongBUS.tinhLuongChoNhanVien(maNV, thang, nam, thuong, phat);
            if (bl != null) {
                bangLuongBUS.luuBangLuong(bl);
            }
        }
        JOptionPane.showMessageDialog(this, "Đã lưu bảng lương tháng " + thang + "/" + nam);
    }
}