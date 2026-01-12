package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Date;

public class ThongKeDAO {

    public ArrayList<Object[]> getTopSanPham(Date tuNgay, Date denNgay) {
        ArrayList<Object[]> danhSach = new ArrayList<>();
        String sql = "SELECT p.ID, p.TenSP, SUM(ct.soLuong) AS TongSoLuong, SUM(ct.thanhTien) AS TongGiaTri " +
                     "FROM CTHoaDon ct " + 
                     "JOIN SanPham p ON ct.ID = p.ID " + 
                     "JOIN HoaDon hd ON ct.maHD = hd.maHD " + 
                     "WHERE hd.ngayLap BETWEEN ? AND ? " + 
                     "GROUP BY p.ID, p.TenSP " +
                     "ORDER BY TongGiaTri DESC";
        
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, tuNgay, denNgay);
            while (rs != null && rs.next()) {
                danhSach.add(new Object[]{
                    rs.getInt("ID"),
                    rs.getString("TenSP"),
                    rs.getInt("TongSoLuong"),
                    rs.getDouble("TongGiaTri") 
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs);
        }
        return danhSach;
    }

    public ArrayList<Object[]> getTopKhachHang(Date tuNgay, Date denNgay) {
        ArrayList<Object[]> danhSach = new ArrayList<>();
        String sql = "SELECT kh.maKH, CONCAT(kh.ho, ' ', kh.ten) AS HoTen, COUNT(hd.maHD) AS TongSoHoaDon, SUM(hd.tongTien) AS TongChiTieu " +
                     "FROM HoaDon hd " + 
                     "JOIN KhachHang kh ON hd.maKH = kh.maKH " + 
                     "WHERE hd.ngayLap BETWEEN ? AND ? " + 
                     "GROUP BY kh.maKH, kh.ho, kh.ten " + 
                     "ORDER BY TongChiTieu DESC";
        
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, tuNgay, denNgay);
            while (rs != null && rs.next()) {
                danhSach.add(new Object[]{
                    rs.getInt("maKH"),
                    rs.getString("HoTen"),
                    rs.getInt("TongSoHoaDon"),
                    rs.getDouble("TongChiTieu") 
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs);
        }
        return danhSach;
    }

    public ArrayList<Object[]> getTopNhanVien(Date tuNgay, Date denNgay) {
        ArrayList<Object[]> danhSach = new ArrayList<>();
        String sql = "SELECT nv.maNV, CONCAT(nv.ho, ' ', nv.ten) AS HoTen, COUNT(hd.maHD) AS TongSoHoaDon, SUM(hd.tongTien) AS TongDoanhSo " +
                     "FROM HoaDon hd " + 
                     "JOIN NhanVien nv ON hd.maNV = nv.maNV " + 
                     "WHERE hd.ngayLap BETWEEN ? AND ? " + 
                     "GROUP BY nv.maNV, nv.ho, nv.ten " + 
                     "ORDER BY TongDoanhSo DESC";
        
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, tuNgay, denNgay);
            while (rs != null && rs.next()) {
                danhSach.add(new Object[]{
                    rs.getInt("maNV"),
                    rs.getString("HoTen"),
                    rs.getInt("TongSoHoaDon"),
                    rs.getDouble("TongDoanhSo") 
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs);
        }
        return danhSach;
    }
    
    public double getTongDoanhThu(Date tuNgay, Date denNgay) {
        double tongDoanhThu = 0;
        String sql = "SELECT SUM(tongTien) AS Tong " +
                     "FROM HoaDon " + 
                     "WHERE ngayLap BETWEEN ? AND ?";
        
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, tuNgay, denNgay);
            if (rs != null && rs.next()) {
                tongDoanhThu = rs.getDouble("Tong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(rs);
        }
        return tongDoanhThu;
    }

    private void closeResources(ResultSet rs) {
        if (rs != null) {
            try {
                Statement stmt = rs.getStatement();
                Connection conn = stmt.getConnection();
                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}