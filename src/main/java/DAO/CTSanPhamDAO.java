package DAO;

import DTO.ProductDetail;
import UTIL.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CTSanPhamDAO {

    public ProductDetail getByProductId(int productId) {
       ProductDetail detail = null;
    String sql = "SELECT * FROM CTSanPham WHERE ID = ?";  

    try (ResultSet rs = DataProvider.executeQuery(sql, productId)) {
            if (rs.next()) {
                detail = new ProductDetail();
                detail.setMaCTSP(rs.getInt("MaCTSP"));
                detail.setID(rs.getInt("ID"));
                detail.setDuongKinhMat(rs.getString("DuongKinhMat"));
                detail.setDoDayMat(rs.getString("DoDayMat"));
                detail.setChatLieuVo(rs.getString("ChatLieuVo"));
                detail.setChatLieuDay(rs.getString("ChatLieuDay"));
                detail.setKieuMat(rs.getString("KieuMat"));
                detail.setKinh(rs.getString("Kinh"));
                detail.setBoMay(rs.getString("BoMay"));
                detail.setNangLuongCo(rs.getString("NangLuongCo"));
                detail.setThoiGianTruCoc(rs.getString("ThoiGianTruCoc"));
                detail.setDoChiuNuoc(rs.getString("DoChiuNuoc"));
                detail.setChucNangKhac(rs.getString("ChucNangKhac"));
                detail.setBaoHanh(rs.getString("BaoHanh"));
                detail.setMauMatSo(rs.getString("MauMatSo"));
                detail.setTrongLuong(rs.getFloat("TrongLuong"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy chi tiết sản phẩm ID = " + productId);
            e.printStackTrace();
        }
        return detail;
    }
    public boolean insertCTSanPham(ProductDetail detail) {
    String sql = "INSERT INTO CTSanPham (DuongKinhMat, DoDayMat, ChatLieuVo, ChatLieuDay, "
               + "KieuMat, Kinh, BoMay, NangLuongCo, ThoiGianTruCoc, DoChiuNuoc, "
               + "ChucNangKhac, BaoHanh, MauMatSo, TrongLuong, ID) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnect.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, detail.getDuongKinhMat());
        ps.setString(2, detail.getDoDayMat());
        ps.setString(3, detail.getChatLieuVo());
        ps.setString(4, detail.getChatLieuDay());
        ps.setString(5, detail.getKieuMat());
        ps.setString(6, detail.getKinh());
        ps.setString(7, detail.getBoMay());
        ps.setString(8, detail.getNangLuongCo());
        ps.setString(9, detail.getThoiGianTruCoc());
        ps.setString(10, detail.getDoChiuNuoc());
        ps.setString(11, detail.getChucNangKhac());
        ps.setString(12, detail.getBaoHanh());
        ps.setString(13, detail.getMauMatSo());
        ps.setFloat(14, detail.getTrongLuong()); 
        ps.setInt(15, detail.getID());

        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public boolean updateCTSanPham(ProductDetail detail) {
        try (Connection conn = DBConnect.getConnection()) {
            String sql = "UPDATE CTSanPham SET DuongKinhMat=?, DoDayMat=?, ChatLieuVo=?, ChatLieuDay=?, KieuMat=?, Kinh=?, BoMay=?, NangLuongCo=?, ThoiGianTruCoc=?, DoChiuNuoc=?, ChucNangKhac=?, BaoHanh=?, MauMatSo=?, TrongLuong=? WHERE ID=?";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, detail.getDuongKinhMat());
            ps.setString(2, detail.getDoDayMat());
            ps.setString(3, detail.getChatLieuVo());
            ps.setString(4, detail.getChatLieuDay());
            ps.setString(5, detail.getKieuMat());
            ps.setString(6, detail.getKinh());
            ps.setString(7, detail.getBoMay());
            ps.setString(8, detail.getNangLuongCo());
            ps.setString(9, detail.getThoiGianTruCoc());
            ps.setString(10, detail.getDoChiuNuoc());
            ps.setString(11, detail.getChucNangKhac());
            ps.setString(12, detail.getBaoHanh());
            ps.setString(13, detail.getMauMatSo());
            ps.setFloat(14, detail.getTrongLuong());
            ps.setInt(15, detail.getID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
