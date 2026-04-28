package GUI;

import BUS.BangLuongBUS;
import DTO.BangLuongDTO;
import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;

public class FrmInBangLuong extends JFrame {
    private int maNV;
    private BangLuongBUS bangLuongBUS;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<Integer> cbThang, cbNam;

    public FrmInBangLuong(int maNV, int thang, int nam) {
        this.maNV = maNV;
        this.bangLuongBUS = new BangLuongBUS();
        initComponents();
        cbThang.setSelectedItem(thang);
        cbNam.setSelectedItem(nam);
        loadData();
        setLocationRelativeTo(null);
    }
    
    public FrmInBangLuong(int maNV) {
        this(
            maNV,
            Calendar.getInstance().get(Calendar.MONTH) + 1,
            Calendar.getInstance().get(Calendar.YEAR)
        );
    }

    private void initComponents() {
        setTitle("In bảng lương");
        setSize(700, 400);
        setLayout(new BorderLayout());

        JPanel pnlFilter = new JPanel();
        pnlFilter.add(new JLabel("Tháng:"));
        cbThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cbThang.addItem(i);
        pnlFilter.add(cbThang);
        pnlFilter.add(new JLabel("Năm:"));
        cbNam = new JComboBox<>();
        for (int i = 2023; i <= 2030; i++) cbNam.addItem(i);
        pnlFilter.add(cbNam);
        JButton btnIn = new JButton("In");
        btnIn.addActionListener(e -> inBangLuong());
        pnlFilter.add(btnIn);
        add(pnlFilter, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"Tháng", "Năm", "Lương cơ bản", "Phụ cấp", "Thưởng", "Phạt", "Tổng lương"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void loadData() {
        model.setRowCount(0);
        int thang = (int) cbThang.getSelectedItem();
        int nam = (int) cbNam.getSelectedItem();
        BangLuongDTO bl = bangLuongBUS.getByMaNVAndMonth(maNV, thang, nam);
        if (bl != null) {
            model.addRow(new Object[]{bl.getThang(), bl.getNam(), bl.getLuongCoBan(), bl.getPhuCap(), bl.getThuong(), bl.getPhat(), bl.getTongLuong()});
        }
    }

    private void inBangLuong() {
        if (table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu để in.");
            return;
        }
        try {
            MessageFormat header = new MessageFormat("BẢNG LƯƠNG NHÂN VIÊN - THÁNG " + cbThang.getSelectedItem() + "/" + cbNam.getSelectedItem());
            MessageFormat footer = new MessageFormat("Trang {0}");
            table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi in: " + e.getMessage());
        }
    }
}