package DAO;

import DTO.ChamCongDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class ChamCongDAO {

    public ArrayList<Object[]> getChamCongChiTiet(int maNV, int thang, int nam) {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT cc.NgayLam, cl.SoCong "
                + "FROM ChamCong cc "
                + "JOIN CaLam cl ON cc.CaLam = cl.TenCa "
                + "WHERE cc.MaNV = ? AND MONTH(cc.NgayLam) = ? AND YEAR(cc.NgayLam) = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, thang, nam)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getDate("NgayLam"), rs.getDouble("SoCong")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public double getTotalCongByDateRange(int maNV, Date fromDate, Date toDate) {
        String sql = "SELECT SUM(cl.SoCong) AS TongCong " +
                     "FROM ChamCong cc " +
                     "JOIN CaLam cl ON cc.CaLam = cl.TenCa " +
                     "WHERE cc.MaNV = ? AND cc.NgayLam BETWEEN ? AND ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime()))) {
            if (rs.next()) {
                return rs.getDouble("TongCong");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

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
            if (isDuplicateKeyError(e)) {
                System.err.println("❌ Lỗi trùng chấm công: Nhân viên này đã có chấm công trong ngày này.");
            } else {
                System.err.println("❌ Lỗi khi thêm chấm công:");
                e.printStackTrace();
            }
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
            if (isDuplicateKeyError(e)) {
                System.err.println("❌ Lỗi trùng chấm công: Không thể cập nhật vì nhân viên này đã có chấm công trong ngày này.");
            } else {
                System.err.println("❌ Lỗi khi cập nhật chấm công:");
                e.printStackTrace();
            }
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
            System.err.println("❌ Lỗi khi xóa chấm công:");
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

            while (rs != null && rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi lấy chấm công theo nhân viên/tháng:");
            e.printStackTrace();

        } finally {
            closeResultSet(rs);
        }

        return list;
    }

    // Lấy danh sách tất cả nhân viên chấm công trong tháng
    public ArrayList<ChamCongDTO> selectAllByMonth(int thang, int nam) {
        ArrayList<ChamCongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM ChamCong WHERE MONTH(NgayLam) = ? AND YEAR(NgayLam) = ? ORDER BY MaNV, NgayLam";
        ResultSet rs = null;

        try {
            rs = DataProvider.executeQuery(sql, thang, nam);

            while (rs != null && rs.next()) {
                list.add(mapResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("❌ Lỗi khi lấy tất cả chấm công theo tháng:");
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
            System.err.println("❌ Lỗi khi kiểm tra trùng chấm công:");
            e.printStackTrace();
            return false;

        } finally {
            closeResultSet(rs);
        }
    }

    // Kiểm tra lỗi trùng UNIQUE KEY trong SQL Server
    private boolean isDuplicateKeyError(Exception e) {
        Throwable t = e;

        while (t != null) {
            if (t instanceof SQLException) {
                SQLException sqlEx = (SQLException) t;
                int code = sqlEx.getErrorCode();

                // 2601: duplicate key row with unique index
                // 2627: violation of UNIQUE KEY constraint
                if (code == 2601 || code == 2627) {
                    return true;
                }
            }

            String msg = t.getMessage();
            if (msg != null) {
                msg = msg.toLowerCase();

                if (msg.contains("duplicate key")
                        || msg.contains("unique key")
                        || msg.contains("uq_chamcong_manv_ngaylam")) {
                    return true;
                }
            }

            t = t.getCause();
        }

        return false;
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
                Statement st = rs.getStatement();
                rs.close();

                if (st != null) {
                    st.close();
                }

            } catch (SQLException e) {
                // Bỏ qua lỗi đóng ResultSet
            }
        }
    }
}
