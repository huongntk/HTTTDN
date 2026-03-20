package BUS;

import DAO.PhanQuyenDAO;
import DTO.PhanQuyen;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PhanQuyenBUS {
    private PhanQuyenDAO pqDAO = new PhanQuyenDAO();
    
    public PhanQuyen getPhanQuyenByTen(String quyen){
        return pqDAO.getPhanQuyenByTen(quyen);
    }
   
    
//    public boolean[] layQuyen(String Quyen) {
//        return pqDAO.getQuyen(Quyen);
//    }
    
    public ArrayList<PhanQuyen> layDanhSachQuyen() {
        return pqDAO.getDanhSachQuyen(); 
    }
    
//    public ArrayList<PhanQuyen> getAll(){
//        return pqDAO.getAll();
//    }
    
    public boolean updateQuyen(PhanQuyen pq) {
        return pqDAO.updateQuyen(pq);
    }
    
    public boolean themQuyen(PhanQuyen pq) {
        if (pq.getTenQuyen() == null || pq.getTenQuyen().trim().isEmpty()) {
            return false;
        }
        return pqDAO.insertQuyen(pq);
    }

    public boolean xoaQuyen(String MaQuyen) {
        //Không được vô hiệu hóa quyền "admin"
        if(MaQuyen.equalsIgnoreCase("admin")){
            return false;
        }
        return pqDAO.deleteQuyen(MaQuyen);
    }

    
//    public boolean update(PhanQuyen pq){
//        return pqDAO.update(pq);
//    }
}

