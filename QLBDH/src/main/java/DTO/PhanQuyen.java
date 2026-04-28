package DTO;

public class PhanQuyen {

    private String maQuyen;
    private String tenQuyen;
    

    
    

    // ==================== QUẢN LÝ NHÂN SỰ ====================
    private boolean nsXemDanhSach;
    private boolean nsThem;
    private boolean nsSua;
    private boolean nsXoa;
    private boolean nsDuyetNghi;
    private boolean nsThayDoiChucVu;
    private boolean nsTinhLuong;
    private boolean nsXemLuongCaNhan;
    private boolean nsInBangLuong;
    private boolean nsChamCong;
    private boolean nsTaoDonNghi;
    private boolean nsXemDonCuaMinh;        // Xem đơn nghỉ của chính mình
    private boolean nsThongKeNhanSu;  

    // ==================== QUẢN LÝ KHO ====================
    private boolean khoXemSanPham;
    private boolean khoThemSanPham;
    private boolean khoSuaSanPham;
    private boolean khoXoaSanPham;
    private boolean khoNhapHang;
    private boolean khoQuanLyNCC;
    private boolean khoBaoCaoTonKho;

    // ==================== QUẢN LÝ BÁN HÀNG ====================
    private boolean bhLapPhieuXuat;
    private boolean bhThongKeSanPham;
    private boolean bhThongKeLoiNhuan;
    private boolean bhThongKeSoLuongXuat;
    private boolean bhThongKeDoanhThu;

    // ==================== PHÂN QUYỀN & ADMIN ====================
    private boolean pqQuanLyPhanQuyen;
    private boolean adminBaoCaoTongHop;
    private boolean adminQuanLyUser;         // Thêm/xoá/sửa tài khoản
    private boolean adminXemTatCaSanPham;    // Xem toàn bộ sản phẩm (không cần quyền kho)
    private boolean adminXemTatCaNCC; 
    

    // ==================== QUYỀN CŨ (GIỮ ĐỂ CODE CŨ KHÔNG BỊ LỖI) ====================
    private boolean qlBanHang;
    private boolean qlNhapHang;
    private boolean qlNhanVien;
    private boolean qlSanPham;
    private boolean qlKhachHang;
    private boolean qlPhanQuyen;

    public boolean isQlPhanQuyen() {
        return qlPhanQuyen;
    }
    private boolean qlKhuyenMai;
    private boolean qlThongKe;

    // ==================== CONSTRUCTOR ====================
    public PhanQuyen() {}

    // ==================== GETTER / SETTER ====================
    public boolean isBhThongKeSanPham() { return bhThongKeSanPham; }
public boolean isBhThongKeLoiNhuan() { return bhThongKeLoiNhuan; }
    public String getMaQuyen() { return maQuyen; }
    public void setMaQuyen(String maQuyen) { this.maQuyen = maQuyen; }

    public String getTenQuyen() { return tenQuyen; }
    public void setTenQuyen(String tenQuyen) { this.tenQuyen = tenQuyen; }

    public boolean isQlBanHang() {
        return qlBanHang;
    }
    

    public boolean isQlNhapHang() {
        return qlNhapHang;
    }

    public boolean isQlNhanVien() {
        return qlNhanVien;
    }

    public boolean isQlSanPham() {
        return qlSanPham;
    }

    public boolean isQlKhachHang() {
        return qlKhachHang;
    }

    public boolean isQlKhuyenMai() {
        return qlKhuyenMai;
    }

    public boolean isQlThongKe() {
        return qlThongKe;
    }
    
    
    // Nhân sự
    public boolean isNsXemDanhSach() { return nsXemDanhSach; }
    public void setNsXemDanhSach(boolean value) { this.nsXemDanhSach = value; }

    public boolean isNsChamCong() { return nsChamCong; }
    public void setNsChamCong(boolean value) { this.nsChamCong = value; }
    
    public boolean isNsThem() { return nsThem; }
    public void setNsThem(boolean value) { this.nsThem = value; }

