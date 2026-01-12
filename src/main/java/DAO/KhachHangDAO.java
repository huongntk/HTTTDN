package DAO;

import DTO.KhachHangDTO;

import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class KhachHangDAO {

    
    public ArrayList<KhachHangDTO> layDanhSachKhachHang() {
        ArrayList<KhachHangDTO> danhSach = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        ResultSet rs = null;
        
        try {
          
            rs = DataProvider.executeQuery(sql);

            
            while (rs != null && rs.next()) {
                KhachHangDTO kh = new KhachHangDTO(
                        rs.getInt("MaKH"),
                        rs.getString("Ho"),
                        rs.getString("Ten"),
                        rs.getString("GioiTinh"),
                        rs.getString("SoDienThoai"),
                        rs.getInt("TongChiTieu"),
                        rs.getBoolean("TinhTrang")
                );
                danhSach.add(kh);
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

  
    public boolean themKhachHang(KhachHangDTO kh) {
        String sql = "INSERT INTO KhachHang (Ho, Ten, GioiTinh, SoDienThoai, TinhTrang) VALUES (?, ?, ?, ?, ?)";
        
        int rows = DataProvider.executeUpdate(sql,
                kh.getHo(),
                kh.getTen(),
                kh.getGioiTinh(),
                kh.getSoDienThoai(),
                kh.isTinhTrang()
        );
        
        return rows > 0;
    }


    public boolean suaKhachHang(KhachHangDTO kh) {
        String sql = "UPDATE KhachHang SET Ho = ?, Ten = ?, GioiTinh = ?, SoDienThoai = ?, TinhTrang = ? WHERE MaKH = ?";
        
        int rows = DataProvider.executeUpdate(sql,
                kh.getHo(),
                kh.getTen(),
                kh.getGioiTinh(),
                kh.getSoDienThoai(),
                kh.isTinhTrang(),
                kh.getMaKH() 
        );
        
        return rows > 0;
    }


    public boolean xoaKhachHang(int maKH) {
        String sql = "UPDATE KhachHang SET TinhTrang = 0 WHERE MaKH = ?";
        
        int rows = DataProvider.executeUpdate(sql, maKH);
        
        return rows > 0;
    }

  
    public ArrayList<KhachHangDTO> timKiemKhachHang(String tuKhoa) {
        ArrayList<KhachHangDTO> danhSach = new ArrayList<>();
        
        String sql = "SELECT * FROM KhachHang WHERE CAST(MaKH AS VARCHAR) LIKE ? OR CONCAT(Ho, ' ', Ten) LIKE ? OR SoDienThoai LIKE ?";
        String keyword = "%" + tuKhoa + "%";
        ResultSet rs = null;

        try {
            
            rs = DataProvider.executeQuery(sql, keyword, keyword, keyword);

            
            while (rs != null && rs.next()) {
                KhachHangDTO kh = new KhachHangDTO(
                        rs.getInt("MaKH"),
                        rs.getString("Ho"),
                        rs.getString("Ten"),
                        rs.getString("GioiTinh"),
                        rs.getString("SoDienThoai"),
                        rs.getInt("TongChiTieu"),
                        rs.getBoolean("TinhTrang")
                );
                danhSach.add(kh);
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
    
    public String getTenKhachHang(int maKH) {
        if (maKH == 0) return "Khách vãng lai"; // Xử lý trường hợp MaKH = 0
        String sql = "SELECT Ho, Ten FROM KhachHang WHERE MaKH = ?";
        String tenKH = null;
        String ho = null;
        String ten = null;
        try (ResultSet rs = DataProvider.executeQuery(sql, maKH)) {
            if (rs.next()) {
                ho = rs.getString("Ho");
                ten = rs.getString("Ten");
                tenKH = ho + "" + ten;
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy tên KH: " + e.getMessage());
        }
        return tenKH != null ? tenKH : "Không tìm thấy";
    }
}