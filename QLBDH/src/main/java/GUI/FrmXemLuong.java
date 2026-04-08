package GUI;

import BUS.BangLuongBUS;
import DTO.BangLuongDTO;
import DTO.PhanQuyen;
import UTIL.Auth;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class FrmXemLuong extends JFrame {
    private int maNV;
    private PhanQuyen phanQuyen;
    private BangLuongBUS bangLuongBUS;
    private JTable tblLuong;
    private DefaultTableModel model;
    private JComboBox<Integer> cbThang, cbNam;
    private JButton btnXem, btnIn, btnClose;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public FrmXemLuong(int maNV, PhanQuyen phanQuyen) {
        this.maNV = maNV;
        this.phanQuyen = phanQuyen;
        this.bangLuongBUS = new BangLuongBUS();
        initComponents();
        loadData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Xem bảng lương");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel lọc
        JPanel pnlFilter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFilter.add(new JLabel("Tháng:"));
        cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);
        pnlFilter.add(cbThang);

        pnlFilter.add(new JLabel("Năm:"));
        cbNam = new JComboBox<>();
        for (int i = 2023; i <= 2030; i++) cbNam.addItem(i);
        pnlFilter.add(cbNam);

        btnXem = new JButton("Xem");
        btnXem.addActionListener(e -> loadData());
        pnlFilter.add(btnXem);

        btnIn = new JButton("In bảng lương");
        btnIn.addActionListener(e -> inBangLuong());
        if (!phanQuyen.isNsInBangLuong()) btnIn.setEnabled(false);
        pnlFilter.add(btnIn);

        btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        pnlFilter.add(btnClose);

        add(pnlFilter, BorderLayout.NORTH);

        // Bảng hiển thị lương
        model = new DefaultTableModel(new String[]{"Tháng", "Năm", "Lương cơ bản", "Phụ cấp", "Thưởng", "Phạt", "Tổng lương"}, 0);
        tblLuong = new JTable(model);
        tblLuong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblLuong.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        add(new JScrollPane(tblLuong), BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);
        int thang = (int) cbThang.getSelectedItem();
        int nam = (int) cbNam.getSelectedItem();
        // Lấy bảng lương của nhân viên theo tháng (nếu có)
        BangLuongDTO bl = bangLuongBUS.getByMaNVAndMonth(maNV, thang, nam);
        if (bl != null) {
            model.addRow(new Object[]{
                bl.getThang(), bl.getNam(),
                currencyFormat.format(bl.getLuongCoBan()),
                currencyFormat.format(bl.getPhuCap()),
                currencyFormat.format(bl.getThuong()),
                currencyFormat.format(bl.getPhat()),
                currencyFormat.format(bl.getTongLuong())
            });
        } else {
            JOptionPane.showMessageDialog(this, "Chưa có bảng lương cho tháng " + thang + "/" + nam);
        }
    }

    private void inBangLuong() {
        // In trực tiếp bảng lương hoặc gọi FrmInBangLuong
        new FrmInBangLuong(maNV).setVisible(true);
    }
}