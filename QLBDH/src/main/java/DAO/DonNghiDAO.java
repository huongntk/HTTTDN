package DAO;

import DTO.DonNghi;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;

public class DonNghiDAO {
    
    public ArrayList<DonNghi> selectByTrangThai(String trangThai) {
        ArrayList<DonNghi> list = new ArrayList<>();
        String sql = "SELECT dn.*, nv.Ho, nv.Ten FROM DonNghi dn JOIN NhanVien nv ON dn.MaNV = nv.MaNV WHERE dn.TrangThai = ? ORDER BY dn.NgayGui DESC";
        try (ResultSet rs = DataProvider.executeQuery(sql, trangThai)) {
            while (rs.next()) {
                DonNghi dn = new DonNghi();
                dn.setMaDon(rs.getInt("MaDon"));
                dn.setMaNV(rs.getInt("MaNV"));
                dn.setHoTen(rs.getString("Ho") + " " + rs.getString("Ten"));
                dn.setNgayBatDau(rs.getDate("NgayBatDau"));
                dn.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                dn.setLyDo(rs.getString("LyDo"));
                dn.setTrangThai(rs.getString("TrangThai"));
                dn.setNgayGui(rs.getDate("NgayGui"));
                list.add(dn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public ArrayList<DonNghi> selectByMaNV(int maNV) {
        ArrayList<DonNghi> list = new ArrayList<>();
        String sql = "SELECT * FROM DonNghi WHERE MaNV = ? ORDER BY NgayGui DESC";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV)) {
            while (rs.next()) {
                DonNghi dn = new DonNghi();
                dn.setMaDon(rs.getInt("MaDon"));
                dn.setMaNV(rs.getInt("MaNV"));
                dn.setNgayBatDau(rs.getDate("NgayBatDau"));
                dn.setNgayKetThuc(rs.getDate("NgayKetThuc"));
                dn.setLyDo(rs.getString("LyDo"));
                dn.setTrangThai(rs.getString("TrangThai"));
                dn.setNgayGui(rs.getDate("NgayGui"));
                list.add(dn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public int insert(DonNghi dn) {
        String sql = "INSERT INTO DonNghi (MaNV, NgayBatDau, NgayKetThuc, LyDo, TrangThai, NgayGui) VALUES (?, ?, ?, ?, ?, ?)";
        return DataProvider.executeUpdate(sql, 
                dn.getMaNV(), 
                new Date(dn.getNgayBatDau().getTime()), 
                new Date(dn.getNgayKetThuc().getTime()), 
                dn.getLyDo(), 
                dn.getTrangThai(), 
                new Date(dn.getNgayGui().getTime()));
    }
    
    public boolean updateTrangThai(int maDon, String trangThai) {
        String sql = "UPDATE DonNghi SET TrangThai = ? WHERE MaDon = ?";
        return DataProvider.executeUpdate(sql, trangThai, maDon) > 0;
    }
}