    public boolean isNsSua() { return nsSua; }
    public void setNsSua(boolean value) { this.nsSua = value; }

    public boolean isNsXoa() { return nsXoa; }
    public void setNsXoa(boolean value) { this.nsXoa = value; }

    public boolean isNsDuyetNghi() { return nsDuyetNghi; }
    public void setNsDuyetNghi(boolean value) { this.nsDuyetNghi = value; }

    public boolean isNsThayDoiChucVu() { return nsThayDoiChucVu; }
    public void setNsThayDoiChucVu(boolean value) { this.nsThayDoiChucVu = value; }

    public boolean isNsTinhLuong() { return nsTinhLuong; }
    public void setNsTinhLuong(boolean value) { this.nsTinhLuong = value; }

    public boolean isNsXemLuongCaNhan() { return nsXemLuongCaNhan; }
    public void setNsXemLuongCaNhan(boolean value) { this.nsXemLuongCaNhan = value; }

    public boolean isNsInBangLuong() { return nsInBangLuong; }
    public void setNsInBangLuong(boolean value) { this.nsInBangLuong = value; }
    
    public boolean isNsTaoDonNghi() { return nsTaoDonNghi; }
    public void setNsTaoDonNghi(boolean nsTaoDonNghi) { this.nsTaoDonNghi = nsTaoDonNghi; }

    public boolean isNsXemDonCuaMinh() { return nsXemDonCuaMinh; }
    public void setNsXemDonCuaMinh(boolean nsXemDonCuaMinh) { this.nsXemDonCuaMinh = nsXemDonCuaMinh; }

    public boolean isNsThongKeNhanSu() { return nsThongKeNhanSu; }
    public void setNsThongKeNhanSu(boolean nsThongKeNhanSu) { this.nsThongKeNhanSu = nsThongKeNhanSu; }
    // Kho
    public boolean isKhoXemSanPham() { return khoXemSanPham; }
    public void setKhoXemSanPham(boolean value) { this.khoXemSanPham = value; }
    public boolean isKhoThemSanPham() { return khoThemSanPham; }
    public void setKhoThemSanPham(boolean value) { this.khoThemSanPham = value; }
    public boolean isKhoSuaSanPham() { return khoSuaSanPham; }
    public void setKhoSuaSanPham(boolean value) { this.khoSuaSanPham = value; }
    public boolean isKhoXoaSanPham() { return khoXoaSanPham; }
    public void setKhoXoaSanPham(boolean value) { this.khoXoaSanPham = value; }
    public boolean isKhoNhapHang() { return khoNhapHang; }
    public void setKhoNhapHang(boolean value) { this.khoNhapHang = value; }
    public boolean isKhoQuanLyNCC() { return khoQuanLyNCC; }
    public void setKhoQuanLyNCC(boolean value) { this.khoQuanLyNCC = value; }
    public boolean isKhoBaoCaoTonKho() { return khoBaoCaoTonKho; }
    public void setKhoBaoCaoTonKho(boolean value) { this.khoBaoCaoTonKho = value; }

    // Bán hàng
    public boolean isBhLapPhieuXuat() { return bhLapPhieuXuat; }
    public void setBhLapPhieuXuat(boolean value) { this.bhLapPhieuXuat = value; }
    public boolean isBhThongKeDoanhThu() { return bhThongKeDoanhThu; }
    public void setBhThongKeDoanhThu(boolean value) { this.bhThongKeDoanhThu = value; }
    public void setBhThongKeLoiNhuan(boolean value) { this.bhThongKeLoiNhuan = value; }
    public boolean isBhThongKeSoLuongXuat() { return bhThongKeSoLuongXuat; }
    public void setBhThongKeSoLuongXuat(boolean bhThongKeSoLuongXuat) { this.bhThongKeSoLuongXuat = bhThongKeSoLuongXuat; }
    // Phân quyền & Admin
    public boolean isPqQuanLyPhanQuyen() { return pqQuanLyPhanQuyen; }
    public void setPqQuanLyPhanQuyen(boolean value) { this.pqQuanLyPhanQuyen = value; }
    public boolean isAdminBaoCaoTongHop() { return adminBaoCaoTongHop; }
    public void setAdminBaoCaoTongHop(boolean value) { this.adminBaoCaoTongHop = value; }
     public boolean isAdminQuanLyUser() { return adminQuanLyUser; }
    public void setAdminQuanLyUser(boolean adminQuanLyUser) { this.adminQuanLyUser = adminQuanLyUser; }

