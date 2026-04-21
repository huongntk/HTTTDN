package GUI;

import BUS.ThongKeBUS;
import DTO.PhanQuyen;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class PnThongKe extends JPanel {
    private JTabbedPane tabThongKe;
    private JPanel pnlKinhDoanh, pnlKho, pnlNhanSu;
    private JTable tblKinhDoanh, tblKho, tblNhanSu;
    private JComboBox<String> cbNam, cbThang, cbQuy;
    private JButton btnThongKe, btnInBaoCao;
    private PhanQuyen phanQuyen;
    private ThongKeBUS thongKeBUS;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    public PnThongKe(PhanQuyen pq) {
        this.phanQuyen = pq;
        this.thongKeBUS = new ThongKeBUS(); // BUS đã được sửa constructor để khởi tạo DAO đúng cách
        initComponents();
        filterTabsByPermission();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- Thanh điều khiển (Filter) ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.setBorder(BorderFactory.createTitledBorder("Bộ lọc thời gian"));

        cbThang = new JComboBox<>(new String[]{"-- Chọn tháng --", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        cbQuy = new JComboBox<>(new String[]{"-- Chọn quý --", "Quý 1", "Quý 2", "Quý 3", "Quý 4"});
        cbNam = new JComboBox<>(new String[]{"2024", "2025", "2026"}); // Có thể cải tiến load năm động từ DB sau

        btnThongKe = new JButton("Thực hiện thống kê");
        // Sử dụng icon an toàn (nếu không có resource thì bỏ qua, tránh NPE)
        try {
            btnThongKe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/thongke.png")));
        } catch (Exception ignored) {}
        
        btnInBaoCao = new JButton("In báo cáo (PDF)");
        try {
            btnInBaoCao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/export.png")));
        } catch (Exception ignored) {}

        pnlTop.add(new JLabel("Tháng:")); pnlTop.add(cbThang);
        pnlTop.add(new JLabel("Quý:")); pnlTop.add(cbQuy);
        pnlTop.add(new JLabel("Năm:")); pnlTop.add(cbNam);
        pnlTop.add(btnThongKe);
        pnlTop.add(btnInBaoCao);

        add(pnlTop, BorderLayout.NORTH);

        // --- Tabs Thống kê ---
        tabThongKe = new JTabbedPane();

        // 1. Tab Kinh Doanh (Lợi nhuận, Doanh thu)
        pnlKinhDoanh = createTabPanel(new String[]{"Thời gian", "Doanh thu", "Giá vốn", "Lợi nhuận"}, tblKinhDoanh = new JTable());
        tabThongKe.addTab("Kinh doanh & Lợi nhuận", pnlKinhDoanh);

        // 2. Tab Kho (Sản phẩm, Tồn kho)
        pnlKho = createTabPanel(new String[]{"Mã SP", "Tên Sản phẩm", "Nhập trong kỳ", "Xuất trong kỳ", "Tồn cuối"}, tblKho = new JTable());
        tabThongKe.addTab("Kho hàng & Sản phẩm", pnlKho);

        // 3. Tab Nhân sự (Lương, Thưởng)
        pnlNhanSu = createTabPanel(new String[]{"Mã NV", "Họ tên", "Chức vụ", "Tổng lương", "Tổng thưởng"}, tblNhanSu = new JTable());
        tabThongKe.addTab("Nhân sự & Chi phí lương", pnlNhanSu);

        add(tabThongKe, BorderLayout.CENTER);

        // Sự kiện
        btnThongKe.addActionListener(e -> thucHienThongKe());
        btnInBaoCao.addActionListener(e -> inBaoCao());
    }

    private JPanel createTabPanel(String[] columnNames, JTable table) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table.setModel(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setRowHeight(25);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    private void filterTabsByPermission() {
        if (phanQuyen == null) {
            return; // Admin có toàn quyền
        }
        // Ẩn các tab không có quyền
        boolean hasKinhDoanh = phanQuyen.isBhThongKeDoanhThu() || phanQuyen.isBhThongKeLoiNhuan() || phanQuyen.isBhThongKeSoLuongXuat();
        if (!hasKinhDoanh) {
            int index = tabThongKe.indexOfTab("Kinh doanh & Lợi nhuận");
            if (index != -1) tabThongKe.removeTabAt(index);
        }
        if (!phanQuyen.isKhoBaoCaoTonKho()) {
            int index = tabThongKe.indexOfTab("Kho hàng & Sản phẩm");
            if (index != -1) tabThongKe.removeTabAt(index);
        }
        if (!phanQuyen.isAdminBaoCaoTongHop()) {
            int index = tabThongKe.indexOfTab("Nhân sự & Chi phí lương");
            if (index != -1) tabThongKe.removeTabAt(index);
        }
    }

    private void thucHienThongKe() {
        // Lấy tham số lọc
        int thang = cbThang.getSelectedIndex(); // 0: không chọn, 1-12: tháng
        int quy = cbQuy.getSelectedIndex();     // 0: không chọn, 1-4: quý
        int nam;
        try {
            nam = Integer.parseInt(cbNam.getSelectedItem().toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Năm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra chỉ chọn một trong hai: tháng hoặc quý
        if (thang > 0 && quy > 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chỉ chọn tháng HOẶC quý, không chọn cả hai!", "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Xác định tab đang chọn để chỉ tải dữ liệu tab đó (tối ưu), hoặc tải tất cả nếu muốn
        int selectedIndex = tabThongKe.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Không có tab nào được chọn!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String tabTitle = tabThongKe.getTitleAt(selectedIndex);

        // Gọi BUS tương ứng với tab đang chọn
        try {
            if (tabTitle.contains("Kinh doanh")) {
                loadKinhDoanh(thang, quy, nam);
            } else if (tabTitle.contains("Kho")) {
                loadKho(thang, quy, nam);
            } else if (tabTitle.contains("Nhân sự")) {
                loadNhanSu(thang, quy, nam);
            }
            // Có thể mở rộng: nếu muốn tải tất cả tab khi nhấn nút, bỏ điều kiện trên và gọi cả 3 load
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu thống kê: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadKinhDoanh(int thang, int quy, int nam) {
        DefaultTableModel model = (DefaultTableModel) tblKinhDoanh.getModel();
        model.setRowCount(0);

        ArrayList<Map<String, Object>> data = thongKeBUS.thongKeLoiNhuan(thang, quy, nam);
        for (Map<String, Object> row : data) {
            model.addRow(new Object[]{
                row.get("ThoiGian"),
                currencyFormat.format(row.get("DoanhThu")),
                currencyFormat.format(row.get("GiaVon")),
                currencyFormat.format(row.get("LoiNhuan"))
            });
        }
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu kinh doanh trong khoảng thời gian đã chọn.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadKho(int thang, int quy, int nam) {
        DefaultTableModel model = (DefaultTableModel) tblKho.getModel();
        model.setRowCount(0);

        ArrayList<Map<String, Object>> data = thongKeBUS.thongKeTonKho(thang, quy, nam);
        if (data == null || data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu tồn kho trong kỳ.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Map<String, Object> row : data) {
            Object maSP = row.getOrDefault("MaSP", "");
            Object tenSP = row.getOrDefault("TenSP", "");
            Object nhap = row.getOrDefault("NhapTrongKy", 0);
            Object xuat = row.getOrDefault("XuatTrongKy", 0);
            Object ton = row.getOrDefault("TonCuoi", 0);

            model.addRow(new Object[]{
                maSP,
                tenSP,
                nhap instanceof Number ? String.format("%,d", ((Number) nhap).intValue()) : nhap,
                xuat instanceof Number ? String.format("%,d", ((Number) xuat).intValue()) : xuat,
                ton instanceof Number ? String.format("%,d", ((Number) ton).intValue()) : ton
            });
        }
    }

    private void loadNhanSu(int thang, int quy, int nam) {
        DefaultTableModel model = (DefaultTableModel) tblNhanSu.getModel();
        model.setRowCount(0);

        ArrayList<Map<String, Object>> data = thongKeBUS.thongKeLuong(thang, quy, nam);
        for (Map<String, Object> row : data) {
            model.addRow(new Object[]{
                row.get("MaNV"),
                row.get("HoTen"),
                row.get("ChucVu"),
                currencyFormat.format(row.get("TongLuong")),
                currencyFormat.format(row.get("TongThuong"))
            });
        }
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu lương nhân viên trong kỳ.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void inBaoCao() {
        try {
            int selectedIndex = tabThongKe.getSelectedIndex();
            if (selectedIndex == -1) {
                JOptionPane.showMessageDialog(this, "Không có tab nào được chọn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JTable currentTable = null;
            String title = tabThongKe.getTitleAt(selectedIndex);
            switch (title) {
                case "Kinh doanh & Lợi nhuận":
                    currentTable = tblKinhDoanh;
                    break;
                case "Kho hàng & Sản phẩm":
                    currentTable = tblKho;
                    break;
                case "Nhân sự & Chi phí lương":
                    currentTable = tblNhanSu;
                    break;
            }
            if (currentTable == null || currentTable.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu để in!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            MessageFormat header = new MessageFormat("BÁO CÁO: " + title.toUpperCase());
            MessageFormat footer = new MessageFormat("Trang {0,number,integer}");
            currentTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi in: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}