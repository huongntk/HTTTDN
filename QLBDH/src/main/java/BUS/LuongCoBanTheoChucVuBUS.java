package BUS;

import DAO.LuongCoBanTheoChucVuDAO;
import java.util.List;

public class LuongCoBanTheoChucVuBUS {
    private LuongCoBanTheoChucVuDAO dao;

    public LuongCoBanTheoChucVuBUS() {
        dao = new LuongCoBanTheoChucVuDAO();
    }

    public double getLuongCoBan(String maCV) {
        return dao.getLuongCoBan(maCV);
    }

    public double getPhuCap(String maCV) {
        return dao.getPhuCap(maCV);
    }

    public boolean update(String maCV, double luongCoBan, double phuCap) {
        return dao.update(maCV, luongCoBan, phuCap);
    }

    public List<String> getAllChucVu() {
        return dao.getAllChucVu();
    }
}