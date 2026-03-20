package BUS;

import DAO.CTSanPhamDAO;
import DTO.ProductDetail;

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
}
