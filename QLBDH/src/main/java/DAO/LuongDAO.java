package DAO;

import DTO.Luong;
import java.sql.ResultSet;
import java.util.ArrayList;

public class LuongDAO {
    
    public ArrayList<Luong> selectByMaNV(int maNV) {
        ArrayList<Luong> list = new ArrayList<>();
        String sql = "SELECT * FROM Luong WHERE MaNV = ? ORDER BY Nam DESC, Thang DESC";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV)) {
            while (rs.next()) {
                Luong l = new Luong();
                l.setMaLuong(rs.getInt("MaLuong"));
                l.setMaNV(rs.getInt("MaNV"));
                l.setThang(rs.getInt("Thang"));
                l.setNam(rs.getInt("Nam"));
                l.setLuongCoBan(rs.getDouble("LuongCoBan"));
                l.setPhuCap(rs.getDouble("PhuCap"));
                l.setThuong(rs.getDouble("Thuong"));
                l.setKhauTru(rs.getDouble("KhauTru"));
                l.setThucLinh(rs.getDouble("ThucLinh"));
                l.setTrangThai(rs.getInt("TrangThai"));
                list.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public int insert(Luong l) {
        String sql = "INSERT INTO Luong (MaNV, Thang, Nam, LuongCoBan, PhuCap, Thuong, KhauTru, ThucLinh, TrangThai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        return DataProvider.executeUpdate(sql, 
                l.getMaNV(), l.getThang(), l.getNam(), l.getLuongCoBan(), l.getPhuCap(),
                l.getThuong(), l.getKhauTru(), l.getThucLinh(), l.getTrangThai());
    }
    
    public int update(Luong l) {
        String sql = "UPDATE Luong SET LuongCoBan = ?, PhuCap = ?, Thuong = ?, KhauTru = ?, ThucLinh = ?, TrangThai = ? WHERE MaLuong = ?";
        return DataProvider.executeUpdate(sql, 
                l.getLuongCoBan(), l.getPhuCap(), l.getThuong(), l.getKhauTru(), l.getThucLinh(), l.getTrangThai(), l.getMaLuong());
    }
    
    public int delete(int maLuong) {
        String sql = "DELETE FROM Luong WHERE MaLuong = ?";
        return DataProvider.executeUpdate(sql, maLuong);
    }
    
    // Phương thức tính lương hàng loạt - giả sử có stored procedure hoặc xử lý logic phức tạp
    // Ở đây chỉ là ví dụ, bạn cần implement logic tính lương thực tế
    public boolean tinhLuong(int thang, int nam) {
        // Có thể gọi procedure hoặc tự tính toán, insert vào bảng Luong cho từng nhân viên
        // Đây là code mẫu đơn giản:
        String sql = "{call sp_TinhLuong(?, ?)}"; // giả sử có procedure
        try {
            int result = DataProvider.executeUpdate(sql, thang, nam);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
