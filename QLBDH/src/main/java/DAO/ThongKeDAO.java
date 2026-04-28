package DAO;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ThongKeDAO {

    private Connection conn;

    public ThongKeDAO(Connection conn) {
        this.conn = conn;
    }

    // ==================== TIỆN ÍCH ====================
    private String buildTimeCondition(int thang, int quy, int nam, String dateColumn) {
        if (thang > 0) {
            return "MONTH(" + dateColumn + ") = ? AND YEAR(" + dateColumn + ") = ?";
        } else if (quy > 0) {
            int startMonth = (quy - 1) * 3 + 1;
            int endMonth = startMonth + 2;
            return "MONTH(" + dateColumn + ") BETWEEN ? AND ? AND YEAR(" + dateColumn + ") = ?";
        } else {
            return "YEAR(" + dateColumn + ") = ?";
        }
    }

    private int setTimeParameters(PreparedStatement ps, int index, int thang, int quy, int nam) throws SQLException {
        if (thang > 0) {
            ps.setInt(index++, thang);
            ps.setInt(index++, nam);
        } else if (quy > 0) {
            int startMonth = (quy - 1) * 3 + 1;
            int endMonth = startMonth + 2;
            ps.setInt(index++, startMonth);
            ps.setInt(index++, endMonth);
            ps.setInt(index++, nam);
        } else {
            ps.setInt(index++, nam);
        }
        return index;
    }

    // ==================== KINH DOANH & LỢI NHUẬN ====================
    public double getTongDoanhThu(int thang, int quy, int nam) throws SQLException {
        String condition = buildTimeCondition(thang, quy, nam, "NgayLap");
        String sql = "SELECT SUM(TongTien) FROM HoaDon WHERE " + condition + " AND TrangThai = N'Đã thanh toán'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setTimeParameters(ps, 1, thang, quy, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double value = rs.getDouble(1);
                    return rs.wasNull() ? 0.0 : value;
                }
            }
        }
        return 0;
    }

    public double getTongGiaVon(int thang, int quy, int nam) throws SQLException {
        String condition = buildTimeCondition(thang, quy, nam, "hd.NgayLap");

        String sql = "SELECT SUM(ct.SoLuong * COALESCE(avg_gia.giaTB, 0)) " +
                     "FROM CTHoaDon ct " +
                     "JOIN HoaDon hd ON ct.MaHD = hd.MaHD " +
                     "JOIN SanPham sp ON ct.ID = sp.ID " +
                     "LEFT JOIN ( " +
                     "    SELECT ctpn.id, SUM(ctpn.ThanhTien) * 1.0 / NULLIF(SUM(ctpn.SoLuong), 0) AS giaTB " +
                     "    FROM CTPhieuNhap ctpn " +
                     "    JOIN PhieuNhap pn ON ctpn.MaPN = pn.MaPN " +
                     "    WHERE pn.TrangThai = 1 " +
                     "    GROUP BY ctpn.id " +
                     ") avg_gia ON ct.ID = avg_gia.id " +
                     "WHERE " + condition + " AND hd.TrangThai = N'Đã thanh toán'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setTimeParameters(ps, 1, thang, quy, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double value = rs.getDouble(1);
                    return rs.wasNull() ? 0.0 : value;
                }
            }
        }
        return 0;
    }

    public int getSoHoaDon(int thang, int quy, int nam) throws SQLException {
        String condition = buildTimeCondition(thang, quy, nam, "NgayLap");
        String sql = "SELECT COUNT(*) FROM HoaDon WHERE " + condition + " AND TrangThai = N'Đã thanh toán'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setTimeParameters(ps, 1, thang, quy, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public int getTongSanPhamBanRa(int thang, int quy, int nam) throws SQLException {
        String condition = buildTimeCondition(thang, quy, nam, "hd.NgayLap");
        String sql = "SELECT SUM(ct.SoLuong) " +
                     "FROM CTHoaDon ct " +
                     "JOIN HoaDon hd ON ct.MaHD = hd.MaHD " +
                     "WHERE " + condition + " AND hd.TrangThai = N'Đã thanh toán'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setTimeParameters(ps, 1, thang, quy, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // ==================== KHO HÀNG & SẢN PHẨM ====================
    public int getTongNhapSoLuong(int thang, int quy, int nam) throws SQLException {
        String condition = buildTimeCondition(thang, quy, nam, "pn.NgayLap");
        String sql = "SELECT SUM(ct.SoLuong) " +
                     "FROM CTPhieuNhap ct " +
                     "JOIN PhieuNhap pn ON ct.MaPN = pn.MaPN " +
                     "WHERE " + condition + " AND pn.TrangThai = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setTimeParameters(ps, 1, thang, quy, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public double getTongNhapGiaTri(int thang, int quy, int nam) throws SQLException {
        String condition = buildTimeCondition(thang, quy, nam, "pn.NgayLap");
        String sql = "SELECT SUM(ct.ThanhTien) " +
                     "FROM CTPhieuNhap ct " +
                     "JOIN PhieuNhap pn ON ct.MaPN = pn.MaPN " +
                     "WHERE " + condition + " AND pn.TrangThai = 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setTimeParameters(ps, 1, thang, quy, nam);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double value = rs.getDouble(1);
                    return rs.wasNull() ? 0.0 : value;
                }
            }
        }
        return 0;
    }

    public int getTongXuatSoLuong(int thang, int quy, int nam) throws SQLException {
        return getTongSanPhamBanRa(thang, quy, nam);
    }

    public int getTonKhoHienTai() throws SQLException {
        String sql = "SELECT SUM(SoLuong) FROM SanPham WHERE TrangThai = 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public ArrayList<Object[]> getTopSanPhamBanChay(int thang, int quy, int nam, int limit) throws SQLException {
        String condition = buildTimeCondition(thang, quy, nam, "hd.NgayLap");
        String sql = "SELECT TOP " + limit + " sp.TenSP, SUM(ct.SoLuong) AS TongBan " +
                     "FROM CTHoaDon ct " +
                     "JOIN HoaDon hd ON ct.MaHD = hd.MaHD " +
                     "JOIN SanPham sp ON ct.ID = sp.ID " +
                     "WHERE " + condition + " AND hd.TrangThai = N'Đã thanh toán' " +
                     "GROUP BY sp.TenSP " +
                     "ORDER BY TongBan DESC";

        ArrayList<Object[]> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setTimeParameters(ps, 1, thang, quy, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{rs.getString("TenSP"), rs.getInt("TongBan")});
                }
            }
        }
        return list;
    }

    // ==================== NHÂN SỰ & CHI PHÍ LƯƠNG ====================
    public double getTongChiPhiLuong(int thang, int quy, int nam) throws SQLException {
        String sql;
        if (thang > 0) {
            sql = "SELECT SUM(TongLuong) FROM BangLuong WHERE Thang = ? AND Nam = ?";
        } else if (quy > 0) {
            int startMonth = (quy - 1) * 3 + 1;
            int endMonth = startMonth + 2;
            sql = "SELECT SUM(TongLuong) FROM BangLuong WHERE Thang BETWEEN ? AND ? AND Nam = ?";
        } else {
            sql = "SELECT SUM(TongLuong) FROM BangLuong WHERE Nam = ?";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (thang > 0) {
                ps.setInt(1, thang);
                ps.setInt(2, nam);
            } else if (quy > 0) {
                int startMonth = (quy - 1) * 3 + 1;
                int endMonth = startMonth + 2;
                ps.setInt(1, startMonth);
                ps.setInt(2, endMonth);
                ps.setInt(3, nam);
            } else {
                ps.setInt(1, nam);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double value = rs.getDouble(1);
                    return rs.wasNull() ? 0.0 : value;
                }
            }
        }
        return 0;
    }

    public int getSoNhanVienDangLam() throws SQLException {
        String sql = "SELECT COUNT(*) FROM NhanVien WHERE TrangThai = 1";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public ArrayList<Object[]> getTopNhanVienBanHang(int thang, int quy, int nam, int limit) throws SQLException {
        String condition = buildTimeCondition(thang, quy, nam, "hd.NgayLap");
        String sql = "SELECT TOP " + limit +
                     " nv.Ho + ' ' + nv.Ten AS HoTen, " +
                     " COUNT(hd.MaHD) AS SoHD, SUM(hd.TongTien) AS DoanhSo " +
                     "FROM HoaDon hd " +
                     "JOIN NhanVien nv ON hd.MaNV = nv.MaNV " +
                     "WHERE " + condition + " AND hd.TrangThai = N'Đã thanh toán' " +
                     "GROUP BY nv.Ho, nv.Ten " +
                     "ORDER BY DoanhSo DESC";

        ArrayList<Object[]> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setTimeParameters(ps, 1, thang, quy, nam);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("HoTen"),
                        rs.getInt("SoHD"),
                        rs.getDouble("DoanhSo")
                    });
                }
            }
        }
        return list;
    }
    
    
    

