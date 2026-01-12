package BUS;

import DAO.PhieuNhapDAO;
import DAO.CTPhieuNhapDAO;
import DTO.PhieuNhapDTO;

import java.util.List;

public class PhieuNhapBUS {
    private PhieuNhapDAO dao = new PhieuNhapDAO();
    private CTPhieuNhapDAO ctDao = new CTPhieuNhapDAO();

    private final int DISPLAY_OFFSET = 100000;

    /* ===== LẤY DỮ LIỆU CƠ BẢN ===== */
    public List<PhieuNhapDTO> getAll() { 
        return dao.getAll(); 
    }

    public PhieuNhapDTO getById(int id) { 
        return dao.getById(id); 
    }

    /* ===== THÊM / SỬA / XÓA ===== */
    // Insert: trả về ID (MaPN thực tế trong DB) nếu ok, -1 nếu fail
    public int add(PhieuNhapDTO pn) { 
        return dao.insert(pn); 
    }

    public boolean update(PhieuNhapDTO pn) { 
        return dao.update(pn); 
    }

    public boolean delete(int id) { 
        // Lưu ý: cần đảm bảo xóa chi tiết trước hoặc ON DELETE CASCADE.
        // Hiện DB không có cascade nên thực tế bạn nên xóa CTPhieuNhap trước.
        return dao.delete(id); 
    }

    /* ===== TÌM KIẾM ===== */
    public List<PhieuNhapDTO> search(String key) { 
        return dao.search(key); 
    }

    /* ===== HIỂN THỊ MÃ "ảo" LÊN UI ===== */
    public String toDisplayCode(int dbId) { 
        return String.valueOf(DISPLAY_OFFSET + dbId); 
    }

    public String nextDisplayEstimate() {
        int next = dao.getNextIdentityEstimate();
        return toDisplayCode(next);
    }

    /* ===== TẠO PHIẾU NHẬP MẶC ĐỊNH KHI NHẤN THÊM ===== */
    public PhieuNhapDTO createDefault(Integer maNCC) {
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        // MaPN sẽ do DB sinh, TongTien = 0
        return new PhieuNhapDTO(0, maNCC, today, 0.0);
    }

    /* ===== NGHIỆP VỤ TỔNG TIỀN ===== */
    // Lấy tổng tiền từ bảng CTPhieuNhap (SUM ThanhTien)
    public double tinhTongTienTuCT(int maPN) {
        return ctDao.sumThanhTien(maPN);
    }

    // Ghi tổng tiền xuống bảng PhieuNhap
    public boolean capNhatTongTien(int maPN, double tongTien) {
        return dao.updateTongTien(maPN, tongTien);
    }

    // Tiện ích gọi cuối form CTPhieuNhap: sum -> update PhieuNhap
    public boolean updateTongTienFromDetail(int maPN) {
        double tong = tinhTongTienTuCT(maPN);
        return capNhatTongTien(maPN, tong);
    }
    
    public boolean updateTrangThai(int maPN, int trangThai) {
        
        return dao.updateTrangThai(maPN, trangThai);
    }
}
