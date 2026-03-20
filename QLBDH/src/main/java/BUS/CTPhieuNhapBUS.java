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
            dao.tangSoLuongSanPham(idSP, soLuong);
        }
        return ok;
    }

    // cập nhật dòng chi tiết hiện có
    public boolean updateDetail(int maPN, int idSP, int soLuong, int giaNhap) {
        int thanhTien = soLuong * giaNhap;
        CTPhieuNhapDTO ct = new CTPhieuNhapDTO(maPN, idSP, soLuong, giaNhap, thanhTien);
        // (Ở đây chưa xử lý bù trừ tồn kho khi sửa số lượng, có thể thêm sau)
        return dao.update(ct);
    }

    // xóa dòng chi tiết
    public boolean deleteDetail(int maPN, int idSP) {
        // (Ở đây cũng chưa hoàn kho trừ lại, có thể bổ sung sau nếu cần)
        return dao.delete(maPN, idSP);
    }

    // tổng tiền theo phiếu nhập
    public double getTongTien(int maPN) {
        return dao.sumThanhTien(maPN);
    }
}
