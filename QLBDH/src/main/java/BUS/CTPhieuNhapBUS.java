package BUS;

import DAO.CTPhieuNhapDAO;
import DTO.CTPhieuNhapDTO;

import java.util.List;

public class CTPhieuNhapBUS {

    private CTPhieuNhapDAO dao = new CTPhieuNhapDAO();

    public List<CTPhieuNhapDTO> getByMaPN(int maPN) {
        return dao.getByMaPN(maPN);
    }

    // thêm dòng chi tiết mới
    public boolean addDetail(int maPN, int idSP, int soLuong, int giaNhap) {
        int thanhTien = soLuong * giaNhap;
        CTPhieuNhapDTO ct = new CTPhieuNhapDTO(maPN, idSP, soLuong, giaNhap, thanhTien);

        boolean ok = dao.insert(ct);
        if (ok) {
            // Chỉ tăng tồn kho nếu insert thành công
            dao.dieuChinhTonKho(idSP, soLuong);
        }
        return ok;
    }

    // cập nhật dòng chi tiết hiện có
    public boolean updateDetail(int maPN, int idSP, int soLuongMoi, int giaNhapMoi) {
        try {
            int soLuongCu = dao.getSoLuongCu(maPN, idSP);
            int thanhTienMoi = soLuongMoi * giaNhapMoi;
            CTPhieuNhapDTO ctMoi = new CTPhieuNhapDTO(maPN, idSP, soLuongMoi, giaNhapMoi, thanhTienMoi);
            return dao.updateWithStock(ctMoi, soLuongCu);
        } catch (RuntimeException e) {
            // Ghi log và ném lại để GUI hiển thị
            e.printStackTrace();
            throw e; // hoặc trả về false và tự hiển thị ở đây
        }
    }

    // xóa dòng chi tiết
    public boolean deleteDetail(int maPN, int idSP) {
        try {
            int soLuongCu = dao.getSoLuongCu(maPN, idSP);
            return dao.deleteWithStock(maPN, idSP, soLuongCu);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
