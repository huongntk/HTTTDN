package GUI;

import BUS.LichLamViecBUS;
import DTO.LichLamViecDTO;
import DTO.TaiKhoan;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PnCaLam extends JPanel {

    private LichLamViecBUS lichLamViecBUS;
    private TaiKhoan taiKhoan;

    private JComboBox<Integer> cboThang;
    private JComboBox<Integer> cboNam;

    private JTable tblCaHienTai;
    private JTable tblCaHoanThanh;

    private DefaultTableModel modelCaHienTai;
    private DefaultTableModel modelCaHoanThanh;

    private JLabel lblTongCaHienTai;
    private JLabel lblTongCaHoanThanh;
    private JLabel lblTongGioHoanThanh;

    public PnCaLam(TaiKhoan tk) {
        this.taiKhoan = tk;
        this.lichLamViecBUS = new LichLamViecBUS();

        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        JPanel pnTopContainer = new JPanel(new BorderLayout(10, 10));
        pnTopContainer.setOpaque(false);

        JLabel lblTitle = new JLabel("CA LÀM CỦA TÔI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setForeground(new Color(0, 102, 102));
        pnTopContainer.add(lblTitle, BorderLayout.NORTH);

        JPanel pnTop = new JPanel(new BorderLayout(10, 10));
        pnTop.setOpaque(false);

        JPanel pnFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pnFilter.setBackground(new Color(240, 248, 248));
        pnFilter.setBorder(BorderFactory.createTitledBorder("Bộ lọc"));

        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            cboThang.addItem(i);
        }
        cboThang.setSelectedItem(Calendar.getInstance().get(Calendar.MONTH) + 1);

        cboNam = new JComboBox<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear - 2; i <= currentYear + 1; i++) {
            cboNam.addItem(i);
        }
        cboNam.setSelectedItem(currentYear);

        JButton btnLoc = new JButton("Lọc");
        btnLoc.setBackground(new Color(0, 153, 153));
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setFocusPainted(false);
        btnLoc.setBorderPainted(false);
        btnLoc.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLoc.addActionListener(e -> loadData());

        pnFilter.add(new JLabel("Tháng:"));
        pnFilter.add(cboThang);
        pnFilter.add(new JLabel("Năm:"));
        pnFilter.add(cboNam);
        pnFilter.add(btnLoc);

        JPanel pnSummary = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 8));
        pnSummary.setBackground(new Color(240, 248, 248));
        pnSummary.setBorder(BorderFactory.createTitledBorder("Tổng quan"));

        lblTongCaHienTai = new JLabel("Ca hôm nay / sắp tới: 0");
        lblTongCaHienTai.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTongCaHienTai.setForeground(new Color(0, 102, 102));

        lblTongCaHoanThanh = new JLabel("Ca đã hoàn thành: 0");
        lblTongCaHoanThanh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTongCaHoanThanh.setForeground(new Color(0, 102, 102));

        lblTongGioHoanThanh = new JLabel("Tổng giờ hoàn thành: 0.0h");
        lblTongGioHoanThanh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTongGioHoanThanh.setForeground(new Color(0, 102, 102));

        pnSummary.add(lblTongCaHienTai);
        pnSummary.add(lblTongCaHoanThanh);
        pnSummary.add(lblTongGioHoanThanh);

        pnTop.add(pnFilter, BorderLayout.WEST);
        pnTop.add(pnSummary, BorderLayout.EAST);

        pnTopContainer.add(pnTop, BorderLayout.CENTER);
        add(pnTopContainer, BorderLayout.NORTH);

        String[] columns = {
            "Ngày làm",
            "Ca làm",
            "Giờ bắt đầu",
            "Giờ kết thúc",
            "Số giờ",
            "Trạng thái",
            "Ghi chú"
        };

        modelCaHienTai = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelCaHoanThanh = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblCaHienTai = createStyledTable(modelCaHienTai);
        tblCaHoanThanh = createStyledTable(modelCaHoanThanh);

        JScrollPane spCaHienTai = new JScrollPane(tblCaHienTai);
        spCaHienTai.setBorder(BorderFactory.createTitledBorder("Ca hôm nay và ca sắp tới"));

        JScrollPane spCaHoanThanh = new JScrollPane(tblCaHoanThanh);
        spCaHoanThanh.setBorder(BorderFactory.createTitledBorder("Ca đã hoàn thành"));

        JPanel pnTabCaHienTai = new JPanel(new BorderLayout());
        pnTabCaHienTai.add(spCaHienTai, BorderLayout.CENTER);

        JPanel pnTabCaHoanThanh = new JPanel(new BorderLayout());
        pnTabCaHoanThanh.add(spCaHoanThanh, BorderLayout.CENTER);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.addTab("Ca làm của tôi", pnTabCaHienTai);
        tabs.addTab("Ca đã hoàn thành", pnTabCaHoanThanh);

        add(tabs, BorderLayout.CENTER);
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(0, 153, 153));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(180, 230, 230));
        table.setGridColor(new Color(200, 230, 230));
        table.setFillsViewportHeight(true);

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(130);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(110);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        table.getColumnModel().getColumn(6).setPreferredWidth(180);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column
            ) {
                Component c = super.getTableCellRendererComponent(
                        table,
                        value,
                        isSelected,
                        hasFocus,
                        row,
                        column
                );

                setHorizontalAlignment(SwingConstants.CENTER);

                if (!isSelected) {
                    String trangThai = "";
                    Object statusObj = table.getModel().getValueAt(row, 5);

                    if (statusObj != null) {
                        trangThai = statusObj.toString().toLowerCase();
                    }

                    if (trangThai.contains("đủ công")) {
                        c.setBackground(new Color(226, 255, 226));
                    } else if (trangThai.contains("đi trễ") || trangThai.contains("về sớm")) {
                        c.setBackground(new Color(255, 248, 220));
                    } else if (trangThai.contains("nghỉ")) {
                        c.setBackground(new Color(255, 235, 235));
                    } else if (trangThai.contains("hôm nay")) {
                        c.setBackground(new Color(235, 255, 235));
                    } else if (trangThai.contains("sắp tới")) {
                        c.setBackground(new Color(235, 245, 255));
                    } else if (trangThai.contains("chưa chấm công")) {
                        c.setBackground(new Color(255, 245, 230));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }

                return c;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        return table;
    }

    private void loadData() {
        if (cboThang.getSelectedItem() == null || cboNam.getSelectedItem() == null) {
            return;
        }

        int thang = (int) cboThang.getSelectedItem();
        int nam = (int) cboNam.getSelectedItem();
        int maNV = taiKhoan.getMaNV();

        ArrayList<LichLamViecDTO> list = lichLamViecBUS.layLichTheoNhanVienThang(maNV, thang, nam);

        modelCaHienTai.setRowCount(0);
        modelCaHoanThanh.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        java.sql.Date today = getTodaySqlDate();

        int tongCaHienTai = 0;
        int tongCaHoanThanh = 0;
        double tongGioHoanThanh = 0;

        for (LichLamViecDTO lich : list) {
            if (lich.getNgayLam() == null) {
                continue;
            }

            String trangThaiChamCong = lich.getTrangThaiChamCong() != null
                    ? lich.getTrangThaiChamCong().trim()
                    : "";

            boolean daHoanThanh = isTrangThaiHoanThanh(trangThaiChamCong);

            String trangThaiHienThi = daHoanThanh
                    ? trangThaiChamCong
                    : getTrangThaiLichChuaHoanThanh(lich.getNgayLam(), today);

            String soGio = tinhSoGio(lich, trangThaiHienThi);

            Object[] row = new Object[]{
                sdf.format(lich.getNgayLam()),
                lich.getTenCa() != null ? lich.getTenCa() : "",
                lich.getGioBatDau() != null ? lich.getGioBatDau().toString() : "",
                lich.getGioKetThuc() != null ? lich.getGioKetThuc().toString() : "",
                soGio,
                trangThaiHienThi,
                lich.getGhiChu() != null ? lich.getGhiChu() : ""
            };

            boolean laHomNayHoacSapToi = !lich.getNgayLam().before(today);

            if (laHomNayHoacSapToi && !daHoanThanh) {
                modelCaHienTai.addRow(row);
                tongCaHienTai++;
            } else if (daHoanThanh && !lich.getNgayLam().after(today)) {
                modelCaHoanThanh.addRow(row);
                tongCaHoanThanh++;

                if (!trangThaiChamCong.toLowerCase().contains("nghỉ")
                        && lich.getGioBatDau() != null
                        && lich.getGioKetThuc() != null) {
                    tongGioHoanThanh += tinhSoGioDouble(lich);
                }
            }
        }

        lblTongCaHienTai.setText("Ca hôm nay / sắp tới: " + tongCaHienTai);
        lblTongCaHoanThanh.setText("Ca đã hoàn thành: " + tongCaHoanThanh);
        lblTongGioHoanThanh.setText(String.format("Tổng giờ hoàn thành: %.1fh", tongGioHoanThanh));
    }

    private boolean isTrangThaiHoanThanh(String trangThai) {
        if (trangThai == null || trangThai.trim().isEmpty()) {
            return false;
        }

        String s = trangThai.trim().toLowerCase();

        return s.equals("đủ công")
                || s.equals("đi trễ")
                || s.equals("về sớm")
                || s.equals("nghỉ có phép")
                || s.equals("nghỉ không phép");
    }

    private String getTrangThaiLichChuaHoanThanh(java.sql.Date ngayLam, java.sql.Date today) {
        if (ngayLam.equals(today)) {
            return "Hôm nay - chưa chấm công";
        }

        if (ngayLam.after(today)) {
            return "Sắp tới";
        }

        return "Chưa chấm công";
    }

    private String tinhSoGio(LichLamViecDTO lich, String trangThai) {
        if (trangThai == null) {
            trangThai = "";
        }

        if (trangThai.toLowerCase().contains("nghỉ")) {
            return "-";
        }

        double gio = tinhSoGioDouble(lich);

        if (gio <= 0) {
            return "";
        }

        return String.format("%.1fh", gio);
    }

    private double tinhSoGioDouble(LichLamViecDTO lich) {
        if (lich.getGioBatDau() == null || lich.getGioKetThuc() == null) {
            return 0;
        }

        try {
            long batDauMs = lich.getGioBatDau().getTime();
            long ketThucMs = lich.getGioKetThuc().getTime();

            return (ketThucMs - batDauMs) / 3600000.0;
        } catch (Exception e) {
            return 0;
        }
    }

    private java.sql.Date getTodaySqlDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return new java.sql.Date(cal.getTimeInMillis());
    }
}