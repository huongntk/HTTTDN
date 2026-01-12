package DAO;

import DTO.PhieuNhapDTO;
import UTIL.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhieuNhapDAO {

    // Lấy toàn bộ danh sách phiếu nhập
    public List<PhieuNhapDTO> getAll() {
        List<PhieuNhapDTO> list = new ArrayList<>();
        String sql = "SELECT MaPN, MaNCC, NgayLap, TongTien FROM PhieuNhap ORDER BY MaPN DESC";

        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            while (rs.next()) {
                PhieuNhapDTO pn = new PhieuNhapDTO(
                        rs.getInt("MaPN"),
                        rs.getObject("MaNCC") == null ? null : rs.getInt("MaNCC"),
                        rs.getDate("NgayLap"),
                        rs.getDouble("TongTien")
                );
                list.add(pn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy phiếu nhập theo mã
    public PhieuNhapDTO getById(int maPN) {
        String sql = "SELECT MaPN, MaNCC, NgayLap, TongTien FROM PhieuNhap WHERE MaPN = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maPN)) {
            if (rs.next()) {
                return new PhieuNhapDTO(
                        rs.getInt("MaPN"),
                        rs.getObject("MaNCC") == null ? null : rs.getInt("MaNCC"),
                        rs.getDate("NgayLap"),
                        rs.getDouble("TongTien")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm phiếu nhập mới, trả về ID tự tăng (hoặc -1 nếu lỗi)
    public int insert(PhieuNhapDTO pn) {
        String sql = "INSERT INTO PhieuNhap (MaNV, MaNCC, NgayLap, TongTien) VALUES (?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setObject(1, null); // MaNV tạm null
            if (pn.getMaNCC() == null) ps.setObject(2, null);
            else ps.setInt(2, pn.getMaNCC());
            ps.setDate(3, pn.getNgayLap());
            ps.setDouble(4, pn.getTongTien());

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) generatedId = keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    // Cập nhật phiếu nhập
    public boolean update(PhieuNhapDTO pn) {
        String sql = "UPDATE PhieuNhap SET MaNCC=?, NgayLap=?, TongTien=? WHERE MaPN=?";
        int rows = DataProvider.executeUpdate(
                sql,
                pn.getMaNCC(),
                pn.getNgayLap(),
                pn.getTongTien(),
                pn.getMaPN()
        );
        return rows > 0;
    }

    // Xóa phiếu nhập
    public boolean delete(int maPN) {
        String sql = "DELETE FROM PhieuNhap WHERE MaPN = ?";
        int rows = DataProvider.executeUpdate(sql, maPN);
        return rows > 0;
    }

    // Lấy ID tiếp theo (ước lượng)
    public int getNextIdentityEstimate() {
        String sql = "SELECT ISNULL(MAX(MaPN),0) AS MaxId FROM PhieuNhap";
        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("MaxId") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // Cập nhật tổng tiền phiếu nhập
    public boolean updateTongTien(int maPN, double tongTien) {
        String sql = "UPDATE PhieuNhap SET TongTien = ? WHERE MaPN = ?";
        int rows = DataProvider.executeUpdate(sql, tongTien, maPN);
        return rows > 0;
    }
        // Tìm kiếm (theo MaPN giả lập, hoặc theo MaNCC)
    public List<PhieuNhapDTO> search(String key) {
        List<PhieuNhapDTO> list = new ArrayList<>();
        // key có thể là mã ảo 100000+MaPN hoặc MaPN thật hoặc MaNCC
        // Ta cố gắng parse số:
        Integer parsedInt = null;
        try { parsedInt = Integer.parseInt(key); } catch (Exception ignore) {}
        String sql;
        if (parsedInt != null) {
            sql = "SELECT MaPN, MaNCC, NgayLap, TongTien "
                + "FROM PhieuNhap "
                + "WHERE MaPN = ? OR MaNCC = ? "
                + "ORDER BY MaPN DESC";
            try (ResultSet rs = DataProvider.executeQuery(sql, parsedInt, parsedInt)) {
                while (rs.next()) {
                    PhieuNhapDTO pn = new PhieuNhapDTO(
                            rs.getInt("MaPN"),
                            rs.getObject("MaNCC")==null?null:rs.getInt("MaNCC"),
                            rs.getDate("NgayLap"),
                            rs.getDouble("TongTien")
                    );
                    list.add(pn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // nếu key không phải số -> tạm thời trả tất cả
            return getAll();
        }
        return list;
    }
    
    public boolean updateTrangThai(int maPN, int trangThai) {
        String sql = "UPDATE PhieuNhap SET TrangThai = ? WHERE MaPN = ?";
        int rows = DataProvider.executeUpdate(sql, trangThai, maPN);
        return rows > 0;
    }
}