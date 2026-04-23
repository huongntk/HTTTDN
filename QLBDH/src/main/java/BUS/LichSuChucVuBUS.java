package BUS;

import DAO.LichSuChucVuDAO;
import DTO.LichSuChucVu;
import java.util.ArrayList;

public class LichSuChucVuBUS {
    private LichSuChucVuDAO lichSuDAO;

    public LichSuChucVuBUS() {
        lichSuDAO = new LichSuChucVuDAO();
    }

    public boolean themLichSu(LichSuChucVu ls){
        return lichSuDAO.insert(ls) > 0;
    }

    public ArrayList<LichSuChucVu> layLichSuTheoNV(int maNV) {
        return lichSuDAO.selectByMaNV(maNV);
    }
}