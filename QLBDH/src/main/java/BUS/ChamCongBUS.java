package BUS;

import DAO.ChamCongDAO;
import DTO.ChamCongDTO;
import java.util.ArrayList;
import java.util.Date;

public class ChamCongBUS {

    private ChamCongDAO chamCongDAO;

    public ChamCongBUS() {
        chamCongDAO = new ChamCongDAO();
    }

    public boolean themChamCong(ChamCongDTO cc) {
        if (cc == null || cc.getNgayLam() == null) {
            return false;
        }

        // Chặn trùng ở tầng BUS trước khi insert
        // Database cũng đã có UNIQUE (MaNV, NgayLam) để chặn lần cuối
        if (chamCongDAO.exists(cc.getMaNV(), cc.getNgayLam())) {
            return false;
        }

        return chamCongDAO.insert(cc);
    }

    public boolean suaChamCong(ChamCongDTO cc) {
        if (cc == null || cc.getNgayLam() == null) {
            return false;
        }

        return chamCongDAO.update(cc);
    }

    public boolean xoaChamCong(int maChamCong) {
        if (maChamCong <= 0) {
            return false;
        }

        return chamCongDAO.delete(maChamCong);
    }

    public ArrayList<ChamCongDTO> layChamCongTheoNhanVienThang(int maNV, int thang, int nam) {
        return chamCongDAO.selectByMaNVAndMonth(maNV, thang, nam);
    }

    public ArrayList<ChamCongDTO> layTatCaChamCongTheoThang(int thang, int nam) {
        return chamCongDAO.selectAllByMonth(thang, nam);
    }

    // Tính số ngày công đơn giản
    // Lưu ý: hàm này đang tính Đi muộn/Về sớm = 1 ngày công
    // Nếu muốn đúng với tính lương chi tiết thì nên xử lý 0.5 công ở BangLuongBUS
    public int tinhSoNgayCong(int maNV, int thang, int nam) {
        ArrayList<ChamCongDTO> list = layChamCongTheoNhanVienThang(maNV, thang, nam);
        int count = 0;

        for (ChamCongDTO cc : list) {
            String tt = cc.getTrangThai();

            if ("Đi làm".equals(tt) || "Đi muộn".equals(tt) || "Về sớm".equals(tt)) {
                count++;
            }
        }

        return count;
    }

    public int tinhSoNgayNghiCoPhep(int maNV, int thang, int nam) {
        ArrayList<ChamCongDTO> list = layChamCongTheoNhanVienThang(maNV, thang, nam);
        int count = 0;

        for (ChamCongDTO cc : list) {
            if ("Nghỉ có phép".equals(cc.getTrangThai())) {
                count++;
            }
        }

        return count;
    }

    public int tinhSoNgayNghiKhongPhep(int maNV, int thang, int nam) {
        ArrayList<ChamCongDTO> list = layChamCongTheoNhanVienThang(maNV, thang, nam);
        int count = 0;

        for (ChamCongDTO cc : list) {
            if ("Nghỉ không phép".equals(cc.getTrangThai())) {
                count++;
            }
        }

        return count;
    }
    public boolean daChamCong(int maNV, Date ngayLam) {
        return chamCongDAO.exists(maNV, ngayLam);
    }
}