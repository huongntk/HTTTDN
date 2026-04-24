/**
     * Tính lương cho một nhân viên trong tháng/năm dựa trên chấm công.
     * Quy tắc (có thể điều chỉnh theo doanh nghiệp):
     * - Ngày công chuẩn: 26 ngày/tháng
     * - Lương ngày = Lương cơ bản / 26
     * - Đi làm đủ: +1 công
     * - Đi muộn / về sớm: +0.5 công (hoặc trừ 0.5 công tùy cách tính)
     * - Nghỉ có phép: +1 công (vẫn hưởng lương)
     * - Nghỉ không phép: +0 công
     * - Thưởng/phạt: nhập tay
     * @param maNV
     * @param thang
     * @param nam
     * @param thuong
     * @param phat
     * @return BangLuongDTO đã tính
     */
package BUS;

import DAO.BangLuongDAO;
import DAO.NhanVienDAO;
import DAO.LichSuChucVuDAO;
import DAO.LuongCoBanTheoChucVuDAO;
import DTO.BangLuongDTO;
import DTO.NhanVienDTO;
import DTO.LichSuChucVu;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class BangLuongBUS {
    private BangLuongDAO bangLuongDAO;
    private NhanVienDAO nhanVienDAO;
    private ChamCongBUS chamCongBUS;
    private LichSuChucVuDAO lichSuDAO;
    private LuongCoBanTheoChucVuDAO luongDAO;

    public BangLuongBUS() {
        bangLuongDAO = new BangLuongDAO();
        nhanVienDAO = new NhanVienDAO();
        chamCongBUS = new ChamCongBUS();
        lichSuDAO = new LichSuChucVuDAO();
        luongDAO = new LuongCoBanTheoChucVuDAO();
    }

    /**
     * Tính lương cho nhân viên theo tháng, có xét đến lịch sử thay đổi chức vụ.
     * Lương cơ bản và phụ cấp được tính riêng cho từng khoảng thời gian trước/sau ngày thay đổi.
     */
    public BangLuongDTO tinhLuongChoNhanVien(int maNV, int thang, int nam, double thuong, double phat) {
        // Lấy lịch sử thay đổi chức vụ
        ArrayList<LichSuChucVu> lichSu = lichSuDAO.selectByMaNV(maNV);
        
        // Ngày đầu tháng và cuối tháng
        Date ngayDauThang = Date.valueOf(String.format("%d-%02d-01", nam, thang));
        Date ngayCuoiThang = Date.valueOf(String.format("%d-%02d-%02d", nam, thang, daysInMonth(thang, nam)));
        
        // Tập hợp các mốc thay đổi trong tháng (gồm đầu tháng, các ngày thay đổi, và ngày sau cuối tháng)
        ArrayList<Date> moc = new ArrayList<>();
        moc.add(ngayDauThang);
        for (LichSuChucVu ls : lichSu) {
            // Sửa lỗi: convert java.util.Date → java.sql.Date
            Date ngay = new Date(ls.getNgayThayDoi().getTime());
            if (ngay.compareTo(ngayDauThang) >= 0 && ngay.compareTo(ngayCuoiThang) <= 0) {
                moc.add(ngay);
            }
        }
        moc.add(dayAfter(ngayCuoiThang));
        moc.sort(Date::compareTo);
        
        double tongLuong = 0;
        double tongPhuCap = 0;
        
        // Duyệt từng khoảng
        for (int i = 0; i < moc.size() - 1; i++) {
            Date start = moc.get(i);
            Date endExclusive = moc.get(i+1);
            Date end = new Date(endExclusive.getTime() - 24 * 60 * 60 * 1000);
            if (end.compareTo(start) < 0) continue;
            
            // Lấy chức vụ áp dụng trong khoảng này
            String maCV = getChucVuAtDate(maNV, start, lichSu);
            if (maCV == null) maCV = nhanVienDAO.selectById(maNV).getMaCV();
            
            double luongCoBan = luongDAO.getLuongCoBan(maCV);
            double phuCap = luongDAO.getPhuCap(maCV);
            
            // Số ngày trong khoảng (từ start đến end, bao gồm cả hai)
            int soNgayTrongKhoang = daysBetween(start, end) + 1;
            // Số công thực tế trong khoảng (dựa vào chấm công)
            double soCong = tinhSoCongTrongKhoang(maNV, start, end, thang, nam);
            
            int ngayCongChuan = 26;
            double luongNgay = luongCoBan / ngayCongChuan;
            double tienLuongKhoang = soCong * luongNgay;
            double phuCapKhoang = phuCap * ((double) soNgayTrongKhoang / daysInMonth(thang, nam));
            
            tongLuong += tienLuongKhoang;
            tongPhuCap += phuCapKhoang;
        }
        
        double tongLuongCuoi = tongLuong + tongPhuCap + thuong - phat;
        if (tongLuongCuoi < 0) tongLuongCuoi = 0;
        
        // Lấy lương cơ bản và phụ cấp hiện tại (cuối tháng) để lưu vào bảng lương
        NhanVienDTO nv = nhanVienDAO.selectById(maNV);
        double luongHienTai = nhanVienDAO.getLuongCoBanByChucVu(nv.getMaCV());
        double phuCapHienTai = nhanVienDAO.getPhuCapByChucVu(nv.getMaCV());
        
        BangLuongDTO bl = new BangLuongDTO();
        bl.setMaNV(maNV);
        bl.setThang(thang);
        bl.setNam(nam);
        bl.setLuongCoBan(tongLuong);
        bl.setPhuCap(tongPhuCap);
        bl.setThuong(thuong);
        bl.setPhat(phat);
        bl.setTongLuong(tongLuongCuoi);
        bl.setNgayTinh(new Date(Calendar.getInstance().getTimeInMillis())); // java.sql.Date
        bl.setTrangThai(0);
        return bl;
    }
    
    // ------------------ Các phương thức helper ------------------
    
    private int daysInMonth(int thang, int nam) {
        Calendar cal = Calendar.getInstance();
        cal.set(nam, thang - 1, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    private Date dayAfter(Date date) {
        return new Date(date.getTime() + 24 * 60 * 60 * 1000);
    }
    
    private int daysBetween(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return (int) (diff / (24 * 60 * 60 * 1000));
    }
    
    private String getChucVuAtDate(int maNV, Date date, ArrayList<LichSuChucVu> lichSu) {
        LichSuChucVu latest = null;
        for (LichSuChucVu ls : lichSu) {
            if (ls.getNgayThayDoi().compareTo(date) <= 0) {
                if (latest == null || ls.getNgayThayDoi().compareTo(latest.getNgayThayDoi()) > 0) {
                    latest = ls;
                }
            }
        }
        if (latest != null) return latest.getMaCVMoi();
        // Nếu chưa có thay đổi, lấy chức vụ gốc từ nhân viên
        NhanVienDTO nv = nhanVienDAO.selectById(maNV);
        return nv != null ? nv.getMaCV() : null;
    }
    
    private double tinhSoCongTrongKhoang(int maNV, Date start, Date end, int thang, int nam) {
        var dsChamCong = chamCongBUS.layChamCongTheoNhanVienThang(maNV, thang, nam);
        double cong = 0;
        for (var cc : dsChamCong) {
            Date ngay = new Date(cc.getNgayLam().getTime()); // java.util.Date -> java.sql.Date
            if (ngay.compareTo(start) >= 0 && ngay.compareTo(end) <= 0) {
                String tt = cc.getTrangThai();
                if ("Đi làm".equals(tt) || "Nghỉ có phép".equals(tt)) {
                    cong += 1.0;
                } else if ("Đi muộn".equals(tt) || "Về sớm".equals(tt)) {
                    cong += 0.5;
                }
                // Nghỉ không phép: 0
            }
        }
        return cong;
    }
    
    // ------------------ Các phương thức cũ (giữ nguyên) ------------------
    
    public boolean kiemTraTonTai(int maNV, int thang, int nam) {
        return bangLuongDAO.kiemTraTonTai(maNV, thang, nam);
    }
    public ArrayList<Object[]> selectAllNhanVienWithLuong() {
        return bangLuongDAO.selectAllNhanVienWithLuong();
    }
    
    public String luuBangLuong(BangLuongDTO bl) {
        if (bangLuongDAO.kiemTraTonTai(bl.getMaNV(), bl.getThang(), bl.getNam())) {
            boolean ok = bangLuongDAO.update(bl);
            return ok ? "Cập nhật bảng lương thành công!" : "Cập nhật thất bại!";
        } else {
            boolean ok = bangLuongDAO.insert(bl);
            return ok ? "Thêm bảng lương thành công!" : "Thêm thất bại!";
        }
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
}