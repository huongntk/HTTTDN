package DAO;

import DTO.DonNghiDTO;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;

public class DonNghiDAO {
    
    // Lấy tất cả đơn (hoặc có thể lọc theo trạng thái)
    public ArrayList<DonNghiDTO> selectByTrangThai(String trangThai) {
        ArrayList<DonNghiDTO> list = new ArrayList<>();
        String sql = "SELECT dn.*, nv.Ho, nv.Ten FROM DonTu dn " +
                     "JOIN NhanVien nv ON dn.MaNV = nv.MaNV " +
                     "WHERE dn.TrangThai = ? ORDER BY dn.MaDon DESC";
        try (ResultSet rs = DataProvider.executeQuery(sql, trangThai)) {
            while (rs.next()) {
                DonNghiDTO dn = new DonNghiDTO();
                dn.setMaDon(rs.getInt("MaDon"));
                dn.setMaNV(rs.getInt("MaNV"));
                dn.setHoTen(rs.getString("Ho") + " " + rs.getString("Ten"));
                dn.setLoaiDon(rs.getString("LoaiDon"));
                dn.setNgayBatDau(rs.getDate("NgayBatDau"));
                dn.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                dn.setLyDo(rs.getString("LyDo"));
                dn.setTrangThai(rs.getString("TrangThai"));
                // Nếu có cột NgayGui thì lấy, không thì có thể set null
                try { dn.setNgayGui(rs.getDate("NgayGui")); } catch (Exception e) {}
                list.add(dn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    // Lấy đơn nghỉ theo mã đơn
    public DonNghiDTO selectById(int maDon) {
        String sql = "SELECT dn.*, nv.Ho, nv.Ten FROM DonTu dn "
                + "JOIN NhanVien nv ON dn.MaNV = nv.MaNV "
                + "WHERE dn.MaDon = ?";

        try (ResultSet rs = DataProvider.executeQuery(sql, maDon)) {
            if (rs != null && rs.next()) {
                DonNghiDTO dn = new DonNghiDTO();
                dn.setMaDon(rs.getInt("MaDon"));
                dn.setMaNV(rs.getInt("MaNV"));
                dn.setHoTen(rs.getString("Ho") + " " + rs.getString("Ten"));
                dn.setLoaiDon(rs.getString("LoaiDon"));
                dn.setNgayBatDau(rs.getDate("NgayBatDau"));
                dn.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                dn.setLyDo(rs.getString("LyDo"));
                dn.setTrangThai(rs.getString("TrangThai"));

                try {
                    dn.setNgayGui(rs.getDate("NgayGui"));
                } catch (Exception e) {
                    // Nếu CSDL không có cột NgayGui thì bỏ qua
                }

                return dn;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    // Lấy tất cả đơn (không lọc trạng thái)
    public ArrayList<DonNghiDTO> selectAll() {
        ArrayList<DonNghiDTO> list = new ArrayList<>();
        String sql = "SELECT dn.*, nv.Ho, nv.Ten FROM DonTu dn " +
                     "JOIN NhanVien nv ON dn.MaNV = nv.MaNV ORDER BY dn.MaDon DESC";
        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            while (rs.next()) {
                DonNghiDTO dn = new DonNghiDTO();
                dn.setMaDon(rs.getInt("MaDon"));
                dn.setMaNV(rs.getInt("MaNV"));
                dn.setHoTen(rs.getString("Ho") + " " + rs.getString("Ten"));
                dn.setLoaiDon(rs.getString("LoaiDon"));
                dn.setNgayBatDau(rs.getDate("NgayBatDau"));
                dn.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                dn.setLyDo(rs.getString("LyDo"));
                dn.setTrangThai(rs.getString("TrangThai"));
                list.add(dn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Lấy đơn theo mã nhân viên
    public ArrayList<DonNghiDTO> selectByMaNV(int maNV) {
        ArrayList<DonNghiDTO> list = new ArrayList<>();
        String sql = "SELECT dn.*, nv.Ho, nv.Ten FROM DonTu dn " +
                     "JOIN NhanVien nv ON dn.MaNV = nv.MaNV " +
                     "WHERE dn.MaNV = ? ORDER BY dn.MaDon DESC";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV)) {
            while (rs.next()) {
                DonNghiDTO dn = new DonNghiDTO();
                dn.setMaDon(rs.getInt("MaDon"));
                dn.setMaNV(rs.getInt("MaNV"));
                dn.setHoTen(rs.getString("Ho") + " " + rs.getString("Ten"));
                dn.setLoaiDon(rs.getString("LoaiDon"));
                dn.setNgayBatDau(rs.getDate("NgayBatDau"));
                dn.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                dn.setLyDo(rs.getString("LyDo"));
                dn.setTrangThai(rs.getString("TrangThai"));
                list.add(dn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Thêm mới đơn nghỉ
    public int insert(DonNghiDTO dn) {
        String sql = "INSERT INTO DonTu (MaNV, LoaiDon, NgayBatDau, NgayKetThuc, LyDo, TrangThai) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        return DataProvider.executeUpdate(sql,
                dn.getMaNV(),
                dn.getLoaiDon(),
                new Date(dn.getNgayBatDau().getTime()),
                new Date(dn.getNgayKetThuc().getTime()),
                dn.getLyDo(),
                dn.getTrangThai() != null ? dn.getTrangThai() : "Chờ duyệt");
    }
    
    // Cập nhật trạng thái đơn
    public boolean updateTrangThai(int maDon, String trangThai) {
        String sql = "UPDATE DonTu SET TrangThai = ? WHERE MaDon = ?";
        return DataProvider.executeUpdate(sql, trangThai, maDon) > 0;
    }
}