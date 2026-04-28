package GUI;

import BUS.BangLuongBUS;
import DTO.BangLuongDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Calendar;

public class FrmInBangLuongNam extends JFrame {
    private int maNV;
    private int nam;
    private BangLuongBUS bangLuongBUS;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<Integer> cbThang, cbNam;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public FrmInBangLuongNam(int maNV,int thang, int nam) {
        this.maNV = maNV;
        this.nam = nam;
        bangLuongBUS = new BangLuongBUS();
        initComponents();
        cbThang.setSelectedItem(thang);
        cbNam.setSelectedItem(nam);
        loadData();
        setLocationRelativeTo(null);
    }
    

    private void initComponents() {
        setTitle("Bảng lương năm " + nam);
        setSize(800, 500);
        setLayout(new BorderLayout());

        model = new DefaultTableModel(new String[]{"Tháng", "Lương cơ bản", "Phụ cấp", "Thưởng", "Phạt", "Tổng lương"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel pnlBottom = new JPanel();
        JButton btnIn = new JButton("In");
        btnIn.addActionListener(e -> inTable());
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dispose());
        pnlBottom.add(btnIn);
        pnlBottom.add(btnClose);
        add(pnlBottom, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        ArrayList<BangLuongDTO> list = bangLuongBUS.getByMaNVAndYear(maNV, nam);
        for (BangLuongDTO bl : list) {
            model.addRow(new Object[]{
                "Tháng " + bl.getThang(),
                currencyFormat.format(bl.getLuongCoBan()),
                currencyFormat.format(bl.getPhuCap()),
                currencyFormat.format(bl.getThuong()),
                currencyFormat.format(bl.getPhat()),
                currencyFormat.format(bl.getTongLuong())
            });
        }
    }

    private void inTable() {
        try {
            MessageFormat header = new MessageFormat("BẢNG LƯƠNG NĂM " + nam);
            MessageFormat footer = new MessageFormat("Trang {0}");
            table.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi in: " + e.getMessage());
        }
    }
}