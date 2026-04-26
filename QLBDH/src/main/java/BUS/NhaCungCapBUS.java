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
    
    // Kiểm tra tên đã tồn tại chưa
    public boolean isTenExists(String ten) {
        return getAll().stream().anyMatch(n -> n.getTenNCC().equalsIgnoreCase(ten));
    }

    // Kiểm tra tên đã tồn tại (trừ mã NCC hiện tại)
    public boolean isTenExists(String ten, int excludeMa) {
        return getAll().stream()
            .anyMatch(n -> n.getMaNCC() != excludeMa && n.getTenNCC().equalsIgnoreCase(ten));
    }

    // Kiểm tra SĐT đã tồn tại chưa
    public boolean isSDTExists(String sdt) {
        return getAll().stream().anyMatch(n -> n.getSoDienThoai().equals(sdt));
    }

    // Kiểm tra SĐT đã tồn tại (trừ mã NCC hiện tại)
    public boolean isSDTExists(String sdt, int excludeMa) {
        return getAll().stream()
            .anyMatch(n -> n.getMaNCC() != excludeMa && n.getSoDienThoai().equals(sdt));
    }
    
    
}
