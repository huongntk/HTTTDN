package BUS;

import DAO.ChucVuDAO;
import DTO.ChucVu;
import java.util.ArrayList;

public class ChucVuBUS {
    private ChucVuDAO chucVuDAO;

    public ChucVuBUS() {
        chucVuDAO = new ChucVuDAO();
    }

    public ArrayList<ChucVu> layDanhSachChucVu() {
        return chucVuDAO.selectAll();
    }

    public ChucVu layChucVuTheoMa(String maCV) {
        return chucVuDAO.selectById(maCV);
    }

    public boolean themChucVu(ChucVu cv) {
        return chucVuDAO.insert(cv) > 0;
    }

    public boolean suaChucVu(ChucVu cv) {
        return chucVuDAO.update(cv) > 0;
    }

    public boolean xoaChucVu(String maCV) {
        return chucVuDAO.delete(maCV) > 0;
    }

    public ChucVu layChucVuTheoTen(String tenCV) {
        return chucVuDAO.selectByTen(tenCV);
    }
}