public ArrayList<Map<String, Object>> thongKeNhanSuChiTiet(int thang, int nam) {
    ArrayList<Map<String, Object>> result = new ArrayList<>();
    
    // Lấy lương từ bảng BangLuong theo tháng/năm
    String sql = "SELECT nv.MaNV, " +
                 "nv.Ho + ' ' + nv.Ten as HoTen, " +
                 "cv.TenCV as ChucVu, " +
                 "ISNULL(bl.LuongCoBan, 0) as LuongCoBan, " +
                 "ISNULL(bl.Thuong, 0) as Thuong, " +
                 "ISNULL(bl.TongLuong, 0) as TongNhan, " +
                 "ISNULL(bl.PhuCap, 0) as PhuCap, " +
                 "ISNULL(bl.Phat, 0) as Phat, " +
                 "nv.TrangThai as DangLamViec " +
                 "FROM NhanVien nv " +
                 "LEFT JOIN ChucVu cv ON nv.MaCV = cv.MaCV " +
                 "LEFT JOIN BangLuong bl ON nv.MaNV = bl.MaNV AND bl.Thang = ? AND bl.Nam = ? AND bl.TrangThai = 1 " +
                 "WHERE nv.TrangThai = 1 " +
                 "ORDER BY nv.MaNV";
    
    try(PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, thang);
        ps.setInt(2, nam);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("MaNV", rs.getInt("MaNV"));
            row.put("HoTen", rs.getString("HoTen"));
            row.put("ChucVu", rs.getString("ChucVu"));
            row.put("LuongCoBan", rs.getDouble("LuongCoBan"));
            row.put("Thuong", rs.getDouble("Thuong"));
            row.put("PhuCap", rs.getDouble("PhuCap"));
            row.put("Phat", rs.getDouble("Phat"));
            row.put("TongNhan", rs.getDouble("TongNhan"));
            row.put("DangLamViec", rs.getBoolean("DangLamViec"));
            result.add(row);
        }
    } catch(SQLException e) {
        e.printStackTrace();
    }
    return result;
}

