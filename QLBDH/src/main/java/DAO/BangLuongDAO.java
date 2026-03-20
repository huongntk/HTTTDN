package DAO;


import DAO.DataProvider;
import UTIL.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author PC
 */
public class BangLuongDAO {
    public ArrayList<BangLuong> getBangLuongTheoThang(int thang, int nam) {
        ArrayList<BangLuong> list = new ArrayList<>();
        String sql = "{CALL sp_TinhLuongThang(?, ?)}"; // Gọi Stored Procedure cho chuyên nghiệp

        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                BangLuong bl = new BangLuong();
                bl.setMaNV(rs.getString("MaNV"));
                bl.setTenNV(rs.getString("TenNV"));
                bl.setSoNgayLam(rs.getInt("SoNgayLamViec"));
                bl.setLuongThucNhan(rs.getDouble("LuongThucNhan"));
                list.add(bl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
