package DAO;

import DTO.TaiKhoan;
import UTIL.DBConnect;
import java.sql.*;

public class TaiKhoanDAO {

    public TaiKhoan login(String user, String pass) {
        TaiKhoan tk = null;
        String sql = "SELECT * FROM TaiKhoan WHERE TaiKhoan=? AND MatKhau=? AND TrangThai=1";
        try (ResultSet rs = DataProvider.executeQuery(sql,user, pass)) {


            if (rs.next()) {
                tk = new TaiKhoan(
                    rs.getInt("MaNV"),
                    rs.getString("TaiKhoan"),
                    rs.getString("MatKhau"),
                    rs.getString("Quyen"),
                    rs.getInt("TrangThai")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tk;
    }
    
    public TaiKhoan getTaiKhoanByUsername(String username) {
        String sql = "SELECT * FROM TaiKhoan WHERE taiKhoan = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, username)) {
            
            if (rs.next()) {
                return new TaiKhoan(
                    rs.getInt("maNV"),
                    rs.getString("taiKhoan"),
                    rs.getString("matKhau"),
                    rs.getString("quyen"),
                    rs.getInt("trangThai")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean doiMatKhau(String username, String newPass) {
    String sql = "UPDATE TaiKhoan SET matKhau = ? WHERE taiKhoan = ?";
    try {
            int result = DataProvider.executeUpdate(sql, newPass, username);
            return result >0;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}


}