public ArrayList<Map<String, Object>> thongKeBienDongNhanSu(int thang, int nam) {
    ArrayList<Map<String, Object>> result = new ArrayList<>();
    
    // Lấy lịch sử thay đổi chức vụ từ bảng LichSuChucVu
    String sql = "SELECT nv.MaNV, " +
                 "nv.Ho + ' ' + nv.Ten as HoTen, " +
                 "N'Thay đổi chức vụ' as LoaiThayDoi, " +
                 "ISNULL(cv_cu.TenCV, N'Không có') as ChucVuCu, " +
                 "ISNULL(cv_moi.TenCV, N'Không có') as ChucVuMoi, " +
                 "ISNULL(lcb_cu.LuongCoBan, 0) as LuongCu, " +
                 "ISNULL(lcb_moi.LuongCoBan, 0) as LuongMoi, " +
                 "CONVERT(varchar, lscv.NgayThayDoi, 103) as NgayApDung " +
                 "FROM LichSuChucVu lscv " +
                 "JOIN NhanVien nv ON lscv.MaNV = nv.MaNV " +
                 "LEFT JOIN ChucVu cv_cu ON lscv.MaCVCu = cv_cu.MaCV " +
                 "LEFT JOIN ChucVu cv_moi ON lscv.MaCVMoi = cv_moi.MaCV " +
                 "LEFT JOIN LuongCoBanTheoChucVu lcb_cu ON lscv.MaCVCu = lcb_cu.MaCV " +
                 "LEFT JOIN LuongCoBanTheoChucVu lcb_moi ON lscv.MaCVMoi = lcb_moi.MaCV " +
                 "WHERE MONTH(lscv.NgayThayDoi) = ? AND YEAR(lscv.NgayThayDoi) = ? " +
                 "UNION ALL " +
                 "SELECT nv.MaNV, " +
                 "nv.Ho + ' ' + nv.Ten as HoTen, " +
                 "N'Nghỉ việc' as LoaiThayDoi, " +
                 "cv.TenCV as ChucVuCu, " +
                 "N'' as ChucVuMoi, " +
                 "0 as LuongCu, " +
                 "0 as LuongMoi, " +
                 "CONVERT(varchar, GETDATE(), 103) as NgayApDung " +
                 "FROM NhanVien nv " +
                 "LEFT JOIN ChucVu cv ON nv.MaCV = cv.MaCV " +
                 "WHERE nv.TrangThai = 0 " +
                 "AND MONTH(GETDATE()) = ? AND YEAR(GETDATE()) = ?";
    
    try(PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, thang);
        ps.setInt(2, nam);
        ps.setInt(3, thang);
        ps.setInt(4, nam);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("MaNV", rs.getInt("MaNV"));
            row.put("HoTen", rs.getString("HoTen"));
            row.put("LoaiThayDoi", rs.getString("LoaiThayDoi"));
            row.put("ChucVuCu", rs.getString("ChucVuCu"));
            row.put("ChucVuMoi", rs.getString("ChucVuMoi"));
            row.put("LuongCu", rs.getDouble("LuongCu"));
            row.put("LuongMoi", rs.getDouble("LuongMoi"));
            row.put("NgayApDung", rs.getString("NgayApDung"));
            result.add(row);
        }
    } catch(SQLException e) {
        e.printStackTrace();
    }
    return result;
}
public ArrayList<Map<String, Object>> thongKeNghiPhep(int thang, int nam) {
    ArrayList<Map<String, Object>> result = new ArrayList<>();
    
    String sql = "SELECT dt.MaDon, " +
                 "nv.MaNV, " +
                 "nv.Ho + ' ' + nv.Ten as HoTen, " +
                 "dt.LoaiDon, " +
                 "dt.NgayBatDau, " +
                 "dt.NgayKetThuc, " +
                 "DATEDIFF(day, dt.NgayBatDau, dt.NgayKetThuc) + 1 as SoNgayNghi, " +
                 "dt.LyDo, " +
                 "dt.TrangThai " +
                 "FROM DonTu dt " +
                 "JOIN NhanVien nv ON dt.MaNV = nv.MaNV " +
                 "WHERE MONTH(dt.NgayBatDau) = ? AND YEAR(dt.NgayBatDau) = ? " +
                 "AND dt.TrangThai = N'Đã duyệt' " +
                 "ORDER BY dt.NgayBatDau";
    
    try(PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, thang);
        ps.setInt(2, nam);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("MaDon", rs.getInt("MaDon"));
            row.put("MaNV", rs.getInt("MaNV"));
            row.put("HoTen", rs.getString("HoTen"));
            row.put("LoaiDon", rs.getString("LoaiDon"));
            row.put("NgayBatDau", rs.getDate("NgayBatDau"));
            row.put("NgayKetThuc", rs.getDate("NgayKetThuc"));
            row.put("SoNgayNghi", rs.getInt("SoNgayNghi"));
            row.put("LyDo", rs.getString("LyDo"));
            row.put("TrangThai", rs.getString("TrangThai"));
            result.add(row);
        }
    } catch(SQLException e) {
        e.printStackTrace();
    }
    return result;
}

