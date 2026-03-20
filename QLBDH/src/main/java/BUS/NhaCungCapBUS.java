package BUS;

import DAO.NhaCungCapDAO;
import DTO.NhaCungCapDTO;
import java.util.List;

public class NhaCungCapBUS {
    private NhaCungCapDAO dao = new NhaCungCapDAO();

    public List<NhaCungCapDTO> getAll() {
        return dao.getAll();
    }

    public boolean themNCC(NhaCungCapDTO ncc) {
        try {
            return dao.insert(ncc);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaNCC(NhaCungCapDTO ncc) {
        try {
            return dao.update(ncc);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaNCC(int ma) {
        try {
            return dao.delete(ma);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
