package BUS;

import DAO.ThongKeDAO;
import UTIL.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ThongKeBUS {

    private ThongKeDAO thongKeDAO;
    private Connection conn;

    public ThongKeBUS() {
        conn = DBConnect.getConnection(); // Có thể hiển thị thông báo lỗi kết nối ở đây
        thongKeDAO = new ThongKeDAO(conn);
    }

    /**
     * Đóng kết nối khi không sử dụng BUS nữa (nên gọi khi đóng ứng dụng hoặc panel)
     */
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ==================== KINH DOANH & LỢI NHUẬN ====================

    public ArrayList<Map<String, Object>> thongKeKinhDoanhTheoSanPham(int thangBatDau, int thangKetThuc, int nam) {
        return thongKeDAO.thongKeKinhDoanhTheoSanPham(thangBatDau, thangKetThuc, nam);
    }

    // ==================== KHO HÀNG & SẢN PHẨM ====================

    /**
     * Thống kê tồn kho: nhập, xuất, tồn cuối kỳ cho từng sản phẩm.
     */
//    public ArrayList<Map<String, Object>> thongKeTonKho(int thang, int quy, int nam) {
//        ArrayList<Map<String, Object>> result = new ArrayList<>();
//        // Phương thức này hiện chưa có trong DAO, cần bổ sung vào ThongKeDAO nếu muốn.
//        // Tạm thời trả về rỗng hoặc gọi DAO với câu lệnh SQL phức tạp.
//        // Để giữ tính toàn vẹn, ta sẽ sử dụng một truy vấn trực tiếp (nhưng nên đưa xuống DAO).
//        // Ở đây ta sẽ dùng Connection để truy vấn tạm (sẽ chuyển sang DAO sau).
//        String sql = buildTonKhoSQL(thang, quy, nam);
//        try (PreparedStatement ps = conn.prepareStatement(sql)) {
//            int idx = 1;
//            if (thang > 0) {
//                ps.setInt(idx++, nam);
//                ps.setInt(idx++, thang);
//                ps.setInt(idx++, nam);
//                ps.setInt(idx++, thang);
//            } else if (quy > 0) {
//                int startMonth = (quy - 1) * 3 + 1;
//                int endMonth = quy * 3;
//                ps.setInt(idx++, nam);
//                ps.setInt(idx++, startMonth);
//                ps.setInt(idx++, endMonth);
//                ps.setInt(idx++, nam);
//                ps.setInt(idx++, startMonth);
//                ps.setInt(idx++, endMonth);
//            } else {
//                ps.setInt(idx++, nam);
//                ps.setInt(idx++, nam);
//            }
//
//            ResultSet rs = ps.executeQuery();
//            while (rs.next()) {
//                Map<String, Object> row = new HashMap<>();
//                row.put("MaSP", rs.getInt("MaSP"));
//                row.put("TenSP", rs.getString("TenSP"));
//                row.put("NhapTrongKy", rs.getInt("Nhap"));
//                row.put("XuatTrongKy", rs.getInt("Xuat"));
//                row.put("TonCuoi", rs.getInt("Ton"));
//                result.add(row);
//            }
//            rs.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    private String buildTonKhoSQL(int thang, int quy, int nam) {
//        String conditionNhap = "YEAR(pn.NgayLap) = ?";
//        if (thang > 0) conditionNhap += " AND MONTH(pn.NgayLap) = ?";
//        else if (quy > 0) conditionNhap += " AND MONTH(pn.NgayLap) BETWEEN ? AND ?";
//
//        String conditionXuat = "YEAR(hd.NgayLap) = ?";
//        if (thang > 0) conditionXuat += " AND MONTH(hd.NgayLap) = ?";
//        else if (quy > 0) conditionXuat += " AND MONTH(hd.NgayLap) BETWEEN ? AND ?";
//
//        return "SELECT sp.ID AS MaSP, sp.TenSP, " +
//               "ISNULL(nhap.SoLuongNhap, 0) AS Nhap, " +
//               "ISNULL(xuat.SoLuongXuat, 0) AS Xuat, " +
//               "(sp.SoLuong + ISNULL(nhap.SoLuongNhap, 0) - ISNULL(xuat.SoLuongXuat, 0)) AS Ton " +
//               "FROM SanPham sp " +
//               "LEFT JOIN (SELECT ct.ID, SUM(ct.SoLuong) AS SoLuongNhap FROM CTPhieuNhap ct " +
//               "JOIN PhieuNhap pn ON ct.MaPN = pn.MaPN WHERE " + conditionNhap + " AND pn.TrangThai = 1 GROUP BY ct.ID) nhap ON sp.ID = nhap.ID " +
//               "LEFT JOIN (SELECT ct.ID, SUM(ct.SoLuong) AS SoLuongXuat FROM CTHoaDon ct " +
//               "JOIN HoaDon hd ON ct.MaHD = hd.MaHD WHERE " + conditionXuat + " AND hd.TrangThai = N'Đã thanh toán' GROUP BY ct.ID) xuat ON sp.ID = xuat.ID " +
//               "WHERE sp.TrangThai = 1";
//    }
    
    public ArrayList<Map<String, Object>> thongKeTonKho(int thangBatDau, int thangKetThuc, int nam, boolean isQuy) {
    return thongKeDAO.thongKeTonKho(thangBatDau, thangKetThuc, nam, isQuy);
}

    // ==================== NHÂN SỰ & LƯƠNG ====================

    /**
     * Thống kê lương nhân viên theo tháng/quý/năm.
     */
    public ArrayList<Map<String, Object>> thongKeLuong(int thang, int quy, int nam) {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        try {
            // Sử dụng DAO để lấy tổng chi phí lương và danh sách nhân viên bán hàng top
            // Nhưng yêu cầu là danh sách từng nhân viên với tổng lương, thưởng.
            // Ta sẽ dùng truy vấn riêng (sẽ chuyển sang DAO sau).
            String sql = buildLuongSQL(thang, quy, nam);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                setLuongParameters(ps, thang, quy, nam);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("MaNV", rs.getInt("MaNV"));
                    row.put("HoTen", rs.getString("HoTen"));
                    row.put("MaCV", rs.getString("MaCV"));
                    row.put("TongLuong", rs.getDouble("TongLuong"));
                    row.put("TongThuong", rs.getDouble("TongThuong"));
                    result.add(row);
                }
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String buildLuongSQL(int thang, int quy, int nam) {
        String condition;
        if (thang > 0) {
            condition = "l.Thang = ? AND l.Nam = ?";
        } else if (quy > 0) {
            condition = "l.Thang BETWEEN ? AND ? AND l.Nam = ?";
        } else {
            condition = "l.Nam = ?";
        }
        return "SELECT nv.MaNV, nv.Ho + ' ' + nv.Ten AS HoTen, nv.MaCV, " +
               "SUM(l.TongLuong) AS TongLuong, SUM(l.Thuong) AS TongThuong " +
               "FROM BangLuong l JOIN NhanVien nv ON l.MaNV = nv.MaNV " +
               "WHERE " + condition + " AND nv.TrangThai = 1 " +
               "GROUP BY nv.MaNV, nv.Ho, nv.Ten, nv.MaCV";
    }

    private void setLuongParameters(PreparedStatement ps, int thang, int quy, int nam) throws SQLException {
        int idx = 1;
        if (thang > 0) {
            ps.setInt(idx++, thang);
            ps.setInt(idx++, nam);
        } else if (quy > 0) {
            int start = (quy - 1) * 3 + 1;
            int end = quy * 3;
            ps.setInt(idx++, start);
            ps.setInt(idx++, end);
            ps.setInt(idx++, nam);
        } else {
            ps.setInt(idx++, nam);
        }
    }
    
    
    public ArrayList<Map<String, Object>> thongKeNhanSuChiTiet(int thang, int nam) {
    return thongKeDAO.thongKeNhanSuChiTiet(thang, nam);
}

public ArrayList<Map<String, Object>> thongKeBienDongNhanSu(int thang, int nam) {
    return thongKeDAO.thongKeBienDongNhanSu(thang, nam);
}

    // ==================== TIỆN ÍCH ====================
    // Các phương thức lấy dữ liệu tổng hợp nhanh (gọi từ DAO)
    public double getTongDoanhThu(int thang, int quy, int nam) throws SQLException {
        return thongKeDAO.getTongDoanhThu(thang, quy, nam);
    }

    public double getTongGiaVon(int thang, int quy, int nam) throws SQLException {
        return thongKeDAO.getTongGiaVon(thang, quy, nam);
    }

    public int getSoHoaDon(int thang, int quy, int nam) throws SQLException {
        return thongKeDAO.getSoHoaDon(thang, quy, nam);
    }

    public int getTongSanPhamBanRa(int thang, int quy, int nam) throws SQLException {
        return thongKeDAO.getTongSanPhamBanRa(thang, quy, nam);
    }

    public int getTongNhapSoLuong(int thang, int quy, int nam) throws SQLException {
        return thongKeDAO.getTongNhapSoLuong(thang, quy, nam);
    }

    public double getTongNhapGiaTri(int thang, int quy, int nam) throws SQLException {
        return thongKeDAO.getTongNhapGiaTri(thang, quy, nam);
    }

    public int getTonKhoHienTai() throws SQLException {
        return thongKeDAO.getTonKhoHienTai();
    }

    public ArrayList<Object[]> getTopSanPhamBanChay(int thang, int quy, int nam, int limit) throws SQLException {
        return thongKeDAO.getTopSanPhamBanChay(thang, quy, nam, limit);
    }

    public double getTongChiPhiLuong(int thang, int quy, int nam) throws SQLException {
        return thongKeDAO.getTongChiPhiLuong(thang, quy, nam);
    }

    public int getSoNhanVienDangLam() throws SQLException {
        return thongKeDAO.getSoNhanVienDangLam();
    }

    public ArrayList<Object[]> getTopNhanVienBanHang(int thang, int quy, int nam, int limit) throws SQLException {
        return thongKeDAO.getTopNhanVienBanHang(thang, quy, nam, limit);
    }
}