public ArrayList<Map<String, Object>> thongKeTongHopLuong(int thang, int nam) {
    ArrayList<Map<String, Object>> result = new ArrayList<>();
    
    String sql = "SELECT cv.TenCV as ChucVu, " +
                 "COUNT(nv.MaNV) as SoLuong, " +
                 "SUM(bl.LuongCoBan) as TongLuongCoBan, " +
                 "SUM(bl.Thuong) as TongThuong, " +
                 "SUM(bl.PhuCap) as TongPhuCap, " +
                 "SUM(bl.Phat) as TongPhat, " +
                 "SUM(bl.TongLuong) as TongLuong, " +
                 "AVG(bl.TongLuong) as LuongTrungBinh " +
                 "FROM BangLuong bl " +
                 "JOIN NhanVien nv ON bl.MaNV = nv.MaNV " +
                 "JOIN ChucVu cv ON nv.MaCV = cv.MaCV " +
                 "WHERE bl.Thang = ? AND bl.Nam = ? AND bl.TrangThai = 1 " +
                 "GROUP BY cv.TenCV " +
                 "ORDER BY TongLuong DESC";
    
    try(PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, thang);
        ps.setInt(2, nam);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("ChucVu", rs.getString("ChucVu"));
            row.put("SoLuong", rs.getInt("SoLuong"));
            row.put("TongLuongCoBan", rs.getDouble("TongLuongCoBan"));
            row.put("TongThuong", rs.getDouble("TongThuong"));
            row.put("TongPhuCap", rs.getDouble("TongPhuCap"));
            row.put("TongPhat", rs.getDouble("TongPhat"));
            row.put("TongLuong", rs.getDouble("TongLuong"));
            row.put("LuongTrungBinh", rs.getDouble("LuongTrungBinh"));
            result.add(row);
        }
    } catch(SQLException e) {
        e.printStackTrace();
    }
    return result;
}

