package DAO;

import DTO.HoaDon; 
import DTO.CTHoaDon; 
import UTIL.DBConnect; 
import java.sql.*;
import java.util.ArrayList;
import java.math.BigDecimal;

public class HoaDonDAO {

    // --- Phương thức Tổng hợp Xử lý Giao dịch ---
    
    public int thucHienGiaoDich(int maNV, int maKH, double tongTien, ArrayList<CTHoaDon> chiTietList) {
        Connection conn = null;
        int maHD = -1;

        try {
            conn = DBConnect.getConnection();
            // Bắt đầu Transaction
            conn.setAutoCommit(false); 

            // 1. INSERT HÓA ĐƠN
            maHD = insertHoaDon(conn, maNV, maKH, tongTien);

            if (maHD <= 0) {
                conn.rollback();
                return -1;
            }

            // 2. INSERT CHI TIẾT HÓA ĐƠN VÀ UPDATE TỒN KHO
            for (CTHoaDon cthd : chiTietList) {
                // a. Insert Chi tiết hóa đơn
                boolean insertCTHDSuccess = insertCTHoaDon(conn, maHD, cthd);
                if (!insertCTHDSuccess) {
                    conn.rollback();
                    return -1;
                }

                // b. Update Số lượng tồn kho (Giảm số lượng)
                boolean updateTonKhoSuccess = updateSoLuongTonKho(conn, cthd.getID(), cthd.getSoLuong());
                if (!updateTonKhoSuccess) {
                    conn.rollback();
                    return -1;
                }
            }

            // 3. COMMIT TRANSACTION
            conn.commit();
            return maHD;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Nếu có lỗi, ROLLBACK Transaction
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return -1;
        } finally {
            // Đặt lại auto commit và đóng kết nối
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // --- Hàm Hỗ trợ 1: Insert HoaDon ---
    private int insertHoaDon(Connection conn, int maNV, int maKH, double tongTien) throws SQLException {
        String sql = "INSERT INTO HoaDon (MaNV, MaKH, NgayLap, TongTien, GhiChu) VALUES (?, ?, GETDATE(), ?,?)";
        int maHDMoi = -1;

        // Sử dụng Statement.RETURN_GENERATED_KEYS để lấy ID vừa tạo
        try (PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, maNV);
            pst.setInt(2, maKH);
            pst.setBigDecimal(3, BigDecimal.valueOf(tongTien)); // Dùng BigDecimal cho tiền tệ
            pst.setString(4, "");
            int affectedRows = pst.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        maHDMoi = rs.getInt(1); // Lấy MaHD vừa tạo
                    }
                }
            }
        }
        return maHDMoi;
    }

    // --- Hàm Hỗ trợ 2: Insert CTHoaDon ---
    private boolean insertCTHoaDon(Connection conn, int maHD, CTHoaDon cthd) throws SQLException {
        String sql = "INSERT INTO CTHoaDon (MaHD, SoLuong, GiaBan, ThanhTien, ID) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, maHD);
            
            pst.setInt(2, cthd.getSoLuong());
            // Giả định DonGia trong CTHoaDon là kiểu double/BigDecimal
            pst.setBigDecimal(3, BigDecimal.valueOf(cthd.getDonGia()));
            pst.setBigDecimal(4, BigDecimal.valueOf(cthd.getThanhTien())); 
            pst.setInt(5, cthd.getID());
            return pst.executeUpdate() > 0;
        }
    }

    // --- Hàm Hỗ trợ 3: Update Số lượng Tồn kho ---
    private boolean updateSoLuongTonKho(Connection conn, int maSP, int soLuongGiam) throws SQLException {
        String sql = "UPDATE SanPham SET SoLuong = SoLuong - ? WHERE ID = ? AND SoLuong >= ?";
        
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, soLuongGiam); // Số lượng cần giảm
            pst.setInt(2, maSP);
            pst.setInt(3, soLuongGiam); // Điều kiện: đảm bảo tồn kho đủ để trừ

            return pst.executeUpdate() > 0;
        }
    }
    
    // Phương thức lấy tất cả Hóa đơn từ CSDL (Dùng cho bảng Hóa đơn)
    public ArrayList<HoaDon> selectAll() {
        ArrayList<HoaDon> list = new ArrayList<>();
        // Cần lấy tất cả các cột cần thiết cho DTO và bảng hiển thị
        String sql = "SELECT MaHD, MaKH, MaNV, NgayLap, TongTien, MaGG, GhiChu, TrangThai FROM HoaDon ORDER BY NgayLap DESC"; 
        
        try (ResultSet rs = DataProvider.executeQuery(sql)) { 
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setMaHD(rs.getInt("MaHD"));
                hd.setMaKH(rs.getInt("MaKH"));
                hd.setMaNV(rs.getInt("MaNV"));
                // Chuyển từ java.sql.Timestamp sang java.util.Date
                hd.setNgayLap(new Date(rs.getTimestamp("NgayLap").getTime())); 
                hd.setTongTien(rs.getDouble("TongTien"));
                hd.setTrangThai(rs.getString("TrangThai"));
                hd.setMaGG(rs.getInt("MaGG")); 
                hd.setGhiChu(rs.getString("GhiChu")); 
                list.add(hd);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi DAO khi lấy danh sách Hóa đơn: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    public HoaDon getHoaDonByMaHD(int maHD) {
        HoaDon hd = null;
        // Đảm bảo truy vấn lấy đầy đủ các cột cần thiết, bao gồm MaGG và GhiChu
        String sql = "SELECT HD.MaHD, HD.MaKH, HD.MaNV, HD.NgayLap, HD.TongTien, HD.MaGG, HD.GhiChu, HD.TrangThai, NV.Ten, KH.Ten FROM HoaDon HD "
                + " JOIN NhanVien NV ON HD.MaNV = NV.MaNV"
                + " JOIN KhachHang KH ON HD.MaKH = KH.MaKH    WHERE MaHD = ?"; 

        try (ResultSet rs = DataProvider.executeQuery(sql, maHD)) { 
            if (rs.next()) {
                hd = new HoaDon();
                hd.setMaHD(rs.getInt("MaHD"));
                hd.setMaKH(rs.getInt("MaKH"));
                hd.setMaNV(rs.getInt("MaNV"));
                hd.setTenNV(rs.getString("Ten"));
                hd.setTenKH(rs.getString("Ten"));
                
                // Lấy ngày lập
                hd.setNgayLap(new java.util.Date(rs.getTimestamp("NgayLap").getTime())); 
                
                hd.setTongTien(rs.getDouble("TongTien"));
                
                // Các trường dữ liệu mới (Mã giảm giá, Ghi chú)
                hd.setMaGG(rs.getInt("MaGG")); 
                hd.setGhiChu(rs.getString("GhiChu"));
                hd.setTrangThai(rs.getString("TrangThai"));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi DAO khi lấy Hóa đơn MaHD=" + maHD + ": " + e.getMessage());
        }
        return hd;
    }
    
    public ArrayList<HoaDon> getListHoaDon(Date dateMin, Date dateMax) {
        try (Connection conn = DBConnect.getConnection()) {
            
            String sql = "SELECT * FROM hoadon WHERE NgayLap BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
            PreparedStatement pre = conn.prepareStatement(sql);
                pre.setDate(1, dateMin);
                pre.setDate(2, dateMax);
                ResultSet rs = pre.executeQuery();

                ArrayList<HoaDon> dshd = new ArrayList<>();
                
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setMaHD(rs.getInt(1));
                    hd.setMaNV(rs.getInt(2));
                    hd.setMaKH(rs.getInt(3));
                    hd.setNgayLap(rs.getDate(4));
                    hd.setTongTien(rs.getInt(5));
                    hd.setMaGG(rs.getInt(6));
                    hd.setGhiChu(rs.getString(7));
                    dshd.add(hd);
                }
                return dshd;
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    
/**
 * Cập nhật Mã KH, Mã GG và Ghi chú cho Hóa đơn.
 */
    public boolean updateKhachHangVaGiamGia(int maHD, int maKH, int maGG, String ghiChu, double tongTienMoi) {
        String sql = "UPDATE HoaDon SET MaKH = ?, MaGG = ?, GhiChu = ?, TongTien = ? WHERE MaHD = ?";

        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, maKH);
            if (maGG == 0) {
                // Nếu MaGG = 0 (Không áp dụng), set tham số SQL là NULL
                pst.setNull(2, java.sql.Types.INTEGER); 
            } else {
                // Nếu MaGG > 0, set giá trị MaGG bình thường
                pst.setInt(2, maGG); 
            }
            pst.setString(3, ghiChu);
            pst.setDouble(4, tongTienMoi);
            pst.setInt(5, maHD);

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật MaKH/MaGG cho HD=" + maHD + ": " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateTrangThai(int maHD, String trangThai) {
        String sql = "UPDATE HoaDon SET TrangThai = ? WHERE MaHD = ?";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, trangThai);
            pst.setInt(2, maHD);

            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi DAO khi cập nhật trạng thái Hóa đơn MaHD=" + maHD + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
   
}