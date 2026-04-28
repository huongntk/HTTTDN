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
        cbNam = new JComboBox<>(new String[]{"2025", "2026", "2027", "2028"}); // Có thể cải tiến load năm động từ DB sau

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
        pnlKinhDoanh = createTabPanel(new String[]{"Mã SP", "Tên sản phẩm", "Số lượng bán", "Giá bán", "Doanh thu", "Lợi nhuận"}, tblKinhDoanh = new JTable());
        tabThongKe.addTab("Kinh doanh & Lợi nhuận", pnlKinhDoanh);
                
        
        // 2. Tab Kho (Sản phẩm, Tồn kho)
        pnlKho = createTabPanel(new String[]{"Mã SP", "Tên sản phẩm", "Thương hiệu", "Số lượng nhập", "Số lượng tồn"}, tblKho = new JTable());
        tabThongKe.addTab("Kho hàng & Sản phẩm", pnlKho);

        // 3. Tab Nhân sự (Lương, Thưởng)
//        pnlNhanSu = createTabPanel(new String[]{"Mã NV", "Họ tên", "Chức vụ", "Tổng lương", "Tổng thưởng"}, tblNhanSu = new JTable());
        pnlNhanSu = createTabPanel(new String[]{"Mã NV", "Họ tên", "Chức vụ", "Lương cơ bản", "Thưởng", "Tổng nhận", "Số ngày nghỉ", "Trạng thái"}, tblNhanSu = new JTable());
        tabThongKe.addTab("Nhân sự & Chi phí lương", pnlNhanSu);

         // Thêm tab Biến động nhân sự
        pnlBienDongNS = createTabPanel(new String[]{"Mã NV", "Họ tên", "Thay đổi", "Từ chức vụ", "Đến chức vụ", "Lương cũ", "Lương mới", "Ngày áp dụng"}, tblBienDongNS = new JTable());
        tabThongKe.addTab("Biến động nhân sự", pnlBienDongNS);
    
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

    // Thêm phương thức load biến động nhân sự
