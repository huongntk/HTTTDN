package BUS;

import DAO.ThongKeDAO;
import java.util.ArrayList;
import java.sql.Date;

public class ThongKeBUS {

    private ThongKeDAO thongKeDAO;

    public ThongKeBUS() {
        thongKeDAO = new ThongKeDAO();
    }

    public ArrayList<Object[]> getTopSanPham(Date tuNgay, Date denNgay) {
        return thongKeDAO.getTopSanPham(tuNgay, denNgay);
    }

    public ArrayList<Object[]> getTopKhachHang(Date tuNgay, Date denNgay) {
        return thongKeDAO.getTopKhachHang(tuNgay, denNgay);
    }

    public ArrayList<Object[]> getTopNhanVien(Date tuNgay, Date denNgay) {
        return thongKeDAO.getTopNhanVien(tuNgay, denNgay);
    }
    
    public double getTongDoanhThu(Date tuNgay, Date denNgay) {
        return thongKeDAO.getTongDoanhThu(tuNgay, denNgay);
    }
}