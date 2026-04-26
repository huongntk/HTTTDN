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
        String sql = "SELECT pn.MaPN, pn.MaNCC, ncc.TenNCC, pn.NgayLap, pn.TongTien, pn.TrangThai " +
             "FROM PhieuNhap pn " +
             "LEFT JOIN NhaCungCap ncc ON pn.MaNCC = ncc.MaNCC " +
             "ORDER BY pn.MaPN DESC";
        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            while (rs.next()) {
                PhieuNhapDTO pn = new PhieuNhapDTO(
                        rs.getInt("MaPN"),
                        rs.getObject("MaNCC") == null ? null : rs.getInt("MaNCC"),
                        rs.getDate("NgayLap"),
                        rs.getDouble("TongTien")
                );
                pn.setTenNCC(rs.getString("TenNCC"));
                // THÊM DÒNG NÀY - đọc trạng thái
                pn.setTrangThai(rs.getBoolean("TrangThai") ? 1 : 0);
                list.add(pn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy phiếu nhập theo mã
    public PhieuNhapDTO getById(int maPN) {
        String sql = "SELECT pn.MaPN, pn.MaNCC, ncc.TenNCC, pn.NgayLap, pn.TongTien, pn.TrangThai " +
                    "FROM PhieuNhap pn " +
                    "LEFT JOIN NhaCungCap ncc ON pn.MaNCC = ncc.MaNCC " +
                    "WHERE pn.MaPN = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maPN)) {
            if (rs.next()) {
                PhieuNhapDTO pn = new PhieuNhapDTO(
                        rs.getInt("MaPN"),
                        rs.getObject("MaNCC") == null ? null : rs.getInt("MaNCC"),
                        rs.getDate("NgayLap"),
                        rs.getDouble("TongTien")
                );
                // THÊM DÒNG NÀY - đọc trạng thái
                pn.setTrangThai(rs.getBoolean("TrangThai") ? 1 : 0);
                return pn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Thêm phiếu nhập mới, trả về ID tự tăng (hoặc -1 nếu lỗi)
    public int insert(PhieuNhapDTO pn) {
        String sql = "INSERT INTO PhieuNhap (MaNV, MaNCC, NgayLap, TongTien, TrangThai) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setObject(1, null); // MaNV tạm null
            if (pn.getMaNCC() == null) ps.setObject(2, null);
            else ps.setInt(2, pn.getMaNCC());
            ps.setDate(3, pn.getNgayLap());
            ps.setDouble(4, pn.getTongTien());
            ps.setBoolean(5, false); // Mặc định TrangThai = false (chưa xuất)

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
        String sql = "UPDATE PhieuNhap SET MaNCC=?, NgayLap=?, TongTien=?, TrangThai=? WHERE MaPN=?";
        int rows = DataProvider.executeUpdate(
                sql,
                pn.getMaNCC(),
                pn.getNgayLap(),
                pn.getTongTien(),
                pn.getTrangThai() == 1,  // Chuyển int -> boolean
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
    
    // Tìm kiếm 
     public List<PhieuNhapDTO> search(String key, java.util.Date start, java.util.Date end) {
        List<PhieuNhapDTO> list = new ArrayList<>();
        // Câu lệnh SQL lọc theo từ khóa và khoảng ngày
        StringBuilder sql = new StringBuilder(
            "SELECT pn.MaPN, pn.MaNCC, ncc.TenNCC, pn.NgayLap, pn.TongTien " +
            "FROM PhieuNhap pn " +
            "LEFT JOIN NhaCungCap ncc ON pn.MaNCC = ncc.MaNCC " +
            "WHERE (CAST(pn.MaPN AS VARCHAR) LIKE ? OR ncc.TenNCC LIKE ?) "
        );

        if (start != null) sql.append(" AND pn.NgayLap >= ? ");
        if (end != null) sql.append(" AND pn.NgayLap <= ? ");
        sql.append(" ORDER BY pn.MaPN DESC");

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            String pattern = "%" + key + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            int paramIndex = 3;
            if (start != null) {
                ps.setDate(paramIndex++, new java.sql.Date(start.getTime()));
            }
            if (end != null) {
                ps.setDate(paramIndex++, new java.sql.Date(end.getTime()));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PhieuNhapDTO pn = new PhieuNhapDTO(
                        rs.getInt("MaPN"),
                        rs.getObject("MaNCC") == null ? null : rs.getInt("MaNCC"),
                        rs.getDate("NgayLap"),
                        rs.getDouble("TongTien")
                );
                pn.setTenNCC(rs.getString("TenNCC"));
                list.add(pn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Cập nhật trạng thái phiếu nhập (đã có, giữ nguyên)
    public boolean updateTrangThai(int maPN, int trangThai) {
        String sql = "UPDATE PhieuNhap SET TrangThai = ? WHERE MaPN = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, trangThai == 1);
            ps.setInt(2, maPN);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateMaNCC(int maPN, Integer maNCC) {
        String sql = "UPDATE PhieuNhap SET MaNCC = ? WHERE MaPN = ?";
        int rows = DataProvider.executeUpdate(sql, maNCC, maPN);
        return rows > 0;
    }
    
    public boolean updateNgayLap(int maPN, java.sql.Date ngayLap) {
        String sql = "UPDATE PhieuNhap SET NgayLap = ? WHERE MaPN = ?";
        int rows = DataProvider.executeUpdate(sql, ngayLap, maPN);
        return rows > 0;
    }
}