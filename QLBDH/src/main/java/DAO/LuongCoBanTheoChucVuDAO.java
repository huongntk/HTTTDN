package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LuongCoBanTheoChucVuDAO {
    
    /**
     * Lấy lương cơ bản theo mã chức vụ
     */
    public double getLuongCoBan(String maCV) {
        String sql = "SELECT LuongCoBan FROM LuongCoBanTheoChucVu WHERE MaCV = ?";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, maCV);
            if (rs.next()) {
                return rs.getDouble("LuongCoBan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return 0;
    }
    
    /**
     * Lấy phụ cấp chức vụ theo mã chức vụ
     */
    public double getPhuCap(String maCV) {
        String sql = "SELECT PhuCapChucVu FROM LuongCoBanTheoChucVu WHERE MaCV = ?";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, maCV);
            if (rs.next()) {
                return rs.getDouble("PhuCapChucVu");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return 0;
    }
    
    /**
     * Lấy toàn bộ dữ liệu lương cơ bản và phụ cấp
     * @return Map<MaCV, double[]{luongCoBan, phuCap}>
     */
    public Map<String, double[]> getAll() {
        Map<String, double[]> map = new HashMap<>();
        String sql = "SELECT MaCV, LuongCoBan, PhuCapChucVu FROM LuongCoBanTheoChucVu";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql);
            while (rs.next()) {
                String maCV = rs.getString("MaCV");
                double luong = rs.getDouble("LuongCoBan");
                double phuCap = rs.getDouble("PhuCapChucVu");
                map.put(maCV, new double[]{luong, phuCap});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return map;
    }
    
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.getStatement().close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Thêm các phương thức sau vào DAO

public boolean update(String maCV, double luongCoBan, double phuCap) {
    String sql = "UPDATE LuongCoBanTheoChucVu SET LuongCoBan = ?, PhuCapChucVu = ? WHERE MaCV = ?";
    try {
        int rows = DataProvider.executeUpdate(sql, luongCoBan, phuCap, maCV);
        return rows > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

    public ArrayList<String> getAllChucVu() {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT MaCV FROM LuongCoBanTheoChucVu";
        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("MaCV"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}