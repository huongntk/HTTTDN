package DAO;

import DTO.ChucVu;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ChucVuDAO {
    
    public ArrayList<ChucVu> selectAll() {
        ArrayList<ChucVu> list = new ArrayList<>();
        String sql = "SELECT * FROM ChucVu";
        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            while (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaCV(rs.getString("MaCV"));
                cv.setTenCV(rs.getString("TenCV"));
               
                list.add(cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public ChucVu selectById(String maCV) {
        String sql = "SELECT * FROM ChucVu WHERE MaCV = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maCV)) {
            if (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaCV(rs.getString("MaCV"));
                cv.setTenCV(rs.getString("TenCV"));
               
                return cv;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public ChucVu selectByTen(String tenCV) {
        String sql = "SELECT * FROM ChucVu WHERE TenCV = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, tenCV)) {
            if (rs.next()) {
                ChucVu cv = new ChucVu();
                cv.setMaCV(rs.getString("MaCV"));
                cv.setTenCV(rs.getString("TenCV"));
                
                return cv;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public int insert(ChucVu cv) {
        String sql = "INSERT INTO ChucVu (TenCV) VALUES (?)";
        return DataProvider.executeUpdate(sql, cv.getTenCV());
    }
    
    public int update(ChucVu cv) {
        String sql = "UPDATE ChucVu SET TenCV = ? WHERE MaCV = ?";
        return DataProvider.executeUpdate(sql, cv.getTenCV(), cv.getMaCV());
    }
    
    public int delete(String maCV) {
        String sql = "DELETE FROM ChucVu WHERE MaCV = ?";
        return DataProvider.executeUpdate(sql, maCV);
    }
}