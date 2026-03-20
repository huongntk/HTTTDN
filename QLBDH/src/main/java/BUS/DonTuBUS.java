package BUS;

import DAO.DonTuDAO;
import DTO.DonTuDTO;
import java.util.ArrayList;

public class DonTuBUS {
    private DonTuDAO donTuDAO = new DonTuDAO();

    public boolean themDon(DonTuDTO don) {
        // Kiểm tra logic: Ngày bắt đầu không được sau ngày kết thúc
        return donTuDAO.insert(don);
    }

    public ArrayList<DonTuDTO> getDonByMaNV(String maNV) {
        return donTuDAO.getByMaNV(maNV);
    }

    public ArrayList<DonTuDTO> getAllDonChoDuyet() {
        return donTuDAO.getByStatus("Chờ duyệt");
    }

    public boolean updateTrangThai(int maDon, String trangThai) {
        return donTuDAO.updateStatus(maDon, trangThai);
    }
}