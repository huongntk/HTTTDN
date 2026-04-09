package DAO;

import DTO.BangLuongDTO;
import DTO.NhanVienDTO;
import java.sql.*;
import java.util.ArrayList;

public class BangLuongDAO {

    public ArrayList<NhanVienDTO> selectAll() {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien";
        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            while (rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(rs.getInt("MaNV"));
                nv.setHo(rs.getString("Ho"));
                nv.setTen(rs.getString("Ten"));
                nv.setGioiTinh(rs.getString("GioiTinh"));
                nv.setSoDienThoai(rs.getString("SoDienThoai"));
                nv.setMaCV(rs.getString("MaCV"));
                nv.setMaQuyen(rs.getString("MaQuyen"));
                nv.setTrangThai(rs.getBoolean("TrangThai"));
                nv.setTenTaiKhoan(rs.getString("TenTaiKhoan"));
                nv.setMatKhau(rs.getString("MatKhau"));
                list.add(nv);
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
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, maNV, thang, nam);
            if (rs != null && rs.next()) {
                return mapResultSetToDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return null;
    }

    // Lấy tất cả bảng lương của một nhân viên (lịch sử)
    public ArrayList<BangLuongDTO> getListByMaNV(int maNV) {
        ArrayList<BangLuongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BangLuong WHERE MaNV=? ORDER BY Nam DESC, Thang DESC";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, maNV);
            while (rs != null && rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return list;
    }

    // Lấy danh sách lương của tất cả nhân viên trong một tháng/năm
    public ArrayList<BangLuongDTO> getListByMonth(int thang, int nam) {
        ArrayList<BangLuongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BangLuong WHERE Thang=? AND Nam=?";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, thang, nam);
            while (rs != null && rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
        }
        return list;
    }

    // Kiểm tra đã có bảng lương của nhân viên trong tháng/năm chưa
    public boolean kiemTraTonTai(int maNV, int thang, int nam) {
        String sql = "SELECT 1 FROM BangLuong WHERE MaNV=? AND Thang=? AND Nam=?";
        ResultSet rs = null;
        try {
            rs = DataProvider.executeQuery(sql, maNV, thang, nam);
            return rs != null && rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
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

    // Helper: đóng ResultSet và Statement (không đóng Connection)
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                Statement stmt = rs.getStatement();
                rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
    
    public ArrayList<BangLuongDTO> getByMaNVAndYear(int maNV, int nam) {
        ArrayList<BangLuongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BangLuong WHERE MaNV = ? AND Nam = ? ORDER BY Thang";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, nam)) {
            while (rs.next()) list.add(mapResultSetToDTO(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}