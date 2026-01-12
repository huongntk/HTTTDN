/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;
import DTO.PhanQuyen;
import java.sql.*;
import java.util.ArrayList;
public class PhanQuyenDAO {

    public PhanQuyen getPhanQuyen(String quyen) {
        PhanQuyen pq = null;
        String sql = "SELECT * FROM PhanQuyen WHERE Quyen = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, quyen)) {

            if (rs.next()) {
                pq = new PhanQuyen();
                pq.setQLNhapHang(rs.getBoolean("QLNhapHang"));
                pq.setQLSanPham(rs.getBoolean("QLSanPham"));
                pq.setQLNhanVien(rs.getBoolean("QLNhanVien"));
                pq.setQLKhachHang(rs.getBoolean("QLKhachHang"));
                pq.setQLThongKe(rs.getBoolean("QLThongKe"));
                pq.setQLKhuyenMai(rs.getBoolean("QLKhuyenMai"));
                pq.setQLPhanQuyen(rs.getBoolean("QLPhanQuyen"));
                pq.setQLBanHang(rs.getBoolean("QLBanHang"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pq;
    }

     public ResultSet getDanhSachQuyen() {
        String sql = "SELECT Quyen FROM PhanQuyen";
        return DataProvider.executeQuery(sql);
    }
    
     // Thêm mới hoặc cập nhật quyền
    public boolean insertQuyen(DTO.PhanQuyen pq) {
        
            // 1. Kiểm tra Quyền đã tồn tại chưa
        if (getPhanQuyen(pq.getQuyen()) != null) {
            // Quyền đã tồn tại (Ví dụ: tên "admin" đã có)
            return false; 
        }

        // Câu lệnh INSERT INTO (Bao gồm 8 cột quyền + 1 cột Quyen)
        String sql = "INSERT INTO PhanQuyen (Quyen, QLBanHang, QLNhapHang, QLNhanVien, QLSanPham, QLKhachHang, QLKhuyenMai, QLPhanQuyen, QLThongKe) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            // Thực hiện INSERT
            int rows = DataProvider.executeUpdate(sql,
                pq.getQuyen(),            // Tham số 1: Tên Quyen
                pq.isQLBanHang(),         // Tham số 2
                pq.isQLSanPham(),        // Tham số 3
                pq.isQLKhachHang(),        // Tham số 4
                pq.isQLNhanVien(),         // Tham số 5
                pq.isQLNhapHang(),       // Tham số 6
                pq.isQLKhuyenMai(),       // Tham số 7
                pq.isQLPhanQuyen(),       // Tham số 8
                pq.isQLThongKe()          // Tham số 9
            );
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateQuyen(String Quyen, boolean qlBanHang,boolean qlSanPham, boolean qlKhachHang, boolean qlNhanVien,
                                        boolean qlNhapHang, boolean qlKhuyenMai, boolean qlPhanQuyen, boolean qlThongKe) {
        try {
            String sql = "UPDATE PhanQuyen SET QLBanHang=?, QLNhapHang=?, QLNhanVien=?, QLSanPham=?, QLKhachHang=?, QlKhuyenMai=?, QLPhanQuyen=?, QLThongKe=? WHERE Quyen=?";
            return DataProvider.executeUpdate(sql, qlBanHang, qlNhapHang, qlNhanVien, qlSanPham, qlKhachHang, qlKhuyenMai, qlPhanQuyen, qlThongKe, Quyen) > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean deleteQuyen(String Quyen) {
        String sql = "UPDATE PhanQuyen SET QLBanHang=0, QLNhapHang=0, QLNhanVien=0, QLSanPham=0, QLKhachHang=0, QlKhuyenMai=0, QLPhanQuyen=0, QLThongKe=0 WHERE Quyen=?";
        try {
            int rows = DataProvider.executeUpdate(sql, Quyen);
            return rows >0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


//    // Thêm quyền
//    public boolean insertQuyen(PhanQuyen pq) {
//        String sql = "INSERT PhanQuyen SET QLNhanVien=?, QLSanPham=?, QLKhachHang=?, QLBanHang=?, QLNhapHang=?, QLThongKe=?) WHERE QUYEN=?";
//        try {
//            int rows = DataProvider.executeUpdate(sql,
//              
//                pq.isQLNhanVien(),
//                pq.isQLSanPham(),
//                pq.isQLKhachHang(),
//                pq.isQLBanHang(),
//                pq.isQLNhapHang(),
//                pq.isQLThongKe()     
//            );
//            return rows >0;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
    
    
}
