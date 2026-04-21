package DAO;

import DTO.Loai;
import UTIL.DBConnect;
import java.sql.*;
import java.util.ArrayList;

public class LoaiDAO {
    public ArrayList<Loai> getAll() {
        ArrayList<Loai> list = new ArrayList<>();
        String sql = "SELECT MaLoai, TenLoai FROM loai ORDER BY MaLoai";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Loai(rs.getInt("MaLoai"), rs.getString("TenLoai")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
