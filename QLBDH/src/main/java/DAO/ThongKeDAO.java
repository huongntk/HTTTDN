package DAO;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
}