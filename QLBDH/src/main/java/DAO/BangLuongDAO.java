package DAO;

import DTO.BangLuongDTO;
import java.sql.*;
import java.util.ArrayList;

public class BangLuongDAO {

    public ArrayList<Object[]> selectAllNhanVienWithLuong() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT nv.MaNV, nv.Ho, nv.Ten, nv.MaCV, "
                   + "ISNULL(lcb.LuongCoBan, 0) AS LuongCoBan, "
                   + "ISNULL(lcb.PhuCapChucVu, 0) AS PhuCap "
                   + "FROM NhanVien nv "
                   + "LEFT JOIN LuongCoBanTheoChucVu lcb ON nv.MaCV = lcb.MaCV "
                   + "WHERE nv.TrangThai = 1"; // chỉ lấy nhân viên đang làm việc
        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("MaNV"),
                    rs.getString("Ho") + " " + rs.getString("Ten"),
                    rs.getString("MaCV"),
                    rs.getDouble("LuongCoBan"),
                    rs.getDouble("PhuCap")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm mới bảng lương
    public boolean insert(BangLuongDTO bl) {
        String sql = "INSERT INTO BangLuong (MaNV, Thang, Nam, LuongCoBan, PhuCap, Thuong, Phat, TongLuong, NgayTinh, GhiChu, TrangThai) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            int rows = DataProvider.executeUpdate(sql,
                bl.getMaNV(), bl.getThang(), bl.getNam(), bl.getLuongCoBan(), bl.getPhuCap(),
                bl.getThuong(), bl.getPhat(), bl.getTongLuong(),
                new java.sql.Date(bl.getNgayTinh().getTime()), bl.getGhiChu(), bl.getTrangThai());
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật bảng lương
    public boolean update(BangLuongDTO bl) {
        String sql = "UPDATE BangLuong SET LuongCoBan=?, PhuCap=?, Thuong=?, Phat=?, TongLuong=?, NgayTinh=?, GhiChu=?, TrangThai=? "
                   + "WHERE MaBL=?";
        try {
            int rows = DataProvider.executeUpdate(sql,
                bl.getLuongCoBan(), bl.getPhuCap(), bl.getThuong(), bl.getPhat(), bl.getTongLuong(),
                new java.sql.Date(bl.getNgayTinh().getTime()), bl.getGhiChu(), bl.getTrangThai(),
                bl.getMaBL());
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy bảng lương của một nhân viên theo tháng/năm
    public BangLuongDTO getByMaNVAndMonth(int maNV, int thang, int nam) {
        String sql = "SELECT * FROM BangLuong WHERE MaNV=? AND Thang=? AND Nam=?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, thang, nam)) {
            if (rs.next()) {
                return mapResultSetToDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả bảng lương của một nhân viên (lịch sử)
    public ArrayList<BangLuongDTO> getListByMaNV(int maNV) {
        ArrayList<BangLuongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BangLuong WHERE MaNV=? ORDER BY Nam DESC, Thang DESC";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV)) {
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách lương của tất cả nhân viên trong một tháng/năm
    public ArrayList<BangLuongDTO> getListByMonth(int thang, int nam) {
        ArrayList<BangLuongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BangLuong WHERE Thang=? AND Nam=?";
        try (ResultSet rs = DataProvider.executeQuery(sql, thang, nam)) {
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Kiểm tra đã có bảng lương của nhân viên trong tháng/năm chưa
    public boolean kiemTraTonTai(int maNV, int thang, int nam) {
        String sql = "SELECT 1 FROM BangLuong WHERE MaNV=? AND Thang=? AND Nam=?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, thang, nam)) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa bảng lương
    public boolean delete(int maBL) {
        String sql = "DELETE FROM BangLuong WHERE MaBL=?";
        try {
            int rows = DataProvider.executeUpdate(sql, maBL);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<BangLuongDTO> getByMaNVAndYear(int maNV, int nam) {
        ArrayList<BangLuongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BangLuong WHERE MaNV = ? AND Nam = ? ORDER BY Thang";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, nam)) {
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper: chuyển ResultSet thành DTO
    private BangLuongDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        BangLuongDTO bl = new BangLuongDTO();
        bl.setMaBL(rs.getInt("MaBL"));
        bl.setMaNV(rs.getInt("MaNV"));
        bl.setThang(rs.getInt("Thang"));
        bl.setNam(rs.getInt("Nam"));
        bl.setLuongCoBan(rs.getDouble("LuongCoBan"));
        bl.setPhuCap(rs.getDouble("PhuCap"));
        bl.setThuong(rs.getDouble("Thuong"));
        bl.setPhat(rs.getDouble("Phat"));
        bl.setTongLuong(rs.getDouble("TongLuong"));
        bl.setNgayTinh(rs.getDate("NgayTinh"));
        bl.setGhiChu(rs.getString("GhiChu"));
        bl.setTrangThai(rs.getInt("TrangThai"));
        return bl;
    }
}