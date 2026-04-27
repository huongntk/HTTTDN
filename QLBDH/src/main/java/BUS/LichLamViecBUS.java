package BUS;

import DAO.LichLamViecDAO;
import DTO.LichLamViecDTO;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

public class LichLamViecBUS {

    private LichLamViecDAO lichLamViecDAO;

    public LichLamViecBUS() {
        lichLamViecDAO = new LichLamViecDAO();
    }

    public String themLichLamViec(LichLamViecDTO lich) {
        if (lich == null) {
            return "Dữ liệu lịch làm không hợp lệ!";
        }

        if (lich.getMaNV() <= 0) {
            return "Vui lòng chọn nhân viên!";
        }

        if (lich.getMaCa() <= 0) {
            return "Vui lòng chọn ca làm!";
        }

        if (lich.getNgayLam() == null) {
            return "Vui lòng chọn ngày làm!";
        }

        if (isNgayQuaKhu(lich.getNgayLam())) {
            return "Không thể tạo lịch làm cho ngày trong quá khứ!";
        }

        if (lichLamViecDAO.exists(lich.getMaNV(), lich.getNgayLam())) {
            return "Nhân viên này đã có lịch làm trong ngày này!";
        }

        lich.setTrangThai(true);

        boolean ok = lichLamViecDAO.insert(lich);
        return ok ? "Thêm lịch làm thành công!" : "Thêm lịch làm thất bại!";
    }

    public String suaLichLamViec(LichLamViecDTO lich) {
        if (lich == null || lich.getMaLich() <= 0) {
            return "Vui lòng chọn lịch làm cần sửa!";
        }

        if (lich.getMaNV() <= 0) {
            return "Vui lòng chọn nhân viên!";
        }

        if (lich.getMaCa() <= 0) {
            return "Vui lòng chọn ca làm!";
        }

        if (lich.getNgayLam() == null) {
            return "Vui lòng chọn ngày làm!";
        }

        if (isNgayQuaKhu(lich.getNgayLam())) {
            return "Không thể tạo lịch làm cho ngày trong quá khứ!";
        }

        LichLamViecDTO old = lichLamViecDAO.selectByMaNVAndNgayLam(lich.getMaNV(), lich.getNgayLam());

        if (old != null && old.getMaLich() != lich.getMaLich()) {
            return "Nhân viên này đã có lịch làm trong ngày này!";
        }

        boolean ok = lichLamViecDAO.update(lich);
        return ok ? "Cập nhật lịch làm thành công!" : "Cập nhật lịch làm thất bại!";
    }

    public String xoaLichLamViec(int maLich) {
        if (maLich <= 0) {
            return "Vui lòng chọn lịch làm cần xóa!";
        }

        boolean ok = lichLamViecDAO.delete(maLich);
        return ok ? "Xóa lịch làm thành công!" : "Xóa lịch làm thất bại!";
    }

    public LichLamViecDTO layLichTheoMa(int maLich) {
        return lichLamViecDAO.selectById(maLich);
    }

    public LichLamViecDTO layLichTheoNhanVienVaNgay(int maNV, Date ngayLam) {
        return lichLamViecDAO.selectByMaNVAndNgayLam(maNV, ngayLam);
    }

    public ArrayList<LichLamViecDTO> layLichTheoNhanVienThang(int maNV, int thang, int nam) {
        return lichLamViecDAO.selectByMaNVAndMonth(maNV, thang, nam);
    }

    public ArrayList<LichLamViecDTO> layTatCaLichTheoThang(int thang, int nam) {
        return lichLamViecDAO.selectAllByMonth(thang, nam);
    }
    private boolean isNgayQuaKhu(Date ngayLam) {
        if (ngayLam == null) {
            return true;
        }

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        Calendar ngay = Calendar.getInstance();
        ngay.setTime(ngayLam);
        ngay.set(Calendar.HOUR_OF_DAY, 0);
        ngay.set(Calendar.MINUTE, 0);
        ngay.set(Calendar.SECOND, 0);
        ngay.set(Calendar.MILLISECOND, 0);

        return ngay.before(today);
    }
}