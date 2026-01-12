package DAO;

import DTO.CTPhieuNhapDTO;
import UTIL.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CTPhieuNhapDAO {

    // Lấy danh sách chi tiết theo MaPN
    public List<CTPhieuNhapDTO> getByMaPN(int maPN) {
        List<CTPhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT MaPN, ID, SoLuong, GiaBan, ThanhTien FROM CTPhieuNhap WHERE MaPN = ?";

        try (ResultSet rs = DataProvider.executeQuery(sql, maPN)) {
            while (rs.next()) {
                CTPhieuNhapDTO ct = new CTPhieuNhapDTO(
                        rs.getInt("MaPN"),
                        rs.getInt("ID"),
                        rs.getInt("SoLuong"),
                        rs.getInt("GiaBan"),
                        rs.getInt("ThanhTien")
                );
                list.add(ct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm dòng chi tiết
    public boolean insert(CTPhieuNhapDTO ct) {
        String sql = "INSERT INTO CTPhieuNhap (MaPN, ID, SoLuong, GiaBan, ThanhTien) VALUES (?, ?, ?, ?, ?)";
        int rows = DataProvider.executeUpdate(
                sql,
                ct.getMaPN(),
                ct.getId(),
                ct.getSoLuong(),
                ct.getGiaNhap(),
                ct.getThanhTien()
        );
        return rows > 0;
    }

    // Cập nhật 1 dòng chi tiết
    public boolean update(CTPhieuNhapDTO ct) {
        String sql = "UPDATE CTPhieuNhap SET SoLuong=?, GiaBan=?, ThanhTien=? WHERE MaPN=? AND ID=?";
        int rows = DataProvider.executeUpdate(
                sql,
                ct.getSoLuong(),
                ct.getGiaNhap(),
                ct.getThanhTien(),
                ct.getMaPN(),
                ct.getId()
        );
        return rows > 0;
    }

    // Xóa chi tiết theo MaPN + ID sản phẩm
    public boolean delete(int maPN, int idSP) {
        String sql = "DELETE FROM CTPhieuNhap WHERE MaPN=? AND ID=?";
        int rows = DataProvider.executeUpdate(sql, maPN, idSP);
        return rows > 0;
    }

    // Tổng tiền của 1 phiếu nhập
    public double sumThanhTien(int maPN) {
        String sql = "SELECT ISNULL(SUM(ThanhTien),0) AS Tong FROM CTPhieuNhap WHERE MaPN = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maPN)) {
            if (rs.next()) {
                return rs.getDouble("Tong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // (tùy chọn) tăng tồn kho SP sau khi nhập
    public void tangSoLuongSanPham(int idSP, int soLuong) {
        String sql = "UPDATE SanPham SET SoLuong = ISNULL(SoLuong,0) + ? WHERE ID = ?";
        DataProvider.executeUpdate(sql, soLuong, idSP);
    }
}
