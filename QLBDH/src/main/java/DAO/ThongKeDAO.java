package DAO;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ThongKeDAO {

    private Connection conn;

    public ThongKeDAO(Connection conn) {
        this.conn = conn;
    }
    
    

//    // ==================== TIỆN ÍCH ====================
//    private String buildTimeCondition(int thang, int quy, int nam, String dateColumn) {
//        if (thang > 0) {
//            return "MONTH(" + dateColumn + ") = ? AND YEAR(" + dateColumn + ") = ?";
//        } else if (quy > 0) {
//            int startMonth = (quy - 1) * 3 + 1;
//            int endMonth = startMonth + 2;
//            return "MONTH(" + dateColumn + ") BETWEEN ? AND ? AND YEAR(" + dateColumn + ") = ?";
//        } else {
//            return "YEAR(" + dateColumn + ") = ?";
//        }
//    }
//
//    private int setTimeParameters(PreparedStatement ps, int index, int thang, int quy, int nam) throws SQLException {
//        if (thang > 0) {
//            ps.setInt(index++, thang);
//            ps.setInt(index++, nam);
//        } else if (quy > 0) {
//            int startMonth = (quy - 1) * 3 + 1;
//            int endMonth = startMonth + 2;
//            ps.setInt(index++, startMonth);
//            ps.setInt(index++, endMonth);
//            ps.setInt(index++, nam);
//        } else {
//            ps.setInt(index++, nam);
//        }
//        return index;
//    }
//
//    
//   ==== THỐNG KÊ KINH DOANH =====    
public ArrayList<Map<String, Object>> getThongKeKinhDoanh(int thangBatDau, int thangKetThuc, int nam) {
    ArrayList<Map<String, Object>> list = new ArrayList<>();
    
    // SQL thực hiện:
    // 1. Tính tổng số lượng bán và doanh thu từ CTHoaDon & HoaDon.
    // 2. Lấy giá nhập (giá vốn) trung bình từ CTPhieuNhap để tính lợi nhuận chính xác hơn.
    // 3. Công thức: LoiNhuan = DoanhThu - (SoLuongBan * GiaNhapTrungBinh).
   String sql = "SELECT sp.MaSP, sp.TenSP, sp.GiaBan, " +
                 "       ISNULL(sq_ban.TongSoLuongBan, 0) AS SoLuongBan, " +
                 "       ISNULL(sq_ban.TongDoanhThu, 0) AS DoanhThu, " +
                 "       ISNULL(sq_nhap.GiaNhapTB, 0) AS GiaNhapTB " +
                 "FROM SanPham sp " +
                 // Truy vấn con 1: Tính chính xác doanh thu bán ra
                 "LEFT JOIN (" +
                 "    SELECT cthd.ID, SUM(cthd.SoLuong) AS TongSoLuongBan, SUM(cthd.ThanhTien) AS TongDoanhThu " +
                 "    FROM CTHoaDon cthd " +
                 "    JOIN HoaDon hd ON cthd.MaHD = hd.MaHD " +
                 "    WHERE hd.TrangThai LIKE N'%Thanh Toán%' " +
                 "    AND YEAR(hd.NgayLap) = ? " +
                 "    AND MONTH(hd.NgayLap) BETWEEN ? AND ? " +
                 "    GROUP BY cthd.ID " +
                 ") AS sq_ban ON sp.ID = sq_ban.ID " +
                 // Truy vấn con 2: Tính giá vốn trung bình
                 "LEFT JOIN (" +
                 "    SELECT ID, AVG(CAST(GiaNhap AS FLOAT)) AS GiaNhapTB " +
                 "    FROM CTPhieuNhap " +
                 "    GROUP BY ID " +
                 ") AS sq_nhap ON sp.ID = sq_nhap.ID " +
                 "WHERE sq_ban.TongSoLuongBan > 0"; // Chỉ hiện những SP có bán được trong kỳ

    try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
        ps.setInt(1, nam);
        ps.setInt(2, thangBatDau);
        ps.setInt(3, thangKetThuc);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                int soLuongBan = rs.getInt("SoLuongBan");
                double doanhThu = rs.getDouble("DoanhThu");
                double giaNhapTB = rs.getDouble("GiaNhapTB");
                
                // Lợi nhuận = Doanh thu thực tế - (Số lượng bán * Giá vốn trung bình)
                double loiNhuan = doanhThu - (soLuongBan * giaNhapTB);

                row.put("MaSP", rs.getString("MaSP"));
                row.put("TenSP", rs.getNString("TenSP"));
                row.put("GiaBan", rs.getDouble("GiaBan"));
                row.put("SoLuongBan", soLuongBan);
                row.put("DoanhThu", doanhThu);
                row.put("LoiNhuan", loiNhuan);
                
                list.add(row);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
    // ==== THỐNG KÊ KHO VÀ SẢN PHẨM =======

    public ArrayList<Map<String, Object>> getThongKeTonKho(int thangBatDau, int thangKetThuc, int nam) {
    ArrayList<Map<String, Object>> list = new ArrayList<>();
    
    // Query này thực hiện:
    // 1. Lấy thông tin cơ bản của sản phẩm
    // 2. Tính tổng số lượng nhập trong khoảng thời gian (thangBatDau -> thangKetThuc)
    // 3. SoLuong hiện tại trong bảng SanPham chính là tồn cuối (theo thiết kế CSDL của bạn)
    String sql = "SELECT sp.ID, sp.MaSP, sp.TenSP, sp.ThuongHieu, sp.SoLuong AS TonCuoi, " +
                 "ISNULL(SUM(ctpn.SoLuong), 0) AS NhapTrongKy " +
                 "FROM SanPham sp " +
                 "LEFT JOIN CTPhieuNhap ctpn ON sp.ID = ctpn.ID " +
                 "LEFT JOIN PhieuNhap pn ON ctpn.MaPN = pn.MaPN " +
                 "AND pn.NgayLap BETWEEN ? AND ? " +
                 "AND pn.TrangThai = 1 " + // Chỉ tính các phiếu nhập đã hoàn tất
                 "GROUP BY sp.ID, sp.MaSP, sp.TenSP, sp.ThuongHieu, sp.SoLuong";



try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
        // Tạo ngày bắt đầu và ngày kết thúc để truy vấn BETWEEN
        // Định dạng: YYYY-MM-DD
        String ngayBD = String.format("%d-%02d-01", nam, thangBatDau);
        
        // Xác định ngày cuối tháng (đơn giản hóa bằng cách lấy ngày 1 tháng sau hoặc dùng logic cuối năm)
        String ngayKT;
        if (thangKetThuc == 12) {
            ngayKT = nam + "-12-31";
        } else {
            ngayKT = String.format("%d-%02d-01", nam, thangKetThuc + 1); 
            // Lưu ý: Trong thực tế nên dùng EOMONTH của SQL hoặc Calendar của Java 
            // để chính xác hơn, ở đây dùng tạm mốc an toàn.
        }

        ps.setString(1, ngayBD);
        ps.setString(2, ngayKT);
        
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("MaSP", rs.getString("MaSP"));
                row.put("TenSP", rs.getNString("TenSP"));
                row.put("ThuongHieu", rs.getNString("ThuongHieu"));
                row.put("NhapTrongKy", rs.getInt("NhapTrongKy"));
                row.put("TonCuoi", rs.getInt("TonCuoi"));
                list.add(row);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
    
    
// ===== THỐNG KÊ NHÂN SỰ VÀ LƯƠNG ======    
public ArrayList<Map<String, Object>> thongKeNhanSuChiTiet(int thang, int nam) {
    ArrayList<Map<String, Object>> list = new ArrayList<>();  
    String sql = "SELECT nv.MaNV, (nv.Ho + ' ' + nv.Ten) AS HoTen, cv.TenCV, " +
                         "bl.LuongCoBan, bl.Thuong, bl.TongLuong, nv.TrangThai, " +
                         "(SELECT COUNT(*) FROM DonTu dt WHERE dt.MaNV = nv.MaNV " +
                         "AND MONTH(dt.NgayBatDau) = ? AND YEAR(dt.NgayBatDau) = ? " +
                         "AND dt.TrangThai = N'Đã duyệt') AS SoNgayNghi " +
                         "FROM NhanVien nv " +
                         "JOIN ChucVu cv ON nv.MaCV = cv.MaCV " +
                         "LEFT JOIN BangLuong bl ON nv.MaNV = bl.MaNV AND bl.Thang = ? AND bl.Nam = ? " +
                         "WHERE nv.TrangThai = 1 OR bl.MaBL IS NOT NULL";
            
            try (PreparedStatement ps = this.conn.prepareStatement(sql)) {

                ps.setInt(1, thang);
                ps.setInt(2, nam);
                ps.setInt(3, thang);
                ps.setInt(4, nam);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        row.put("MaNV", rs.getInt("MaNV"));
                        row.put("HoTen", rs.getNString("HoTen"));
                        row.put("ChucVu", rs.getNString("TenCV"));
                        // Xử lý giá trị null cho decimal/double để tránh lỗi hiển thị
                        row.put("LuongCoBan", rs.getDouble("LuongCoBan"));
                        row.put("Thuong", rs.getDouble("Thuong"));
                        row.put("TongNhan", rs.getDouble("TongLuong"));
                        row.put("SoNgayNghi", rs.getInt("SoNgayNghi"));
                        row.put("DangLamViec", rs.getBoolean("TrangThai"));
                        list.add(row);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return list;
        }

    public ArrayList<Map<String, Object>> thongKeBienDongNhanSu(int thang, int nam) {
    ArrayList<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT ls.MaNV, (nv.Ho + ' ' + nv.Ten) AS HoTen, " +
                     "cvCu.TenCV AS ChucVuCu, cvMoi.TenCV AS ChucVuMoi, " +
                     "lcu.LuongCoBan AS LuongCu, lmoi.LuongCoBan AS LuongMoi, " +
                     "ls.NgayThayDoi, ls.GhiChu " +
                     "FROM LichSuChucVu ls " +
                     "JOIN NhanVien nv ON ls.MaNV = nv.MaNV " +
                     "LEFT JOIN ChucVu cvCu ON ls.MaCVCu = cvCu.MaCV " +
                     "JOIN ChucVu cvMoi ON ls.MaCVMoi = cvMoi.MaCV " +
                     "LEFT JOIN LuongCoBanTheoChucVu lcu ON ls.MaCVCu = lcu.MaCV " +
                     "JOIN LuongCoBanTheoChucVu lmoi ON ls.MaCVMoi = lmoi.MaCV " +
                     "WHERE MONTH(ls.NgayThayDoi) = ? AND YEAR(ls.NgayThayDoi) = ?";

        try (PreparedStatement ps = this.conn.prepareStatement(sql)) {
            ps.setInt(1, thang);
            ps.setInt(2, nam);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("MaNV", rs.getInt("MaNV"));
                    row.put("HoTen", rs.getNString("HoTen"));
                    row.put("LoaiThayDoi", "Thay đổi chức vụ");
                    row.put("ChucVuCu", rs.getNString("ChucVuCu"));
                    row.put("ChucVuMoi", rs.getNString("ChucVuMoi"));
                    row.put("LuongCu", rs.getDouble("LuongCu"));
                    row.put("LuongMoi", rs.getDouble("LuongMoi"));
                    row.put("NgayApDung", rs.getTimestamp("NgayThayDoi"));
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}

