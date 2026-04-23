package GUI;

import BUS.PhanQuyenBUS;
import DTO.PhanQuyen;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class PnPhanQuyen extends JPanel {
    private PhanQuyenBUS pqBUS = new PhanQuyenBUS();
    private JComboBox<String> cboQuyen;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;
    
    // NS
    private JCheckBox chkNSXemDanhSach, chkNSThem, chkNSSua, chkNSXoa, chkNSDuyetNghi, chkNSThayDoiChucVu, chkNSTinhLuong, chkNSXemLuongCaNhan, chkNSInBangLuong;
    // KHO
    private JCheckBox chkKHOXemSP, chkKHOThemSP, chkKHOSuaSP, chkKHOXoaSP, chkKHONhapHang, chkKHOQuanLyNCC, chkKHOBaoCaoTonKho;
    // BH
    private JCheckBox chkBHLapPhieuXuat, chkBHThongKeDoanhThu, chkBHThongKeLoiNhuan;
    // PQ Admin
    private JCheckBox chkPQQuanLyPhanQuyen, chkAdminBaoCaoTongHop;
    // Legacy (hiển thị nhưng không sửa)
    private JCheckBox chkQLBanHang, chkQLNhapHang, chkQLNhanVien, chkQLSanPham, chkQLKhachHang, chkQLKhuyenMai, chkQLPhanQuyen, chkQLThongKe;
    
    private JPanel centerPanel; // để dễ dàng reset
    private PhanQuyen phanQuyen;
    public PnPhanQuyen(PhanQuyen pq) {
        initComponents();
        this.phanQuyen = pq;
        loadDanhSachQuyen();
        configureByPermission();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Panel trên cùng: combo box và các nút
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cboQuyen = new JComboBox<>();
        cboQuyen.addActionListener(e -> loadQuyenSelected());
        topPanel.add(new JLabel("Chọn quyền:"));
        topPanel.add(cboQuyen);
        
        btnThem = new JButton("Thêm", new ImageIcon(getClass().getResource("/icon/them.png")));
        btnThem.addActionListener(e -> themQuyen());
        topPanel.add(btnThem);
        
        btnSua = new JButton("Sửa", new ImageIcon(getClass().getResource("/icon/sua.png")));
        btnSua.addActionListener(e -> suaQuyen());
        topPanel.add(btnSua);
        
        btnXoa = new JButton("Xóa", new ImageIcon(getClass().getResource("/icon/xoa.png")));
        btnXoa.addActionListener(e -> xoaQuyen());
        topPanel.add(btnXoa);
        
        btnLamMoi = new JButton("Làm mới", new ImageIcon(getClass().getResource("/icon/refresh.png")));
        btnLamMoi.addActionListener(e -> lamMoi());
        topPanel.add(btnLamMoi);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Panel trung tâm chứa tất cả checkbox
        centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        
        // Nhân sự
        JPanel nsPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        nsPanel.setBorder(BorderFactory.createTitledBorder("Quản lý nhân sự"));
        chkNSXemDanhSach = new JCheckBox("Xem danh sách");
        chkNSThem = new JCheckBox("Thêm");
        chkNSSua = new JCheckBox("Sửa");
        chkNSXoa = new JCheckBox("Xóa");
        chkNSDuyetNghi = new JCheckBox("Duyệt nghỉ");
        chkNSThayDoiChucVu = new JCheckBox("Thay đổi chức vụ");
        chkNSTinhLuong = new JCheckBox("Tính lương");
        chkNSXemLuongCaNhan = new JCheckBox("Xem lương cá nhân");
        chkNSInBangLuong = new JCheckBox("In bảng lương");
        nsPanel.add(chkNSXemDanhSach);
        nsPanel.add(chkNSThem);
        nsPanel.add(chkNSSua);
        nsPanel.add(chkNSXoa);
        nsPanel.add(chkNSDuyetNghi);
        nsPanel.add(chkNSThayDoiChucVu);
        nsPanel.add(chkNSTinhLuong);
        nsPanel.add(chkNSXemLuongCaNhan);
        nsPanel.add(chkNSInBangLuong);
        centerPanel.add(nsPanel);
        
        // Kho
        JPanel khoPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        khoPanel.setBorder(BorderFactory.createTitledBorder("Quản lý kho"));
        chkKHOXemSP = new JCheckBox("Xem sản phẩm");
        chkKHOThemSP = new JCheckBox("Thêm sản phẩm");
        chkKHOSuaSP = new JCheckBox("Sửa sản phẩm");
        chkKHOXoaSP = new JCheckBox("Xóa sản phẩm");
        chkKHONhapHang = new JCheckBox("Nhập hàng");
        chkKHOQuanLyNCC = new JCheckBox("Quản lý NCC");
        chkKHOBaoCaoTonKho = new JCheckBox("Báo cáo tồn kho");
        khoPanel.add(chkKHOXemSP);
        khoPanel.add(chkKHOThemSP);
        khoPanel.add(chkKHOSuaSP);
        khoPanel.add(chkKHOXoaSP);
        khoPanel.add(chkKHONhapHang);
        khoPanel.add(chkKHOQuanLyNCC);
        khoPanel.add(chkKHOBaoCaoTonKho);
        centerPanel.add(khoPanel);
        
        // Bán hàng
        JPanel bhPanel = new JPanel(new GridLayout(0, 3, 5, 5));
        bhPanel.setBorder(BorderFactory.createTitledBorder("Quản lý bán hàng"));
        chkBHLapPhieuXuat = new JCheckBox("Lập phiếu xuất");
        chkBHThongKeDoanhThu = new JCheckBox("Thống kê doanh thu");
        chkBHThongKeLoiNhuan = new JCheckBox("Thống kê lợi nhuận");
        bhPanel.add(chkBHLapPhieuXuat);
        bhPanel.add(chkBHThongKeDoanhThu);
        bhPanel.add(chkBHThongKeLoiNhuan);
        centerPanel.add(bhPanel);
        
        // Phân quyền & Admin
        JPanel pqPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        pqPanel.setBorder(BorderFactory.createTitledBorder("Phân quyền & Admin"));
        chkPQQuanLyPhanQuyen = new JCheckBox("Quản lý phân quyền");
        chkAdminBaoCaoTongHop = new JCheckBox("Báo cáo tổng hợp");
        pqPanel.add(chkPQQuanLyPhanQuyen);
        pqPanel.add(chkAdminBaoCaoTongHop);
        centerPanel.add(pqPanel);
        
   
        
        JScrollPane scroll = new JScrollPane(centerPanel);
        add(scroll, BorderLayout.CENTER);
    }
    
    private void loadDanhSachQuyen() {
        cboQuyen.removeAllItems();
        cboQuyen.addItem("-- Chọn quyền --");
        // Giả sử layDanhSachQuyen trả về ArrayList<String>
        ArrayList<PhanQuyen> list = pqBUS.layDanhSachQuyen();   // ← đúng kiểu

        for (PhanQuyen q : list) {
            cboQuyen.addItem(q.getTenQuyen());   // ← chỉ lấy tên quyền
        }
    }
    
    private void configureByPermission() {
        // Giả sử quyền quản lý khách hàng nằm trong nhóm cũ (qlKhachHang)
        // Hoặc nếu bạn đã tách riêng, hãy dùng cờ tương ứng. Ở đây tạm dùng qlKhachHang.
//        boolean hasFullAccess = phanQuyen.isQLKhachHang(); // hoặc cờ chi tiết hơn nếu có
//        btnThem.setVisible(hasFullAccess);
//        btnSua.setVisible(hasFullAccess);
//        btnXoa.setVisible(hasFullAccess);
        // btnLamMoi thường cho phép mọi người xem
        // Nếu phanQuyen == null, coi như admin (có toàn quyền)
        boolean isAdmin = (phanQuyen == null) || phanQuyen.isPqQuanLyPhanQuyen() || "ADMIN".equalsIgnoreCase(phanQuyen.getTenQuyen());

        btnThem.setVisible(isAdmin);
        btnSua.setVisible(isAdmin);
        btnXoa.setVisible(isAdmin);
        btnLamMoi.setVisible(true);  // Luôn cho phép làm mới
    }
    
    private void loadQuyenSelected() {
        String quyen = (String) cboQuyen.getSelectedItem();
        if (quyen == null || quyen.equals("-- Chọn quyền --")) {
            resetCheckboxes();
            return;
        }
        PhanQuyen pq = pqBUS.getPhanQuyenByTen(quyen);
        if (pq != null) {
            // NS
            chkNSXemDanhSach.setSelected(pq.isNsXemDanhSach());
            chkNSThem.setSelected(pq.isNsThem());
            chkNSSua.setSelected(pq.isNsSua());
            chkNSXoa.setSelected(pq.isNsXoa());
            chkNSDuyetNghi.setSelected(pq.isNsDuyetNghi());
            chkNSThayDoiChucVu.setSelected(pq.isNsThayDoiChucVu());
            chkNSTinhLuong.setSelected(pq.isNsTinhLuong());
            chkNSXemLuongCaNhan.setSelected(pq.isNsXemLuongCaNhan());
            chkNSInBangLuong.setSelected(pq.isNsInBangLuong());
            
            // KHO
            chkKHOXemSP.setSelected(pq.isKhoXemSanPham());
            chkKHOThemSP.setSelected(pq.isKhoThemSanPham());
            chkKHOSuaSP.setSelected(pq.isKhoSuaSanPham());
            chkKHOXoaSP.setSelected(pq.isKhoXoaSanPham());
            chkKHONhapHang.setSelected(pq.isKhoNhapHang());
            chkKHOQuanLyNCC.setSelected(pq.isKhoQuanLyNCC());
            chkKHOBaoCaoTonKho.setSelected(pq.isKhoBaoCaoTonKho());
            
            // BH
            chkBHLapPhieuXuat.setSelected(pq.isBhLapPhieuXuat());
            chkBHThongKeDoanhThu.setSelected(pq.isBhThongKeDoanhThu());
            chkBHThongKeLoiNhuan.setSelected(pq.isBhThongKeLoiNhuan());
            
            // PQ
            chkPQQuanLyPhanQuyen.setSelected(pq.isPqQuanLyPhanQuyen());
            chkAdminBaoCaoTongHop.setSelected(pq.isAdminBaoCaoTongHop());
            
            // Legacy
            chkQLBanHang.setSelected(pq.isQLBanHang());
            chkQLNhapHang.setSelected(pq.isQLNhapHang());
            chkQLNhanVien.setSelected(pq.isQLNhanVien());
            chkQLSanPham.setSelected(pq.isQLSanPham());
            chkQLKhachHang.setSelected(pq.isQLKhachHang());
            chkQLKhuyenMai.setSelected(pq.isQLKhuyenMai());
            chkQLPhanQuyen.setSelected(pq.isQLPhanQuyen());
            chkQLThongKe.setSelected(pq.isQLThongKe());
        }
    }
    
    private void resetCheckboxes() {
        // Duyệt tất cả các panel con trong centerPanel
        for (Component comp : centerPanel.getComponents()) {
            if (comp instanceof JPanel) {
                for (Component c : ((JPanel) comp).getComponents()) {
                    if (c instanceof JCheckBox) {
                        ((JCheckBox) c).setSelected(false);
                    }
                }
            }
        }
    }
    
    private PhanQuyen getPhanQuyenFromForm() {
        PhanQuyen pq = new PhanQuyen();
        pq.setTenQuyen((String) cboQuyen.getSelectedItem());
        
        // NS
        pq.setNsXemDanhSach(chkNSXemDanhSach.isSelected());
        pq.setNsThem(chkNSThem.isSelected());
        pq.setNsSua(chkNSSua.isSelected());
        pq.setNsXoa(chkNSXoa.isSelected());
        pq.setNsDuyetNghi(chkNSDuyetNghi.isSelected());
        pq.setNsThayDoiChucVu(chkNSThayDoiChucVu.isSelected());
        pq.setNsTinhLuong(chkNSTinhLuong.isSelected());
        pq.setNsXemLuongCaNhan(chkNSXemLuongCaNhan.isSelected());
        pq.setNsInBangLuong(chkNSInBangLuong.isSelected());
        
        // KHO
        pq.setKhoXemSanPham(chkKHOXemSP.isSelected());
        pq.setKhoThemSanPham(chkKHOThemSP.isSelected());
        pq.setKhoSuaSanPham(chkKHOSuaSP.isSelected());
        pq.setKhoXoaSanPham(chkKHOXoaSP.isSelected());
        pq.setKhoNhapHang(chkKHONhapHang.isSelected());
        pq.setKhoQuanLyNCC(chkKHOQuanLyNCC.isSelected());
        pq.setKhoBaoCaoTonKho(chkKHOBaoCaoTonKho.isSelected());
        
        // BH
        pq.setBhLapPhieuXuat(chkBHLapPhieuXuat.isSelected());
        pq.setBhThongKeDoanhThu(chkBHThongKeDoanhThu.isSelected());
        pq.setBhThongKeLoiNhuan(chkBHThongKeLoiNhuan.isSelected());
        
        // PQ
        pq.setPqQuanLyPhanQuyen(chkPQQuanLyPhanQuyen.isSelected());
        pq.setAdminBaoCaoTongHop(chkAdminBaoCaoTongHop.isSelected());
        
        // Tính toán quyền cũ từ quyền chi tiết
        pq.setQLBanHang(pq.isBhLapPhieuXuat() || pq.isBhThongKeDoanhThu() || pq.isBhThongKeLoiNhuan());
        pq.setQLNhapHang(pq.isKhoNhapHang());
        pq.setQLNhanVien(pq.isNsXemDanhSach() || pq.isNsThem() || pq.isNsSua() || pq.isNsXoa() || pq.isNsDuyetNghi() || pq.isNsThayDoiChucVu() || pq.isNsTinhLuong() || pq.isNsXemLuongCaNhan() || pq.isNsInBangLuong());
        pq.setQLSanPham(pq.isKhoXemSanPham() || pq.isKhoThemSanPham() || pq.isKhoSuaSanPham() || pq.isKhoXoaSanPham() || pq.isKhoBaoCaoTonKho());
        pq.setQLKhachHang(false);
        pq.setQLKhuyenMai(false);
        pq.setQLPhanQuyen(pq.isPqQuanLyPhanQuyen());
        pq.setQLThongKe(pq.isAdminBaoCaoTongHop() || pq.isBhThongKeDoanhThu() || pq.isBhThongKeLoiNhuan());
        
        return pq;
    }
    
    private void themQuyen() {
        String newQuyen = JOptionPane.showInputDialog(this, "Nhập tên quyền mới:");
        if (newQuyen == null || newQuyen.trim().isEmpty()) return;
        if (pqBUS.getPhanQuyenByTen(newQuyen) != null) {
            JOptionPane.showMessageDialog(this, "Quyền đã tồn tại!");
            return;
        }
        PhanQuyen pq = new PhanQuyen();
        pq.setTenQuyen(newQuyen);
        boolean success = pqBUS.themQuyen(pq);
        if (success) {
            JOptionPane.showMessageDialog(this, "Thêm quyền thành công!");
            loadDanhSachQuyen();
            cboQuyen.setSelectedItem(newQuyen);
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
        }
    }
    
    private void suaQuyen() {
        String quyen = (String) cboQuyen.getSelectedItem();
        if (quyen == null || quyen.equals("-- Chọn quyền --")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn quyền!");
            return;
        }
        PhanQuyen pq = getPhanQuyenFromForm();
        boolean success = pqBUS.updateQuyen(pq);
        if (success) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadQuyenSelected();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }
    
    private void xoaQuyen() {
        String quyen = (String) cboQuyen.getSelectedItem();
        if (quyen == null || quyen.equals("-- Chọn quyền --")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn quyền!");
            return;
        }
        if (quyen.equalsIgnoreCase("admin")) {
            JOptionPane.showMessageDialog(this, "Không thể xóa quyền admin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa quyền " + quyen + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = pqBUS.xoaQuyen(quyen);
            if (success) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDanhSachQuyen();
                resetCheckboxes();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }
    
    private void lamMoi() {
        loadQuyenSelected();
    }
}