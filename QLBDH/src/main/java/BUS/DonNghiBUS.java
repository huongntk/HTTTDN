package BUS;

import DAO.DonNghiDAO;
import DTO.DonNghi;
import java.util.ArrayList;

public class DonNghiBUS {
    private DonNghiDAO donNghiDAO;

    public DonNghiBUS() {
        donNghiDAO = new DonNghiDAO();
    }

    public ArrayList<DonNghi> layDonChoDuyet() {
        return donNghiDAO.selectByTrangThai("Chờ duyệt");
    }

    public boolean duyetDon(int maDon, String trangThaiMoi) {
        return donNghiDAO.updateTrangThai(maDon, trangThaiMoi);
    }

    public boolean themDon(DonNghi don) {
        return donNghiDAO.insert(don) > 0;
    }

    public ArrayList<DonNghi> layDonTheoNV(int maNV) {
        return donNghiDAO.selectByMaNV(maNV);
    }
}