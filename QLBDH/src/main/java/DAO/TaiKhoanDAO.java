package DAO;

import DTO.TaiKhoan;
import UTIL.DBConnect;
import java.sql.*;
import java.util.ArrayList;

public class TaiKhoanDAO {

    public TaiKhoan login(String user, String pass) {
        TaiKhoan tk = null;
        String sql = "SELECT tk.*,(nv.Ho + ' ' + nv.Ten) as TenNV " +
                 "FROM TaiKhoan tk " +
                 "INNER JOIN NhanVien nv ON tk.MaNV = nv.MaNV " +
                 "WHERE tk.TaiKhoan = ? AND tk.MatKhau = ? AND tk.TrangThai = 1";
        System.out.println("Login with: [" + user + "] / [" + pass + "]");
        try (ResultSet rs = DataProvider.executeQuery(sql,user, pass)) {


            if (rs.next()) {
                tk = new TaiKhoan();
                    tk.setMaNV(rs.getInt("MaNV"));
                    tk.setTenNV(rs.getString("TenNV"));
                    tk.setTaiKhoan(rs.getString("TaiKhoan"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setMaQuyen(rs.getString("MaQuyen"));
                    tk.setTrangThai(rs.getInt("TrangThai"));
                return tk;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tk;
    }
    
    public TaiKhoan getTaiKhoanByUsername(String username) {
        String sql = "SELECT * FROM TaiKhoan WHERE TaiKhoan = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, username)) {
            
            if (rs.next()) {
                return new TaiKhoan(
                    rs.getInt("MaNV"),
                    rs.getString("TaiKhoan"),
                    rs.getString("MatKhau"),
                    rs.getString("MaQuyen"),
                    rs.getInt("TrangThai")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean doiMatKhau(String username, String newPass) {
    String sql = "UPDATE TaiKhoan SET MatKhau = ? WHERE TaiKhoan = ?";
    try {
            int result = DataProvider.executeUpdate(sql, newPass, username);
            return result >0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}
public ArrayList<TaiKhoan> layDanhSachTaiKhoan() {
        ArrayList<TaiKhoan> list = new ArrayList<>();
        String sql = "SELECT MaNV, TaiKhoan, MatKhau, MaQuyen, TrangThai FROM TaiKhoan";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TaiKhoan tk = new TaiKhoan();
                tk.setMaNV(rs.getInt("MaNV"));
                tk.setTaiKhoan(rs.getString("TaiKhoan"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setMaQuyen(rs.getString("MaQuyen"));
                
                tk.setTrangThai(rs.getInt("TrangThai"));
                list.add(tk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy tài khoản theo mã
    public TaiKhoan layTaiKhoanTheoMa(int MaNV) {
        TaiKhoan tk = null;
        String sql = "SELECT MaNV, TaiKhoan, MatKhau, MaQuyen, TrangThai FROM TaiKhoan WHERE MaNV = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, MaNV);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tk = new TaiKhoan();
                    tk.setMaNV(rs.getInt("MaNV"));
                    tk.setTaiKhoan(rs.getString("TaiKhoan"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setMaQuyen(rs.getString("MaQuyen"));
                    
                    tk.setTrangThai(rs.getInt("TrangThai"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tk;
    }

    // Lấy tài khoản theo tên đăng nhập (dùng để kiểm tra trùng)
    public TaiKhoan layTaiKhoanTheoTenDangNhap(String tenDangNhap) {
        TaiKhoan tk = null;
        String sql = "SELECT MaNV, TaiKhoan, MatKhau, MaQuyen, TrangThai FROM TaiKhoan WHERE TaiKhoan = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tenDangNhap);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tk = new TaiKhoan();
                    tk.setMaNV(rs.getInt("MaNV"));
                    tk.setTaiKhoan(rs.getString("TaiKhoan"));
                    tk.setMatKhau(rs.getString("MatKhau"));
                    tk.setMaQuyen(rs.getString("MaQuyen"));            
                    tk.setTrangThai(rs.getInt("TrangThai"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tk;
    }

    // Thêm tài khoản mới
    public boolean themTaiKhoan(TaiKhoan tk) {
        String sql = "INSERT INTO TaiKhoan(MaNV, TaiKhoan, MatKhau, MaQuyen, TrangThai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tk.getMaNV());
            ps.setString(2, tk.getTaiKhoan());
            ps.setString(3, tk.getMatKhau());
            ps.setString(4, tk.getMaQuyen());
            ps.setInt(5, tk.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sửa tài khoản (có thể cập nhật mật khẩu hoặc không)
    public boolean suaTaiKhoan(TaiKhoan tk, boolean updatePassword) {
        String sql;
        if (updatePassword) {
            sql = "UPDATE TaiKhoan SET TaiKhoan = ?, MatKhau = ?, MaQuyen = ?,  TrangThai = ? WHERE MaNV = ?";
        } else {
            sql = "UPDATE TaiKhoan SET TaiKhoan = ?, MaQuyen = ?,  TrangThai = ? WHERE MaNV = ?";
        }
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = 1;
            ps.setString(i++, tk.getTaiKhoan());
            if (updatePassword) {
                ps.setString(i++, tk.getMatKhau());
            }
            ps.setString(i++, tk.getMaQuyen());
            ps.setInt(i++, tk.getMaNV());
            ps.setInt(i++, tk.getTrangThai());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa mềm (vô hiệu hóa)
    public boolean xoaTaiKhoan(int maNV) {
        String sql = "UPDATE TaiKhoan SET TrangThai = 0 WHERE MaNV = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