    public boolean isAdminXemTatCaSanPham() { return adminXemTatCaSanPham; }
    public void setAdminXemTatCaSanPham(boolean adminXemTatCaSanPham) { this.adminXemTatCaSanPham = adminXemTatCaSanPham; }

    public boolean isAdminXemTatCaNCC() { return adminXemTatCaNCC; }
    public void setAdminXemTatCaNCC(boolean adminXemTatCaNCC) { this.adminXemTatCaNCC = adminXemTatCaNCC; }
    // Quyền cũ (giữ nguyên tên để code cũ không lỗi)
    public boolean isQLBanHang() { return qlBanHang; }
    public void setQLBanHang(boolean value) { this.qlBanHang = value; }
    public boolean isQLNhapHang() { return qlNhapHang; }
    public void setQLNhapHang(boolean value) { this.qlNhapHang = value; }
    public boolean isQLNhanVien() { return qlNhanVien; }
    public void setQLNhanVien(boolean value) { this.qlNhanVien = value; }
    public boolean isQLSanPham() { return qlSanPham; }
    public void setQLSanPham(boolean value) { this.qlSanPham = value; }
    public boolean isQLKhachHang() { return qlKhachHang; }
    public void setQLKhachHang(boolean value) { this.qlKhachHang = value; }
    public boolean isQLPhanQuyen() { return qlPhanQuyen; }
    public void setQLPhanQuyen(boolean value) { this.qlPhanQuyen = value; }
    public boolean isQLKhuyenMai() { return qlKhuyenMai; }
    public void setQLKhuyenMai(boolean value) { this.qlKhuyenMai = value; }
    public boolean isQLThongKe() { return qlThongKe; }
    public void setQLThongKe(boolean value) { this.qlThongKe = value; }
    public void setAllPermissions(boolean value) {
    // NS
    this.nsXemDanhSach = value;
    this.nsThem = value;
    this.nsSua = value;
    this.nsXoa = value;
    this.nsDuyetNghi = value;
    this.nsThayDoiChucVu = value;
    this.nsTinhLuong = value;
    this.nsXemLuongCaNhan = value;
    this.nsInBangLuong = value;
    this.nsTaoDonNghi = value;
    this.nsChamCong = value;
    this.nsXemDonCuaMinh = value;
    this.nsThongKeNhanSu = value;
    // KHO
    this.khoXemSanPham = value;
    this.khoThemSanPham = value;
    this.khoSuaSanPham = value;
    this.khoXoaSanPham = value;
    this.khoNhapHang = value;
    this.khoQuanLyNCC = value;
    this.khoBaoCaoTonKho = value;
    // BH
    this.bhLapPhieuXuat = value;
    this.bhThongKeDoanhThu = value;
    this.bhThongKeLoiNhuan = value;
    this.bhThongKeSoLuongXuat = value;
    // PQ & Admin
    this.pqQuanLyPhanQuyen = value;
    this.adminBaoCaoTongHop = value;
    this.adminQuanLyUser = value;
    this.adminXemTatCaSanPham = value;
    this.adminXemTatCaNCC = value;
    // Legacy
    this.qlBanHang = value;
    this.qlNhapHang = value;
    this.qlNhanVien = value;
    this.qlSanPham = value;
    this.qlKhachHang = value;
    this.qlPhanQuyen = value;
    this.qlKhuyenMai = value;
    this.qlThongKe = value;
}

   
}