public ArrayList<Map<String, Object>> thongKeTonKho(int thangBatDau, int thangKetThuc, int nam, boolean isQuy) {
    ArrayList<Map<String, Object>> result = new ArrayList<>();
    
    String sql;
    if(isQuy) {
        // Thống kê theo quý
        sql = "SELECT " +
              "sp.MaSP, " +
              "sp.TenSP, " +
              "sp.ThuongHieu, " +
              "ISNULL(pn.NhapTrongKy, 0) as NhapTrongKy, " +
              "sp.SoLuong as TonCuoi " +
              "FROM SanPham sp " +
              "LEFT JOIN ( " +
              "    SELECT ctpn.ID, SUM(ctpn.SoLuong) as NhapTrongKy " +
              "    FROM CTPhieuNhap ctpn " +
              "    JOIN PhieuNhap pn ON ctpn.MaPN = pn.MaPN " +
              "    WHERE MONTH(pn.NgayLap) BETWEEN ? AND ? " +
              "    AND YEAR(pn.NgayLap) = ? " +
              "    AND pn.TrangThai = 1 " +
              "    GROUP BY ctpn.ID " +
              ") pn ON sp.ID = pn.ID " +
              "WHERE sp.TrangThai = 1 " +
              "ORDER BY sp.TenSP";
    } else {
        // Thống kê theo tháng
        sql = "SELECT " +
              "sp.MaSP, " +
              "sp.TenSP, " +
              "sp.ThuongHieu, " +
              "ISNULL(pn.NhapTrongKy, 0) as NhapTrongKy, " +
              "sp.SoLuong as TonCuoi " +
              "FROM SanPham sp " +
              "LEFT JOIN ( " +
              "    SELECT ctpn.ID, SUM(ctpn.SoLuong) as NhapTrongKy " +
              "    FROM CTPhieuNhap ctpn " +
              "    JOIN PhieuNhap pn ON ctpn.MaPN = pn.MaPN " +
              "    WHERE MONTH(pn.NgayLap) = ? " +
              "    AND YEAR(pn.NgayLap) = ? " +
              "    AND pn.TrangThai = 1 " +
              "    GROUP BY ctpn.ID " +
              ") pn ON sp.ID = pn.ID " +
              "WHERE sp.TrangThai = 1 " +
              "ORDER BY sp.TenSP";
    }
    
    try(PreparedStatement ps = conn.prepareStatement(sql)) {
        if(isQuy) {
            ps.setInt(1, thangBatDau);
            ps.setInt(2, thangKetThuc);
            ps.setInt(3, nam);
        } else {
            ps.setInt(1, thangBatDau);
            ps.setInt(2, nam);
        }
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("MaSP", rs.getString("MaSP"));
            row.put("TenSP", rs.getString("TenSP"));
            row.put("ThuongHieu", rs.getString("ThuongHieu"));
            row.put("NhapTrongKy", rs.getInt("NhapTrongKy"));
            row.put("TonCuoi", rs.getInt("TonCuoi")); // Số lượng tồn cuối kỳ (lấy từ bảng SanPham)
            result.add(row);
        }
    } catch(SQLException e) {
        e.printStackTrace();
    }
    return result;
}

