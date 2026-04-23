package DAO;

import DTO.CTHoaDon;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CTHoaDonDAO {

    /**
     * Phương thức lấy Chi tiết Hóa đơn theo Mã Hóa đơn (Dùng cho bảng Chi tiết HD)
     * @param maHD Mã Hóa đơn cần lấy chi tiết
     * @return Danh sách CTHoaDon
     */
    public ArrayList<CTHoaDon> selectByMaHD(int maHD) {
        ArrayList<CTHoaDon> list = new ArrayList<>();
        // SELECT DonGia là DOUBLE, ThanhTien là INT theo cấu trúc DTO.CTHoaDon bạn cung cấp
        String sql = "SELECT MaHD, ID, SoLuong, GiaBan, ThanhTien FROM CTHoaDon WHERE MaHD = ?";
        
        // Sử dụng DataProvider.executeQuery() để thực thi lệnh SELECT
        try (ResultSet rs = DataProvider.executeQuery(sql, maHD)) {
            
            while (rs.next()) {
                CTHoaDon cthd = new CTHoaDon();
                cthd.setMaHD(rs.getInt("MaHD")); 
                cthd.setID(rs.getInt("ID")); 
                cthd.setSoLuong(rs.getInt("SoLuong")); 
                cthd.setDonGia(rs.getDouble("GiaBan")); // double
                cthd.setThanhTien(rs.getDouble("ThanhTien")); // double
                list.add(cthd);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi DAO khi lấy Chi tiết Hóa đơn MaHD=" + maHD + ": " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    // Lưu ý: Các phương thức liên quan đến Transaction (INSERT CTHoaDon)
    // đã được bạn xử lý trong hàm thucHienGiaoDich() của HoaDonDAO.
    // Nếu bạn muốn thêm hàm INSERT/UPDATE độc lập, bạn có thể thêm như sau:
    
    /**
     * Thêm mới một Chi tiết Hóa đơn (Dùng cho các trường hợp không cần Transaction)
     * @param cthd DTO Chi tiết Hóa đơn
     * @return Số dòng bị ảnh hưởng
     */
    public int insert(CTHoaDon cthd) {
        String sql = "INSERT INTO CTHoaDon (MaHD, SoLuong, DonGia, ThanhTien, ID) VALUES (?, ?, ?, ?, ?)";
        return DataProvider.executeUpdate(sql, 
            cthd.getMaHD(), 
            
            cthd.getSoLuong(), 
            cthd.getDonGia(),
            cthd.getThanhTien(),
            cthd.getID()
        );
    }
}