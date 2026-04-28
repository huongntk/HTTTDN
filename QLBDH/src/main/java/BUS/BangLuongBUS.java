package BUS;

import DAO.*;
import DTO.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class BangLuongBUS {

    private BangLuongDAO bangLuongDAO;
    private NhanVienDAO nhanVienDAO;
    private ChamCongDAO chamCongDAO;
    private LichSuChucVuDAO lichSuDAO;
    private LuongCoBanTheoChucVuDAO luongCVDAO;
    private LichLamViecDAO lichLamViecDAO;

    public BangLuongBUS() {
        bangLuongDAO = new BangLuongDAO();
        nhanVienDAO = new NhanVienDAO();
        chamCongDAO = new ChamCongDAO();
        lichSuDAO = new LichSuChucVuDAO();
        luongCVDAO = new LuongCoBanTheoChucVuDAO();
        lichLamViecDAO = new LichLamViecDAO();
    }

    /**
     * Tính lương cho nhân viên theo tháng, có xét đến thay đổi chức vụ.
     * Công chuẩn = số ngày trong lịch làm việc của nhân viên.
     * Công thực tế = tổng SoCong từ chấm công (đã JOIN với CaLam).
     */
    public BangLuongDTO tinhLuongChoNhanVien(int maNV, int thang, int nam, double thuong, double phat) {
        // Ngày đầu và cuối tháng (java.util.Date)
        Calendar cal = Calendar.getInstance();
        
        cal.set(nam, thang - 1, 1); 
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        java.util.Date startOfMonth = cal.getTime();

        // Thiết lập ngày cuối tháng
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        java.util.Date endOfMonth = cal.getTime();

        // Lấy lịch sử thay đổi chức vụ trong tháng
        ArrayList<LichSuChucVu> dsThayDoi = lichSuDAO.selectByMaNVAndMonth(maNV, thang, nam);

        // Xác định chức vụ áp dụng tại đầu tháng
        String chucVuDauThang = getChucVuDauThang(maNV, dsThayDoi);
        if (chucVuDauThang == null || chucVuDauThang.isEmpty()) {
            return null; // Không xác định được chức vụ
        }

        // Chia tháng thành các khoảng theo mốc thay đổi chức vụ
        ArrayList<DateRange> danhSachKhoang = new ArrayList<>();
        java.util.Date currentStart = startOfMonth;

        if (dsThayDoi.isEmpty()) {
            // Cả tháng không đổi chức vụ
            DateRange dr = new DateRange();
            dr.start = startOfMonth;
            dr.end = endOfMonth;
            dr.maCV = chucVuDauThang;
            danhSachKhoang.add(dr);
        } else {
            for (int i = 0; i <= dsThayDoi.size(); i++) {
                DateRange dr = new DateRange();
                if (i < dsThayDoi.size()) {
                    LichSuChucVu ls = dsThayDoi.get(i);
                    java.util.Date ngayThayDoi = ls.getNgayThayDoi(); // java.util.Date

                    dr.start = currentStart;
                    // Ngày kết thúc khoảng là ngày trước ngày thay đổi
                    if (ngayThayDoi.after(startOfMonth)) {
                        Calendar calEnd = Calendar.getInstance();
                        calEnd.setTime(ngayThayDoi);
                        calEnd.add(Calendar.DAY_OF_MONTH, -1);
                        calEnd.set(Calendar.HOUR_OF_DAY, 23);
                        calEnd.set(Calendar.MINUTE, 59);
                        calEnd.set(Calendar.SECOND, 59);
                        calEnd.set(Calendar.MILLISECOND, 999);
                        dr.end = calEnd.getTime();
                    } else {
                        dr.end = new java.util.Date(startOfMonth.getTime() - 1); // khoảng rỗng
                    }
                    // Chức vụ cho khoảng này
                    if (i == 0) {
                        dr.maCV = ls.getMaCVCu(); // trước lần đổi đầu tiên
                    } else {
                        dr.maCV = dsThayDoi.get(i - 1).getMaCVMoi();
                    }
                    currentStart = ngayThayDoi;
                } else {
                    // Khoảng cuối cùng từ lần đổi cuối đến cuối tháng
                    dr.start = currentStart;
                    dr.end = endOfMonth;
                    dr.maCV = dsThayDoi.get(dsThayDoi.size() - 1).getMaCVMoi();
                }

                // Chỉ thêm khoảng nếu start <= end và nằm trong tháng
                if (dr.start != null && dr.end != null && !dr.start.after(dr.end)
                        && !dr.start.before(startOfMonth) && !dr.end.after(endOfMonth)) {
                    danhSachKhoang.add(dr);
                }
            }
        }

        double tongLuongCoBan = 0.0;
        double tongPhuCap = 0.0;

        for (DateRange khoang : danhSachKhoang) {
            if (khoang.maCV == null || khoang.maCV.isEmpty()) continue;

            // Chuyển sang java.sql.Date để gọi DAO
            java.sql.Date sqlStart = new java.sql.Date(khoang.start.getTime());
            java.sql.Date sqlEnd = new java.sql.Date(khoang.end.getTime());

            double luongCB = luongCVDAO.getLuongCoBan(khoang.maCV);
            double phuCapCV = luongCVDAO.getPhuCap(khoang.maCV);

            // Công chuẩn = số ngày trong lịch làm việc
            int congChuan = lichLamViecDAO.getTotalDaysInSchedule(maNV, sqlStart, sqlEnd);
            // Công thực tế = tổng SoCong trong chấm công
            double congThucTe = chamCongDAO.getTotalCongByDateRange(maNV, sqlStart, sqlEnd);

            if (congChuan > 0) {
                tongLuongCoBan += (luongCB / congChuan) * congThucTe;
                // Phụ cấp cũng tính theo tỷ lệ công
                tongPhuCap += (phuCapCV / congChuan) * congThucTe;
            }
        }

        double tongLuong = tongLuongCoBan + tongPhuCap + thuong - phat;
        if (tongLuong < 0) tongLuong = 0;

        BangLuongDTO bl = new BangLuongDTO();
        bl.setMaNV(maNV);
        bl.setThang(thang);
        bl.setNam(nam);
        bl.setLuongCoBan(tongLuongCoBan);
        bl.setPhuCap(tongPhuCap);
        bl.setThuong(thuong);
        bl.setPhat(phat);
        bl.setTongLuong(tongLuong);
        bl.setNgayTinh(new Date(Calendar.getInstance().getTimeInMillis()));
        bl.setTrangThai(0);
        return bl;
    }

    /**
     * Xác định chức vụ của nhân viên tại ngày đầu tháng.
     * Nếu có lịch sử thay đổi, ưu tiên MaCVCu của lần thay đổi đầu tiên (nếu đầu tháng trước ngày đó),
     * nếu không thì lấy chức vụ hiện tại.
     */
    private String getChucVuDauThang(int maNV, ArrayList<LichSuChucVu> dsThayDoi) {
        if (dsThayDoi == null || dsThayDoi.isEmpty()) {
            NhanVienDTO nv = nhanVienDAO.selectById(maNV);
            return (nv != null) ? nv.getMaCV() : null;
        }
        // Giả sử danh sách đã sắp xếp tăng dần theo ngày
        return dsThayDoi.get(0).getMaCVCu();
    }

    // Lớp nội bộ để biểu diễn một khoảng thời gian với một chức vụ
    private static class DateRange {
        java.util.Date start;
        java.util.Date end;
        String maCV;
    }

    // ---------- Các phương thức giữ nguyên từ code cũ (không thay đổi) ----------
    public boolean kiemTraTonTai(int maNV, int thang, int nam) {
        return bangLuongDAO.kiemTraTonTai(maNV, thang, nam);
    }

    public ArrayList<Object[]> selectAllNhanVienWithLuong() {
        return bangLuongDAO.selectAllNhanVienWithLuong();
    }

    public String luuBangLuong(BangLuongDTO bl) {
        BangLuongDTO old = bangLuongDAO.getByMaNVAndMonth(
            bl.getMaNV(),
            bl.getThang(),
            bl.getNam()
        );
        if (old != null) {
            if (old.getTrangThai() == 1) {
                return "Bảng lương tháng này đã được duyệt, không thể tính lại!";
            }
            bl.setMaBL(old.getMaBL());
            if (bl.getGhiChu() == null || bl.getGhiChu().trim().isEmpty()) {
                bl.setGhiChu(old.getGhiChu());
            }
            boolean ok = bangLuongDAO.update(bl);
            return ok ? "Cập nhật bảng lương thành công!" : "Cập nhật thất bại!";
        }
        boolean ok = bangLuongDAO.insert(bl);
        return ok ? "Thêm bảng lương thành công!" : "Thêm thất bại!";
    }

    public BangLuongDTO getByMaNVAndMonth(int maNV, int thang, int nam) {
        return bangLuongDAO.getByMaNVAndMonth(maNV, thang, nam);
    }

    public ArrayList<BangLuongDTO> getListByMaNV(int maNV) {
        return bangLuongDAO.getListByMaNV(maNV);
    }

    public ArrayList<BangLuongDTO> getListByMonth(int thang, int nam) {
        return bangLuongDAO.getListByMonth(thang, nam);
    }

    public int tinhLuongHangLoat(int thang, int nam, double thuongMacDinh, double phatMacDinh) {
        ArrayList<NhanVienDTO> dsNV = nhanVienDAO.selectAll();
        int count = 0;
        for (NhanVienDTO nv : dsNV) {
            if (!nv.isTrangThai()) continue;
            BangLuongDTO bl = tinhLuongChoNhanVien(nv.getMaNV(), thang, nam, thuongMacDinh, phatMacDinh);
            if (bl != null) {
                String result = luuBangLuong(bl);
                if (result.contains("thành công")) count++;
            }
        }
        return count;
    }

    public ArrayList<BangLuongDTO> getByMaNVAndYear(int maNV, int nam) {
        return bangLuongDAO.getByMaNVAndYear(maNV, nam);
    }
    
    public String duyetLuong(int maNV, int thang, int nam) {
        BangLuongDTO bl = bangLuongDAO.getByMaNVAndMonth(maNV, thang, nam);

        if (bl == null) {
            return "Chưa có bảng lương tháng này để duyệt!";
        }

        if (bl.getTrangThai() == 1) {
            return "Bảng lương tháng này đã được duyệt rồi!";
        }

        boolean ok = bangLuongDAO.duyetLuong(maNV, thang, nam);

        if (ok) {
            return "Duyệt lương thành công!";
        }

        return "Duyệt lương thất bại!";
    }
}