public ArrayList<Map<String, Object>> thongKeNhapXuatChiTiet(int thang, int nam) {
    ArrayList<Map<String, Object>> result = new ArrayList<>();
    
    String sql = "SELECT " +
                 "FORMAT(pn.NgayLap, 'dd/MM/yyyy') as NgayNhap, " +
                 "pn.MaPN, " +
                 "ncc.TenNCC, " +
                 "sp.MaSP, " +
                 "sp.TenSP, " +
                 "ctpn.SoLuong, " +
                 "ctpn.GiaNhap, " +
                 "ctpn.ThanhTien " +
                 "FROM PhieuNhap pn " +
                 "JOIN CTPhieuNhap ctpn ON pn.MaPN = ctpn.MaPN " +
                 "JOIN SanPham sp ON ctpn.ID = sp.ID " +
                 "JOIN NhaCungCap ncc ON pn.MaNCC = ncc.MaNCC " +
                 "WHERE MONTH(pn.NgayLap) = ? AND YEAR(pn.NgayLap) = ? " +
                 "ORDER BY pn.NgayLap DESC";
    
    try(PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, thang);
        ps.setInt(2, nam);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("NgayNhap", rs.getString("NgayNhap"));
            row.put("MaPN", rs.getInt("MaPN"));
            row.put("TenNCC", rs.getString("TenNCC"));
            row.put("MaSP", rs.getString("MaSP"));
            row.put("TenSP", rs.getString("TenSP"));
            row.put("SoLuong", rs.getInt("SoLuong"));
            row.put("GiaNhap", rs.getDouble("GiaNhap"));
            row.put("ThanhTien", rs.getDouble("ThanhTien"));
            result.add(row);
        }
    } catch(SQLException e) {
        e.printStackTrace();
    }
    return result;
}

