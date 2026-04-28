
package DAO;

import DTO.PhanQuyen;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PhanQuyenDAO {

    public PhanQuyen getPhanQuyenByTen(String tenQuyen) {
        PhanQuyen pq = null;
        // Giả sử tên quyền là duy nhất (cột TenQuyen)
        String sql = "SELECT * FROM PhanQuyen WHERE MaQuyen = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, tenQuyen)) {
            if (rs.next()) {
                pq = extractPhanQuyenFromResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pq;
    }

    
    public ArrayList<PhanQuyen> getDanhSachQuyen() {
        ArrayList<PhanQuyen> list = new ArrayList<>();
        String sql = "SELECT * FROM PhanQuyen";
        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractPhanQuyenFromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private PhanQuyen extractPhanQuyenFromResultSet(ResultSet rs) throws Exception {
        PhanQuyen pq = new PhanQuyen();
        pq.setMaQuyen(rs.getString("MaQuyen"));
        pq.setTenQuyen(rs.getString("TenQuyen"));

        // Nhân sự
        pq.setNsXemDanhSach(rs.getBoolean("NS_XemDanhSach"));
        pq.setNsThem(rs.getBoolean("NS_Them"));
        pq.setNsSua(rs.getBoolean("NS_Sua"));
        pq.setNsXoa(rs.getBoolean("NS_Xoa"));
        pq.setNsDuyetNghi(rs.getBoolean("NS_DuyetNghi"));
        pq.setNsThayDoiChucVu(rs.getBoolean("NS_ThayDoiChucVu"));
        pq.setNsTinhLuong(rs.getBoolean("NS_TinhLuong"));
        pq.setNsChamCong(rs.getBoolean("NS_ChamCong"));
        pq.setNsXemLuongCaNhan(rs.getBoolean("NS_XemLuongCaNhan"));
        pq.setNsInBangLuong(rs.getBoolean("NS_InBangLuong"));

        // Kho
        pq.setKhoXemSanPham(rs.getBoolean("KHO_XemSanPham"));
        pq.setKhoThemSanPham(rs.getBoolean("KHO_ThemSanPham"));
        pq.setKhoSuaSanPham(rs.getBoolean("KHO_SuaSanPham"));
        pq.setKhoXoaSanPham(rs.getBoolean("KHO_XoaSanPham"));
        pq.setKhoNhapHang(rs.getBoolean("KHO_NhapHang"));
        pq.setKhoQuanLyNCC(rs.getBoolean("KHO_QuanLyNCC"));
        pq.setKhoBaoCaoTonKho(rs.getBoolean("KHO_BaoCaoTonKho"));

        // Bán hàng
        pq.setBhLapPhieuXuat(rs.getBoolean("BH_LapPhieuXuat"));
        pq.setBhThongKeDoanhThu(rs.getBoolean("BH_ThongKeDoanhThu"));
        pq.setBhThongKeLoiNhuan(rs.getBoolean("BH_ThongKeLoiNhuan"));

        // Phân quyền
        pq.setPqQuanLyPhanQuyen(rs.getBoolean("PQ_QuanLyPhanQuyen"));
        pq.setAdminBaoCaoTongHop(rs.getBoolean("Admin_BaoCaoTongHop"));

        // Quyền cũ - tính từ quyền chi tiết (có thể gán trực tiếp nếu cột vẫn tồn tại)
        // Ở đây ta tính dựa trên logic: qlBanHang = true nếu có ít nhất một quyền bán hàng
        pq.setQLBanHang(pq.isBhLapPhieuXuat() || pq.isBhThongKeDoanhThu() || pq.isBhThongKeLoiNhuan());
        pq.setQLSanPham(pq.isKhoXemSanPham() || pq.isKhoThemSanPham() || pq.isKhoSuaSanPham() || pq.isKhoXoaSanPham());
        pq.setQLNhanVien(pq.isNsXemDanhSach() || pq.isNsThem() || pq.isNsSua() || pq.isNsXoa() || pq.isNsDuyetNghi() || pq.isNsThayDoiChucVu() || pq.isNsTinhLuong() || pq.isNsXemLuongCaNhan() || pq.isNsInBangLuong());
        pq.setQLNhapHang(pq.isKhoNhapHang()); // hoặc kết hợp
        pq.setQLKhachHang(rs.getBoolean("QLKhachHang")); 
        pq.setQLKhuyenMai(rs.getBoolean("QLKhuyenMai"));
        
        pq.setQLThongKe(pq.isBhThongKeDoanhThu() || pq.isBhThongKeLoiNhuan() || pq.isKhoBaoCaoTonKho() || pq.isAdminBaoCaoTongHop());
        pq.setQLPhanQuyen(pq.isPqQuanLyPhanQuyen());

        return pq;
    }

    public boolean insertQuyen(PhanQuyen pq) {
        // Kiểm tra tên quyền đã tồn tại chưa
        if (getPhanQuyenByTen(pq.getTenQuyen()) != null) {
            return false;
        }

        String sql = "INSERT INTO PhanQuyen (TenQuyen, NS_XemDanhSach, NS_Them,"
                + " NS_Sua, NS_Xoa, NS_DuyetNghi, NS_ThayDoiChucVu, NS_TinhLuong,"
                + " NS_XemLuongCaNhan, NS_InBangLuong, KHO_XemSanPham, KHO_ThemSanPham,"
                + " KHO_SuaSanPham, KHO_XoaSanPham, KHO_NhapHang, KHO_QuanLyNCC,"
                + " KHO_BaoCaoTonKho, BH_LapPhieuXuat, BH_ThongKeDoanhThu,"
                + " BH_ThongKeLoiNhuan, PQ_QuanLyPhanQuyen, Admin_BaoCaoTongHop,"
                + " QLBanHang, QLNhapHang, QLNhanVien, QLSanPham, QLKhachHang, "
                + "QLKhuyenMai, QLPhanQuyen, QLThongKe) "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            int rows = DataProvider.executeUpdate(sql,
                pq.getTenQuyen(),
                pq.isNsXemDanhSach(),
                pq.isNsThem(),
                pq.isNsSua(),
                pq.isNsXoa(),
                pq.isNsDuyetNghi(),
                pq.isNsThayDoiChucVu(),
                pq.isNsTinhLuong(),
                pq.isNsXemLuongCaNhan(),
                pq.isNsInBangLuong(),
                pq.isKhoXemSanPham(),
                pq.isKhoThemSanPham(),
                pq.isKhoSuaSanPham(),
                pq.isKhoXoaSanPham(),
                pq.isKhoNhapHang(),
                pq.isKhoQuanLyNCC(),
                pq.isKhoBaoCaoTonKho(),
                pq.isBhLapPhieuXuat(),
                pq.isBhThongKeDoanhThu(),
                pq.isBhThongKeLoiNhuan(),
                pq.isPqQuanLyPhanQuyen(),
                pq.isAdminBaoCaoTongHop(),
                pq.isQLBanHang(),
                pq.isQLNhapHang(),
                pq.isQLNhanVien(),
                pq.isQLSanPham(),
                pq.isQLKhachHang(),
                pq.isQLKhuyenMai(),
                pq.isQLPhanQuyen(),
                pq.isQLThongKe()
            );
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateQuyen(PhanQuyen pq) {
        String sql = "UPDATE PhanQuyen SET NS_XemDanhSach=?, NS_Them=?, NS_Sua=?,"
                + " NS_Xoa=?, NS_DuyetNghi=?, NS_ThayDoiChucVu=?, NS_TinhLuong=?,"
                + " NS_XemLuongCaNhan=?, NS_InBangLuong=?, KHO_XemSanPham=?,"
                + " KHO_ThemSanPham=?, KHO_SuaSanPham=?, KHO_XoaSanPham=?,"
                + " KHO_NhapHang=?, KHO_QuanLyNCC=?, KHO_BaoCaoTonKho=?, "
                + "BH_LapPhieuXuat=?, BH_ThongKeDoanhThu=?, BH_ThongKeLoiNhuan=?,"
                + " PQ_QuanLyPhanQuyen=?, Admin_BaoCaoTongHop=?, QLBanHang=?, "
                + "QLNhapHang=?, QLNhanVien=?, QLSanPham=?, QLKhachHang=?,"
                + " QLKhuyenMai=?, QLPhanQuyen=?, QLThongKe=? WHERE TenQuyen=?";

        try {
            int rows = DataProvider.executeUpdate(sql,
                pq.isNsXemDanhSach(),
                pq.isNsThem(),
                pq.isNsSua(),
                pq.isNsXoa(),
                pq.isNsDuyetNghi(),
                pq.isNsThayDoiChucVu(),
                pq.isNsTinhLuong(),
                pq.isNsXemLuongCaNhan(),
                pq.isNsInBangLuong(),
                pq.isKhoXemSanPham(),
                pq.isKhoThemSanPham(),
                pq.isKhoSuaSanPham(),
                pq.isKhoXoaSanPham(),
                pq.isKhoNhapHang(),
                pq.isKhoQuanLyNCC(),
                pq.isKhoBaoCaoTonKho(),
                pq.isBhLapPhieuXuat(),
                pq.isBhThongKeDoanhThu(),
                pq.isBhThongKeLoiNhuan(),
                pq.isPqQuanLyPhanQuyen(),
                pq.isAdminBaoCaoTongHop(),
                pq.isQLBanHang(),
                pq.isQLNhapHang(),
                pq.isQLNhanVien(),
                pq.isQLSanPham(),
                pq.isQLKhachHang(),
                pq.isQLKhuyenMai(),
                pq.isQLPhanQuyen(),
                pq.isQLThongKe(),
                pq.getTenQuyen()
            );
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteQuyen(String maQuyen) {
        // Có thể xóa hẳn hoặc set quyền về 0, tùy yêu cầu. Ở đây xóa hẳn.
        String sql = "UPDATE PhanQuyen SET NS_XemDanhSach=0, NS_Them=0, NS_Sua=0, NS_Xoa=0, NS_DuyetNghi=0, NS_ThayDoiChucVu=0, NS_TinhLuong=0, NS_XemLuongCaNhan=0, NS_InBangLuong=0, KHO_XemSanPham=0, KHO_ThemSanPham=0, KHO_SuaSanPham=0, KHO_XoaSanPham=0, KHO_NhapHang=0, KHO_QuanLyNCC=0, KHO_BaoCaoTonKho=0, BH_LapPhieuXuat=0, BH_ThongKeDoanhThu=0, BH_ThongKeLoiNhuan=0, PQ_QuanLyPhanQuyen=0, Admin_BaoCaoTongHop=0, QLBanHang=0, QLNhapHang=0, QLNhanVien=0, QLSanPham=0, QLKhachHang=0, QLKhuyenMai=0, QLPhanQuyen=0, QLThongKe=0 WHERE Quyen=?";
        try {
            int rows = DataProvider.executeUpdate(sql, maQuyen);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}