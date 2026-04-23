package DAO;

import DTO.KhuyenMai;
import DAO.DataProvider;
import UTIL.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class KhuyenMaiDAO {
    
    public ArrayList<KhuyenMai> layDanhSachKhuyenMai() {
        ArrayList<KhuyenMai> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM GiamGia";
        ResultSet rs = null;
        
        try {
          
            rs = DataProvider.executeQuery(sql);

            while (rs != null && rs.next()) {
                KhuyenMai km = new KhuyenMai(
                        rs.getInt("MaGG"),
                        rs.getString("TenGG"),
                        rs.getInt("PhanTramGiam"),
                        rs.getInt("DieuKien"),
                        rs.getDate("NgayBD"),
                        rs.getDate("NgayKT"),
                        rs.getBoolean("TinhTrang")
                );
                danhSach.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
         
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
        return danhSach;
    }
    
    public boolean themKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO GiamGia (TenGG, PhanTramGiam, DieuKien, NgayBD, NgayKT, TinhTrang) VALUES (?, ?, ?, ?, ?, ?)";
    
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, km.getTenGG());
            ps.setInt(2, km.getPhanTramGiam());
            ps.setInt(3, km.getDieuKien());
            ps.setDate(4, new java.sql.Date(km.getNgayBD().getTime()));
            ps.setDate(5, new java.sql.Date(km.getNgayKT().getTime()));
            ps.setBoolean(6, km.isTinhTrang());

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra console để debug
            return false;
        }
    }

    public boolean suaKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE GiamGia SET TenGG = ?, PhanTramGiam = ?, DieuKien = ?, NgayBD = ?, NgayKT = ?, TinhTrang = ? WHERE MaGG = ?";
        
        int rows = DataProvider.executeUpdate(sql,
                km.getTenGG(),
                km.getPhanTramGiam(),
                km.getDieuKien(),
                new java.sql.Date(km.getNgayBD().getTime()),
                km.getNgayKT(),
                km.isTinhTrang(),
                km.getMaGG() 
        );
        
        return rows > 0;
    }
    
    public boolean xoaKhuyenMai(int maGG) {
        String sql = "UPDATE GiamGia SET TinhTrang = 0 WHERE MaGG = ?";
        
        int rows = DataProvider.executeUpdate(sql, maGG);
        
        return rows > 0;
    }
    
    public ArrayList<KhuyenMai> timKiemKhuyenMai(String tuKhoa) {
        ArrayList<KhuyenMai> danhSach = new ArrayList<>();
    
        // SQL chuẩn: CAST các cột số sang VARCHAR để LIKE
        String sql = "SELECT * FROM GiamGia " +
                     "WHERE CAST(MaGG AS VARCHAR) LIKE ? " +
                     "OR TenGG LIKE ? " +
                     "OR CAST(DieuKien AS VARCHAR) LIKE ?";
    
        String keyword = "%" + tuKhoa + "%";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
        
            // Truyền tham số
            stmt.setString(1, keyword);
            stmt.setString(2, keyword);
            stmt.setString(3, keyword);

            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                KhuyenMai km = new KhuyenMai(
                    rs.getInt("MaGG"),
                    rs.getString("TenGG"),
                    rs.getInt("PhanTramGiam"),
                    rs.getInt("DieuKien"),
                    rs.getDate("NgayBD"),
                    rs.getDate("NgayKT"),
                    rs.getBoolean("TinhTrang")
                );
                danhSach.add(km);
            }   

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return danhSach;
    }
    
    public String getTenMaGiamGia(int maGG) {
        if (maGG == 0) {
            return "Không áp dụng";
        }
        // Xử lý trường hợp MaGG = 0 
        String sql = "SELECT TenGG FROM GiamGia WHERE MaGG = ? AND TinhTrang = 1 ";
        String tenGG = null;
        
        try (ResultSet rs = DataProvider.executeQuery(sql, maGG)) {
            
            if (rs.next()) {
                tenGG = rs.getString("TenGG"); 
            } 
        } catch (SQLException e) {
            System.err.println("Lỗi lấy tên Mã GG: " + e.getMessage());
        }
        return tenGG != null ? tenGG : "Không hợp lệ";
    }
    
    public double getPhanTramGiam (int maGG){
        if (maGG <= 0){
            return 0.0;
        }
        String sql = "Select PhanTramGiam FROM GiamGia WHERE MaGG= ? AND TinhTrang = 1";
        double phanTram = 0.0;
        try (ResultSet rs = DataProvider.executeQuery(sql, maGG)){
            if (rs.next()){
                phanTram = rs.getDouble("PhanTramGiam");
            }
        } catch (SQLException e){
            System.err.println("Lỗi khi lấy phần trăm giảm giá MaGG=" + maGG + ": " + e.getMessage());
        }
        return phanTram;
    }
}