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
        // Có thể kiểm tra trùng ngày ở đây
        if (chamCongDAO.exists(cc.getMaNV(), cc.getNgayLam())) {
            return false; // Đã tồn tại, không thêm mới
        }
        return chamCongDAO.insert(cc);
    }

    public boolean suaChamCong(ChamCongDTO cc) {
        return chamCongDAO.update(cc);
    }

    public boolean xoaChamCong(int maChamCong) {
        return chamCongDAO.delete(maChamCong);
    }

    public ArrayList<ChamCongDTO> layChamCongTheoNhanVienThang(int maNV, int thang, int nam) {
        return chamCongDAO.selectByMaNVAndMonth(maNV, thang, nam);
    }

    public ArrayList<ChamCongDTO> layTatCaChamCongTheoThang(int thang, int nam) {
        return chamCongDAO.selectAllByMonth(thang, nam);
    }

    // Tính số ngày công trong tháng (chỉ tính trạng thái 'Đi làm' hoặc 'Đi muộn'/'Về sớm' vẫn được coi là có công)
    public int tinhSoNgayCong(int maNV, int thang, int nam) {
        ArrayList<ChamCongDTO> list = layChamCongTheoNhanVienThang(maNV, thang, nam);
        int count = 0;
        for (ChamCongDTO cc : list) {
            String tt = cc.getTrangThai();
            if (tt.equals("Đi làm") || tt.equals("Đi muộn") || tt.equals("Về sớm")) {
                count++;
            }
        }
        return count;
    }

    // Tính số ngày nghỉ có phép
    public int tinhSoNgayNghiCoPhep(int maNV, int thang, int nam) {
        ArrayList<ChamCongDTO> list = layChamCongTheoNhanVienThang(maNV, thang, nam);
        int count = 0;
        for (ChamCongDTO cc : list) {
            if (cc.getTrangThai().equals("Nghỉ có phép")) count++;
        }
        return count;
    }

    // Tính số ngày nghỉ không phép
    public int tinhSoNgayNghiKhongPhep(int maNV, int thang, int nam) {
        ArrayList<ChamCongDTO> list = layChamCongTheoNhanVienThang(maNV, thang, nam);
        int count = 0;
        for (ChamCongDTO cc : list) {
            if (cc.getTrangThai().equals("Nghỉ không phép")) count++;
        }
        return count;
    }
}