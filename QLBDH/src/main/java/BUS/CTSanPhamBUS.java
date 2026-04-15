package BUS;

import DAO.CTSanPhamDAO;
import DTO.ProductDetail;
import DAO.CTPhieuNhapDAO;

public class CTSanPhamBUS {
    private CTSanPhamDAO dao = new CTSanPhamDAO();

    public ProductDetail getDetail(int id) {
        return dao.getByProductId(id);
    }
    public boolean insertCTSanPham(ProductDetail detail){
        return dao.insertCTSanPham(detail);
    }
    public boolean updateCTSanPham(ProductDetail detail) {
        return dao.updateCTSanPham(detail);
    }
 
    public boolean isSanPhamInUse(int maSP) {
        try {           
            return new CTPhieuNhapDAO().isSanPhamInUse(maSP);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
