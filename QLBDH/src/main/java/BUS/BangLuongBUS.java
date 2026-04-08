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

    public BangLuongBUS() {
        bangLuongDAO = new BangLuongDAO();
        nhanVienDAO = new NhanVienDAO();
    }

    /**
     * Tính lương cho một nhân viên trong tháng/năm cụ thể.
     * Công thức: TongLuong = LuongCoBan + PhuCap + Thuong - Phat
     * Trong đó LuongCoBan được lấy từ cấu hình theo chức vụ tại thời điểm tính.
     * @param maNV
     * @param thang
     * @param nam
     * @param thuong  (có thể truyền từ form nhập)
     * @param phat    (có thể truyền từ form nhập)
     * @return BangLuongDTO đã được tính toán
     */
    public BangLuongDTO tinhLuongChoNhanVien(int maNV, int thang, int nam, double thuong, double phat) {
        // 1. Lấy thông tin nhân viên (bao gồm chức vụ hiện tại)
        NhanVienDTO nv = nhanVienDAO.selectById(maNV);
        if (nv == null) return null;

        // 2. Lấy lương cơ bản + phụ cấp từ bảng cấu hình theo chức vụ (giả sử có method getLuongCoBanTheoChucVu)
        double luongCoBan = nhanVienDAO.getLuongCoBanByChucVu(nv.getMaCV());
        double phuCap = nhanVienDAO.getPhuCapByChucVu(nv.getMaCV());

        // 3. Tính tổng lương
        double tongLuong = luongCoBan + phuCap + thuong - phat;

        // 4. Tạo đối tượng BangLuongDTO
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
        bl.setTrangThai(0); // 0: tạm tính, chờ duyệt

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
}