public ArrayList<Map<String, Object>> thongKeLoiNhuan(int thangBatDau, int thangKetThuc, int nam, boolean isQuy, boolean isThang) {
    ArrayList<Map<String, Object>> result = new ArrayList<>();
    
    String sql;
    if(isThang) {
        // Thống kê theo tháng
        sql = "SELECT " +
              "FORMAT(hd.NgayLap, 'dd/MM/yyyy') as ThoiGian, " +
              "SUM(cthd.SoLuong) as SoLuong, " +
              "SUM(cthd.ThanhTien) as DoanhThu, " +
              "SUM(cthd.SoLuong * ctpn.GiaNhap) as GiaVon, " +
              "SUM(cthd.ThanhTien - (cthd.SoLuong * ctpn.GiaNhap)) as LoiNhuan " +
              "FROM HoaDon hd " +
              "JOIN CTHoaDon cthd ON hd.MaHD = cthd.MaHD " +
              "LEFT JOIN SanPham sp ON cthd.ID = sp.ID " +
              "LEFT JOIN ( " +
              "    SELECT ctpn.ID, ctpn.GiaNhap " +
              "    FROM CTPhieuNhap ctpn " +
              "    JOIN PhieuNhap pn ON ctpn.MaPN = pn.MaPN " +
              ") ctpn ON sp.ID = ctpn.ID " +
              "WHERE MONTH(hd.NgayLap) = ? AND YEAR(hd.NgayLap) = ? " +
              "AND hd.TrangThai = N'Đã Thanh Toán' " +
              "GROUP BY hd.NgayLap " +
              "ORDER BY hd.NgayLap";
    } else if(isQuy) {
        // Thống kê theo quý (gộp 3 tháng)
        sql = "SELECT " +
              "N'Quý ' + CAST(DATEPART(QUARTER, hd.NgayLap) AS VARCHAR) + '/' + CAST(YEAR(hd.NgayLap) AS VARCHAR) as ThoiGian, " +
              "SUM(cthd.SoLuong) as SoLuong, " +
              "SUM(cthd.ThanhTien) as DoanhThu, " +
              "SUM(cthd.SoLuong * ctpn.GiaNhap) as GiaVon, " +
              "SUM(cthd.ThanhTien - (cthd.SoLuong * ctpn.GiaNhap)) as LoiNhuan " +
              "FROM HoaDon hd " +
              "JOIN CTHoaDon cthd ON hd.MaHD = cthd.MaHD " +
              "LEFT JOIN SanPham sp ON cthd.ID = sp.ID " +
              "LEFT JOIN ( " +
              "    SELECT ctpn.ID, ctpn.GiaNhap " +
              "    FROM CTPhieuNhap ctpn " +
              "    JOIN PhieuNhap pn ON ctpn.MaPN = pn.MaPN " +
              ") ctpn ON sp.ID = ctpn.ID " +
              "WHERE MONTH(hd.NgayLap) BETWEEN ? AND ? AND YEAR(hd.NgayLap) = ? " +
              "AND hd.TrangThai = N'Đã Thanh Toán' " +
              "GROUP BY DATEPART(QUARTER, hd.NgayLap), YEAR(hd.NgayLap) " +
              "ORDER BY YEAR(hd.NgayLap), DATEPART(QUARTER, hd.NgayLap)";
    } else {
        // Thống kê theo năm
        sql = "SELECT " +
              "CAST(YEAR(hd.NgayLap) AS VARCHAR) as ThoiGian, " +
              "SUM(cthd.SoLuong) as SoLuong, " +
              "SUM(cthd.ThanhTien) as DoanhThu, " +
              "SUM(cthd.SoLuong * ctpn.GiaNhap) as GiaVon, " +
              "SUM(cthd.ThanhTien - (cthd.SoLuong * ctpn.GiaNhap)) as LoiNhuan " +
              "FROM HoaDon hd " +
              "JOIN CTHoaDon cthd ON hd.MaHD = cthd.MaHD " +
              "LEFT JOIN SanPham sp ON cthd.ID = sp.ID " +
              "LEFT JOIN ( " +
              "    SELECT ctpn.ID, ctpn.GiaNhap " +
              "    FROM CTPhieuNhap ctpn " +
              "    JOIN PhieuNhap pn ON ctpn.MaPN = pn.MaPN " +
              ") ctpn ON sp.ID = ctpn.ID " +
              "WHERE YEAR(hd.NgayLap) = ? " +
              "AND hd.TrangThai = N'Đã Thanh Toán' " +
              "GROUP BY YEAR(hd.NgayLap) " +
              "ORDER BY YEAR(hd.NgayLap)";
    }
    
    try(PreparedStatement ps = conn.prepareStatement(sql)) {
        if(isThang) {
            ps.setInt(1, thangBatDau);
            ps.setInt(2, nam);
        } else if(isQuy) {
            ps.setInt(1, thangBatDau);
            ps.setInt(2, thangKetThuc);
            ps.setInt(3, nam);
        } else {
            ps.setInt(1, nam);
        }
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("ThoiGian", rs.getString("ThoiGian"));
            row.put("SoLuong", rs.getInt("SoLuong"));
            row.put("DoanhThu", rs.getDouble("DoanhThu"));
            row.put("GiaVon", rs.getDouble("GiaVon"));
            row.put("LoiNhuan", rs.getDouble("LoiNhuan"));
            result.add(row);
        }
    } catch(SQLException e) {
        e.printStackTrace();
    }
    return result;
}

