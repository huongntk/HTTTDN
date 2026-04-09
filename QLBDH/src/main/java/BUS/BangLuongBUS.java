package BUS;

import DAO.BangLuongDAO;
import DAO.NhanVienDAO;
import DTO.BangLuongDTO;
import DTO.NhanVienDTO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class BangLuongBUS {
    private BangLuongDAO bangLuongDAO;
    private NhanVienDAO nhanVienDAO;
    private ChamCongBUS chamCongBUS;

    public BangLuongBUS() {
        bangLuongDAO = new BangLuongDAO();
        nhanVienDAO = new NhanVienDAO();
        chamCongBUS = new ChamCongBUS();
    }

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
    public BangLuongDTO tinhLuongChoNhanVien(int maNV, int thang, int nam, double thuong, double phat) {
        NhanVienDTO nv = nhanVienDAO.selectById(maNV);
        if (nv == null) return null;

        // Lấy lương cơ bản & phụ cấp theo chức vụ
        double luongCoBan = nhanVienDAO.getLuongCoBanByChucVu(nv.getMaCV());
        double phuCap = nhanVienDAO.getPhuCapByChucVu(nv.getMaCV());

        // Lấy dữ liệu chấm công trong tháng
        int soNgayDiLam = chamCongBUS.tinhSoNgayCong(maNV, thang, nam);
        int soNgayNghiCoPhep = chamCongBUS.tinhSoNgayNghiCoPhep(maNV, thang, nam);
        int soNgayNghiKhongPhep = chamCongBUS.tinhSoNgayNghiKhongPhep(maNV, thang, nam);
        // (Có thể lấy thêm số ngày đi muộn/về sớm nếu cần trừ riêng)

        int ngayCongChuan = 26; // hoặc lấy từ cấu hình
        double luongNgay = luongCoBan / ngayCongChuan;

        // Tính số công thực tế (theo quy tắc: đi làm + nghỉ có phép = 1 công, nghỉ không phép = 0)
        double soCong = soNgayDiLam + soNgayNghiCoPhep;
        // Nếu muốn trừ đi muộn/về sớm: soCong -= (soNgayDiMuon + soNgayVeSom) * 0.5;

        double tienLuong = soCong * luongNgay;
        double tongLuong = tienLuong + phuCap + thuong - phat;
        if (tongLuong < 0) tongLuong = 0;

        BangLuongDTO bl = new BangLuongDTO();
        bl.setMaNV(maNV);
        bl.setThang(thang);
        bl.setNam(nam);
        bl.setLuongCoBan(luongCoBan);
        bl.setPhuCap(phuCap);
        bl.setThuong(thuong);
        bl.setPhat(phat);
        bl.setTongLuong(tongLuong);
        bl.setNgayTinh(new Date(Calendar.getInstance().getTimeInMillis()));
        bl.setTrangThai(0);
        return bl;
    }
    /**
     * Lưu bảng lương (thêm mới hoặc cập nhật nếu đã tồn tại)
     */
    public String luuBangLuong(BangLuongDTO bl) {
        if (bangLuongDAO.kiemTraTonTai(bl.getMaNV(), bl.getThang(), bl.getNam())) {
            boolean ok = bangLuongDAO.update(bl);
            return ok ? "Cập nhật bảng lương thành công!" : "Cập nhật thất bại!";
        } else {
            boolean ok = bangLuongDAO.insert(bl);
            return ok ? "Thêm bảng lương thành công!" : "Thêm thất bại!";
        }
    }

    // Các phương thức khác: xem lương theo nhân viên, xem tất cả theo tháng, ...

    public BangLuongDTO getByMaNVAndMonth(int maNV, int thang, int nam) {
        return bangLuongDAO.getByMaNVAndMonth(maNV, thang, nam);
    }
    
        /**
     * Kiểm tra đã có bảng lương của nhân viên trong tháng/năm chưa
     */
    public boolean kiemTraTonTai(int maNV, int thang, int nam) {
        return bangLuongDAO.kiemTraTonTai(maNV, thang, nam);
    }

    /**
     * Lấy danh sách bảng lương của một nhân viên (lịch sử)
     */
    public ArrayList<BangLuongDTO> getListByMaNV(int maNV) {
        return bangLuongDAO.getListByMaNV(maNV);
    }

    /**
     * Lấy danh sách bảng lương của tất cả nhân viên trong tháng/năm
     */
    public ArrayList<BangLuongDTO> getListByMonth(int thang, int nam) {
        return bangLuongDAO.getListByMonth(thang, nam);
    }

    /**
     * Tính lương cho tất cả nhân viên đang làm việc trong tháng (hàng loạt)
     * @param thang
     * @param nam
     * @param thuongMacDinh  giá trị thưởng mặc định (có thể truyền 0)
     * @param phatMacDinh    giá trị phạt mặc định (có thể truyền 0)
     * @return số lượng nhân viên được tính thành công
     */
    public int tinhLuongHangLoat(int thang, int nam, double thuongMacDinh, double phatMacDinh) {
        NhanVienDAO nvDAO = new NhanVienDAO();
        ArrayList<NhanVienDTO> dsNV = nvDAO.selectAll();
        int count = 0;
        for (NhanVienDTO nv : dsNV) {
            // Chỉ tính cho nhân viên đang làm (trangThai == true)
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