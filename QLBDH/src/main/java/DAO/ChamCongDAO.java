package DAO;

import DTO.ChamCongDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class ChamCongDAO {

    // Thêm mới bản ghi chấm công
    public boolean insert(ChamCongDTO cc) {
        String sql = "INSERT INTO ChamCong (MaNV, NgayLam, CaLam, GioVao, GioRa, TrangThai, GhiChu) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            int rows = DataProvider.executeUpdate(sql,
                cc.getMaNV(),
                new java.sql.Date(cc.getNgayLam().getTime()),
                cc.getCaLam(),
                cc.getGioVao(),
                cc.getGioRa(),
                cc.getTrangThai(),
                cc.getGhiChu()
            );
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật bản ghi chấm công
    public boolean update(ChamCongDTO cc) {
        String sql = "UPDATE ChamCong SET MaNV=?, NgayLam=?, CaLam=?, GioVao=?, GioRa=?, TrangThai=?, GhiChu=? WHERE MaChamCong=?";
        try {
            int rows = DataProvider.executeUpdate(sql,
                cc.getMaNV(),
                new java.sql.Date(cc.getNgayLam().getTime()),
                cc.getCaLam(),
                cc.getGioVao(),
                cc.getGioRa(),
                cc.getTrangThai(),
                cc.getGhiChu(),
                cc.getMaChamCong()
            );
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa bản ghi theo mã
    public boolean delete(int maChamCong) {
        String sql = "DELETE FROM ChamCong WHERE MaChamCong=?";
        try {
            int rows = DataProvider.executeUpdate(sql, maChamCong);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy danh sách chấm công theo nhân viên và tháng/năm
    public ArrayList<ChamCongDTO> selectByMaNVAndMonth(int maNV, int thang, int nam) {
        ArrayList<ChamCongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ChamCong WHERE MaNV = ? AND MONTH(NgayLam) = ? AND YEAR(NgayLam) = ? ORDER BY NgayLam";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, maNV, thang, nam);
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return list;
    }

    // Lấy danh sách tất cả nhân viên chấm công trong tháng (dùng cho bảng tổng hợp)
    public ArrayList<ChamCongDTO> selectAllByMonth(int thang, int nam) {
        ArrayList<ChamCongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ChamCong WHERE MONTH(NgayLam) = ? AND YEAR(NgayLam) = ? ORDER BY MaNV, NgayLam";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, thang, nam);
            while (rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return list;
    }

    // Kiểm tra đã có bản ghi chấm công của nhân viên trong ngày chưa
    public boolean exists(int maNV, Date ngayLam) {
        String sql = "SELECT 1 FROM ChamCong WHERE MaNV = ? AND NgayLam = ?";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, maNV, new java.sql.Date(ngayLam.getTime()));
            return rs != null && rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeResultSet(rs);
        }
    }

    // Helper chuyển ResultSet -> DTO
    private ChamCongDTO mapResultSet(ResultSet rs) throws SQLException {
        ChamCongDTO cc = new ChamCongDTO();
        cc.setMaChamCong(rs.getInt("MaChamCong"));
        cc.setMaNV(rs.getInt("MaNV"));
        cc.setNgayLam(rs.getDate("NgayLam"));
        cc.setCaLam(rs.getString("CaLam"));
        cc.setGioVao(rs.getTime("GioVao"));
        cc.setGioRa(rs.getTime("GioRa"));
        cc.setTrangThai(rs.getString("TrangThai"));
        cc.setGhiChu(rs.getString("GhiChu"));
        return cc;
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.getStatement().close();
                rs.close();
            } catch (SQLException e) {}
        }
    }
}