// Thêm phương thức thống kê số lượng sản phẩm đã xuất chi tiết theo từng sản phẩm
public ArrayList<Map<String, Object>> thongKeSanPhamDaXuat(int thang, int quy, int nam) {
    ArrayList<Map<String, Object>> result = new ArrayList<>();
    
    int thangBatDau = thang;
    int thangKetThuc = thang;
    
    if(quy > 0) {
        switch(quy) {
            case 1: thangBatDau = 1; thangKetThuc = 3; break;
            case 2: thangBatDau = 4; thangKetThuc = 6; break;
            case 3: thangBatDau = 7; thangKetThuc = 9; break;
            case 4: thangBatDau = 10; thangKetThuc = 12; break;
        }
    }
    
    String sql = "SELECT " +
                 "sp.MaSP, " +
                 "sp.TenSP, " +
                 "sp.ThuongHieu, " +
                 "SUM(cthd.SoLuong) as SoLuongDaBan, " +
                 "SUM(cthd.ThanhTien) as DoanhThu, " +
                 "AVG(cthd.GiaBan) as GiaBanTrungBinh " +
                 "FROM HoaDon hd " +
                 "JOIN CTHoaDon cthd ON hd.MaHD = cthd.MaHD " +
                 "JOIN SanPham sp ON cthd.ID = sp.ID " +
                 "WHERE MONTH(hd.NgayLap) BETWEEN ? AND ? " +
                 "AND YEAR(hd.NgayLap) = ? " +
                 "AND hd.TrangThai = N'Đã Thanh Toán' " +
                 "GROUP BY sp.MaSP, sp.TenSP, sp.ThuongHieu " +
                 "ORDER BY SoLuongDaBan DESC";
    
    try(PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, thangBatDau);
        ps.setInt(2, thangKetThuc);
        ps.setInt(3, nam);
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            Map<String, Object> row = new HashMap<>();
            row.put("MaSP", rs.getString("MaSP"));
            row.put("TenSP", rs.getString("TenSP"));
            row.put("ThuongHieu", rs.getString("ThuongHieu"));
            row.put("SoLuongDaBan", rs.getInt("SoLuongDaBan"));
            row.put("DoanhThu", rs.getDouble("DoanhThu"));
            row.put("GiaBanTrungBinh", rs.getDouble("GiaBanTrungBinh"));
            result.add(row);
        }
    } catch(SQLException e) {
        e.printStackTrace();
    }
    return result;
}

public ArrayList<Map<String, Object>> thongKeKinhDoanhTheoSanPham(int thangBatDau, int thangKetThuc, int nam) {
    ArrayList<Map<String, Object>> result = new ArrayList<>();
    
    String sql = "SELECT " +
                 "sp.MaSP, " +
                 "sp.TenSP, " +
                 "SUM(cthd.SoLuong) as SoLuongBan, " +
                 "cthd.GiaBan, " +
                 "SUM(cthd.ThanhTien) as DoanhThu, " +
                 "SUM(cthd.SoLuong * COALESCE(ctpn.GiaNhap, 0)) as TongGiaNhap " +
                 "FROM HoaDon hd " +
                 "INNER JOIN CTHoaDon cthd ON hd.MaHD = cthd.MaHD " +
                 "INNER JOIN SanPham sp ON cthd.ID = sp.ID " +
                 "LEFT JOIN CTPhieuNhap ctpn ON sp.ID = ctpn.ID " +
                 "LEFT JOIN PhieuNhap pn ON ctpn.MaPN = pn.MaPN " +
                 "WHERE MONTH(hd.NgayLap) BETWEEN ? AND ? " +
                 "AND YEAR(hd.NgayLap) = ? " +
                 "AND hd.TrangThai = N'Đã Thanh Toán' " +
                 "GROUP BY sp.MaSP, sp.TenSP, cthd.GiaBan " +
                 "ORDER BY SoLuongBan DESC";
    
    try(PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, thangBatDau);
        ps.setInt(2, thangKetThuc);
        ps.setInt(3, nam);
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()) {
            double doanhThu = rs.getDouble("DoanhThu");
            double tongGiaNhap = rs.getDouble("TongGiaNhap");
            double loiNhuan = doanhThu - tongGiaNhap;
            
            Map<String, Object> row = new HashMap<>();
            row.put("MaSP", rs.getString("MaSP"));
            row.put("TenSP", rs.getString("TenSP"));
            row.put("SoLuongBan", rs.getInt("SoLuongBan"));
            row.put("GiaBan", rs.getDouble("GiaBan"));
            row.put("DoanhThu", doanhThu);
            row.put("LoiNhuan", loiNhuan);
            result.add(row);
        }
    } catch(SQLException e) {
        e.printStackTrace();
    }
    return result;
}
}