private void loadBienDongNhanSu(int thang, int quy, int nam) {
    DefaultTableModel model = (DefaultTableModel) tblBienDongNS.getModel();
    model.setRowCount(0);
    
    ArrayList<Map<String, Object>> data = thongKeBUS.thongKeBienDongNhanSu(thang, nam);
    
    for (Map<String, Object> row : data) {
        model.addRow(new Object[]{
            row.get("MaNV"),
            row.get("HoTen"),
            row.get("LoaiThayDoi"), // "Chuyển chức" hoặc "Nghỉ việc"
            row.get("ChucVuCu"),
            row.get("ChucVuMoi"),
            currencyFormat.format(row.get("LuongCu")),
            currencyFormat.format(row.get("LuongMoi")),
            row.get("NgayApDung")
        });
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
            } else if (tabTitle.contains("Biến động")) {
                loadBienDongNhanSu(thang, quy, nam);
            
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
    
    // Xác định kỳ báo cáo
    int thangBatDau = 1;
    int thangKetThuc = 12;
    
    if (thang > 0) {
        thangBatDau = thang;
        thangKetThuc = thang;
    }
    
    if(quy > 0) {
        // Nếu chọn quý
        switch(quy) {
            case 1: thangBatDau = 1; thangKetThuc = 3; break;
            case 2: thangBatDau = 4; thangKetThuc = 6; break;
            case 3: thangBatDau = 7; thangKetThuc = 9; break;
            case 4: thangBatDau = 10; thangKetThuc = 12; break;
        }
       
    }
    
    // Gọi BUS lấy dữ liệu thống kê kinh doanh
     ArrayList<Map<String, Object>> data = thongKeBUS.thongKeKinhDoanhTheoSanPham(thangBatDau, thangKetThuc, nam);
    
    if (data == null || data.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Không có dữ liệu kinh doanh trong kỳ.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    double tongSoLuong = 0;
    double tongDoanhThu = 0;
    double tongLoiNhuan = 0;
    
     for (Map<String, Object> row : data) {
        int soLuong = (int) row.getOrDefault("SoLuongBan", 0);
        double doanhThu = (double) row.getOrDefault("DoanhThu", 0.0);
        double loiNhuan = (double) row.getOrDefault("LoiNhuan", 0.0);
        
        tongSoLuong += soLuong;
        tongDoanhThu += doanhThu;
        tongLoiNhuan += loiNhuan;
        
        model.addRow(new Object[]{
            row.get("MaSP"),
            row.get("TenSP"),
            String.format("%,d", soLuong),
            currencyFormat.format(row.get("GiaBan")),
            currencyFormat.format(doanhThu),
            currencyFormat.format(loiNhuan)
        });
    }
    
    
    // Thêm dòng tổng cộng
     model.addRow(new Object[]{
        "TỔNG CỘNG", "",
        String.format("%,d", (long) tongSoLuong),
        "",
        currencyFormat.format(tongDoanhThu),
        currencyFormat.format(tongLoiNhuan)
    });
    
    // Căn chỉnh độ rộng cột
    tblKinhDoanh.getColumnModel().getColumn(0).setPreferredWidth(80);   
    tblKinhDoanh.getColumnModel().getColumn(1).setPreferredWidth(200);  
    tblKinhDoanh.getColumnModel().getColumn(2).setPreferredWidth(100);  
    tblKinhDoanh.getColumnModel().getColumn(3).setPreferredWidth(120);  
    tblKinhDoanh.getColumnModel().getColumn(4).setPreferredWidth(120); 
    tblKinhDoanh.getColumnModel().getColumn(5).setPreferredWidth(120);  
}
    
    private void loadKho(int thang, int quy, int nam) {
    DefaultTableModel model = (DefaultTableModel) tblKho.getModel();
    model.setRowCount(0);
    
    // Xác định kỳ báo cáo
//    int thangBaoCao = thang;
    int thangBatDau = thang;
    int thangKetThuc = thang;
    
    if(thang == 0) {
        thangBatDau = 1;
        thangKetThuc = 12;
    }
    
    if(quy > 0) {
        // Nếu chọn quý, lấy từ đầu quý đến cuối quý
        switch(quy) {
            case 1: thangBatDau = 1; thangKetThuc = 3; break;
            case 2: thangBatDau = 4; thangKetThuc = 6; break;
            case 3: thangBatDau = 7; thangKetThuc = 9; break;
            case 4: thangBatDau = 10; thangKetThuc = 12; break;
        }
    }
    
    // Gọi BUS lấy dữ liệu thống kê kho
    ArrayList<Map<String, Object>> data = thongKeBUS.thongKeTonKho(thangBatDau, thangKetThuc, nam, quy > 0);
    
    if (data == null || data.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Không có dữ liệu tồn kho trong kỳ.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
    
    int tongNhap = 0;
    int tongTon = 0;
    
    for (Map<String, Object> row : data) {
        int nhapTrongKy = ((Number) row.getOrDefault("NhapTrongKy", 0)).intValue();
        int tonCuoi = ((Number) row.getOrDefault("TonCuoi", 0)).intValue();
        
        tongNhap += nhapTrongKy;
        tongTon += tonCuoi;
        
        model.addRow(new Object[]{
            row.getOrDefault("MaSP", ""),
            row.getOrDefault("TenSP", ""),
            row.getOrDefault("ThuongHieu", ""),
            String.format("%,d", nhapTrongKy),
            String.format("%,d", tonCuoi)
        });
    }
    
    
    // Thêm dòng tổng cộng
    model.addRow(new Object[]{
        "TỔNG CỘNG", "", "", 
        String.format("%,d", tongNhap), 
        String.format("%,d", tongTon)
    });
    
    // Căn chỉnh độ rộng cột
    tblKho.getColumnModel().getColumn(0).setPreferredWidth(80);
    tblKho.getColumnModel().getColumn(1).setPreferredWidth(200);
    tblKho.getColumnModel().getColumn(2).setPreferredWidth(100);
    tblKho.getColumnModel().getColumn(3).setPreferredWidth(100);
    tblKho.getColumnModel().getColumn(4).setPreferredWidth(100);
}

    private void loadNhanSu(int thang, int quy, int nam) {
    DefaultTableModel model = (DefaultTableModel) tblNhanSu.getModel();
    model.setRowCount(0);
    
    // Xác định kỳ báo cáo
    int thangBaoCao = thang;
    if(quy > 0) {
        // Nếu chọn quý, lấy tháng cuối quý để hiển thị báo cáo theo quý
        switch(quy) {
            case 1: thangBaoCao = 3; break;
            case 2: thangBaoCao = 6; break;
            case 3: thangBaoCao = 9; break;
            case 4: thangBaoCao = 12; break;
        }
    }
    
    // Gọi BUS lấy dữ liệu nhân sự chi tiết
    ArrayList<Map<String, Object>> data = thongKeBUS.thongKeNhanSuChiTiet(thangBaoCao, nam);
    
    for (Map<String, Object> row : data) {
        String trangThai = (boolean)row.getOrDefault("DangLamViec", true) ? "Đang làm việc" : "Đã nghỉ";
        model.addRow(new Object[]{
            row.get("MaNV"),
            row.get("HoTen"),
            row.get("ChucVu"),
            currencyFormat.format(row.get("LuongCoBan")),
            currencyFormat.format(row.get("Thuong")),
            currencyFormat.format(row.get("TongNhan")),
            row.get("SoNgayNghi"),
            trangThai
        });
    }
    
    // Thêm dòng tổng cộng
    if(data.size() > 0) {
        model.addRow(new Object[]{
            "TỔNG CỘNG", "", "", 
            currencyFormat.format(tinhTong(data, "LuongCoBan")),
            currencyFormat.format(tinhTong(data, "Thuong")),
            currencyFormat.format(tinhTong(data, "TongNhan")),
            "", ""
        });
    }
    
    if (data.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Không có dữ liệu nhân sự trong kỳ.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
}

// Thêm phương thức tính tổng
private double tinhTong(ArrayList<Map<String, Object>> data, String field) {
    double sum = 0;
    for(Map<String, Object> row : data) {
        Object val = row.get(field);
        if(val instanceof Number) {
            sum += ((Number)val).doubleValue();
        }
    }
    return sum;
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
    
    private JPanel pnlBienDongNS;
    private JTable tblBienDongNS;
    private JPanel pnlSanPhamXuat;
    private JTable tblSanPhamXuat;
}

