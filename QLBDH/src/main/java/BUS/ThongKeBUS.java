//package BUS;
//
//import DAO.ThongKeDAO;
//import java.util.ArrayList;
//import java.sql.Date;
//
//public class ThongKeBUS {
//
//    private ThongKeDAO thongKeDAO;
//
//    public ThongKeBUS() {
//        thongKeDAO = new ThongKeDAO();
//    }
//
//    public ArrayList<Object[]> getTopSanPham(Date tuNgay, Date denNgay) {
//        return thongKeDAO.getTopSanPham(tuNgay, denNgay);
//    }
//
//    public ArrayList<Object[]> getTopKhachHang(Date tuNgay, Date denNgay) {
//        return thongKeDAO.getTopKhachHang(tuNgay, denNgay);
//    }
//
//    public ArrayList<Object[]> getTopNhanVien(Date tuNgay, Date denNgay) {
//        return thongKeDAO.getTopNhanVien(tuNgay, denNgay);
//    }
//    
//    public double getTongDoanhThu(Date tuNgay, Date denNgay) {
//        return thongKeDAO.getTongDoanhThu(tuNgay, denNgay);
//    }
//}

//package BUS;
//
//import DAO.ThongKeDAO;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class ThongKeBUS {
//    private ThongKeDAO tkDAO = new ThongKeDAO();
//
//    public ArrayList<HashMap<String, Object>> thongKeLoiNhuanQuy(int nam) {
//        if (nam < 2000 || nam > 2100) return new ArrayList<>(); // Kiểm tra năm hợp lệ
//        return tkDAO.getLoiNhuanTheoQuy(nam);
//    }
//
//    public ArrayList<HashMap<String, Object>> thongKeNhanSuThang(int thang, int nam) {
//        if (thang < 1 || thang > 12) return new ArrayList<>();
//        return tkDAO.getChiPhiNhanSu(thang, nam);
//    }
//
//    // Hàm hỗ trợ tính tổng lợi nhuận cả năm từ danh sách các quý
//    public double tinhTongLoiNhuanNam(ArrayList<HashMap<String, Object>> dsQuy) {
//        double tong = 0;
//        for (HashMap<String, Object> map : dsQuy) {
//            tong += (double) map.get("LoiNhuan");
//        }
//        return tong;
//    }
//}

package BUS;

