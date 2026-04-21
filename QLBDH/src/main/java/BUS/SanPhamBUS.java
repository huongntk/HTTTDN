package BUS;

import java.util.ArrayList;

import DAO.SanphamDAO;
import DAO.CTSanPhamDAO;
import DTO.SanPhamDTO;

public class SanPhamBUS {
    private SanphamDAO dao = new SanphamDAO();

    public ArrayList<SanPhamDTO> getSanPham() {
        return dao.getALL();
    }

    public ArrayList<SanPhamDTO> getSanPhamById(int id) {
        return dao.getById(id);
    }
    public ArrayList<SanPhamDTO> getSanPhamByCate(int maLoai){
        return dao.getByCate(maLoai);
    }
    public ArrayList<SanPhamDTO> getSanPhamByName(String searchName){
        return dao.getByName(searchName);
    }
    public int insertSanPham(SanPhamDTO p){
    return dao.insertSanPham(p);
    }
    public boolean updateSanPham(SanPhamDTO p) {
        return dao.updateSanPham(p);
    }
    public boolean deleteSanPham(int id) {
        if (id <= 0) {
            return false;
        }
        return dao.deleteById(id);
    }
    // Cộng tồn kho khi nhập hàng
    public boolean congTonKho(int maSP, int soLuongNhap) {
        return dao.congTonKho(maSP, soLuongNhap);
    }
}
