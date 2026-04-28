package DAO;

import DTO.BangLuongDTO;
import java.sql.*;
import DTO.LichSuChucVu;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class BangLuongDAO {

    public ArrayList<Object[]> selectAllNhanVienWithLuong() {
        ArrayList<Object[]> list = new ArrayList<>();
        String sql = "SELECT nv.MaNV, nv.Ho, nv.Ten, nv.MaCV, "
                + "ISNULL(lcb.LuongCoBan, 0) AS LuongCoBan, "
                + "ISNULL(lcb.PhuCapChucVu, 0) AS PhuCap "
                + "FROM NhanVien nv "
                + "LEFT JOIN LuongCoBanTheoChucVu lcb ON nv.MaCV = lcb.MaCV "
                + "WHERE nv.TrangThai = 1"; // chỉ lấy nhân viên đang làm việc
        try (ResultSet rs = DataProvider.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("MaNV"),
                    rs.getString("Ho") + " " + rs.getString("Ten"),
                    rs.getString("MaCV"),
                    rs.getDouble("LuongCoBan"),
                    rs.getDouble("PhuCap")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Thêm mới bảng lương
    public boolean insert(BangLuongDTO bl) {
        String sql = "INSERT INTO BangLuong (MaNV, Thang, Nam, LuongCoBan, PhuCap, Thuong, Phat, TongLuong, NgayTinh, GhiChu, TrangThai) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            int rows = DataProvider.executeUpdate(sql,
                    bl.getMaNV(), bl.getThang(), bl.getNam(), bl.getLuongCoBan(), bl.getPhuCap(),
                    bl.getThuong(), bl.getPhat(), bl.getTongLuong(),
                    new java.sql.Date(bl.getNgayTinh().getTime()), bl.getGhiChu(), bl.getTrangThai());
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật bảng lương
    public boolean update(BangLuongDTO bl) {
        String sql = "UPDATE BangLuong SET LuongCoBan=?, PhuCap=?, Thuong=?, Phat=?, TongLuong=?, NgayTinh=?, GhiChu=?, TrangThai=? "
                + "WHERE MaBL=?";
        try {
            int rows = DataProvider.executeUpdate(sql,
                    bl.getLuongCoBan(), bl.getPhuCap(), bl.getThuong(), bl.getPhat(), bl.getTongLuong(),
                    new java.sql.Date(bl.getNgayTinh().getTime()), bl.getGhiChu(), bl.getTrangThai(),
                    bl.getMaBL());
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Lấy bảng lương của một nhân viên theo tháng/năm
    public BangLuongDTO getByMaNVAndMonth(int maNV, int thang, int nam) {
        String sql = "SELECT * FROM BangLuong WHERE MaNV=? AND Thang=? AND Nam=?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, thang, nam)) {
            if (rs.next()) {
                return mapResultSetToDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả bảng lương của một nhân viên (lịch sử)
    public ArrayList<BangLuongDTO> getListByMaNV(int maNV) {
        ArrayList<BangLuongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BangLuong WHERE MaNV=? ORDER BY Nam DESC, Thang DESC";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV)) {
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy danh sách lương của tất cả nhân viên trong một tháng/năm
    public ArrayList<BangLuongDTO> getListByMonth(int thang, int nam) {
        ArrayList<BangLuongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BangLuong WHERE Thang=? AND Nam=?";
        try (ResultSet rs = DataProvider.executeQuery(sql, thang, nam)) {
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Kiểm tra đã có bảng lương của nhân viên trong tháng/năm chưa
    public boolean kiemTraTonTai(int maNV, int thang, int nam) {
        String sql = "SELECT 1 FROM BangLuong WHERE MaNV=? AND Thang=? AND Nam=?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, thang, nam)) {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Xóa bảng lương
    public boolean delete(int maBL) {
        String sql = "DELETE FROM BangLuong WHERE MaBL=?";
        try {
            int rows = DataProvider.executeUpdate(sql, maBL);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<BangLuongDTO> getByMaNVAndYear(int maNV, int nam) {
        ArrayList<BangLuongDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BangLuong WHERE MaNV = ? AND Nam = ? ORDER BY Thang";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV, nam)) {
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper: chuyển ResultSet thành DTO
    private BangLuongDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        BangLuongDTO bl = new BangLuongDTO();
        bl.setMaBL(rs.getInt("MaBL"));
        bl.setMaNV(rs.getInt("MaNV"));
        bl.setThang(rs.getInt("Thang"));
        bl.setNam(rs.getInt("Nam"));
        bl.setLuongCoBan(rs.getDouble("LuongCoBan"));
        bl.setPhuCap(rs.getDouble("PhuCap"));
        bl.setThuong(rs.getDouble("Thuong"));
        bl.setPhat(rs.getDouble("Phat"));
        bl.setTongLuong(rs.getDouble("TongLuong"));
        bl.setNgayTinh(rs.getDate("NgayTinh"));
        bl.setGhiChu(rs.getString("GhiChu"));
        bl.setTrangThai(rs.getInt("TrangThai"));
        return bl;
    }

    // Đã thay đổi sử dụng LocalDate cho chính xác
    private static class DateRange {

        LocalDate start;
        LocalDate end;
        String maCV;

        public DateRange(LocalDate start, LocalDate end, String maCV) {
            this.start = start;
            this.end = end;
            this.maCV = maCV;
        }
    }

    /**
     * HÀM BỔ SUNG: Lấy mã chức vụ hiện tại của nhân viên từ bảng NhanVien
     */
    private String getMaCVHienTai(int maNV) {
        String sql = "SELECT MaCV FROM NhanVien WHERE MaNV = ?";
        try (ResultSet rs = DataProvider.executeQuery(sql, maNV)) {
            if (rs.next()) {
                return rs.getString("MaCV");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * TÍNH LƯƠNG ĐÃ ĐƯỢC CẢI TIẾN: Sử dụng java.time.LocalDate
     */
    public double[] tinhLuongTheoChucVu(int maNV, int thang, int nam) {
        double luongCoBan = 0.0;
        double phuCap = 0.0;

        LichSuChucVuDAO lichSuDAO = new LichSuChucVuDAO();
        ChamCongDAO chamCongDAO = new ChamCongDAO();
        LuongCoBanTheoChucVuDAO luongCVDAO = new LuongCoBanTheoChucVuDAO();
        LichLamViecDAO lichLamViecDAO = new LichLamViecDAO();

        ArrayList<LichSuChucVu> dsThayDoi = lichSuDAO.selectByMaNVAndMonth(maNV, thang, nam);

        // Sử dụng LocalDate để tránh lỗi liên quan đến giờ/phút/giây
        LocalDate startOfMonth = LocalDate.of(nam, thang, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        ArrayList<DateRange> danhSachKhoang = new ArrayList<>();

        if (dsThayDoi.isEmpty()) {
            // Không có thay đổi: Cả tháng dùng chức vụ hiện tại
            String maCV = getMaCVHienTai(maNV);
            if (maCV != null && !maCV.trim().isEmpty()) {
                danhSachKhoang.add(new DateRange(startOfMonth, endOfMonth, maCV));
            }
        } else {
            // Chia khoảng thời gian khi có thay đổi chức vụ
            LocalDate currentStart = startOfMonth;

            for (int i = 0; i <= dsThayDoi.size(); i++) {
                if (i < dsThayDoi.size()) {
                    LichSuChucVu ls = dsThayDoi.get(i);
                    // Chuyển java.sql.Date sang java.time.LocalDate
                    LocalDate ngayThayDoi = new java.sql.Date(ls.getNgayThayDoi().getTime()).toLocalDate();

                    // Nếu ngày thay đổi nằm sau ngày bắt đầu của khoảng hiện tại
                    if (ngayThayDoi.isAfter(currentStart) && !ngayThayDoi.isAfter(endOfMonth)) {
                        LocalDate endOfRange = ngayThayDoi.minusDays(1);
                        String maCV = (i == 0) ? ls.getMaCVCu() : dsThayDoi.get(i - 1).getMaCVMoi();
                        danhSachKhoang.add(new DateRange(currentStart, endOfRange, maCV));
                    }

                    // Cập nhật mốc bắt đầu mới
                    // Nếu ngayThayDoi < startOfMonth (bản ghi rác), ép nó về startOfMonth để an toàn
                    currentStart = ngayThayDoi.isBefore(startOfMonth) ? startOfMonth : ngayThayDoi;

                } else {
                    // Khoảng cuối cùng: từ lần thay đổi cuối đến hết tháng
                    if (!currentStart.isAfter(endOfMonth)) {
                        String maCV = dsThayDoi.get(dsThayDoi.size() - 1).getMaCVMoi();
                        danhSachKhoang.add(new DateRange(currentStart, endOfMonth, maCV));
                    }
                }
            }
        }

        // Tính toán lương cho từng khoảng
        for (DateRange khoang : danhSachKhoang) {
            if (khoang.maCV == null || khoang.maCV.trim().isEmpty()) {
                continue;
            }

            // Chuyển lại LocalDate sang java.sql.Date để gọi DAO
            java.sql.Date sqlStart = java.sql.Date.valueOf(khoang.start);
            java.sql.Date sqlEnd = java.sql.Date.valueOf(khoang.end);

            int congChuan = lichLamViecDAO.getTotalDaysInSchedule(maNV, sqlStart, sqlEnd);
            double congThucTe = chamCongDAO.getTotalCongByDateRange(maNV, sqlStart, sqlEnd);

            double luongCB = luongCVDAO.getLuongCoBan(khoang.maCV);
            double phuCapCV = luongCVDAO.getPhuCap(khoang.maCV);

            if (congChuan > 0) {
                // Tiền lương của khoảng = (Lương tháng / Tổng công chuẩn của khoảng) * Công thực tế của khoảng
                luongCoBan += (luongCB / congChuan) * congThucTe;
                phuCap += (phuCapCV / congChuan) * congThucTe;
            } else if (congThucTe > 0) {
                // XỬ LÝ EDGE CASE: Có đi làm nhưng không có lịch làm việc (Không có công chuẩn)
                // Gợi ý: Có thể chia mặc định cho 26 ngày (hoặc số ngày của tháng) nếu không có lịch xếp trước.
                System.err.println("Cảnh báo: Nhân viên " + maNV + " có " + congThucTe + " công thực tế nhưng không có lịch làm việc từ " + sqlStart + " đến " + sqlEnd);

                // Giải pháp tạm thời (Tính theo số ngày chuẩn mặc định là 26):
                // luongCoBan += (luongCB / 26.0) * congThucTe;
                // phuCap += (phuCapCV / 26.0) * congThucTe;
            }
        }

        return new double[]{luongCoBan, phuCap};
    }

    public boolean duyetLuong(int maNV, int thang, int nam) {
        String sql = "UPDATE BangLuong "
                + "SET TrangThai = 1 "
                + "WHERE MaNV = ? AND Thang = ? AND Nam = ? AND TrangThai = 0";

        try {
            int rows = DataProvider.executeUpdate(sql, maNV, thang, nam);
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
