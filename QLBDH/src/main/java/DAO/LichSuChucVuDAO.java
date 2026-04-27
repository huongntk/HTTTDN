package DAO;

import DTO.LichSuChucVu;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;

public class LichSuChucVuDAO {

    public ArrayList<LichSuChucVu> selectByMaNV(int maNV) {
        ArrayList<LichSuChucVu> list = new ArrayList<>();
        String sql = "SELECT * FROM LichSuChucVu WHERE MaNV = ? ORDER BY NgayThayDoi DESC";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV)) {
            while (rs.next()) {
                LichSuChucVu ls = new LichSuChucVu();
                ls.setMaLS(rs.getInt("MaLS"));
                ls.setMaNV(rs.getInt("MaNV"));
                ls.setMaCVCu(rs.getString("MaCVCu"));
                ls.setMaCVMoi(rs.getString("MaCVMoi"));
                ls.setNgayThayDoi(rs.getDate("NgayThayDoi"));
                ls.setGhiChu(rs.getString("GhiChu"));
                list.add(ls);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public ArrayList<LichSuChucVu> selectByMaNVAndMonth(int maNV, int thang, int nam) {
        ArrayList<LichSuChucVu> list = new ArrayList<>();
        String sql = "SELECT * FROM LichSuChucVu WHERE MaNV = ? " +
                     "AND MONTH(NgayThayDoi) = ? AND YEAR(NgayThayDoi) = ? " +
                     "ORDER BY NgayThayDoi ASC";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, thang, nam)) {
            while (rs.next()) {
                LichSuChucVu ls = new LichSuChucVu();
                ls.setMaLS(rs.getInt("MaLS"));
                ls.setMaNV(rs.getInt("MaNV"));
                ls.setMaCVCu(rs.getString("MaCVCu"));
                ls.setMaCVMoi(rs.getString("MaCVMoi"));
                ls.setNgayThayDoi(rs.getDate("NgayThayDoi"));
                ls.setGhiChu(rs.getString("GhiChu"));
                list.add(ls);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(LichSuChucVu ls) {
        String sql = "INSERT INTO LichSuChucVu (MaNV, MaCVCu, MaCVMoi, NgayThayDoi, GhiChu) VALUES (?, ?, ?, ?, ?)";
        return DataProvider.executeUpdate(sql,
                ls.getMaNV(),
                ls.getMaCVCu(),
                ls.getMaCVMoi(),
                new Date(ls.getNgayThayDoi().getTime()),
                ls.getGhiChu());
    }
}
