package BUS;

import java.util.ArrayList;

import DAO.SanphamDAO;
import DAO.CTSanPhamDAO;
import DTO.Product;

public class SanPhamBUS {
    private SanphamDAO dao = new SanphamDAO();

    public ArrayList<Product> getSanPham() {
        return dao.getALL();
    }

    public ArrayList<Product> getSanPhamById(int id) {
        return dao.getById(id);
    }
    public ArrayList<Product> getSanPhamByCate(int maLoai){
        return dao.getByCate(maLoai);
    }
    public ArrayList<Product> getSanPhamByName(String searchName){
        return dao.getByName(searchName);
    }
    public int insertSanPham(Product p){
    return dao.insertSanPham(p);
    }
    public boolean updateSanPham(Product p) {
        return dao.updateSanPham(p);
    }
    public boolean deleteSanPham(int id) {
        if (id <= 0) {
            return false;
        }
        return dao.deleteById(id);
    }
}
