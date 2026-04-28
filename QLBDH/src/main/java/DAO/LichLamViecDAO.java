package DAO;

import DTO.LichLamViecDTO;
import java.sql.*;
import java.util.ArrayList;

public class LichLamViecDAO {

    public boolean insert(LichLamViecDTO lich) {
        String sql = "INSERT INTO LichLamViec (MaNV, MaCa, NgayLam, GhiChu, TrangThai) VALUES (?, ?, ?, ?, ?)";

        try {
            int rows = DataProvider.executeUpdate(
                sql,
                lich.getMaNV(),
                lich.getMaCa(),
                lich.getNgayLam(),
                lich.getGhiChu(),
                lich.isTrangThai()
            );
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm lịch làm việc:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(LichLamViecDTO lich) {
        String sql = "UPDATE LichLamViec SET MaNV=?, MaCa=?, NgayLam=?, GhiChu=?, TrangThai=? WHERE MaLich=?";

        try {
            int rows = DataProvider.executeUpdate(
                sql,
                lich.getMaNV(),
                lich.getMaCa(),
                lich.getNgayLam(),
                lich.getGhiChu(),
                lich.isTrangThai(),
                lich.getMaLich()
            );
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật lịch làm việc:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maLich) {
        String sql = "DELETE FROM LichLamViec WHERE MaLich=?";

        try {
            int rows = DataProvider.executeUpdate(sql, maLich);
            return rows > 0;
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa lịch làm việc:");
            e.printStackTrace();
            return false;
        }
    }
    
    public int getTotalDaysInSchedule(int maNV, Date fromDate, Date toDate) {
        String sql = "SELECT COALESCE(SUM(cl.SoCong),0) AS TotalDays FROM LichLamViec llv"
                + " JOIN CaLam cl ON llv.MaCa = cl.MaCa"
                + " WHERE llv.MaNV = ? AND llv.NgayLam BETWEEN ? AND ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, new java.sql.Date(fromDate.getTime()), new java.sql.Date(toDate.getTime()))) {
            if (rs.next()) {
                return rs.getInt("TotalDays");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public boolean exists(int maNV, Date ngayLam) {
        String sql = "SELECT 1 FROM LichLamViec WHERE MaNV = ? AND NgayLam = ?";
        ResultSet rs = null;

        try {
            rs = DataProvider.executeQuery(sql, maNV, ngayLam);
            return rs != null && rs.next();
        } catch (SQLException e) {
            System.err.println("Lỗi khi kiểm tra lịch làm trùng:");
            e.printStackTrace();
            return false;
        } finally {
            closeResultSet(rs);
        }
    }

    public LichLamViecDTO selectById(int maLich) {
        String sql = getBaseSelectSql() + " WHERE llv.MaLich = ?";
        ResultSet rs = null;

        try {
            rs = DataProvider.executeQuery(sql, maLich);
            if (rs != null && rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch làm theo mã:");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }

        return null;
    }

    public LichLamViecDTO selectByMaNVAndNgayLam(int maNV, Date ngayLam) {
        String sql = getBaseSelectSql() + " WHERE llv.MaNV = ? AND llv.NgayLam = ?";
        ResultSet rs = null;

        try {
            rs = DataProvider.executeQuery(sql, maNV, ngayLam);
            if (rs != null && rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch làm theo nhân viên/ngày:");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }

        return null;
    }

    public ArrayList<LichLamViecDTO> selectByMaNVAndMonth(int maNV, int thang, int nam) {
        ArrayList<LichLamViecDTO> list = new ArrayList<>();
        String sql = getBaseSelectSql()
                + " WHERE llv.MaNV = ? AND MONTH(llv.NgayLam) = ? AND YEAR(llv.NgayLam) = ? "
                + " ORDER BY llv.NgayLam";
        ResultSet rs = null;

        try {
            rs = DataProvider.executeQuery(sql, maNV, thang, nam);
            while (rs != null && rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy lịch làm theo nhân viên/tháng:");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }

        return list;
    }

    public ArrayList<LichLamViecDTO> selectAllByMonth(int thang, int nam) {
        ArrayList<LichLamViecDTO> list = new ArrayList<>();
        String sql = getBaseSelectSql()
                + " WHERE MONTH(llv.NgayLam) = ? AND YEAR(llv.NgayLam) = ? "
                + " ORDER BY llv.NgayLam, llv.MaNV";
        ResultSet rs = null;

        try {
            rs = DataProvider.executeQuery(sql, thang, nam);
            while (rs != null && rs.next()) {
                list.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả lịch làm theo tháng:");
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }

        return list;
    }

    private String getBaseSelectSql() {
        return "SELECT "
                + "llv.MaLich, llv.MaNV, llv.MaCa, llv.NgayLam, llv.GhiChu, llv.TrangThai, "
                + "nv.Ho + ' ' + nv.Ten AS HoTenNhanVien, "
                + "cl.TenCa, cl.GioBatDau, cl.GioKetThuc, cl.SoCong, "
                + "cc.TrangThai AS TrangThaiChamCong, cc.GioVao, cc.GioRa "
                + "FROM LichLamViec llv "
                + "JOIN NhanVien nv ON llv.MaNV = nv.MaNV "
                + "JOIN CaLam cl ON llv.MaCa = cl.MaCa "
                + "LEFT JOIN ChamCong cc ON cc.MaNV = llv.MaNV AND cc.NgayLam = llv.NgayLam ";
    }

    private LichLamViecDTO mapResultSet(ResultSet rs) throws SQLException {
        LichLamViecDTO lich = new LichLamViecDTO();

        lich.setMaLich(rs.getInt("MaLich"));
        lich.setMaNV(rs.getInt("MaNV"));
        lich.setMaCa(rs.getInt("MaCa"));
        lich.setNgayLam(rs.getDate("NgayLam"));
        lich.setGhiChu(rs.getString("GhiChu"));
        lich.setTrangThai(rs.getBoolean("TrangThai"));

        lich.setHoTenNhanVien(rs.getString("HoTenNhanVien"));
        lich.setTenCa(rs.getString("TenCa"));
        lich.setGioBatDau(rs.getTime("GioBatDau"));
        lich.setGioKetThuc(rs.getTime("GioKetThuc"));
        lich.setSoCong(rs.getDouble("SoCong"));
        lich.setTrangThaiChamCong(rs.getString("TrangThaiChamCong"));
        lich.setGioVao(rs.getTime("GioVao"));
        lich.setGioRa(rs.getTime("GioRa"));

        return lich;
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