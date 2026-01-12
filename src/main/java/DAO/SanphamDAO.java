/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DTO.Product;
import UTIL.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.*;
import java.util.*;
/**
 *
 * @author ltd96
 *         PRODUCT DATA ACCESS OBJECT
 */
public class SanphamDAO {
    public ArrayList<Product> getALL() {
        ArrayList<Product> listTmp = new ArrayList();
        String sql = "SELECT SP.*, L.TenLoai, NCC.TenNCC "
                + "FROM SanPham SP "
                + "INNER JOIN LOAI L ON SP.MaLoai = L.MaLoai "
                + "INNER JOIN NhaCungCap NCC ON SP.MaNCC = NCC.MaNCC WHERE SP.TrangThai = ?";
        ResultSet rs = DataProvider.executeQuery(sql, 1);
        try {
            while (rs.next()) {
                Product p = new Product();
                p.setID(rs.getInt("ID"));
                p.setTenSP(rs.getString("TenSP"));
                p.setThuongHieu(rs.getString("ThuongHieu"));
                p.setXuatXu(rs.getString("XuatXu"));
                p.setMaLoai(rs.getInt("MaLoai"));
                p.setGioiTinh(rs.getString("GioiTinh"));
                p.setGiaBan(rs.getBigDecimal("GiaBan"));
                p.setSoLuong(rs.getInt("SoLuong"));
                p.setHinhAnh(rs.getString("HinhAnh"));
                p.setMoTa(rs.getString("MoTa"));
                p.setMaNCC(rs.getInt("maNCC"));
                p.setTrangThai(rs.getBoolean("TrangThai"));
                p.setTenLoai(rs.getString("TenLoai")); 
                p.setTenNCC(rs.getString("TenNCC"));
                listTmp.add(p);
            }
        } catch (SQLException ex) {
            System.getLogger(SanphamDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

        return listTmp;
    }

    public ArrayList<Product> getById(int id) {
        ArrayList<Product> listTmp = new ArrayList();
        String sql = "SELECT SP.*, L.TenLoai "
                + "FROM SanPham SP "
                + "INNER JOIN LOAI L ON SP.MaLoai = L.MaLoai WHERE SP.ID=? AND SP.TrangThai = 1";
        ResultSet rs = DataProvider.executeQuery(sql, id);
        try {
            while (rs.next()) {
                Product p = new Product();
                p.setID(rs.getInt("ID"));
                p.setTenSP(rs.getString("TenSP"));
                p.setThuongHieu(rs.getString("ThuongHieu"));
                p.setXuatXu(rs.getString("XuatXu"));
                p.setMaLoai(rs.getInt("MaLoai"));
                p.setGioiTinh(rs.getString("GioiTinh"));
                p.setGiaBan(rs.getBigDecimal("GiaBan"));
                p.setSoLuong(rs.getInt("SoLuong"));
                p.setHinhAnh(rs.getString("HinhAnh"));
                p.setMoTa(rs.getString("MoTa"));
                p.setMaNCC(rs.getInt("maNCC"));
                p.setTenLoai(rs.getString("TenLoai")); 
                p.setTrangThai(rs.getBoolean("TrangThai"));
                listTmp.add(p);
            }
        } catch (SQLException ex) {
            System.getLogger(SanphamDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return listTmp;
    }

    public ArrayList<Product> getByCate(int maLoai) {
        ArrayList<Product> listTmp = new ArrayList();
        String sql = ("SELECT * FROM SanPham WHERE MaLoai = ? AND TrangThai = 1");
        ResultSet rs = DataProvider.executeQuery(sql, maLoai);
        try {
            while (rs.next()) {
                Product p = new Product();
                p.setID(rs.getInt("ID"));
                p.setTenSP(rs.getString("TenSP"));
                p.setThuongHieu(rs.getString("ThuongHieu"));
                p.setXuatXu(rs.getString("XuatXu"));
                p.setMaLoai(rs.getInt("MaLoai"));
                p.setGioiTinh(rs.getString("GioiTinh"));
                p.setGiaBan(rs.getBigDecimal("GiaBan"));
                p.setSoLuong(rs.getInt("SoLuong"));
                p.setHinhAnh(rs.getString("HinhAnh"));
                p.setMoTa(rs.getString("MoTa"));
                p.setMaNCC(rs.getInt("maNCC"));
                p.setTrangThai(rs.getBoolean("TrangThai"));
                listTmp.add(p);
            }
        } catch (SQLException ex) {
            System.getLogger(SanphamDAO.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        return listTmp;

    }

    public int insertSanPham(Product p) {
        String sql = "INSERT INTO SanPham " +
                "(TenSP, ThuongHieu, XuatXu, MaLoai, GioiTinh, GiaBan, SoLuong, HinhAnh, MoTa, maNCC,TrangThai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,1)";
        int newId = -1;
        try (Connection conn = DBConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getTenSP());
            ps.setString(2, p.getThuongHieu());
            ps.setString(3, p.getXuatXu());
            ps.setInt(4, p.getMaLoai());
            ps.setString(5, p.getGioiTinh());
            ps.setBigDecimal(6, p.getGiaBan());
            ps.setInt(7, p.getSoLuong());
            ps.setString(8, p.getHinhAnh());
            ps.setString(9, p.getMoTa());
            ps.setInt(10, p.getMaNCC());

            int row = ps.executeUpdate();
            if (row > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next())
                    newId = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public boolean updateSanPham(Product p) {
        String sql = "UPDATE SanPham SET TenSP=?, ThuongHieu=?, XuatXu=?, MaLoai=?, GioiTinh=?, GiaBan=?, " +
                "SoLuong=?, HinhAnh=?, MoTa=?, MaNCC=? WHERE ID=?";

        int rows = DataProvider.executeUpdate(sql,
                p.getTenSP(),
                p.getThuongHieu(),
                p.getXuatXu(),
                p.getMaLoai(),
                p.getGioiTinh(),
                p.getGiaBan(),
                p.getSoLuong(),
                p.getHinhAnh(),
                p.getMoTa(),
                p.getMaNCC(),
                p.getID());
        return rows > 0;
    }

    public boolean deleteById(int id) {
        String sql = ("UPDATE SanPham SET TrangThai = 0 WHERE id = ?");
        int row = DataProvider.executeUpdate(sql, id);
        if (row <= 0)
            return false;

        return true;
    }

    public ArrayList<Product> getByName(String searchName) {
        ArrayList<Product> listTmp = new ArrayList<>();

        String sql = "SELECT * FROM SanPham WHERE TenSP LIKE ? AND TrangThai = 1";

        try (ResultSet rs = DataProvider.executeQuery(sql, "%" + searchName + "%")) {

            while (rs.next()) {
                Product p = new Product();
                p.setID(rs.getInt("ID"));
                p.setTenSP(rs.getString("TenSP"));
                p.setThuongHieu(rs.getString("ThuongHieu"));
                p.setXuatXu(rs.getString("XuatXu"));
                p.setMaLoai(rs.getInt("MaLoai"));
                p.setGioiTinh(rs.getString("GioiTinh"));
                p.setGiaBan(rs.getBigDecimal("GiaBan"));
                p.setSoLuong(rs.getInt("SoLuong"));
                p.setHinhAnh(rs.getString("HinhAnh"));
                p.setMoTa(rs.getString("MoTa"));
                p.setMaNCC(rs.getInt("maNCC"));
                p.setTrangThai(rs.getBoolean("TrangThai"));
                listTmp.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listTmp;
    }


    // Lấy 1 sản phẩm theo ID dựa trên getById(int) đã có
public Product getOneById(int id) {
    ArrayList<Product> list = getById(id);
    return (list == null || list.isEmpty()) ? null : list.get(0);
}

    // Lấy tất cả sản phẩm đang hoạt động của 1 nhà cung cấp
    public List<Product> getByNccActive(int maNCC) {
        List<Product> list = new ArrayList<>();
        String sql =
            "SELECT ID, TenSP, maNCC " +
            "FROM SanPham " +
            "WHERE maNCC = ? AND TrangThai = 1 " +
            "ORDER BY TenSP";

        try (ResultSet rs = DataProvider.executeQuery(sql, maNCC)) {
            while (rs != null && rs.next()) {
                Product p = new Product();
                p.setID(rs.getInt("ID"));          // ✅ đúng cú pháp
                p.setTenSP(rs.getString("TenSP"));  // ✅ đúng cú pháp
                p.setMaNCC(rs.getInt("maNCC"));     // ✅ đúng cú pháp
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public Product selectById(int id) {
        Product product = null;
        // Giả định tên bảng là 'SanPham' và cột khóa chính là 'ID'
        String sql = "SELECT * FROM SanPham WHERE ID = ?"; 

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            // Thiết lập tham số cho câu lệnh SQL
            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    // Điền dữ liệu từ ResultSet vào đối tượng Product
                    product.setID(rs.getInt("ID"));
                    product.setTenSP(rs.getString("TenSP"));
                    product.setSoLuong(rs.getInt("SoLuong")); // Lấy số lượng tồn kho
                    product.setGiaBan(rs.getBigDecimal("GiaBan"));
                    // ... các trường dữ liệu khác của Product DTO
                }
            }

        } catch (SQLException e) {
            System.err.println("Lỗi DAO khi lấy chi tiết Sản phẩm ID=" + id + ": " + e.getMessage());
            e.printStackTrace();
        }
        return product;
    }
    
    public String getTenSanPham(int maSP) {
        if (maSP == 0) return "Không có sản phẩm"; // Xử lý trường hợp MaGG = 0
        String sql = "SELECT TenSP FROM SanPham WHERE ID = ? AND TrangThai = 1 ";
        String tenSP = null;
        try (ResultSet rs = DataProvider.executeQuery(sql, maSP)) {
            if (rs.next()) {
                tenSP = rs.getString("TenSP");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy tên Mã GG: " + e.getMessage());
        }
        return tenSP != null ? tenSP : "Không hợp lệ";
    }
}