import DAO.DataProvider;
import DTO.HoaDon;
import DTO.CTHoaDon;
import DTO.Product;
import DTO.NhanVienDTO;
import UTIL.DBConnect;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThongKeBUS {

    /**
     * Thống kê doanh thu, giá vốn, lợi nhuận theo tháng/quý/năm.
     * @param thang (0 nếu không chọn tháng)
     * @param quy (0 nếu không chọn quý)
     * @param nam
     * @return ArrayList các đối tượng Map chứa các cặp key: "ThoiGian", "DoanhThu", "GiaVon", "LoiNhuan"
     */
    public ArrayList<Map<String, Object>> thongKeLoiNhuan(int thang, int quy, int nam) {
    ArrayList<Map<String, Object>> result = new ArrayList<>();
    String sql;
    ResultSet rs = null;
    
    try {
        if (thang > 0) {
            // Thống kê theo tháng
            sql = "SELECT MONTH(NgayLap) AS Thang, SUM(TongTien) AS DoanhThu " +
                  "FROM HoaDon WHERE MONTH(NgayLap) = ? AND YEAR(NgayLap) = ? " +
                  "GROUP BY MONTH(NgayLap)";
            rs = DataProvider.executeQuery(sql, thang, nam);
            if (rs != null && rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("ThoiGian", "Tháng " + rs.getInt("Thang"));
                row.put("DoanhThu", rs.getDouble("DoanhThu"));
                row.put("GiaVon", 0.0);      // Tạm tính, nếu cần thì tính từ CTHoaDon + SanPham
                row.put("LoiNhuan", rs.getDouble("DoanhThu")); // Tạm tính = doanh thu
                result.add(row);
            }
        } else if (quy > 0) {
            // Thống kê theo quý: lấy từ tháng đầu đến tháng cuối quý
            int thangBatDau = (quy - 1) * 3 + 1;
            int thangKetThuc = quy * 3;
            sql = "SELECT ? AS Quy, SUM(TongTien) AS DoanhThu " +
                  "FROM HoaDon WHERE MONTH(NgayLap) BETWEEN ? AND ? AND YEAR(NgayLap) = ?";
            rs = DataProvider.executeQuery(sql, quy, thangBatDau, thangKetThuc, nam);
            if (rs != null && rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("ThoiGian", "Quý " + rs.getInt("Quy"));
                row.put("DoanhThu", rs.getDouble("DoanhThu"));
                row.put("GiaVon", 0.0);
                row.put("LoiNhuan", rs.getDouble("DoanhThu"));
                result.add(row);
            }
        } else {
            // Cả năm
            sql = "SELECT YEAR(NgayLap) AS Nam, SUM(TongTien) AS DoanhThu " +
                  "FROM HoaDon WHERE YEAR(NgayLap) = ? GROUP BY YEAR(NgayLap)";
            rs = DataProvider.executeQuery(sql, nam);
            if (rs != null && rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("ThoiGian", "Năm " + rs.getInt("Nam"));
                row.put("DoanhThu", rs.getDouble("DoanhThu"));
                row.put("GiaVon", 0.0);
                row.put("LoiNhuan", rs.getDouble("DoanhThu"));
                result.add(row);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        closeResultSet(rs);
    }
    return result;
}

    private String buildThongKeLoiNhuanSQL(int thang, int quy, int nam) {
        StringBuilder sql = new StringBuilder();
        if (quy > 0) {
            // Thống kê theo quý
            sql.append("SELECT CONCAT('Q', QUARTER(ngayLap), '/', YEAR(ngayLap)) AS thoi_gian, ")
               .append("SUM(tongTien) AS doanh_thu, ")
               .append("SUM(IFNULL(giaVon,0)) AS gia_von, ")
               .append("SUM(tongTien - IFNULL(giaVon,0)) AS loi_nhuan ")
               .append("FROM hoadon h ")
               .append("LEFT JOIN (")
               .append("    SELECT maHD, SUM(soLuong * giaNhap) AS giaVon ")
               .append("    FROM cthoadon ct ")
               .append("    JOIN sanpham sp ON ct.maSP = sp.maSP ")
               .append("    GROUP BY maHD")
               .append(") v ON h.maHD = v.maHD ")
               .append("WHERE YEAR(ngayLap) = ? ");
            if (quy >= 1 && quy <= 4) {
                sql.append("AND QUARTER(ngayLap) = ? ");
            }
            sql.append("GROUP BY QUARTER(ngayLap), YEAR(ngayLap) ");
            sql.append("ORDER BY YEAR(ngayLap), QUARTER(ngayLap)");
        } else if (thang > 0) {
            // Thống kê theo tháng
            sql.append("SELECT DATE_FORMAT(ngayLap, '%m/%Y') AS thoi_gian, ")
               .append("SUM(tongTien) AS doanh_thu, ")
               .append("SUM(IFNULL(giaVon,0)) AS gia_von, ")
               .append("SUM(tongTien - IFNULL(giaVon,0)) AS loi_nhuan ")
               .append("FROM hoadon h ")
               .append("LEFT JOIN (")
               .append("    SELECT maHD, SUM(soLuong * giaNhap) AS giaVon ")
               .append("    FROM cthoadon ct ")
               .append("    JOIN sanpham sp ON ct.maSP = sp.maSP ")
               .append("    GROUP BY maHD")
               .append(") v ON h.maHD = v.maHD ")
               .append("WHERE YEAR(ngayLap) = ? ");
            if (thang >= 1 && thang <= 12) {
                sql.append("AND MONTH(ngayLap) = ? ");
            }
            sql.append("GROUP BY MONTH(ngayLap), YEAR(ngayLap) ")
               .append("ORDER BY YEAR(ngayLap), MONTH(ngayLap)");
        } else {
            // Thống kê theo năm
            sql.append("SELECT YEAR(ngayLap) AS thoi_gian, ")
               .append("SUM(tongTien) AS doanh_thu, ")
               .append("SUM(IFNULL(giaVon,0)) AS gia_von, ")
               .append("SUM(tongTien - IFNULL(giaVon,0)) AS loi_nhuan ")
               .append("FROM hoadon h ")
               .append("LEFT JOIN (")
               .append("    SELECT maHD, SUM(soLuong * giaNhap) AS giaVon ")
               .append("    FROM cthoadon ct ")
               .append("    JOIN sanpham sp ON ct.maSP = sp.maSP ")
               .append("    GROUP BY maHD")
               .append(") v ON h.maHD = v.maHD ")
               .append("WHERE YEAR(ngayLap) = ? ")
               .append("GROUP BY YEAR(ngayLap)");
        }
        return sql.toString();
    }

    /**
     * Thống kê tồn kho: nhập, xuất, tồn cuối kỳ theo tháng/quý/năm.
     */
    public ArrayList<Map<String, Object>> thongKeTonKho(int thang, int quy, int nam) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        String sql = buildThongKeTonKhoSQL(thang, quy, nam);
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Set tham số nếu cần (tùy theo cách build SQL)
            // Ở đây giả sử SQL đã được build sẵn với điều kiện
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("MaSP", rs.getInt("maSP"));
                row.put("TenSP", rs.getString("tenSP"));
                row.put("NhapTrongKy", rs.getInt("nhap"));
                row.put("XuatTrongKy", rs.getInt("xuat"));
                row.put("TonCuoi", rs.getInt("ton"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String buildThongKeTonKhoSQL(int thang, int quy, int nam) {
        // Giả sử có bảng sanpham (maSP, tenSP, soLuongTon), phieunhap, phieuxuat
        // Tính nhập trong kỳ từ phieunhap, xuất từ hoadon
        // Phức tạp, tạm thời trả về SQL mẫu
        return "SELECT sp.maSP, sp.tenSP, "
             + "IFNULL(nhap.soLuongNhap,0) AS nhap, "
             + "IFNULL(xuat.soLuongXuat,0) AS xuat, "
             + "(sp.soLuongTon + IFNULL(nhap.soLuongNhap,0) - IFNULL(xuat.soLuongXuat,0)) AS ton "
             + "FROM sanpham sp "
             + "LEFT JOIN ("
             + "    SELECT maSP, SUM(soLuong) AS soLuongNhap "
             + "    FROM ctphieunhap "
             + "    JOIN phieunhap pn ON ctphieunhap.maPN = pn.maPN "
             + "    WHERE pn.trangThai = 1 AND YEAR(pn.ngayLap) = ? "
             + (thang > 0 ? "AND MONTH(pn.ngayLap) = ? " : "")
             + (quy > 0 ? "AND QUARTER(pn.ngayLap) = ? " : "")
             + "    GROUP BY maSP"
             + ") nhap ON sp.maSP = nhap.maSP "
             + "LEFT JOIN ("
             + "    SELECT ct.maSP, SUM(soLuong) AS soLuongXuat "
             + "    FROM cthoadon ct "
             + "    JOIN hoadon h ON ct.maHD = h.maHD "
             + "    WHERE YEAR(h.ngayLap) = ? "
             + (thang > 0 ? "AND MONTH(h.ngayLap) = ? " : "")
             + (quy > 0 ? "AND QUARTER(h.ngayLap) = ? " : "")
             + "    GROUP BY maSP"
             + ") xuat ON sp.maSP = xuat.maSP";
    }

    /**
     * Thống kê lương nhân viên theo tháng/quý/năm.
     */
    public ArrayList<Map<String, Object>> thongKeLuong(int thang, int quy, int nam) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        String sql = "SELECT nv.maNV, nv.ho, nv.ten, nv.chucVu, "
                   + "SUM(l.thucLinh) AS tongLuong, "
                   + "SUM(l.thuong) AS tongThuong "
                   + "FROM luong l "
                   + "JOIN nhanvien nv ON l.maNV = nv.maNV "
                   + "WHERE l.nam = ? "
                   + (thang > 0 ? "AND l.thang = ? " : "")
                   + (quy > 0 ? "AND l.thang BETWEEN ? AND ? " : "") // quý 1-3, 4-6,...
                   + "GROUP BY nv.maNV, nv.ho, nv.ten, nv.chucVu";
        
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            int index = 1;
            ps.setInt(index++, nam);
            if (thang > 0) {
                ps.setInt(index++, thang);
            }
            if (quy > 0) {
                int thangBatDau = (quy - 1) * 3 + 1;
                int thangKetThuc = quy * 3;
                ps.setInt(index++, thangBatDau);
                ps.setInt(index++, thangKetThuc);
            }
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("MaNV", rs.getInt("maNV"));
                row.put("HoTen", rs.getString("ho") + " " + rs.getString("ten"));
                row.put("ChucVu", rs.getString("chucVu"));
                row.put("TongLuong", rs.getDouble("tongLuong"));
                row.put("TongThuong", rs.getDouble("tongThuong"));
                result.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.getStatement().close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}