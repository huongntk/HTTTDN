package BUS;

import DAO.PhanQuyenDAO;
import DTO.PhanQuyen;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PhanQuyenBUS {
    private PhanQuyenDAO pqDAO = new PhanQuyenDAO();
    
    public PhanQuyen getPhanQuyen(String quyen){
        return pqDAO.getPhanQuyen(quyen);
    }
    
//    public boolean[] layQuyen(String Quyen) {
//        return pqDAO.getQuyen(Quyen);
//    }
    
    public ResultSet layDanhSachQuyen() {
        return pqDAO.getDanhSachQuyen();
    }
    
//    public ArrayList<PhanQuyen> getAll(){
//        return pqDAO.getAll();
//    }
    
    public boolean updateQuyen(String Quyen, boolean qlBanHang,boolean qlSanPham, boolean qlKhachHang, boolean qlNhanVien,
                                        boolean qlNhapHang, boolean qlKhuyenMai, boolean qlPhanQuyen, boolean qlThongKe) {
        return pqDAO.updateQuyen(Quyen, qlBanHang, qlSanPham, qlKhachHang, qlNhanVien, qlNhapHang,  qlKhuyenMai, qlPhanQuyen, qlThongKe);
    }
    
    public boolean insertQuyen(DTO.PhanQuyen pq) {
//        // Kiểm tra quyền đã tồn tại chưa
//        if (pqDAO.getPhanQuyen(Quyen) != null) { 
//            return false; // Trả về false nếu tên quyền đã tồn tại
//        }
        return pqDAO.insertQuyen(pq); // Gọi hàm INSERT (add) trong DAO
    }

    public boolean xoaQuyen(String Quyen) {
        //Không được vô hiệu hóa quyền "admin"
        if(Quyen.equalsIgnoreCase("admin")){
            return false;
        }
        return pqDAO.deleteQuyen(Quyen);
    }

    
//    public boolean update(PhanQuyen pq){
//        return pqDAO.update(pq);
//    }
}

