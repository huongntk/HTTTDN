package DAO;

import DTO.CaLamDTO;
import java.sql.*;
import java.util.ArrayList;

public class CaLamDAO {

    public ArrayList<CaLamDTO> selectAll() {
        ArrayList<CaLamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM CaLam ORDER BY MaCa";
        ResultSet rs = null;

        try {
            rs = DataProvider.executeQuery(sql);
            while (rs != null && rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách ca làm:");
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<CaLamDTO> selectActive() {
        ArrayList<CaLamDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM CaLam WHERE TrangThai = 1 ORDER BY MaCa";
        ResultSet rs = null;

        try {
            rs = DataProvider.executeQuery(sql);
            while (rs != null && rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy ca làm đang hoạt động:");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }

        return list;
    }

    public CaLamDTO selectById(int maCa) {
        String sql = "SELECT * FROM CaLam WHERE MaCa = ?";
        ResultSet rs = null;

        try {
            rs = DataProvider.executeQuery(sql, maCa);
            if (rs != null && rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy ca làm theo mã:");
            e.printStackTrace();
        } 

        return null;
    }

    public boolean insert(CaLamDTO ca) {
        String sql = "INSERT INTO CaLam (TenCa, GioBatDau, GioKetThuc, SoCong, TrangThai) VALUES (?, ?, ?, ?, ?)";

        try {
            int rows = DataProvider.executeUpdate(
                sql,
                ca.getTenCa(),
                ca.getGioBatDau(),
                ca.getGioKetThuc(),
                ca.getSoCong(),
                ca.isTrangThai()
            );
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm ca làm:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(CaLamDTO ca) {
        String sql = "UPDATE CaLam SET TenCa=?, GioBatDau=?, GioKetThuc=?, SoCong=?, TrangThai=? WHERE MaCa=?";

        try {
            int rows = DataProvider.executeUpdate(
                sql,
                ca.getTenCa(),
                ca.getGioBatDau(),
                ca.getGioKetThuc(),
                ca.getSoCong(),
                ca.isTrangThai(),
                ca.getMaCa()
            );
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật ca làm:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maCa) {
        String sql = "DELETE FROM CaLam WHERE MaCa=?";

        try {
            int rows = DataProvider.executeUpdate(sql, maCa);
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa ca làm:");
            e.printStackTrace();
            return false;
        }
    }

    private CaLamDTO mapResultSet(ResultSet rs) throws SQLException {
        CaLamDTO ca = new CaLamDTO();
        ca.setMaCa(rs.getInt("MaCa"));
        ca.setTenCa(rs.getString("TenCa"));
        ca.setGioBatDau(rs.getTime("GioBatDau"));
        ca.setGioKetThuc(rs.getTime("GioKetThuc"));
        ca.setSoCong(rs.getDouble("SoCong"));
        ca.setTrangThai(rs.getBoolean("TrangThai"));
        return ca;
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                Statement st = rs.getStatement();
                rs.close();

                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
            }
        }
    }
}